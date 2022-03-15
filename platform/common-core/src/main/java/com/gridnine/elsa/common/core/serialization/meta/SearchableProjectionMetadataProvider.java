/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.common.meta.domain.SearchableProjectionDescription;

class SearchableProjectionMetadataProvider extends BaseSearchableMetadataProvider {
    SearchableProjectionMetadataProvider(SearchableProjectionDescription description){
        super(description);
        addProperty(new SerializablePropertyDescription(BaseSearchableProjection.Fields.document, SerializablePropertyType.ENTITY_REFERENCE, null, false));
        addProperty(new SerializablePropertyDescription(BaseSearchableProjection.Fields.navigationKey, SerializablePropertyType.INT, null, false));
    }
}
