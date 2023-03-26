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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalDateTimeSerializationHandler implements SerializationHandler<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss-SSS");

    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.LOCAL_DATE_TIME;
    }

    @Override
    public void serialize(LocalDateTime value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        generator.writeString(value.format(dateTimeFormatter));
    }

    @Override
    public void processDuplicatingEntities(LocalDateTime value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, LocalDateTime currentValue) throws Exception {
        return LocalDateTime.parse(parser.getText(), dateTimeFormatter);
    }

    @Override
    public LocalDateTime clone(LocalDateTime source, LocalDateTime target, PropertySerializationMetadata prop, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public LocalDateTime toCachedObject(LocalDateTime source, LocalDateTime target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public LocalDateTime toStandardObject(LocalDateTime source, LocalDateTime target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }
}
