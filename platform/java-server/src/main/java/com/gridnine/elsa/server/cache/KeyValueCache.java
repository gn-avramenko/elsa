/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.cache;

public interface KeyValueCache<K, D> {
    CachedValue<D> get(K key);

    void put(K key, CachedValue<D> value);

    boolean replace(K key, CachedValue<D> oldValue, CachedValue<D> newValue);

}
