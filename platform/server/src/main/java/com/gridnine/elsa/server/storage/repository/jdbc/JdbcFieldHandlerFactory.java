/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

public interface JdbcFieldHandlerFactory {
    JdbcFieldHandler createHandler(String entityId, String fieldName, boolean indexed);
}
