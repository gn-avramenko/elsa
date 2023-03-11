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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalDateSerializationHandler implements SerializationHandler<LocalDate> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.LOCAL_DATE;
    }

    @Override
    public void serialize(LocalDate value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        generator.writeString(value.format(dateFormatter));
    }

    @Override
    public void processDuplicatingEntities(LocalDate value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public LocalDate deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, LocalDate currentValue) throws Exception {
        return LocalDate.parse(parser.getText(), dateFormatter);
    }
}
