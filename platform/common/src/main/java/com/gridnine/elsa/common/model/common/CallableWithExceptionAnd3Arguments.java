/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

public interface CallableWithExceptionAnd3Arguments<R,A1,A2,A3> {
    R call(A1 arg1,A2 arg2,A3 arg3) throws Exception;
}
