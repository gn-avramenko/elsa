/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.adapter;

import com.gridnine.elsa.server.core.storage.database.DatabaseBinaryData;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcFieldType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcSequenceDescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

public interface JdbcDialect {
    Set<String> getTableNames();
    Map<String, JdbcFieldType> getColumnTypes(String tableName);

    Map<String, JdbcIndexDescription> getIndexes(String tableName);

    String getSqlType(JdbcFieldType value);

    String createDropIndexQuery(String tableName, String index);

    String getCreateIndexSql(String tableName, String key, JdbcIndexDescription value);

    Set<String> geSequencesNames();

    String getDeleteSequenceSql(String sequenceName);

    String getCreateSequenceSql(JdbcSequenceDescription sequence);

    String getSequenceNextValueSql(String sequenceName);

    void setBlob(PreparedStatement ps, int idx, DatabaseBinaryData value) throws Exception;

    DatabaseBinaryData readBlob(ResultSet ps,  String fieldName) throws Exception;

    String getCardinalitySql(String property);

    void deleteBlob(Connection cnn, Long id);

    String getIlikeFunctionName();
}
