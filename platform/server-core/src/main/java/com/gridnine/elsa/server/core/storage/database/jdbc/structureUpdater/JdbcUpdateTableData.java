/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.structureUpdater;

import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcFieldType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexDescription;

import java.util.Map;
import java.util.Set;

public record JdbcUpdateTableData(String tableName, Map<String, JdbcFieldType> columnsToCreate, Map<String, JdbcIndexDescription> indexesToCreate,
                                  Set<String> columnsToDelete, Set<String> indexesToDelete) {
}
