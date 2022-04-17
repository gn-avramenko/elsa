/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import com.gridnine.elsa.common.meta.domain.AssetDescription;

class AssetMetadataProvider extends BaseSearchableMetadataProvider {
    AssetMetadataProvider(AssetDescription description){
        super(description);
        addProperty(new SerializablePropertyDescription(BaseIdentity.Fields.id, SerializablePropertyType.LONG, null, false));
        addProperty(new SerializablePropertyDescription(BaseAsset.Fields.versionInfo, SerializablePropertyType.ENTITY, VersionInfo.class.getName(), false));
    }
}
