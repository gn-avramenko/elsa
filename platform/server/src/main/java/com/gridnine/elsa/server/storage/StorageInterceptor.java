/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.common.model.common.HasPriority;
import com.gridnine.elsa.common.model.domain.BaseAsset;
import com.gridnine.elsa.common.model.domain.BaseDocument;

public interface StorageInterceptor extends HasPriority {
  default <A extends BaseAsset> void onSave(A asset, OperationContext<A> context)  throws Exception{}

  default   <A extends BaseAsset> void onDelete(A oldAssset, OperationContext<A> operationContext) throws Exception{
  }

   default <D extends BaseDocument> void onSave(D doc, OperationContext<D> context) throws Exception{}

    default  <D extends BaseDocument> void onDelete(D document, OperationContext<D> operationContext) throws Exception {}


}
