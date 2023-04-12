/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.storage.repository.RepositoryBinaryData;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexType;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcSequenceDescription;

import javax.sql.XADataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface JdbcDialect {
    Set<String> getTableNames();
    Map<String, String> getColumnTypes(String tableName);

    Map<String, JdbcIndexDescription> getIndexes(String tableName);

    String createDropIndexQuery(String tableName, String index);

    String getCreateIndexSql(String tableName, String key, JdbcIndexDescription value);

    Set<String> geSequencesNames();

    String getDeleteSequenceSql(String sequenceName);

    String getCreateSequenceSql(JdbcSequenceDescription sequence);

    String getSequenceNextValueSql(String sequenceName);

    void setBlob(PreparedStatement ps, int idx, RepositoryBinaryData value) throws Exception;

    RepositoryBinaryData readBlob(ResultSet ps,  String fieldName) throws Exception;

    String getCardinalitySql(String property);

    void deleteBlob(Connection cnn, Long id) throws SQLException;

    String getIlikeFunctionName();

    String createIndexExtensionsSql(JdbcIndexType type);

    String getSqlType(String value);

    static JdbcDialect get(){
        return Environment.getPublished(JdbcDialect.class);
    }

    XADataSource createXADataSource() throws SQLException;
}
