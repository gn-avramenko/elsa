/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gridnine.elsa.core.CoreSerializableTypesConfigurator;
import com.gridnine.elsa.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.core.serialization.SerializationHandler;
import com.gridnine.elsa.core.serialization.metadata.PropertySerializationMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ArrayListSerializationHandler implements SerializationHandler<ArrayList<?>> {
    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.ARRAY_LIST;
    }

    @Override
    public void serialize(ArrayList<?> value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String, Object> params) throws IOException {
        generator.writeStartArray();
        for (Object item : value) {
            var nestedProp = property.genericsSerializers.get("element-class-name");
            nestedProp.handler.serialize(item, nestedProp, generator, duplicatingEntities, processed, counter, params);
        }
        generator.writeEndArray();
    }

    @Override
    public void processDuplicatingEntities(ArrayList<?> value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        for (Object item : value) {
            var nestedProp = generics.get("element-class-name");
            nestedProp.handler.processDuplicatingEntities(item, nestedProp.genericsSerializers, duplicatingEntities, processed);
        }
    }

    public ArrayList<?> deserialize(JsonParser parser, PropertySerializationMetadata property, Map<String, Object> params, Map<Integer, Object> processed, ArrayList<?> currentValue) throws Exception {
        var array = (ArrayList<Object>) (currentValue != null ? currentValue: new ArrayList<>());
        while (parser.nextToken() != JsonToken.END_ARRAY){
            var nestedProp = property.genericsSerializers.get("element-class-name");
            var value = nestedProp.handler.deserialize(parser, nestedProp, params, processed, null);
            array.add(value);
        }
        return array;
    }
}