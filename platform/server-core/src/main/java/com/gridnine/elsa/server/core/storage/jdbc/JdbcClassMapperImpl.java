/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.server.core.storage.jdbc.structureUpdater.JdbcStructureUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class JdbcClassMapperImpl implements ClassMapper {
    private Map<String,Integer> name2Id = new HashMap<>();
    private Map<Integer,String> id2name = new HashMap<>();
    @Autowired
    private DomainMetaRegistry metaRegistry;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcStructureUpdater updater;


    @Autowired
    JdbcDialect dialect;

    @Override
    public int getId(String name) {
        if(!name2Id.containsKey(name)){
            throw Xeption.forDeveloper("mapping for class %s was not found".formatted(name));
        }
        return name2Id.get(name);
    }

    @Override
    public String getName(int id) {
        if(!id2name.containsKey(id)){
            throw Xeption.forDeveloper("mapping for id %s was not found".formatted(id));
        }
        return id2name.get(id);
    }

    @PostConstruct
    private void init(){
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.queryForList("select id, classname from classmapping").forEach(map ->{
            var id = ((Number) map.get("id")).intValue();
            var className = (String) map.get("classname");
            name2Id.put(className, id);
            id2name.put(id, className);
        });
        metaRegistry.getEnums().values().forEach(it -> check(template, it.getId()));
        metaRegistry.getDocuments().values().forEach(it ->{
            if(!it.isAbstract()) {
                check(template, it.getId());
            }
        });
        metaRegistry.getEntities().values().forEach(it ->{
            if(!it.isAbstract()) {
                check(template, it.getId());
            }
        });
        metaRegistry.getAssets().values().forEach(it ->{
            if(!it.isAbstract()) {
                check(template, it.getId());
            }
        });
        metaRegistry.getSearchableProjections().values().forEach(it -> check(template, it.getId()));
    }

    private void check(JdbcTemplate template, String className) {
        if(!name2Id.containsKey(className)){
            Integer id = null;
            while (id == null || id2name.containsKey(id)) {
                    id = template.query(dialect.getSequenceNextValueSql("intid"), rs -> {
                    rs.next();
                    return ((ResultSet) rs).getInt(1);
                });
            }
            template.execute("insert into classmapping(id, classname) values (%s, '%s')".formatted(id, className));
            id2name.put(id, className);
            name2Id.put(className, id);
        }
    }
}
