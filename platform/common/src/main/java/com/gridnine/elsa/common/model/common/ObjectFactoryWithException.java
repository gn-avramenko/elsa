/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

public interface ObjectFactoryWithException<T> {
    T createObject() throws Exception;
}
