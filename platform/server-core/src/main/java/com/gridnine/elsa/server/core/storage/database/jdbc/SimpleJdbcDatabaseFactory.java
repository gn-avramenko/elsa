/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc;

import com.gridnine.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.common.IdGenerator;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.server.core.storage.database.Database;
import com.gridnine.elsa.server.core.storage.database.DatabaseFactory;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDataSourceProvider;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcDatabaseMetadataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@ConditionalOnMissingBean
public class SimpleJdbcDatabaseFactory implements DatabaseFactory {

    private Database database;

    @Value("${elsa.storage.adapter:hsqldb}")
    private String adapterId;

    @Autowired
    private List<JdbcDataSourceProvider> providers;

    @Autowired
    private JdbcDatabaseMetadataProvider jdbcDatabaseMetadataProvider;

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Autowired
    private CustomMetaRegistry customMetaRegistry;

    @Autowired
    private ReflectionFactory reflectionFactory;

    @Autowired
    private SupportedLocalesProvider supportedLocalesProvider;

    private ClassMapper classMapper;

    private EnumMapper enumMapper;

    private IdGenerator idGenerator;

    private PlatformTransactionManager transactionManager;

    @PostConstruct
    public void init() throws Exception {
        var adapter = providers.stream().filter(it -> it.getId().equals(adapterId)).findFirst().orElse(null);
        if(adapter == null){
            throw Xeption.forDeveloper("unsupported adapter id: %s".formatted(adapterId));
        }
        DataSource ds = adapter.createDataSource();
        JdbcDialect dialect = adapter.createDialect();
        var template = new JdbcTemplate(ds);
        classMapper = new JdbcClassMapperImpl(domainMetaRegistry, customMetaRegistry, template ,dialect);
        enumMapper = new JdbcEnumMapperImpl(domainMetaRegistry, template ,supportedLocalesProvider, dialect);
        database = new JdbcDatabase(template, jdbcDatabaseMetadataProvider, enumMapper, classMapper,
                dialect, domainMetaRegistry, reflectionFactory);
        idGenerator = new JdbcIdGeneratorImpl(template, dialect);
        transactionManager = new JdbcTransactionManager(ds);
    }

    @Override
    public Database getPrimaryDatabase() {
        return database;
    }

    @Override
    public ClassMapper getClassMapper() {
        return classMapper;
    }

    @Override
    public EnumMapper getEnumMapper() {
        return enumMapper;
    }

    @Override
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }
}
