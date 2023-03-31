/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

public interface RunnableWithExceptionAnd3Arguments<A1,A2,A3> {
    void run(A1 arg1,A2 arg2, A3 arg3) throws Exception;
}
