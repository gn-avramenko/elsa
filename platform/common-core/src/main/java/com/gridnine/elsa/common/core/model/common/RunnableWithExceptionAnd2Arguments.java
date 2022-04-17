/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

public interface RunnableWithExceptionAnd2Arguments<A1,A2> {
    void run(A1 arg1,A2 arg2) throws Exception;
}
