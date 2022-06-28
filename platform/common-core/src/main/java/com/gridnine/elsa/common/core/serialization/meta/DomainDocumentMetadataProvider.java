/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.domain.BaseDocument;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DocumentDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;

public class DomainDocumentMetadataProvider extends EntityMetadataProvider{
    DomainDocumentMetadataProvider(DocumentDescription documentDescription, DomainMetaRegistry dr, CustomMetaRegistry cr, RemotingMetaRegistry rr) {
        super(documentDescription, dr,  cr, rr,
                new SerializablePropertyDescription(BaseIdentity.Fields.id, SerializablePropertyType.LONG, null, false),
                new SerializablePropertyDescription(BaseDocument.Fields.versionInfo, SerializablePropertyType.ENTITY, VersionInfo.class.getName(), false)
                );
    }
}
