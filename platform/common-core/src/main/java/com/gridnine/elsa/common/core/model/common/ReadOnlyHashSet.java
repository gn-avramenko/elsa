/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;


import java.util.Collection;
import java.util.HashSet;

public class ReadOnlyHashSet<T> extends HashSet<T> {
    private boolean allowChanges = false;

    public void setAllowChanges(boolean allowChanges) {
        this.allowChanges = allowChanges;
    }

    @Override
    public boolean add(T t) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.add(t);
    }

    @Override
    public void clear() {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.clear();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean addAll(Collection<? extends T> coll) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.addAll(coll);
    }

    @Override
    public boolean removeAll(Collection<?> coll) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.removeAll(coll);
    }

    @Override
    public boolean remove(Object o) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.remove(o);
    }
}
