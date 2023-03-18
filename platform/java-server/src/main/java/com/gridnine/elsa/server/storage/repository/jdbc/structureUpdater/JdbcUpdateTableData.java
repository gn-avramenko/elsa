/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.structureUpdater;

import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;

import java.util.Map;
import java.util.Set;

public record JdbcUpdateTableData(String tableName, Map<String, String> columnsToCreate, Map<String, JdbcIndexDescription> indexesToCreate,
                                  Set<String> columnsToDelete, Set<String> indexesToDelete) {
}
