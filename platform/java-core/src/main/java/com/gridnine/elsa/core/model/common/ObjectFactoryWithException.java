/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.common;

public interface ObjectFactoryWithException<T> {
    T createObject() throws Exception;
}
