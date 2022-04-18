/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.structureUpdater;

import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcFieldType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexDescription;

import java.util.Map;

public record JdbcCreateTableData(String tableName, Map<String, JdbcFieldType> columns, Map<String, JdbcIndexDescription> indexes) {
}
