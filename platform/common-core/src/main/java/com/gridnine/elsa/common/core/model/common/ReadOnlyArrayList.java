/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

import java.util.ArrayList;
import java.util.Collection;

public class ReadOnlyArrayList<T> extends ArrayList<T> {
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

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.addAll(c);
    }

    @Override
    public void add(int index, T element) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.add(index, element);
    }

    @Override
    public T remove(int index) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.removeAll(c);
    }

    @Override
    public boolean remove(Object o) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        return super.remove(o);
    }
}
