/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.postgres.dialect;

import com.gridnine.elsa.server.storage.repository.jdbc.handlers.*;

public class StandardPostgresqlTypesHandler implements PostgresqlTypesHandler {
    @Override
    public String getPostgresqlType(String jdbcType) {
        return switch (jdbcType) {
            case SqlTypeLongIdHandler.type -> "BIGINT PRIMARY KEY";
            case SqlTypeIntIdHandler.type -> "INT PRIMARY KEY";
            case SqlTypeStringHandler.type -> "CHAR VARYING(256)";
            case SqlTypeBooleanHandler.type -> "BOOLEAN";
            case SqlTypeTextHandler.type -> "TEXT";
            case SqlTypeDateHandler.type -> "DATE";
            case SqlTypeTimestampHandler.type -> "timestamp without time zone";
            case SqlTypeLongHandler.type -> "BIGINT";
            case SqlTypeIntHandler.type -> "INT";
            case SqlTypeBigDecimalHandler.type -> "numeric(19,2)";
            case SqlTypeBlobHandler.type -> "OID";
            case SqlTypeStringArrayHandler.type -> "CHAR VARYING(256)[]";
            case SqlTypeLongArrayHandler.type -> "BIGINT ARRAY";
            case SqlTypeIntArrayHandler.type -> "INT ARRAY";
            default -> null;
        };
    }

    @Override
    public String getJdbcType(String dataType, int maxLength, String columnName) {
        if ("character varying".equalsIgnoreCase(dataType)) {
            return SqlTypeStringHandler.type;
        }
        if ("text".equalsIgnoreCase(dataType)) {
            return SqlTypeTextHandler.type;
        }
        if ("timestamp without time zone".equalsIgnoreCase(dataType)) {
            return SqlTypeTimestampHandler.type;
        }
        if ("date".equalsIgnoreCase(dataType)) {
            return SqlTypeDateHandler.type;
        }
        if ("oid".equalsIgnoreCase(dataType)) {
            return SqlTypeBlobHandler.type;
        }
        if ("bigint".equalsIgnoreCase(dataType)) {
            return "id".equalsIgnoreCase(columnName)? SqlTypeLongIdHandler.type : SqlTypeLongHandler.type;
        }
        if ("integer".equalsIgnoreCase(dataType)) {
            return "id".equalsIgnoreCase(columnName)? SqlTypeIntIdHandler.type : SqlTypeIntHandler.type;
        }
        if ("numeric".equalsIgnoreCase(dataType)) {
            return SqlTypeBigDecimalHandler.type;
        }
        if ("boolean".equalsIgnoreCase(dataType)) {
            return SqlTypeBooleanHandler.type;
        }
        if ("ARRAY".equalsIgnoreCase(dataType)) {
            return SqlTypeStringArrayHandler.type;
        }
        return null;
    }
}
