/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.utils;

public interface CallableWithExceptionAndArgument<R,A> {
    R call(A arg) throws Exception;
}
