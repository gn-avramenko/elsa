/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.structureUpdater;

import com.gridnine.elsa.server.core.storage.jdbc.model.IndexDescription;
import com.gridnine.elsa.server.core.storage.jdbc.model.SqlType;

import java.util.Map;
import java.util.Set;

public record UpdateTableData(String tableName, Map<String, SqlType> columnsToCreate, Map<String, IndexDescription> indexesToCreate,
                              Set<String> columnsToDelete, Set<String> indexesToDelete) {
}
