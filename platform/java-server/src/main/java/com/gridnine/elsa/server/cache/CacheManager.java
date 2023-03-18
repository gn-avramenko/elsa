/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.cache;

public interface CacheManager {
    <K,D> KeyValueCache<K, D> createKeyValueCache(Class<K> keyClass, Class<D> cls, String name, int capacity, int expirationInSeconds);

}
