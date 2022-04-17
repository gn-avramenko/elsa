/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.domain.BaseDocument;
import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;

import java.util.List;
import java.util.Set;

public interface SearchableProjectionHandler<D extends BaseDocument, I extends BaseSearchableProjection<D>> {
    Class<D> getDocumentClass();
    Class<I> getProjectionClass();
    List<I> createProjections(D doc, Set<String> properties) throws Exception;
}
