/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.cache.ehCache;

import com.gridnine.elsa.server.cache.CachedValue;
import com.gridnine.elsa.server.cache.KeyValueCache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.time.Duration;

public class EhCacheManager implements com.gridnine.elsa.server.cache.CacheManager {
    private final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);


    public void close(){
        cacheManager.close();
    }

    @Override
    public <K, D> KeyValueCache<K, D> createKeyValueCache(Class<K> keyClass, Class<D> cls, String name, int capacity, int expirationInSeconds) {
        var delegate = cacheManager.createCache(name,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, CachedValue.class, ResourcePoolsBuilder.heap(capacity))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(expirationInSeconds))));
        return new KeyValueCache<>() {
            @SuppressWarnings("unchecked")
            @Override
            public CachedValue<D> get(K key) {
                return delegate.get(key);
            }

            @Override
            public void put(K key, CachedValue<D> value) {
                delegate.put(key, value);
            }

            @Override
            public boolean replace(K key, CachedValue<D> oldValue, CachedValue<D> newValue) {
                if (oldValue == null) {
                    return delegate.putIfAbsent(key, newValue) == null;
                } else {
                    return delegate.replace(key, oldValue, newValue);
                }
            }
        };
    }
}
