/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting.standard;

import com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionRequest;
import com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionResponse;
import com.gridnine.elsa.common.model.remoting.RAttribute;
import com.gridnine.elsa.common.model.remoting.REntityDescription;
import com.gridnine.elsa.common.model.remoting.REntityType;
import com.gridnine.elsa.common.model.remoting.RPropertyDescription;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.remoting.RemotingCallContext;
import com.gridnine.elsa.server.remoting.RemotingServerCallHandler;

import java.util.Map;

public class EntityDescriptionRemotingHandler implements RemotingServerCallHandler<GetRemotingEntityDescriptionRequest, GetRemotingEntityDescriptionResponse> {

    @Override
    public GetRemotingEntityDescriptionResponse service(GetRemotingEntityDescriptionRequest request, RemotingCallContext context) throws Exception {
        if(DomainMetaRegistry.get().getProjectionsIds().contains(request.getEntityId())){
            return createEntityDescription(REntityType.DOMAIN_DATABASE_ENTITY, SerializableMetaRegistry.get().getEntities().get(request.getEntityId()));
        }
        if(DomainMetaRegistry.get().getAssetsIds().contains(request.getEntityId())){
            return createEntityDescription(REntityType.DOMAIN_DATABASE_ENTITY, SerializableMetaRegistry.get().getEntities().get(request.getEntityId()));
        }
        if(DomainMetaRegistry.get().getEntitiesIds().contains(request.getEntityId())){
            return createEntityDescription(REntityType.DOMAIN_ENTITY, SerializableMetaRegistry.get().getEntities().get(request.getEntityId()));
        }
        if(DomainMetaRegistry.get().getDocumentsIds().contains(request.getEntityId())){
            return createEntityDescription(REntityType.DOMAIN_ENTITY, SerializableMetaRegistry.get().getEntities().get(request.getEntityId()));
        }
        if(CustomMetaRegistry.get().getEntitiesIds().contains(request.getEntityId())){
            return createEntityDescription(REntityType.CUSTOM, SerializableMetaRegistry.get().getEntities().get(request.getEntityId()));
        }
        if(RemotingMetaRegistry.get().getEntitiesIds().contains(request.getEntityId())){
            return createEntityDescription(REntityType.REMOTING, SerializableMetaRegistry.get().getEntities().get(request.getEntityId()));
        }
        throw new IllegalArgumentException("unable to find description for " + request.getEntityId());
    }

    private GetRemotingEntityDescriptionResponse createEntityDescription(REntityType type, EntityDescription entityDescription) {
        var result = new GetRemotingEntityDescriptionResponse();
        result.setType(type);
        var rDescription = new REntityDescription();
        rDescription.setId(entityDescription.getId());
        for(Map.Entry<String, String> attr: entityDescription.getAttributes().entrySet()){
            var rAttr = new RAttribute();
            rAttr.setName(attr.getKey());
            rAttr.setValue(attr.getValue());
            rDescription.getAttributes().add(rAttr);
        }
        for(PropertyDescription prop: entityDescription.getProperties().values()){
            var rProp = new RPropertyDescription();
            rProp.setId(prop.getId());
            rProp.setTagName(prop.getTagName());
            for(Map.Entry<String, String> attr: prop.getAttributes().entrySet()){
                var rAttr = new RAttribute();
                rAttr.setName(attr.getKey());
                rAttr.setValue(attr.getValue());
                rProp.getAttributes().add(rAttr);
            }
            rDescription.getProperties().add(rProp);
        }
        result.setDescription(rDescription);
        return result;
    }
}
