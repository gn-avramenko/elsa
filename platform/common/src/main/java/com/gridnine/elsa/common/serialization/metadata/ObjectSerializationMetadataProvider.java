/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization.metadata;

import com.gridnine.elsa.common.serialization.SerializationHandlersRegistry;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.GenericDeclaration;
import com.gridnine.elsa.meta.serialization.GenericsDeclaration;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import com.gridnine.elsa.meta.serialization.SingleGenericDeclaration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectSerializationMetadataProvider {

    private final Map<String, ObjectSerializationMetadata> objects = new ConcurrentHashMap<>();

    public ObjectSerializationMetadata getMetadata(String objectId) {
        var result = objects.get(objectId);
        if (result != null) {
            return result;
        }
        return objects.computeIfAbsent(objectId, (id) -> {
            var res = new ObjectSerializationMetadata();
            fillMetadata(res, id);
            return res;
        });
    }

    private void fillMetadata(ObjectSerializationMetadata res, String id) {
        var eid = id;
        boolean document = false;
        boolean asset = false;
        boolean defaultClassProcessed = false;
        while (eid != null){
            EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(eid);
            Map<String, TagDescription> tags;
            if(DomainMetaRegistry.get().getAssetsIds().contains(eid)){
                tags = (Map) DomainTypesRegistry.get().getDatabaseTags();
                asset = true;
            }else if(DomainMetaRegistry.get().getProjectionsIds().contains(eid)){
                tags = (Map) DomainTypesRegistry.get().getDatabaseTags();
            } else if(DomainMetaRegistry.get().getDocumentsIds().contains(eid)){
                tags = DomainTypesRegistry.get().getEntityTags();
                document = true;
            } else if(DomainMetaRegistry.get().getEntitiesIds().contains(eid)){
                tags = DomainTypesRegistry.get().getEntityTags();
            } else if(CustomMetaRegistry.get().getEntitiesIds().contains(eid)){
                tags = CustomTypesRegistry.get().getEntityTags();
            } else if(RemotingMetaRegistry.get().getEntitiesIds().contains(eid)){
                tags = RemotingTypesRegistry.get().getEntityTags();
            } else {
                throw new UnsupportedOperationException("type %s is not supported".formatted(eid));
            }
            for(PropertyDescription prop: ed.getProperties().values()){
                var td = tags.get(prop.getTagName());
                var st = SerializableTypesRegistry.get().getTypes().get(td.getType());
                var pm = new PropertySerializationMetadata(prop, td.getType(), st, td, SerializationHandlersRegistry.get().getHandler(td.getType()));
                if(st != null && !st.getGenerics().isEmpty() && !td.getGenerics().isEmpty()) {
                    processGenerics(pm.genericsSerializers, td.getGenerics(), td, prop, st.getGenerics());
                }
                res.getAllProperties().put(prop.getId(), pm);
            }
            eid = ed.getAttributes().get("extends");
            if(eid == null && !defaultClassProcessed){
                if(document){
                    eid = "com.gridnine.elsa.common.model.domain.BaseDocument";
                } else if (asset){
                    eid = "com.gridnine.elsa.common.model.domain.BaseAsset";
                }
                defaultClassProcessed = true;
            }
        }

    }

    private void processGenerics(Map<String, PropertySerializationMetadata> genericsSerializers, List<GenericDescription> tGenerics, TagDescription td, PropertyDescription prop, List<GenericDeclaration> generics) {
        for(GenericDeclaration gd: generics){
            if(gd instanceof SingleGenericDeclaration sgd){
                var gDesc =tGenerics.stream().filter(it -> it.getId().equals(sgd.getId())).findFirst().get();
                var st = SerializableTypesRegistry.get().getTypes().get(gDesc.getType());
                var ntg = new TagDescription();
                ntg.setObjectIdAttributeName(gDesc.getObjectIdAttributeName());
                ntg.setType(gDesc.getType());
                var md = new PropertySerializationMetadata(prop, gDesc.getType(), st, ntg, SerializationHandlersRegistry.get().getHandler(gDesc.getType()));
                if(st != null && !st.getGenerics().isEmpty() && !gDesc.getNestedGenerics().isEmpty()){
                    processGenerics(md.genericsSerializers, gDesc.getNestedGenerics(), ntg, prop, st.getGenerics());
                }
                genericsSerializers.put(sgd.getId(), md);
                continue;
            }
            processGenerics(genericsSerializers, tGenerics, td, prop, ((GenericsDeclaration) gd).getGenerics());
        }
    }

    public static ObjectSerializationMetadataProvider get() {
        return Environment.getPublished(ObjectSerializationMetadataProvider.class);
    }
}
