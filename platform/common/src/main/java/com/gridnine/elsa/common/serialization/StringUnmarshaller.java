/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.common.serialization.metadata.ObjectSerializationMetadataProvider;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.meta.config.Environment;

import java.util.HashMap;
import java.util.Map;

public class StringUnmarshaller {

    public <T extends BaseIntrospectableObject> T unmarshal(String className, String data, Map<String, Object> params) {
        return ExceptionUtils.wrapException(() -> {
            var metadata = ObjectSerializationMetadataProvider.get().getMetadata(className);
            String[] parts = data.split("&");
            T result = ReflectionFactory.get().newInstance(className);
            var properties = new HashMap<String,String>();
            for(String part:  parts){
                var args = part.split("=");
                properties.put(args[0].trim(), args[1].trim());
            }
            for(Map.Entry<String, String> entry: properties.entrySet()){
                String name = entry.getKey();
                String value = entry.getValue();
                var prop = metadata.getAllProperties().get(name);
                if(prop.serializableTypeDescription != null && prop.serializableTypeDescription.isFinalField()){
                    prop.handler.deserialize(value, prop, params,  result.getValue(name));
                } else {
                    result.setValue(name, prop.handler.deserialize(value, prop, params,  null));
                }
            }
            return result;
        });

    }

    public static StringUnmarshaller get(){
        return Environment.getPublished(StringUnmarshaller.class);
    }
}
