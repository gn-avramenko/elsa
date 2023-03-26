/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization;

import com.gridnine.elsa.common.model.domain.CachedObject;
import com.gridnine.elsa.common.serialization.handlers.EntitySerializationHandler;
import com.gridnine.elsa.common.serialization.metadata.PropertySerializationMetadata;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.util.HashMap;

public class CachedObjectConverter {

    public<T> T toCachedObject(T object){
        if(object == null || object instanceof CachedObject){
            return object;
        }
        String className = object.getClass().getName();
        EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(className);
        if(ed != null){
            var pd = new PropertyDescription();
            pd.getAttributes().put("class-name", ed.getId());
            var tg = new TagDescription();
            tg.setType("ENTITY");
            tg.setObjectIdAttributeName("class-name");
            var md = new PropertySerializationMetadata(pd, "ENTITY", null, tg, SerializationHandlersRegistry.get().getHandler("ENTITY"));
            return (T) EntitySerializationHandler.toCachedObject0((BaseIntrospectableObject) object, null, md, new HashMap<>());
        }
        var typeId = SerializableTypesRegistry.get().getTypes().values().stream().filter(it -> className.equals(it.getJavaQualifiedName())).findFirst().get().getId();
        return (T) SerializationHandlersRegistry.get().getHandler(typeId).toCachedObject(object, null, null, null);
    }

    public<T> T toStandardObject(T object){
        if(!(object instanceof CachedObject)){
            return object;
        }
        String className = object.getClass().getName().replace("_Cached", "");
        EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(className);
        if(ed != null){
            var pd = new PropertyDescription();
            pd.getAttributes().put("class-name", ed.getId());
            var tg = new TagDescription();
            tg.setType("ENTITY");
            tg.setObjectIdAttributeName("class-name");
            var md = new PropertySerializationMetadata(pd, "ENTITY", null, tg, SerializationHandlersRegistry.get().getHandler("ENTITY"));
            return (T) EntitySerializationHandler.toStandardObject0((BaseIntrospectableObject) object, null, md, new HashMap<>());
        }
        var typeId = SerializableTypesRegistry.get().getTypes().values().stream().filter(it -> className.equals(it.getJavaQualifiedName())).findFirst().get().getId();
        return (T) SerializationHandlersRegistry.get().getHandler(typeId).toStandardObject(object, null, null, null);
    }

    public static CachedObjectConverter get(){
        return Environment.getPublished(CachedObjectConverter.class);
    }
}
