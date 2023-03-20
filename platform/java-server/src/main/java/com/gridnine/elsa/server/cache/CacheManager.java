/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.cache;

import com.gridnine.elsa.meta.config.Environment;

public interface CacheManager {
    <K,D> KeyValueCache<K, D> createKeyValueCache(Class<K> keyClass, Class<D> cls, String name, int capacity, int expirationInSeconds);

    public static CacheManager get(){
        return Environment.getPublished(CacheManager.class);
    }
}
