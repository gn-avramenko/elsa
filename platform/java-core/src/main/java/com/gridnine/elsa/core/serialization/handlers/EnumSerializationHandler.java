/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.gridnine.elsa.core.model.common.EnumMapper;
import com.gridnine.elsa.core.reflection.ReflectionFactory;
import com.gridnine.elsa.core.serialization.SerializationHandler;
import com.gridnine.elsa.core.serialization.StandardSerializationParameters;
import com.gridnine.elsa.core.serialization.metadata.PropertySerializationMetadata;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class EnumSerializationHandler implements SerializationHandler<Enum<?>> {
    @Override
    public String getType() {
        return "ENUM";
    }

    @Override
    public void serialize(Enum<?> value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        switch (StandardSerializationParameters.getEnumSerializationStrategy(params)){
            case ID -> generator.writeNumber(EnumMapper.get().getId(value));
            case NAME -> generator.writeString(value.name());
        }
    }

    @Override
    public void processDuplicatingEntities(Enum<?> value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public Enum<?> deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, Enum<?> currentValue) throws Exception {
        String className = nestedProp.property.getAttributes().get(nestedProp.tagDescription.getObjectIdAttributeName());
        return switch (StandardSerializationParameters.getEnumSerializationStrategy(params)){
            case ID -> ReflectionFactory.get().safeGetEnum(className, EnumMapper.get().getName(parser.getIntValue(), ReflectionFactory.get().getClass(className)));
            case NAME -> ReflectionFactory.get().safeGetEnum(className, parser.getText());
        };
    }
}
