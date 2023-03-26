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
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class BigDecimalSerializationHandler implements SerializationHandler<BigDecimal> {
    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.BIG_DECIMAL;
    }

    @Override
    public void serialize(BigDecimal value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        generator.writeNumber(value);
    }

    @Override
    public void processDuplicatingEntities(BigDecimal value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public BigDecimal deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, BigDecimal currentValue) throws Exception {
        return BigDecimal.valueOf(parser.getDoubleValue());
    }

    @Override
    public BigDecimal clone(BigDecimal source, BigDecimal target, PropertySerializationMetadata prop, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public BigDecimal toCachedObject(BigDecimal source, BigDecimal target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }

    @Override
    public BigDecimal toStandardObject(BigDecimal source, BigDecimal target, PropertySerializationMetadata property, Map<Object, Object> processed) {
        return source;
    }
}
