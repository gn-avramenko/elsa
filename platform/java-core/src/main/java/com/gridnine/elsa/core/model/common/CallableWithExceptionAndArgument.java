/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.common;

public interface CallableWithExceptionAndArgument<R,A> {
    R call(A arg) throws Exception;
}
