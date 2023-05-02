/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.remoting;

import com.gridnine.elsa.common.search.SearchQueryBuilder;
import com.gridnine.elsa.common.search.SortOrder;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjectionFields;
import com.gridnine.elsa.demo.model.remoting.GetIndexesRequest;
import com.gridnine.elsa.demo.model.remoting.GetIndexesResponse;
import com.gridnine.elsa.server.remoting.RemotingCallContext;
import com.gridnine.elsa.server.remoting.RemotingServerCallHandler;
import com.gridnine.elsa.server.storage.Storage;

public class GetIndexesHandler implements RemotingServerCallHandler<GetIndexesRequest, GetIndexesResponse> {
    @Override
    public GetIndexesResponse service(GetIndexesRequest request, RemotingCallContext context) throws Exception {
        var result = new GetIndexesResponse();
        result.getIndexes().addAll(Storage.get().searchDocuments(DemoDomainDocumentProjection.class,
                new SearchQueryBuilder().addOrder(DemoDomainDocumentProjectionFields.stringProperty, SortOrder.ASC).build()));
        return result;
    }
}
