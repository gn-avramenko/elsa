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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteArraySerializationHandler implements SerializationHandler<byte[]> {


    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.BYTE_ARRAY;
    }

    @Override
    public void serialize(byte[] value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String, Object> params) throws IOException {
        generator.writeString(Base64.getEncoder().encodeToString(value));
    }

    @Override
    public void processDuplicatingEntities(byte[] value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public byte[] deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, byte[] currentValue) throws Exception {
        return Base64.getDecoder().decode(parser.getValueAsString());
    }

    @Override
    public byte[] clone(byte[] source, byte[] target, PropertySerializationMetadata prop, Map<Object, Object> processed) {
        return Arrays.copyOf(source, source.length);
    }

    @Override
    public byte[] toCachedObject(byte[] source, byte[] target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public byte[] toStandardObject(byte[] source, byte[] target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public byte[] deserialize(String value, PropertySerializationMetadata nestedProp, Map<String, Object> params, byte[] currentValue) throws Exception {
        return Base64.getDecoder().decode(value);
    }


}
