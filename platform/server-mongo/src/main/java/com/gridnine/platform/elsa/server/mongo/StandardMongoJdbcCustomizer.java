/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo;

import com.gridnine.platform.elsa.common.meta.common.BaseModelElementDescription;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.storage.database.jdbc.JdbcDatabaseCustomizer;
import com.gridnine.platform.elsa.core.storage.database.jdbc.model.JdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class StandardMongoJdbcCustomizer implements JdbcDatabaseCustomizer {

    private final Set<String> ignoredTables;

    public StandardMongoJdbcCustomizer(@Autowired DomainMetaRegistry dmr){
        ignoredTables = new HashSet<>();
        dmr.getAssets().values().forEach(a ->{
                ignoredTables.add(JdbcUtils.getVersionTableName(a.getId()));
        });
        dmr.getDocuments().values().forEach(a ->{
                ignoredTables.add(JdbcUtils.getVersionTableName(a.getId()));
        });
    }

    @Override
    public Set<String> getIgnoredTableNames() {
        return ignoredTables;
    }
}
