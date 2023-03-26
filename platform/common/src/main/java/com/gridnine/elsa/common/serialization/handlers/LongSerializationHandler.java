/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.gridnine.elsa.common.CoreSerializableTypesConfigurator;
import com.gridnine.elsa.common.serialization.SerializationHandler;
import com.gridnine.elsa.common.serialization.metadata.PropertySerializationMetadata;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LongSerializationHandler implements SerializationHandler<Long> {
    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.LONG;
    }

    @Override
    public void serialize(Long value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        generator.writeNumber(value);
    }

    @Override
    public void processDuplicatingEntities(Long value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public Long deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, Long currentValue) throws Exception {
        return parser.getLongValue();
    }

    @Override
    public Long clone(Long source, Long target, PropertySerializationMetadata prop, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public Long toCachedObject(Long source, Long target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public Long toStandardObject(Long source, Long target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }
}
