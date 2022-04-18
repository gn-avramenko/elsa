/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.structureUpdater;

import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JdbcStructureUpdater {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate template;

    private JdbcDialect dialect;

    private JdbcDatabaseMetadataProvider metadataProvider;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setDialect(JdbcDialect dialect){
        this.dialect = dialect;
    }

    @Autowired
    public void setMetadataProvider(JdbcDatabaseMetadataProvider metadataProvider){
        this.metadataProvider = metadataProvider;
    }

    @PostConstruct
    public void init(){
        long start = System.currentTimeMillis();
        final JdbcDatabaseStructureAnalysisResult analysisResult = analyze();
        log.debug("Database analysis was completed in %s ms. Result:\n%s".formatted(System.currentTimeMillis()-start, analysisResult) );
        if(analysisResult.tablesToCreate().isEmpty() && analysisResult.tablesToUpdate().isEmpty() && analysisResult.tablesToDelete().isEmpty()){
            return;
        }
        start = System.currentTimeMillis();
        for(var tableName : analysisResult.tablesToDelete()){
            template.execute("drop table %s".formatted(tableName));
            log.debug("table %s deleted".formatted(tableName));
        }
        for(var tableData: analysisResult.tablesToCreate()){
            template.execute("create table %s(\n%s\n)".formatted(tableData.tableName(),
                    StringUtils.join(tableData.columns().entrySet().stream().map(it -> "%s %s".formatted(it.getKey(), 
                            dialect.getSqlType(it.getValue()))).toList(), ",\n")));
            log.debug("table %s was created".formatted(tableData.tableName()));
            createIndexes(tableData.tableName(), tableData.indexes());
        }
        for(var tableData: analysisResult.tablesToUpdate()){
            for(var index: tableData.indexesToDelete()){
                template.execute(dialect.createDropIndexQuery(tableData.tableName(), index));
                log.info("index %s of table %s was deleted".formatted(index, tableData.tableName()));
            }
            for (var column : tableData.columnsToDelete()) {
                template.execute("ALTER TABLE %s DROP COLUMN %s".formatted(tableData.tableName(), column));
                log.info("column %s of table %s was deleted".formatted(tableData.tableName(), column));
            }
            for (var column : tableData.columnsToCreate().entrySet()) {
                template.execute("ALTER TABLE %s ADD %s %s".formatted(tableData.tableName(), column.getKey(), dialect.getSqlType(column.getValue())));
                log.info("column %s of type %s in table %s was created".formatted(column.getKey(), column.getValue(), tableData.tableName()));
            }
            createIndexes(tableData.tableName(), tableData.indexesToCreate());
        }
        for(var sequence: analysisResult.sequencesToDelete()){
            template.execute(dialect.getDeleteSequenceSql(sequence));
            log.info("sequence %s was deleted".formatted(sequence));
        }
        for(var sequence: analysisResult.sequencesToCreate()){
            template.execute(dialect.getCreateSequenceSql(sequence));
            log.info("sequence %s of type %s was created".formatted(sequence.sequenceName(), sequence.type()));
        }
        log.debug("Database structure was updated in %s ms".formatted(System.currentTimeMillis()-start) );
    }

    private void createIndexes(String tableName, Map<String, JdbcIndexDescription> indexes) {
        indexes.forEach((key, value) ->{
            template.execute(dialect.getCreateIndexSql(tableName, key, value));
            log.info("index %s on field %s of table %s was created".formatted(key, value.field(), tableName));
        });
    }

    private JdbcDatabaseStructureAnalysisResult analyze() {
        var tablesToCreate = new LinkedHashSet<JdbcCreateTableData>();
        var descriptions = metadataProvider.getDescriptions();
        var newTableNames = descriptions.keySet();
        var existingTableNames = dialect.getTableNames();
        var tablesToDelete = new LinkedHashSet<>(exclusion(existingTableNames, newTableNames));
        for (var tableName : exclusion(newTableNames, existingTableNames)) {
            tablesToCreate.add(toTableData(descriptions.get(tableName)));
        }
        var tablesToUpdate = new LinkedHashSet<JdbcUpdateTableData>();
        for (var tableName : exclusion(existingTableNames, tablesToDelete)) {
            var existingColumns = dialect.getColumnTypes(tableName);
            var existingIndexes = dialect.getIndexes(tableName);
            var newTableData = toTableData(descriptions.get(tableName));
            var columnsToDelete = exclusion(existingColumns.keySet(), newTableData.columns().keySet());
            var indexesToDelete = exclusion(existingIndexes.keySet(), newTableData.indexes().keySet());
            newTableData.columns().forEach((key1, value1) -> {
                var et = getIgnoreCase(existingColumns, key1);
                if (et != null && value1 != et) {
                    columnsToDelete.add(key1);
                    removeIgnoreCase(existingColumns, key1);
                    Set<String> delete2 = new HashSet<>();
                    existingIndexes.forEach((key, value) -> {
                        if (value.field().equalsIgnoreCase(key1)) {
                            delete2.add(key);
                        }
                    });
                    indexesToDelete.addAll(delete2);
                    delete2.forEach(it -> removeIgnoreCase(existingIndexes, it));
                }
            });
            var columnsToCreate = new LinkedHashMap<String, JdbcFieldType>();
            exclusion(newTableData.columns().keySet(), existingColumns.keySet()).forEach(c -> columnsToCreate.put(c, getIgnoreCase(newTableData.columns(), c)));
            var indexesToCreate = new LinkedHashMap<String, JdbcIndexDescription>();
            exclusion(newTableData.indexes().keySet(), existingIndexes.keySet()).forEach(c -> indexesToCreate.put(c, getIgnoreCase(newTableData.indexes(), c)));
            if(columnsToCreate.isEmpty() && columnsToDelete.isEmpty() && indexesToCreate.isEmpty() && indexesToDelete.isEmpty()){
                continue;
            }
            tablesToUpdate.add(new JdbcUpdateTableData(tableName, columnsToCreate, indexesToCreate, columnsToDelete, indexesToDelete));
        }
        var existingSequencesNames = dialect.geSequencesNames();
        var newSequences = metadataProvider.getSequencesMap().values();
        var newSequencesName = newSequences.stream().map(JdbcSequenceDescription::sequenceName).collect(Collectors.toSet());
        var sequencesToDelete = exclusion(existingSequencesNames, newSequencesName);
        var sequencesToCreate = newSequences.stream().filter(it -> !existingSequencesNames.contains(it.sequenceName())).collect(Collectors.toSet());
        return new JdbcDatabaseStructureAnalysisResult(tablesToDelete, tablesToCreate, tablesToUpdate, sequencesToDelete, sequencesToCreate);
    }

    private void removeIgnoreCase(Map<String, ?> existingColumns, String key1) {
        existingColumns.keySet().stream().filter(it -> it.equalsIgnoreCase(key1)).findFirst().ifPresent(existingColumns::remove);
    }

    private<T> T getIgnoreCase(Map<String, T> existingColumns, String key1) {
        for(var entry: existingColumns.entrySet()){
            if(entry.getKey().equalsIgnoreCase(key1)){
                return entry.getValue();
            }
        }
        return  null;
    }

    private JdbcCreateTableData toTableData(JdbcTableDescription databaseTableDescription) {
        var columns = new LinkedHashMap<String, JdbcFieldType>();
        var indexes = new LinkedHashMap<String, JdbcIndexDescription>();
        databaseTableDescription.getFields().forEach((key, value) -> {
            columns.putAll(value.getColumns());
            indexes.putAll(value.getIndexes(databaseTableDescription.getName()));
        });
        return new JdbcCreateTableData(databaseTableDescription.getName(), columns, indexes);
    }

    private Set<String> exclusion(Collection<String> all, Collection<String> toRemove) {
        var result= new LinkedHashSet<>(all);
        result.removeIf(it -> toRemove.stream().anyMatch(r -> r.equalsIgnoreCase(it)));
        return result;
    }
}
