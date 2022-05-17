/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.postgres;

import com.gridnine.elsa.common.core.utils.IoUtils;
import com.gridnine.elsa.server.core.storage.database.DatabaseBinaryData;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.*;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObjectManager;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class PostgresDialect implements JdbcDialect {

    private final JdbcTemplate template;

    public PostgresDialect(DataSource template) {
        this.template = new JdbcTemplate(template);
    }

    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<>(template.query("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = 'public'", (rs, rowNum) ->
                rs.getString(1)));
    }

    @Override
    public Map<String, JdbcFieldType> getColumnTypes(String tableName) {
        var result = new LinkedHashMap<String, JdbcFieldType>();
        template.queryForList("SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '%s'".
                formatted(tableName)).forEach(map ->{
                    var columnName = (String) map.get("COLUMN_NAME");
            var maxLength = (Integer) map.get("CHARACTER_MAXIMUM_LENGTH");
            var dataTypeStr = (String) map.get("DATA_TYPE");
            result.put(columnName, getType(dataTypeStr, maxLength, columnName));
        });
        return result;
    }

    private JdbcFieldType getType(String dataType, Number maxLength, String columnName) {
        if ("character varying".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.STRING;
        }
        if ("text".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.TEXT;
        }
        if ("timestamp without time zone".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.DATE_TIME;
        }
        if ("date".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.DATE;
        }
        if ("oid".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.BLOB;
        }
        if ("bigint".equalsIgnoreCase(dataType)) {
            return "id".equalsIgnoreCase(columnName)? JdbcFieldType.LONG_ID : JdbcFieldType.LONG;
        }
        if ("integer".equalsIgnoreCase(dataType)) {
            return "id".equalsIgnoreCase(columnName)? JdbcFieldType.INT_ID : JdbcFieldType.INT;
        }
        if ("numeric".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.BIG_DECIMAL;
        }
        if ("boolean".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.BOOLEAN;
        }
        if ("ARRAY".equalsIgnoreCase(dataType)) {
            return JdbcFieldType.STRING_ARRAY;
        }
        throw new IllegalStateException("unsupported type: %s".formatted(dataType));
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        var result = new LinkedHashMap<String, JdbcIndexDescription>();
        template.queryForList("SELECT indexname, indexdef FROM pg_indexes WHERE schemaname = 'public' and tablename = '%s'".formatted(tableName))
                .forEach(map ->{
                    var indexName = ((String) map.get("indexname"));
                    if(!indexName.endsWith("_pkey")) {
                        var indexdef = ((String) map.get("indexdef"));
                        var idx = indexName.lastIndexOf("_");
                        var columnName = indexName.substring(idx + 1);
                        var indexType = indexdef.contains("USING btree") ? JdbcIndexType.BTREE : JdbcIndexType.GIN;
                        result.put(indexName, new JdbcIndexDescription(columnName, indexType));
                    }
                });
        return result;
    }

    @Override
    public String getSqlType(JdbcFieldType value) {
        return switch (value){
            case LONG_ID -> "BIGINT PRIMARY KEY";
            case INT_ID -> "INT PRIMARY KEY";
            case STRING -> "CHAR VARYING(256)";
            case BOOLEAN -> "BOOLEAN";
            case TEXT -> "TEXT";
            case DATE -> "DATE";
            case DATE_TIME -> "timestamp without time zone";
            case LONG -> "BIGINT";
            case INT -> "INT";
            case BIG_DECIMAL -> "numeric(19,2)";
            case BLOB -> "OID";
            case STRING_ARRAY -> "CHAR VARYING(256)[]";
            case LONG_ARRAY -> "BIGINT ARRAY";
            case INT_ARRAY -> "INT ARRAY";
            default -> throw new IllegalArgumentException("unsupported type %s".formatted(value));
        };
    }

    @Override
    public String createDropIndexQuery(String tableName, String index) {
        return "DROP INDEX IF EXISTS %s".formatted(index);
    }

    @Override
    public String getCreateIndexSql(String tableName, String indexName, JdbcIndexDescription value) {
        return switch (value.type()){
            case BTREE -> "CREATE INDEX %s ON %s USING btree(%s)".formatted(indexName, tableName, value.field());
            case GIN -> "CREATE INDEX %s ON %s USING gin(%s)".formatted(indexName, tableName, value.field());
        };
    }

    @Override
    public Set<String> geSequencesNames() {
        return new LinkedHashSet<>(template.query("SELECT sequence_name FROM information_schema.sequences",
                (rs, rowNum) -> rs.getString(1)));
    }

    @Override
    public String getDeleteSequenceSql(String sequenceName) {
        return "DROP SEQUENCE %s".formatted(sequenceName);
    }

    @Override
    public String getCreateSequenceSql(JdbcSequenceDescription sequence) {
        return "CREATE SEQUENCE %s".formatted(sequence.sequenceName());
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        return "select nextval('%s')".formatted(sequenceName);
    }

    @Override
    public void setBlob(PreparedStatement ps, int idx, DatabaseBinaryData value) throws Exception {
        var lobj = ps.getConnection().unwrap(PGConnection.class).getLargeObjectAPI();
        if(value.id() != null){
            lobj.delete(value.id());
        }
        var oid = lobj.createLO(LargeObjectManager.WRITE | LargeObjectManager.READ);
        try(var obj = lobj.open(oid, LargeObjectManager.WRITE)){
            obj.write(value.content());
        }
        ps.setLong(idx, oid);
    }

    @Override
    public DatabaseBinaryData readBlob(ResultSet rs, String fieldName) throws Exception {
        var lobj = rs.getStatement().getConnection().unwrap(PGConnection.class).getLargeObjectAPI();
        var oidVal = rs.getLong(fieldName);
        try(var obj = lobj.open(oidVal, LargeObjectManager.READ)){
            var baos = new ByteArrayOutputStream();
            IoUtils.copy(obj.getInputStream(), baos);
            return new DatabaseBinaryData(oidVal, baos.toByteArray());
        }
    }

    @Override
    public String getCardinalitySql(String property) {
        return "cardinality(%s)".formatted(property);
    }

    @Override
    public void deleteBlob(Connection conn, Long id) throws SQLException {
        var lobj = conn.unwrap(PGConnection.class).getLargeObjectAPI();
        lobj.delete(id);
    }

    @Override
    public String getIlikeFunctionName() {
        return "ilike";
    }

    @Override
    public String createIndexExtensionsSql(JdbcIndexType type) {
        return "CREATE EXTENSION pg_trgm; CREATE EXTENSION btree_gin;";
    }
}
