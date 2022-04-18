/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;

public record OperationContext<I extends BaseIdentity> (GlobalOperationContext globalContext, LocalOperationContext<I> localContext) {
}
