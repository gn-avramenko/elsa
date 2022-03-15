/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.common;

import com.gridnine.elsa.common.core.ElsaCommonCoreCustomMetaRegistryConfigurator;
import com.gridnine.elsa.common.core.lock.LockManager;
import com.gridnine.elsa.common.core.lock.LockTemplate;
import com.gridnine.elsa.common.core.lock.standard.StandardLockManager;
import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.CachedCaptionProvider;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.core.serialization.CachedObjectConverter;
import com.gridnine.elsa.common.core.serialization.Cloner;
import com.gridnine.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.elsa.common.core.test.ElsaCommonCoreTestDomainMetaRegistryConfigurator;
import com.gridnine.elsa.common.core.test.ElsaCommonCoreTestRestMetaRegistryConfigurator;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.dataTransfer.DataTransferMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistryConfigurator;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistryConfigurator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ElsaCoreTestConfiguration {

    @Bean
    public ReflectionFactory reflectionFactory() {
        return new ReflectionFactory();
    }

    @Bean
    public Environment environment() {
        return new Environment();
    }

    @Bean
    public LockManager lockManager() {
        return new StandardLockManager();
    }

    @Bean
    public LockTemplate lockTemplate() {
        return new LockTemplate();
    }

    @Bean
    public DomainMetaRegistry domainMetaRegistry() {
        return new DomainMetaRegistry();
    }

    @Bean
    public CustomMetaRegistry customMetaRegistry() {
        return new CustomMetaRegistry();
    }

    @Bean
    public RestMetaRegistry restMetaRegistry() {
        return new RestMetaRegistry();
    }

    @Bean
    public DataTransferMetaRegistry dataTransferMetaRegistry() {
        return new DataTransferMetaRegistry();
    }

    @Bean
    public DomainMetaRegistryConfigurator coreTestDomainConfigurator() {
        return new ElsaCommonCoreTestDomainMetaRegistryConfigurator();
    }

    @Bean
    public RestMetaRegistryConfigurator coreTestRestConfigurator() {
        return new ElsaCommonCoreTestRestMetaRegistryConfigurator();
    }

    @Bean
    public ObjectMetadataProvidersFactory objectMetadataProviderFactory(){
        return new ObjectMetadataProvidersFactory();
    }

    @Bean
    public JsonMarshaller jsonMarshaller(){
        return new JsonMarshaller();
    }

    @Bean
    public ElsaCommonCoreCustomMetaRegistryConfigurator coreCustomMetaRegistryConfigurator(){
        return new ElsaCommonCoreCustomMetaRegistryConfigurator();
    }

    @Bean
    @ConditionalOnMissingBean(value = EnumMapper.class)
    public EnumMapper enumMapper(){
        return new EnumMapper(){

            @Override
            public int getId(Enum<?> value) {
                return value.ordinal();
            }

            @Override
            public String getName(int id, Class<Enum<?>> cls) {
                for(Enum<?> item : cls.getEnumConstants()){
                    if(item.ordinal() == id){
                        return item.name();
                    }
                }
                return null;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(value = ClassMapper.class)
    public ClassMapper classMapper(){
        return new ClassMapper() {
            @Override
            public int getId(String name) {
                throw Xeption.forDeveloper("not implemented");
            }

            @Override
            public String getName(int id) {
                throw Xeption.forDeveloper("not implemented");
            }
        };
    }
    @Bean
    @ConditionalOnMissingBean(value = CachedCaptionProvider.class)
    public CachedCaptionProvider captionActualizer(){
        return EntityReference::getCaption;
    }

    @Bean
    public JsonUnmarshaller jsonUnmarshaller(){
        return new JsonUnmarshaller();
    }

    @Bean
    public Cloner cloner(){
        return new Cloner();
    }

    @Bean
    public CachedObjectConverter cachedObjectConverter(){
        return new CachedObjectConverter();
    }
}
