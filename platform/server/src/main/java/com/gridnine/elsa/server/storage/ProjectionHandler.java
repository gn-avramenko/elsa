/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.BaseProjection;

import java.util.List;
import java.util.Set;

public interface ProjectionHandler<D extends BaseDocument, I extends BaseProjection<D>> {
    Class<D> getDocumentClass();
    Class<I> getProjectionClass();
    List<I> createProjections(D doc, Set<String> properties) throws Exception;
}
