/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test;

import com.gridnine.elsa.server.storage.repository.jdbc.handlers.*;

public class StandardHsqldbTypeHandler implements HsqldbTypeHandler{
    @Override
    public String getHsqldbType(String jdbcType) {
        return switch (jdbcType){
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
            default -> null;
        };
    }

    @Override
    public String getJdbcType(String hsqlDbType, int maxLength) {
        return switch (hsqlDbType){
            case "CHARACTER VARYING" -> maxLength > 256? SqlTypeTextHandler.type: SqlTypeStringHandler.type;
            default -> null;
        };
    }
}
