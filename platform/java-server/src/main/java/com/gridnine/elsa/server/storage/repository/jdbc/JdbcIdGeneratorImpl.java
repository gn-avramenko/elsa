/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import com.gridnine.elsa.core.model.common.IdGenerator;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

public class JdbcIdGeneratorImpl implements IdGenerator {

    @Override
    public long nextId() {
        var found = true;
        var id = 0L;
        while (found) {
            id = JdbcUtils.updateAndReturnResult(JdbcDialect.get().getSequenceNextValueSql("longid"), rs -> rs.getLong(1));
            found = JdbcUtils.isNotEmptyResult("select id from identifiers where id=%s".formatted(id));
            if(!found) {
                try {
                    JdbcUtils.update("insert into identifiers(id) values(%s)".formatted(id));
                } catch (Exception e) {
                 found = true;
                }
            }
        }
        return id;
    }

    @Override
    public void ensureIdRegistered(long id) {
        var found = false;
        while (!found){
            found = JdbcUtils.isNotEmptyResult("select id from identifiers where id=%s".formatted(id));
            if(!found){
                try {
                    JdbcUtils.update("insert into identifiers(id) values(%s)".formatted(id));
                } catch (Exception e) {
                    found = true;
                }
            }
        }
    }
}
