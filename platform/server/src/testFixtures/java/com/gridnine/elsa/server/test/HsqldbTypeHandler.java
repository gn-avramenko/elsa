/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test;

public interface HsqldbTypeHandler {
    String getHsqldbType(String jdbcType);
    String getJdbcType(String hsqlDbType, int maxLength);
}
