/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gridnine.elsa.core.CoreSerializableTypesConfigurator;
import com.gridnine.elsa.core.model.common.BaseIdentity;
import com.gridnine.elsa.core.model.common.CaptionProvider;
import com.gridnine.elsa.core.model.common.ClassMapper;
import com.gridnine.elsa.core.model.domain.EntityReference;
import com.gridnine.elsa.core.reflection.ReflectionFactory;
import com.gridnine.elsa.core.serialization.SerializationHandler;
import com.gridnine.elsa.core.serialization.StandardSerializationParameters;
import com.gridnine.elsa.core.serialization.metadata.PropertySerializationMetadata;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityReferenceSerializationHandler implements SerializationHandler<EntityReference<?>> {
    @Override
    public String getType() {
        return CoreSerializableTypesConfigurator.ENTITY_REFERENCE;
    }

    @Override
    public void serialize(EntityReference<?> value, PropertySerializationMetadata property, JsonGenerator generator, Set<Object> duplicatingEntities, Map<Object, Integer> processed, AtomicInteger counter, Map<String,Object> params) throws IOException {
        var serializeCaption = true;
        switch (StandardSerializationParameters.getEntityReferenceCaptionSerializationStrategy(params)){
            case ONLY_NOT_CACHED -> {
                serializeCaption = !"true".equals(SerializableMetaRegistry.get().getEntities().get(value.getType().getName()).getAttributes().get("cache-caption"));
            }
        }
        var serializeTypeAsName = true;
        var serializeTypeAsInt = false;
        switch (StandardSerializationParameters.getEntityReferenceTypeSerializationStrategy(params)){
            case ABSTRACT_CLASS_ID -> {
                serializeTypeAsInt = !"true".equals(SerializableMetaRegistry.get().getEntities().get(property.property.getAttributes().get(property.tagDescription.getObjectIdAttributeName())).getAttributes().get("abstract"));
                serializeTypeAsName = false;
            }
            case ALL_CLASS_NAME -> {
                serializeTypeAsInt = false;
                serializeTypeAsName = true;
            }
        }
        if(!serializeCaption && !serializeTypeAsInt && !serializeTypeAsName){
            generator.writeNumber(value.getId());
            return;
        }
        generator.writeStartObject();
        generator.writeNumberField("id", value.getId());
        if(serializeTypeAsInt){
            generator.writeNumberField("type", ClassMapper.get().getId(value.getType().getName()));
        } else if (serializeTypeAsName){
            generator.writeStringField("type", value.getType().getName());
        }
        if(serializeCaption && value.getCaption() != null){
            generator.writeStringField("caption", value.getCaption());
        }
        generator.writeEndObject();

    }

    @Override
    public void processDuplicatingEntities(EntityReference<?> value, Map<String, PropertySerializationMetadata> generics, Set<Object> duplicatingEntities, Set<Object> processed) {
        //noops
    }

    @Override
    public EntityReference<?> deserialize(JsonParser parser, PropertySerializationMetadata nestedProp, Map<String, Object> params, Map<Integer, Object> processed, EntityReference<?> currentValue) throws Exception {
        if(parser.currentToken() == JsonToken.START_OBJECT){
            var result = new EntityReference<>();
            while(parser.nextToken() != JsonToken.END_OBJECT){
                var tagName = parser.currentName();
                parser.nextToken();
                switch (tagName){
                    case BaseIdentity.Fields.id -> result.setId(parser.getLongValue());
                    case EntityReference.Fields.type -> {
                        switch (StandardSerializationParameters.getEntityReferenceTypeSerializationStrategy(params)){
                            case ALL_CLASS_NAME -> result.setType(ReflectionFactory.get().getClass(parser.getText()));
                            case ABSTRACT_CLASS_ID -> result.setType(ReflectionFactory.get().getClass(ClassMapper.get().getName(parser.getIntValue())));
                        }
                    }
                    case EntityReference.Fields.caption -> result.setCaption(parser.getText());
                }
            }
            if(result.getType() == null){
                result.setType(ReflectionFactory.get().getClass(nestedProp.property.getAttributes().get(nestedProp.tagDescription.getObjectIdAttributeName())));
            }
            var dd = DomainMetaRegistry.get().getDocumentsIds().contains(result.getType().getName());
            var ett = SerializableMetaRegistry.get().getEntities().get(result.getType().getName());
            if("true".equals(ett.getAttributes().get("cache-caption"))){
                result.setCaption(CaptionProvider.get().getCaption(result));
            }
            return result;
        }
        return null;
    }
}
