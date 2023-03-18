/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.core.model.common.BaseIdentity;

public record OperationContext<I extends BaseIdentity> (GlobalOperationContext globalContext, LocalOperationContext<I> localContext) {
}
