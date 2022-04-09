/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc;

import com.gridnine.elsa.server.core.storage.BlobWrapper;
import com.gridnine.elsa.server.core.storage.jdbc.model.IndexDescription;
import com.gridnine.elsa.server.core.storage.jdbc.model.SequenceDescription;
import com.gridnine.elsa.server.core.storage.jdbc.model.SqlType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface JdbcDialect {
    Set<String> getTableNames();
    Map<String, SqlType> getColumnTypes(String tableName);

    Map<String, IndexDescription> getIndexes(String tableName);

    String getSqlType(SqlType value);

    String createDropIndexQuery(String tableName, String index);

    String getCreateIndexSql(String tableName, String key, IndexDescription value);

    Set<String> geSequencesNames();

    String getDeleteSequenceSql(String sequenceName);

    String getCreateSequenceSql(SequenceDescription sequence);

    String getSequenceNextValueSql(String sequenceName);

    void setBlob(PreparedStatement ps, int idx, BlobWrapper value) throws Exception;

    BlobWrapper readBlob(ResultSet ps,  String fieldName) throws Exception;

    String getCardinalitySql(String property);

    void deleteBlob(Long id);

    String getIlikeFunctionName();
}
