/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization.metadata;

import com.gridnine.elsa.common.serialization.SerializationHandler;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.serialization.SerializableType;

import java.util.LinkedHashMap;
import java.util.Map;

public class PropertySerializationMetadata {
    public final PropertyDescription property;
    public final SerializableType serializableTypeDescription;

    public final String serializableType;
    public final TagDescription tagDescription;

    public final SerializationHandler<Object> handler;

    public final Map<String,PropertySerializationMetadata> genericsSerializers = new LinkedHashMap<>();

    public PropertySerializationMetadata(PropertyDescription property, String serializableType, SerializableType serializableTypeDescription,
                                         TagDescription tagDescription, SerializationHandler<Object> handler) {
        this.property = property;
        this.serializableTypeDescription = serializableTypeDescription;
        this.serializableType = serializableType;
        this.tagDescription = tagDescription;
        this.handler = handler;
    }
}
