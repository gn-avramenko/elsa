/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.postgres.dialect;

import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.common.utils.IoUtils;
import com.gridnine.elsa.server.storage.repository.RepositoryBinaryData;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcDialect;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexType;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcSequenceDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObjectManager;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PostgresDialect implements JdbcDialect {

    private final Map<String,String> typesMapping = new ConcurrentHashMap<>();

    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<>(JdbcUtils.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = 'public'", (rs) ->
                rs.getString(1)));
    }

    @Override
    public Map<String, String> getColumnTypes(String tableName) {
        var result = new LinkedHashMap<String, String>();
        JdbcUtils.queryForList("SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, UDT_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '%s'".
                formatted(tableName), rs ->{
            var columnName = rs.getString("COLUMN_NAME");
            var maxLength = rs.getInt("CHARACTER_MAXIMUM_LENGTH");
            var dataTypeStr = rs.getString("DATA_TYPE");
            var udtName = rs.getString("UDT_NAME");
            return new Pair<>(columnName, getType(dataTypeStr, maxLength, columnName, udtName));
        }).forEach(map -> result.put(map.key(), map.value()));
        return result;
    }

    private String getType(String dataType, int maxLength, String columnName,String udtName) {
        for(PostgresqlTypesHandler handler: PostgresqlRegistry.get().getHandlers()){
            String type = handler.getJdbcType(dataType, maxLength, columnName,udtName);
            if(type != null){
                return type;
            }
        }
        throw new IllegalStateException("unsupported type: %s".formatted(dataType));
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        var result = new LinkedHashMap<String, JdbcIndexDescription>();
        JdbcUtils.queryForList("SELECT indexname, indexdef FROM pg_indexes WHERE schemaname = 'public' and tablename = '%s'".formatted(tableName),
                rs->{
                    var indexName = rs.getString("indexname");
                    if(!indexName.endsWith("_pkey")) {
                        var indexdef = rs.getString("indexdef");
                        var idx = indexName.lastIndexOf("_");
                        var columnName = indexName.substring(idx + 1);
                        var indexType = indexdef.contains("USING btree") ? JdbcIndexType.BTREE : JdbcIndexType.GIN;
                        return new Pair<>(indexName, new JdbcIndexDescription(columnName, indexType));
                    }
                    return null;
                })
                .forEach(map ->{
                    if(map != null) {
                        result.put(map.key(), map.value());
                    }
                });
        return result;
    }

    @Override
    public String getSqlType(String value) {
        String type = null;
        if(!typesMapping.containsKey(value)){
            for(PostgresqlTypesHandler handler: PostgresqlRegistry.get().getHandlers()){
                String psType = handler.getPostgresqlType(value);
                if(psType != null){
                    type = psType;
                    break;
                }
            }
            typesMapping.put(value, type);
        }
        type = typesMapping.get(value);
        if(type != null){
            return type;
        }
        throw new IllegalArgumentException("unsupported type %s".formatted(value));
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
        return new LinkedHashSet<>(JdbcUtils.queryForList("SELECT sequence_name FROM information_schema.sequences",
                (rs) -> rs.getString(1)));
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
    public void setBlob(PreparedStatement ps, int idx, RepositoryBinaryData value) throws Exception {
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
    public RepositoryBinaryData readBlob(ResultSet rs, String fieldName) throws Exception {
        var lobj = rs.getStatement().getConnection().unwrap(PGConnection.class).getLargeObjectAPI();
        var oidVal = rs.getLong(fieldName);
        try(var obj = lobj.open(oidVal, LargeObjectManager.READ)){
            var baos = new ByteArrayOutputStream();
            IoUtils.copy(obj.getInputStream(), baos);
            return new RepositoryBinaryData(oidVal, baos.toByteArray());
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
