/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test;

import com.gridnine.elsa.core.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.RepositoryBinaryData;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcDialect;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.*;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexType;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcSequenceDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcSequenceType;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;
import org.hsqldb.jdbc.JDBCBlob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class HsqldbDialect implements JdbcDialect {

    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<>(JdbcUtils.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' ORDER BY TABLE_NAME ASC",
                (rs) -> rs.getString(1).toLowerCase()));
    }

    @Override
    public Map<String, String> getColumnTypes(String tableName) {
        var result = new LinkedHashMap<String, String>();
        JdbcUtils.queryForList("SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='%s' ORDER BY ORDINAL_POSITION ASC".formatted(tableName.toUpperCase()),
                (rs) ->{
                    var dataType = rs.getString(2);
                    var maxLength = rs.getInt(3);
                    var sqlType = switch (dataType){
                        case "CHARACTER VARYING" -> maxLength > 256? SqlTypeTextHandler.type: SqlTypeStringHandler.type;
                        default -> throw new UnsupportedOperationException("type %s %s is not supported".formatted(dataType, maxLength));
                    };
                    return new Pair<>(rs.getString(1).toLowerCase(), sqlType);
                }).forEach(it -> result.put(it.key(), it.value()));
        return result;
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        var result = new LinkedHashMap<String, JdbcIndexDescription>();
        JdbcUtils.queryForList("SELECT INDEX_NAME, COLUMN_NAME, TYPE FROM INFORMATION_SCHEMA.SYSTEM_INDEXINFO WHERE TABLE_NAME='%s' ORDER BY ORDINAL_POSITION ASC".formatted(tableName.toUpperCase()),
                (rs) -> {
                    var indexName = rs.getString(1).toLowerCase();
                    var type = rs.getInt (3);
                    var columnName = rs.getString(2).toLowerCase();
                    var indexType = switch (type){
                        case 3 -> JdbcIndexType.BTREE;
                        default -> throw new UnsupportedOperationException("index type %s is not supported".formatted(type));
                    };
                    return new Pair<>(indexName, new JdbcIndexDescription(columnName, indexType));
                }).forEach((it) -> result.put(it.key(), it.value()));
        return result;
    }

    @Override
    public String getSqlType(String value) {
        return switch (value){
            case SqlTypeLongIdHandler.type -> "BIGINT PRIMARY KEY";
            case SqlTypeIntIdHandler.type -> "INT PRIMARY KEY";
            case SqlTypeStringHandler.type -> "CHAR VARYING(256)";
            case SqlTypeBooleanHandler.type -> "BOOLEAN";
            case SqlTypeTextHandler.type -> "LONGVARCHAR";
            case SqlTypeDateHandler.type -> "DATE";
            case SqlTypeTimestampHandler.type -> "TIMESTAMP(2)";
            case SqlTypeLongHandler.type -> "BIGINT";
            case SqlTypeIntHandler.type -> "INT";
            case SqlTypeBigDecimalHandler.type -> "DECIMAL(19,2)";
            case SqlTypeBlobHandler.type -> "BLOB";
            case SqlTypeStringArrayHandler.type -> "CHAR VARYING(256) ARRAY";
            case SqlTypeLongArrayHandler.type -> "BIGINT ARRAY";
            case SqlTypeIntArrayHandler.type -> "INT ARRAY";
            default -> throw new IllegalArgumentException("unsupported type %s".formatted(value));
        };
    }

    @Override
    public String createDropIndexQuery(String tableName, String index) {
        return "DROP INDEX %s IF EXISTS".formatted(index.toUpperCase());
    }

    @Override
    public String getCreateIndexSql(String tableName, String indexName, JdbcIndexDescription value) {
        return "CREATE INDEX %s ON %s (%s)".formatted(indexName.toUpperCase(), tableName.toUpperCase(),value.field().toUpperCase() );
    }

    @Override
    public Set<String> geSequencesNames() {
        return new LinkedHashSet<>(JdbcUtils.queryForList("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SYSTEM_SEQUENCES WHERE SEQUENCE_SCHEMA != 'SYSTEM_LOBS' ORDER BY SEQUENCE_NAME ASC",
                (rs) -> rs.getString(1).toLowerCase()));
    }

    @Override
    public String getDeleteSequenceSql(String sequenceName) {
        return "DROP SEQUENCE %s".formatted(sequenceName.toUpperCase());
    }

    @Override
    public String getCreateSequenceSql(JdbcSequenceDescription sequence) {
        return "CREATE SEQUENCE %s AS %s".formatted(sequence.sequenceName().toUpperCase(), sequence.type() == JdbcSequenceType.LONG? "BIGINT": "INT");
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        return "VALUES NEXT VALUE FOR %s".formatted(sequenceName.toUpperCase());
    }

    @Override
    public void setBlob(PreparedStatement ps, int idx, RepositoryBinaryData value) throws Exception {
        ps.setBlob(idx, new JDBCBlob(value.content()));
    }

    @Override
    public RepositoryBinaryData readBlob(ResultSet ps, String fieldName) throws Exception {
        var bytes = ps.getBytes(fieldName);
        if(bytes == null){
            return null;
        }
        return new RepositoryBinaryData(null, bytes);
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

    @Override
    public String createIndexExtensionsSql(JdbcIndexType type) {
        return "select 1";
    }
}
