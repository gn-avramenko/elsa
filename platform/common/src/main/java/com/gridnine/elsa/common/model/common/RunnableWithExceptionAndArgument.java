/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

public interface RunnableWithExceptionAndArgument<A> {
    void run(A arg) throws Exception;
}
