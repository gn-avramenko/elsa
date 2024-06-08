/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo;

import com.gridnine.platform.elsa.common.core.model.domain.BaseDocument;

public interface MongoStorageCustomizer {

    String KEY = "MONGO_STORAGE_CUSTOMIZER";

    <T extends BaseDocument> String getCollectionName(Class<?> entityType);

}
