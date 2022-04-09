/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.utils;

public interface RunnableWithExceptionAndArgument<A> {
    void run(A arg) throws Exception;
}
