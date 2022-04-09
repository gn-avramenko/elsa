/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.utils.HasPriority;

public interface StorageInterceptor extends HasPriority {
  default <A extends BaseAsset> void onSave(A asset, OperationContext<A> context) {}

  default   <A extends BaseAsset> void onDelete(A oldAssset, OperationContext<A> operationContext) {
  }
}
