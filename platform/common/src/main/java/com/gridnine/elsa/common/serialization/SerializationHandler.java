/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.gridnine.elsa.common.serialization.metadata.PropertySerializationMetadata;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public interface SerializationHandler<T> {
    String getType();

    void serialize(T value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String, Object> params) throws IOException;

    void processDuplicatingEntities(T value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed);

    T deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, T currentValue) throws Exception;

    T clone(T source, T target, PropertySerializationMetadata prop, Map<Object,Object> processed);

    T toCachedObject(T source, T target, PropertySerializationMetadata property, Map<Object, Object> processed);

    T toStandardObject(T source, T target, PropertySerializationMetadata property, Map<Object, Object> processed);

    T deserialize(String value, PropertySerializationMetadata nestedProp, Map<String, Object> params, T currentValue) throws Exception;

}
