/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting.standard;

import com.gridnine.elsa.common.model.remoting.RGenericDeclaration;
import com.gridnine.elsa.common.model.remoting.RGenericDescription;
import com.gridnine.elsa.common.model.remoting.RSerializableType;
import com.gridnine.elsa.common.model.remoting.RTagDescription;
import com.gridnine.elsa.common.model.remoting.TypesMetadata;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.GenericDeclaration;
import com.gridnine.elsa.meta.serialization.GenericsDeclaration;
import com.gridnine.elsa.meta.serialization.SerializableType;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import com.gridnine.elsa.meta.serialization.SingleGenericDeclaration;
import com.gridnine.elsa.server.remoting.RemotingServerCallContext;
import com.gridnine.elsa.server.remoting.RemotingServerCallHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypesMetadataRemotingHandler implements RemotingServerCallHandler<Void, TypesMetadata> {
    @Override
    public TypesMetadata service(Void request, RemotingServerCallContext context) throws Exception {
        var result = new TypesMetadata();
        for(SerializableType item: SerializableTypesRegistry.get().getTypes().values()){
            var type = new RSerializableType();
            type.setId(item.getId());
            processGenericDeclarations(item.getGenerics(), type.getGenerics());
            result.getSerializableTypes().add(type);
        }
        processTags(DomainTypesRegistry.get().getDatabaseTags().values(), result.getDomainDatabaseTags());
        processTags(DomainTypesRegistry.get().getEntityTags().values(), result.getDomainEntityTags());
        processTags(CustomTypesRegistry.get().getEntityTags().values(), result.getCustomEntityTags());
        processTags(RemotingTypesRegistry.get().getEntityTags().values(), result.getRemotingEntityTags());
        processTags(L10nTypesRegistry.get().getParameterTypeTags().values(), result.getL10nParameterTypeTags());
        return result;
    }

    private<T extends TagDescription> void processTags(Collection<T> values, ArrayList<RTagDescription> remotingValues) {
        for(T item: values){
            var type = new RTagDescription();
            type.setType(item.getType());
            type.setTagName(item.getTagName());
            processGenerics(item.getGenerics(), type.getGenerics());
            remotingValues.add(type);
        }
    }

    private void processGenerics(List<GenericDescription> modelGenerics, ArrayList<RGenericDescription> remotingGenerics) {
        for(GenericDescription mg: modelGenerics){
            var item = new RGenericDescription();
            item.setId(mg.getId());
            item.setType(mg.getType());
            item.setObjectIdAttributeName(mg.getObjectIdAttributeName());
            processGenerics(mg.getNestedGenerics(), item.getNestedGenerics());
            remotingGenerics.add(item);
        }
    }

    private void processGenericDeclarations(List<GenericDeclaration> modelTypes, ArrayList<RGenericDeclaration> remotingTypes) {
        for(GenericDeclaration mt: modelTypes){
            var generic = new RGenericDeclaration();
            if(mt instanceof SingleGenericDeclaration sgd){
                generic.setId(sgd.getId());
            } else {
                processGenericDeclarations(((GenericsDeclaration) mt).getGenerics(), generic.getNestedGenerics() );
            }
            remotingTypes.add(generic);
        }
    }
}
