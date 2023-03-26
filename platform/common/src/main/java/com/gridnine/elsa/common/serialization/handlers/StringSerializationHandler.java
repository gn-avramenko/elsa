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

public class StringSerializationHandler implements SerializationHandler<String> {
    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.STRING;
    }

    @Override
    public void serialize(String value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        generator.writeString(value);
    }

    @Override
    public void processDuplicatingEntities(String value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public String deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, String currentValue) throws Exception {
        return parser.getValueAsString();
    }

    @Override
    public String clone(String source, String target, PropertySerializationMetadata prop, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public String toCachedObject(String source, String target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public String toStandardObject(String source, String target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }
}
