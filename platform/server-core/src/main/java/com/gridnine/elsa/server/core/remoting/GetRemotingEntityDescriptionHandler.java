/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

import com.gridnine.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.elsa.common.core.serialization.meta.SerializablePropertyType;
import com.gridnine.elsa.common.rest.core.*;
import org.springframework.beans.factory.annotation.Autowired;

public class GetRemotingEntityDescriptionHandler implements RemotingServerCallHandler<GetRemotingEntityDescriptionRequest, GetRemotingEntityDescriptionResponse> {

    @Autowired
    private ObjectMetadataProvidersFactory providersFactory;

    @Override
    public String getId() {
        return "core:meta:get-entity-description";
    }

    @Override
    public GetRemotingEntityDescriptionResponse service(GetRemotingEntityDescriptionRequest request, RemotingServerCallContext context) throws Exception {
        var provider = providersFactory.getProvider(request.getEntityId());
        var ed = new RemotingEntityDescription();
        provider.getAllProperties().forEach(prop -> {
            var pd=new RemotingEntityPropertyDescription();
            pd.setId(prop.id());
            pd.setClassName(prop.className());
            pd.setType(toType(prop.type()));
            pd.setIsAbsctract(prop.isAbstract());
            ed.getProperties().add(pd);
        });
        provider.getAllCollections().forEach(coll -> {
            var cd=new RemotingEntityCollectionDescription();
            cd.setId(coll.id());
            cd.setElementClassName(coll.elementClassName());
            cd.setElementType(toType(coll.elementType()));
            cd.setIsAbsctract(coll.isAbstract());
            ed.getCollections().add(cd);
        });
        provider.getAllMaps().forEach(map -> {
            var md=new RemotingEntityMapDescription();
            md.setId(map.id());
            md.setKeyClassName(map.keyClassName());
            md.setKeyType(toType(map.keyType()));
            md.setKeyIsAbsctract(map.keyIsAbstract());
            ed.getMaps().add(md);
        });
        var result = new GetRemotingEntityDescriptionResponse();
        result.setDescription(ed);
        return result;
    }

    private RemotingEntityValueType toType(SerializablePropertyType type) {
        return switch (type){
            default -> RemotingEntityValueType.valueOf(type.name());
        };
    }


}
