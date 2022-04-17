/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.cache;

import java.util.Objects;

public record CachedValue<D>(long timeStamp,D value){
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CachedValue<?> that = (CachedValue<?>) o;
        return timeStamp == that.timeStamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp);
    }

}
