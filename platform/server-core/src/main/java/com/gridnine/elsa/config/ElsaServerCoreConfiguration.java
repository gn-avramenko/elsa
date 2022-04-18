/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.common.IdGenerator;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.server.core.codec.DesCodec;
import com.gridnine.elsa.server.core.storage.Storage;
import com.gridnine.elsa.server.core.storage.database.Database;
import com.gridnine.elsa.server.core.storage.database.DatabaseFactory;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcDatabaseMetadataProvider;
import com.gridnine.elsa.server.core.storage.transaction.ElsaTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ElsaServerCoreConfiguration {

    @Autowired
    private DatabaseFactory factory;

    @Bean
    public DesCodec desCodec(){
        return new DesCodec();
    }

    @Bean
    public Storage storage(){
        return new Storage();
    }

    @Bean
    public  Database primaryDatabase(){
        return factory.getPrimaryDatabase();
    }

    @Bean
    public  ClassMapper classMapper(){
        return factory.getClassMapper();
    }

    @Bean
    public  EnumMapper enumMapper(){
        return factory.getEnumMapper();
    }

    @Bean
    public  IdGenerator idGenerator(){
        return factory.getIdGenerator();
    }

    @Bean
    public  PlatformTransactionManager transactionManager(){
        return factory.getTransactionManager();
    }

    @Bean
    public ElsaTransactionManager elsaTransactionManager(){
        return new ElsaTransactionManager();
    }

    @Bean
    public JdbcDatabaseMetadataProvider databaseMetadataProvider(){
        return new JdbcDatabaseMetadataProvider();
    }
}
