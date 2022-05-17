/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.common.core.l10n.Localizer;
import com.gridnine.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.core.lock.LockManager;
import com.gridnine.elsa.common.core.lock.standard.StandardLockManager;
import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.domain.CaptionProvider;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.core.serialization.CachedObjectConverter;
import com.gridnine.elsa.common.core.serialization.Cloner;
import com.gridnine.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Configuration
public class ElsaCommonCoreConfiguration {

    @Bean
    public Localizer localizer(){
        return new Localizer();
    }

    @Bean
    public SupportedLocalesProvider supportedLocalesProvider(){
        return new SupportedLocalesProvider();
    }

    @Bean
    public LockManager standardLockManager(){
        return new StandardLockManager();
    }


    @Bean
    @ConditionalOnMissingBean
    public CaptionProvider standardCaptionProvider(){
        return EntityReference::getCaption;
    }

    @Bean
    @ConditionalOnMissingBean
    public EnumMapper standardEnumMapper(){
        return new EnumMapper() {
            @Override
            public int getId(Enum<?> value) {
                return value.ordinal();
            }

            @Override
            public String getName(int id, Class<Enum<?>> cls) {
                return Stream.of(cls.getEnumConstants()).filter(it -> it.ordinal() == id).map(Enum::name).findFirst().orElse(null);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ClassMapper fakeClassMapper(){
        return new ClassMapper() {
            private AtomicInteger ref = new AtomicInteger(0);
            private final Map<Integer, String> id2NameMap = new ConcurrentHashMap<>();
            private final Map<String, Integer> name2IdMap = new ConcurrentHashMap<>();
            @Override
            public int getId(String name) {
                var res = name2IdMap.get(name);
                if(res == null){
                    synchronized (this){
                        res = name2IdMap.get(name);
                        if(res == null){
                            res = ref.incrementAndGet();
                            name2IdMap.put(name, res);
                            id2NameMap.put(res, name);
                        }
                    }
                }
                return name2IdMap.get(name);
            }

            @Override
            public String getName(int id) {
                return id2NameMap.get(id);
            }
        };
    }

    @Bean
    public ReflectionFactory reflectionFactory(){
        return new ReflectionFactory();
    }

    @Bean
    public CachedObjectConverter cachedObjectConverter(){
        return new CachedObjectConverter();
    }

    @Bean
    public Cloner cloner(){
        return new Cloner();
    }

    @Bean
    public JsonMarshaller jsonMarshaller(){
        return new JsonMarshaller();
    }

    @Bean
    public JsonUnmarshaller jsonUnmarshaller(){
        return new JsonUnmarshaller();
    }

    @Bean
    public ObjectMetadataProvidersFactory objectMetadataProvidersFactory(){
        return new ObjectMetadataProvidersFactory();
    }

    @Bean
    public ElsaCommonCoreCustomMetaRegistryConfigurator elsaCommonCoreCustomMetaRegistryConfigurator(){
        return new ElsaCommonCoreCustomMetaRegistryConfigurator();
    }
}
