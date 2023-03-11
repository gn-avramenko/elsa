/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.gridnine.elsa.core.serialization.metadata.PropertySerializationMetadata;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public interface SerializationHandler<T> {
    String getType();

    void serialize(T value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String, Object> params) throws IOException;

    void processDuplicatingEntities(T value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed);

    T deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, T currentValue) throws Exception;
}
