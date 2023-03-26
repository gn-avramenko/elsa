/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

public interface CallableWithExceptionAndArgument<R,A> {
    R call(A arg) throws Exception;
}
