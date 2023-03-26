/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization;

import com.gridnine.elsa.common.serialization.metadata.PropertySerializationMetadata;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.common.serialization.handlers.EntitySerializationHandler;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;

import java.util.HashMap;

public class Cloner {


    @SuppressWarnings("unchecked")
    public <T extends BaseIntrospectableObject> T clone(T source) {
        var result = (T) ReflectionFactory.get().newInstance(source.getClass().getName());
        copy(source, result);
        return result;
    }

    public<T extends BaseIntrospectableObject>  void copy (T source, T result){
        var pd = new PropertyDescription();
        pd.getAttributes().put("class-name", source.getClass().getName());
        var tg = new TagDescription();
        tg.setType("ENTITY");
        tg.setObjectIdAttributeName("class-name");
        var md = new PropertySerializationMetadata(pd, "ENTITY", null, tg, SerializationHandlersRegistry.get().getHandler("ENTITY"));
        EntitySerializationHandler.clone0(source, result, md, new HashMap<>());
    }

    public static Cloner get(){
        return Environment.getPublished(Cloner.class);
    }
}
