/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.structureUpdater;

import com.gridnine.elsa.server.core.storage.jdbc.model.IndexDescription;
import com.gridnine.elsa.server.core.storage.jdbc.model.SqlType;

import java.util.Map;

public record CreateTableData(String tableName, Map<String, SqlType> columns, Map<String, IndexDescription> indexes) {
}
