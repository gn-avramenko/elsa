/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.structureUpdater;

import com.gridnine.elsa.common.utils.TextUtils;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcSequenceDescription;

import java.util.Set;
import java.util.stream.Collectors;

public record JdbcDatabaseStructureAnalysisResult(Set<String> tablesToDelete, Set<JdbcCreateTableData> tablesToCreate, Set<JdbcUpdateTableData> tablesToUpdate, Set<String> sequencesToDelete, Set<JdbcSequenceDescription> sequencesToCreate) {

    @Override
    public String toString() {
        if (tablesToDelete.isEmpty() && tablesToCreate.isEmpty()
                && tablesToUpdate.isEmpty()) {
            return "DatabaseStructureAnalysisResult: nothing to change";
        }
        var result = new StringBuilder("DatabaseStructureAnalysisResult:");
        result.append("\ntable to delete: %s".formatted(tablesToDelete));
        result.append("\ntable to create:");
        for (var table : tablesToCreate) {
            result.append("\n%s".formatted(table.tableName()));
            result.append("\n\tcolumns:%s".formatted(TextUtils.join(table.columns().entrySet().stream().map(it ->
                    "\n\t\t%s: %s".formatted(it.getKey(), it.getValue())).collect(Collectors.toList()), ", ")));
            result.append("\n\tindexes:%s".formatted(TextUtils.join(table.indexes().entrySet().stream().map(it ->
                    "\n\t\t%s: %s".formatted(it.getKey(), it.getValue())).collect(Collectors.toList()), ", ")));
        }
        result.append("\ntable to update:");
        for (var table : tablesToUpdate) {
            result.append("\n%s".formatted(table.tableName()));
            result.append("\n\tcolumns to delete:%s".formatted(TextUtils.join(table.columnsToDelete().stream()
                    .map("\n\t\t%s"::formatted).collect(Collectors.toList()), ", ")));
            result.append("\n\tindexes to delete:%s".formatted(TextUtils.join(table.indexesToDelete().stream()
                    .map("\n\t\t%s"::formatted).collect(Collectors.toList()), ", ")));
            result.append("\n\tindexes to create:%s".formatted(TextUtils.join(table.indexesToCreate().entrySet().stream().map(it ->
                    "\n\t\t%s: %s".formatted(it.getKey(), it.getValue())).collect(Collectors.toList()), ", ")));
            result.append("\n\tcolumns to create:%s".formatted(TextUtils.join(table.columnsToCreate().entrySet().stream().map(it ->
                    "\n\t\t%s: %s".formatted(it.getKey(), it.getValue())).collect(Collectors.toList()), ", ")));
            result.append("\n\tindexes to create:%s".formatted(TextUtils.join(table.indexesToCreate().entrySet().stream().map(it ->
                    "\n\t\t%s: %s".formatted(it.getKey(), it.getValue())).collect(Collectors.toList()), ", ")));
        }
        result.append("\nsequences to delete: %s".formatted(sequencesToDelete));
        result.append("\nsequences to create:");
        for (var sequence : sequencesToCreate) {
            result.append("\n%s as %s".formatted(sequence.sequenceName(), sequence.type()));
        }
        return result.toString();
    }

}
