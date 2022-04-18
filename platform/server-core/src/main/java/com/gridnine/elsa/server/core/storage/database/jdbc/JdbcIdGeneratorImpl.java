/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc;

import com.gridnine.elsa.common.core.model.common.IdGenerator;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("ClassCanBeRecord")
public class JdbcIdGeneratorImpl implements IdGenerator {

    private final JdbcTemplate template;

    private final JdbcDialect dialect;

    public JdbcIdGeneratorImpl(JdbcTemplate template, JdbcDialect dialect) {
        this.template = template;
        this.dialect = dialect;
    }

    @SuppressWarnings("ConstantConditions")
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
