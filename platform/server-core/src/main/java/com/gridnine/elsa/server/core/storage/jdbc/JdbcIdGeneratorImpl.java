/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc;

import com.gridnine.elsa.common.core.model.common.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcIdGeneratorImpl implements IdGenerator {

    private JdbcTemplate template;

    @Autowired
    private JdbcDialect dialect;
    @Autowired
    public void setDataSource(DataSource ds){
        template = new JdbcTemplate(ds);
    }

    @Override
    public long nextId() {
        var found = true;
        var id = 0L;
        while (found) {
            id = template.query(dialect.getSequenceNextValueSql("longid"), rs -> {
                rs.next();
                return rs.getLong(1);
            });
            found = !template.queryForList("select id from identifiers where id=%s".formatted(id)).isEmpty();
            if(!found) {
                try {
                    template.execute("insert into identifiers(id) values(%s)".formatted(id));
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
            found = !template.queryForList("select id from identifiers where id=%s".formatted(id)).isEmpty();
            if(!found){
                try {
                    template.execute("insert into identifiers(id) values(%s)".formatted(id));
                } catch (Exception e) {
                    found = true;
                }
            }
        }
    }
}
