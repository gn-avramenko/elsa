/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.gridnine.elsa.core.CoreSerializableTypesConfigurator;
import com.gridnine.elsa.core.serialization.SerializationHandler;
import com.gridnine.elsa.core.serialization.metadata.PropertySerializationMetadata;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IntSerializationHandler implements SerializationHandler<Integer> {
    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.INT;
    }

    @Override
    public void serialize(Integer value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        generator.writeNumber(value);
    }

    @Override
    public void processDuplicatingEntities(Integer value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public Integer deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, Integer currentValue) throws Exception {
        return parser.getIntValue();
    }

    @Override
    public Integer clone(Integer source, Integer target, PropertySerializationMetadata prop, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public Integer toCachedObject(Integer source, Integer target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public Integer toStandardObject(Integer source, Integer target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }
}
