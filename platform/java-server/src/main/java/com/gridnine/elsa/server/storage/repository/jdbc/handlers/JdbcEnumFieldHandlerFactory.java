/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.JdbcFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcFieldHandlerFactory;

public class JdbcEnumFieldHandlerFactory implements JdbcFieldHandlerFactory {

    @Override
    public JdbcFieldHandler createHandler(String entityId, String fieldName, boolean indexed) {
        return new JdbcEnumFieldHandler(entityId, fieldName, indexed);
    }
}
