/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.structureUpdater;

import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;

import java.util.Map;

public record JdbcCreateTableData(String tableName, Map<String, String> columns, Map<String, JdbcIndexDescription> indexes) {
}
