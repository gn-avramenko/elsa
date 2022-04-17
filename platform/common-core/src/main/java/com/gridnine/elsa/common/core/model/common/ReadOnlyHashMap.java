/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

import java.util.HashMap;
import java.util.Map;

public class ReadOnlyHashMap<K,T> extends HashMap<K,T> {
    private boolean allowChanges = false;

    public void setAllowChanges(boolean allowChanges) {
        this.allowChanges = allowChanges;
    }

    @Override
    public T put(K key, T value) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.put(key, value);
    }

    @Override
    public void clear() {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.clear();
    }

    @Override
    public T remove(Object key) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends T> m) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.putAll(m);
    }

    @Override
    public boolean remove(Object key, Object value) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.remove(key, value);
    }
}
