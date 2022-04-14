/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.common;

import com.gridnine.elsa.server.core.storage.BlobWrapper;
import com.gridnine.elsa.server.core.storage.jdbc.JdbcDialect;
import com.gridnine.elsa.server.core.storage.jdbc.model.*;
import org.hsqldb.jdbc.JDBCBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class HsqldbDialect implements JdbcDialect {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<>(template.query("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' ORDER BY TABLE_NAME ASC",
                (rs, rowNum) -> rs.getString(1).toLowerCase()));
    }

    @Override
    public Map<String, SqlType> getColumnTypes(String tableName) {
        var result = new LinkedHashMap<String, SqlType>();
        template.queryForList("SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='%s' ORDER BY ORDINAL_POSITION ASC".formatted(tableName.toUpperCase()))
                .forEach(map ->{
                    var dataType = ((String) map.get("DATA_TYPE"));
                    var maxLength = ((Number) map.get("CHARACTER_MAXIMUM_LENGTH"));
                    var sqlType = switch (dataType){
                       case "CHARACTER VARYING" -> maxLength.intValue() > 256? SqlType.TEXT: SqlType.STRING;
                        default -> throw new UnsupportedOperationException("type %s %s is not supported".formatted(dataType, maxLength));
                    };
                    result.put(((String) map.get("COLUMN_NAME")).toLowerCase(), sqlType);
                });
        return result;
    }

    @Override
    public Map<String, IndexDescription> getIndexes(String tableName) {
        var result = new LinkedHashMap<String, IndexDescription>();
        template.queryForList("SELECT INDEX_NAME, COLUMN_NAME, TYPE FROM INFORMATION_SCHEMA.SYSTEM_INDEXINFO WHERE TABLE_NAME='%s' ORDER BY ORDINAL_POSITION ASC".formatted(tableName.toUpperCase()))
                .forEach(map ->{
                    var indexName = ((String) map.get("INDEX_NAME")).toLowerCase();
                    var type = ((Number) map.get("TYPE")).intValue();
                    var columnName = ((String) map.get("COLUMN_NAME")).toLowerCase();
                    var indexType = switch (type){
                        case 3 -> IndexType.BTREE;
                        default -> throw new UnsupportedOperationException("index type %s is not supported".formatted(type));
                    };
                    result.put(indexName, new IndexDescription(columnName, indexType));
                });
        return result;
    }

    @Override
    public String getSqlType(SqlType value) {
        return switch (value){
            case LONG_ID -> "BIGINT PRIMARY KEY";
            case INT_ID -> "INT PRIMARY KEY";
            case STRING -> "CHAR VARYING(256)";
            case BOOLEAN -> "BOOLEAN";
            case TEXT -> "LONGVARCHAR";
            case DATE -> "DATE";
            case DATE_TIME -> "TIMESTAMP(2)";
            case LONG -> "BIGINT";
            case INT -> "INT";
            case BIG_DECIMAL -> "DECIMAL(19,2)";
            case BLOB -> "BLOB";
            case STRING_ARRAY -> "CHAR VARYING(256) ARRAY";
            case LONG_ARRAY -> "BIGINT ARRAY";
            case INT_ARRAY -> "INT ARRAY";
            default -> throw new IllegalArgumentException("unsupported type %s".formatted(value));
        };
    }

    @Override
    public String createDropIndexQuery(String tableName, String index) {
        return "DROP INDEX %s IF EXISTS".formatted(index.toUpperCase());
    }

    @Override
    public String getCreateIndexSql(String tableName, String indexName, IndexDescription value) {
        return "CREATE INDEX %s ON %s (%s)".formatted(indexName.toUpperCase(), tableName.toUpperCase(),value.field().toUpperCase() );
    }

    @Override
    public Set<String> geSequencesNames() {
        return new LinkedHashSet<>(template.query("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SYSTEM_SEQUENCES WHERE SEQUENCE_SCHEMA != 'SYSTEM_LOBS' ORDER BY SEQUENCE_NAME ASC",
                (rs, rowNum) -> rs.getString(1).toLowerCase()));
    }

    @Override
    public String getDeleteSequenceSql(String sequenceName) {
        return "DROP SEQUENCE %s".formatted(sequenceName.toUpperCase());
    }

    @Override
    public String getCreateSequenceSql(SequenceDescription sequence) {
        return "CREATE SEQUENCE %s AS %s".formatted(sequence.sequenceName().toUpperCase(), sequence.type() == SequenceType.LONG? "BIGINT": "INT");
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        return "VALUES NEXT VALUE FOR %s".formatted(sequenceName.toUpperCase());
    }

    @Override
    public void setBlob(PreparedStatement ps, int idx, BlobWrapper value) throws Exception {
        ps.setBlob(idx, new JDBCBlob(value.content()));
    }

    @Override
    public BlobWrapper readBlob(ResultSet ps, String fieldName) throws Exception {
        var bytes = ps.getBytes(fieldName);
        if(bytes == null){
            return null;
        }
        return new BlobWrapper(null, bytes);
    }

    @Override
    public String getCardinalitySql(String property) {
        return "CARDINALITY(%s)".formatted(property);
    }

    @Override
    public void deleteBlob(Connection conn, Long id) {
        //noops
    }

    @Override
    public String getIlikeFunctionName() {
        return "like";
    }
}
