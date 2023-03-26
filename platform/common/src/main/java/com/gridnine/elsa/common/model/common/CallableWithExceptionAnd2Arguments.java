/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

public interface CallableWithExceptionAnd2Arguments<R,A1,A2> {
    R call(A1 arg1,A2 arg2) throws Exception;
}
