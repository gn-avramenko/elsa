/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.cache.ehCache;

import com.gridnine.elsa.server.core.cache.CachedValue;
import com.gridnine.elsa.server.core.cache.KeyValueCache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.nio.ByteBuffer;
import java.time.Duration;

@Component
public class EhCacheManager implements com.gridnine.elsa.server.core.cache.CacheManager {
    private final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);


    @Autowired
    private Environment env;

    @PreDestroy
    public void close(){
        cacheManager.close();
    }

    @Override
    public <K, D> KeyValueCache<K, D> createKeyValueCache(Class<K> keyClass, Class<D> cls, String name, int capacity, int expirationInSeconds) {
        var delegate = cacheManager.createCache(name,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, CachedValue.class, ResourcePoolsBuilder.heap(capacity))
                        .withValueSerializer(new Serializer<>() {
                            @Override
                            public ByteBuffer serialize(CachedValue object) throws SerializerException {
                                return null;
                            }

                            @Override
                            public CachedValue read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
                                return null;
                            }

                            @Override
                            public boolean equals(CachedValue object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
                                return false;
                            }
                        })
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(expirationInSeconds))));
        return new KeyValueCache<>() {
            @Override
            public CachedValue<D> get(K key) {
                return delegate.get(key);
            }

            @Override
            public void put(K key, CachedValue<D> value) {
                delegate.put(key, value);
            }

            @Override
            public void replace(K key, CachedValue<D> oldValue, CachedValue<D> newValue) {
                if (oldValue == null) {
                    delegate.putIfAbsent(key, newValue);
                } else {
                    delegate.replace(key, oldValue, newValue);
                }
            }
        };
    }
}
