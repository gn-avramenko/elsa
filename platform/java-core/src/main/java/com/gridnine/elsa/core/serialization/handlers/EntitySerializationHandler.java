/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gridnine.elsa.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.core.model.common.ClassMapper;
import com.gridnine.elsa.core.reflection.ReflectionFactory;
import com.gridnine.elsa.core.serialization.SerializationHandler;
import com.gridnine.elsa.core.serialization.StandardSerializationParameters;
import com.gridnine.elsa.core.serialization.metadata.ObjectSerializationMetadataProvider;
import com.gridnine.elsa.core.serialization.metadata.PropertySerializationMetadata;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class EntitySerializationHandler implements SerializationHandler<BaseIntrospectableObject> {

    final static String INTERNAL_ID_PROPERTY="_id";

    final static String CLASS_NAME_PROPERTY = "_cn";

    @Override
    public String getType() {
        return "ENTITY";
    }

    public void serialize(BaseIntrospectableObject obj, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        var ett = SerializableMetaRegistry.get().getEntities().get(property.property.getAttributes().get(property.tagDescription.getObjectIdAttributeName()));
        serialize(obj, "true".equals(ett.getAttributes().get("abstract")), generator, duplicatingEntities, processed, counter, params);
    }

    public static void serialize(BaseIntrospectableObject obj,boolean isAbstract, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        if(processed.containsKey(obj)){
            generator.writeStartObject();
            generator.writeNumberField(INTERNAL_ID_PROPERTY, processed.get(obj));
            generator.writeEndObject();
            return;
        }
        processed.put(obj, counter.incrementAndGet());
        var key = obj.getClass().getName();
        var index = key.indexOf("_Cached");
        if(index != -1){
            key = key.substring(0, key.lastIndexOf("."))+"."+key.substring(index+7);
        }
        var metadata = ObjectSerializationMetadataProvider.get().getMetadata(key);
        generator.writeStartObject();
        if(duplicatingEntities.contains(obj)){
            generator.writeNumberField(INTERNAL_ID_PROPERTY, counter.incrementAndGet());
        }
        if(isAbstract){
            switch (StandardSerializationParameters.getClassSerializationStrategy(params)){
                case NAME ->generator.writeStringField(CLASS_NAME_PROPERTY, key);
                case ID -> generator.writeNumberField(CLASS_NAME_PROPERTY, ClassMapper.get().getId(key));
            }
        }
        for(var prop: metadata.getAllProperties().entrySet()){
            var value = obj.getValue(prop.getKey());
            if(value != null){
                generator.writeFieldName(prop.getKey());
                var data = prop.getValue();
                data.handler.serialize(value, data, generator, duplicatingEntities, processed, counter, params);
            }
        }
        generator.writeEndObject();
    }

    @Override
    public void processDuplicatingEntities(BaseIntrospectableObject obj, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        processDuplicatingEntities0(obj, generics, duplicatingEntities, processed);
    }

    public static void processDuplicatingEntities0(BaseIntrospectableObject obj, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed){
        if(processed.contains(obj)){
            duplicatingEntities.add(obj);
            return;
        }
        var key = obj.getClass().getName();
        var index = key.indexOf("_Cached");
        if(index != -1){
            key = key.substring(0, key.lastIndexOf("."))+"."+key.substring(index+7);
        }
        var data = ObjectSerializationMetadataProvider.get().getMetadata(key);
        processed.add(obj);
        for(var prop: data.getAllProperties().entrySet()){
            var value = obj.getValue(prop.getKey());
            if(value != null){
                if(processed.contains(value)){
                    duplicatingEntities.add(value);
                }
                prop.getValue().handler.processDuplicatingEntities(value, prop.getValue().genericsSerializers, duplicatingEntities, processed);
            }
        }
    }

    @Override
    public BaseIntrospectableObject deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, BaseIntrospectableObject currentValue) throws Exception {
        return deserialize0(parser, nestedProp, params, processed, currentValue);
    }

    public static BaseIntrospectableObject deserialize0(JsonParser parser, PropertySerializationMetadata property, Map<String,Object> params, Map<Integer, Object> processed, BaseIntrospectableObject currentValue) throws Exception {
        BaseIntrospectableObject result = null;
        String className = property.property.getAttributes().get(property.tagDescription.getObjectIdAttributeName());
        var realClassName = className;
        var metadata = ObjectSerializationMetadataProvider.get().getMetadata(className);
        Integer id = null;
        while(parser.nextToken() != JsonToken.END_OBJECT){
            if(parser.currentToken() == JsonToken.START_OBJECT){
                continue;
            }
            var tagName = parser.currentName();
            if(INTERNAL_ID_PROPERTY.equals(tagName)){
                parser.nextToken();
                id = parser.getIntValue();
                if(processed.containsKey(id)){
                    parser.nextToken();
                    return (BaseIntrospectableObject) processed.get(id);
                }
                continue;
            }
            if(CLASS_NAME_PROPERTY.equals(tagName)){
                parser.nextToken();
                switch (StandardSerializationParameters.getClassSerializationStrategy(params)){
                    case NAME -> realClassName = parser.getText();
                    case ID -> realClassName = ClassMapper.get().getName(parser.getIntValue());
                }
                metadata = ObjectSerializationMetadataProvider.get().getMetadata(realClassName);
                continue;
            }
            if(result == null){
                result = ReflectionFactory.get().newInstance(realClassName);
                if(id != null){
                    processed.put(id, result);
                }
            }
            var prop = metadata.getAllProperties().get(tagName);
            parser.nextToken();
            if(prop.serializableTypeDescription != null && prop.serializableTypeDescription.isFinalField()){
                prop.handler.deserialize(parser, prop, params, processed, result.getValue(tagName));
            } else {
                result.setValue(tagName, prop.handler.deserialize(parser, prop, params, processed, null));
            }
        }
        return result;
    }
}
