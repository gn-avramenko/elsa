/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

public interface RunnableWithExceptionAnd4Arguments<A1,A2,A3,A4> {
    void run(A1 arg1,A2 arg2,A3 arg3,A4 arg4) throws Exception;
}
