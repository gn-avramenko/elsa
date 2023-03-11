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

public class BooleanSerializationHandler implements SerializationHandler<Boolean> {
    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.BOOLEAN;
    }

    @Override
    public void serialize(Boolean value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        generator.writeBoolean(value);
    }

    @Override
    public void processDuplicatingEntities(Boolean value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public Boolean deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, Boolean currentValue) throws Exception {
        return parser.getBooleanValue();
    }
}
