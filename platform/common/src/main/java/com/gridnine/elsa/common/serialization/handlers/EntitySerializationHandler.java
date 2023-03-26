/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gridnine.elsa.common.model.common.ClassMapper;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.common.serialization.SerializationHandler;
import com.gridnine.elsa.common.serialization.StandardSerializationParameters;
import com.gridnine.elsa.common.serialization.metadata.ObjectSerializationMetadataProvider;
import com.gridnine.elsa.common.serialization.metadata.PropertySerializationMetadata;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.domain.CachedObject;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.IOException;
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

    @Override
    public BaseIntrospectableObject clone(BaseIntrospectableObject source, BaseIntrospectableObject target, PropertySerializationMetadata prop, Map<Object, Object> processed) {
        return clone0(source, target, prop, processed);
    }

    @Override
    public BaseIntrospectableObject toCachedObject(BaseIntrospectableObject source, BaseIntrospectableObject target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return toCachedObject0(source, target, property, processed);
    }

    @Override
    public BaseIntrospectableObject toStandardObject(BaseIntrospectableObject source, BaseIntrospectableObject target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return toStandardObject0(source, target, property, processed);
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


    public static BaseIntrospectableObject clone0(BaseIntrospectableObject source, BaseIntrospectableObject tgt, PropertySerializationMetadata property, Map<Object, Object> processed) {
        if(processed.containsKey(source)){
            return (BaseIntrospectableObject) processed.get(source);
        }
        BaseIntrospectableObject target = tgt == null ? ReflectionFactory.get().newInstance(source.getClass().getName()): tgt;
        var metadata = ObjectSerializationMetadataProvider.get().getMetadata(source.getClass().getName());
        for(var prop: metadata.getAllProperties().entrySet()){
            var value = source.getValue(prop.getKey());
            if(value == null){
                target.setValue(prop.getKey(), null);
                continue;
            }
            if(prop.getValue().serializableTypeDescription != null && prop.getValue().serializableTypeDescription.isFinalField()){
                prop.getValue().handler.clone(value, target.getValue(prop.getKey()), prop.getValue(), processed);
            } else {
                target.setValue(prop.getKey(), prop.getValue().handler.clone(value, null, prop.getValue(), processed));
            }
        }
        return target;
    }

    public static BaseIntrospectableObject toCachedObject0(BaseIntrospectableObject source, BaseIntrospectableObject tgt, PropertySerializationMetadata property, Map<Object, Object> processed) {
        if(processed.containsKey(source)){
            return (BaseIntrospectableObject) processed.get(source);
        }
        if(source instanceof CachedObject){
            return source;
        }
        final var className = source.getClass().getName();
        final var idx = className.lastIndexOf(".");
        final var cachedClassName = className.substring(0, idx)+"._Cached"+className.substring(idx+1);
        final var target = (BaseIntrospectableObject) ReflectionFactory.get().newInstance(cachedClassName);
        ((CachedObject)  target).setAllowChanges(true);
        var metadata = ObjectSerializationMetadataProvider.get().getMetadata(source.getClass().getName());
        for(var prop: metadata.getAllProperties().entrySet()){
            var value = source.getValue(prop.getKey());
            if(value == null){
                target.setValue(prop.getKey(), null);
                continue;
            }
            if(prop.getValue().serializableTypeDescription != null && prop.getValue().serializableTypeDescription.isFinalField()){
                prop.getValue().handler.toCachedObject(value, target.getValue(prop.getKey()), prop.getValue(), processed);
            } else {
                target.setValue(prop.getKey(), prop.getValue().handler.toCachedObject(value, null, prop.getValue(), processed));
            }
        }
        ((CachedObject)  target).setAllowChanges(false);
        return target;
    }

    public static BaseIntrospectableObject toStandardObject0(BaseIntrospectableObject source, BaseIntrospectableObject tgt, PropertySerializationMetadata property, Map<Object, Object> processed) {
        if(processed.containsKey(source)){
            return (BaseIntrospectableObject) processed.get(source);
        }
        if(!(source instanceof CachedObject)){
            return source;
        }
        final var className = source.getClass().getName().replace("_Cached","");
        final var target = (BaseIntrospectableObject) ReflectionFactory.get().newInstance(className);
        var metadata = ObjectSerializationMetadataProvider.get().getMetadata(className);
        for(var prop: metadata.getAllProperties().entrySet()){
            var value = source.getValue(prop.getKey());
            if(value == null){
                target.setValue(prop.getKey(), null);
                continue;
            }
            if(prop.getValue().serializableTypeDescription != null && prop.getValue().serializableTypeDescription.isFinalField()){
                prop.getValue().handler.toStandardObject(value, target.getValue(prop.getKey()), prop.getValue(), processed);
            } else {
                target.setValue(prop.getKey(), prop.getValue().handler.toStandardObject(value, null, prop.getValue(), processed));
            }
        }
        return target;
    }
}
