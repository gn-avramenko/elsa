/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.postgres.dialect;

public interface PostgresqlTypesHandler {
    String getPostgresqlType(String jdbcType);
    String getJdbcType(String hsqlDbType, int maxLength, String columnName);
}
