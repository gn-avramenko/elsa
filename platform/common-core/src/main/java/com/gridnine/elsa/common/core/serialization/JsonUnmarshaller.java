/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.CaptionProvider;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.core.serialization.meta.BaseObjectMetadataProvider;
import com.gridnine.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.elsa.common.core.serialization.meta.SerializablePropertyType;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

public class JsonUnmarshaller {

    @Autowired
    private ObjectMetadataProvidersFactory metadataProvidersFactory;

    @Autowired
    private ReflectionFactory reflectionFactory;

    @Autowired
    private EnumMapper enumMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private CaptionProvider captionProvider;

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss-SSS");

    private final JsonFactory jsonFactory = new JsonFactory();
    public <T> T unmarshal(Class<T> cls, InputStream is, SerializationParameters params) {
        return unmarshal(cls.getName(), is, params);

    }
    public <T> T unmarshal(String className, InputStream is, SerializationParameters params) {
        return ExceptionUtils.wrapException(() -> {
            try (var parser = jsonFactory.createParser(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return unmarshal(parser, className, params);
            }
        });
    }
    @SuppressWarnings("unchecked")
    private <T> T unmarshal(JsonParser parser, String className, SerializationParameters params) throws Exception {
        T result = null;
        var realClassName = className;
        var provider = (BaseObjectMetadataProvider<Object>) metadataProvidersFactory.getProvider(realClassName);
        while(parser.nextToken() != JsonToken.END_OBJECT){
            if(parser.currentToken() == JsonToken.START_ARRAY){
                var nextToken = parser.nextToken();
                if (nextToken == JsonToken.END_OBJECT){
                    result = reflectionFactory.newInstance(realClassName);
                    break;
                }
            }
            var tagName = parser.currentName();
            if(JsonMarshaller.CLASS_NAME_PROPERTY.equals(tagName)){
               parser.nextToken();
               realClassName = parser.getText();
               provider = (BaseObjectMetadataProvider<Object>) metadataProvidersFactory.getProvider(realClassName);
               continue;
            }
            if(result == null){
                result = reflectionFactory.newInstance(realClassName);
            }
            var propertyDescription = provider.getProperty(tagName);
            if(propertyDescription != null){
                var value = readJsonValue(propertyDescription.type(), propertyDescription.className(),  parser, params);
                provider.setPropertyValue(result, propertyDescription.id(), value);
                continue;
            }
            var collectionDescription = provider.getCollection(tagName);
            if(collectionDescription != null){
                var collection = (Collection<Object>) provider.getCollection(result, tagName);
                while (parser.nextToken() != JsonToken.END_ARRAY){
                    if (parser.currentToken() == JsonToken.START_ARRAY) {
                        parser.nextToken();
                    }
                    var value = switch (collectionDescription.elementType()){
                        case STRING -> parser.getText();
                        case ENUM -> switch (params.getEnumSerializationStrategy()){
                            case ID -> reflectionFactory.safeGetEnum(collectionDescription.elementClassName(),
                                    enumMapper.getName(parser.getIntValue(),
                                    reflectionFactory.getClass(collectionDescription.elementClassName())));
                            case NAME -> reflectionFactory.safeGetEnum(collectionDescription.elementClassName(), parser.getText());
                        };
                        case CLASS -> switch (params.getClassSerializationStrategy()){
                            case ID -> reflectionFactory.getClass(classMapper.getName(parser.getIntValue()));
                            case NAME -> reflectionFactory.getClass(parser.getText());
                        };
                        case ENTITY -> unmarshal(parser, collectionDescription.elementClassName(), params);
                        case BIG_DECIMAL -> BigDecimal.valueOf(parser.getDoubleValue());
                        case INT -> parser.getIntValue();
                        case ENTITY_REFERENCE -> readEntityReference(parser, className, params);
                        case LONG -> parser.getLongValue();
                        case BYTE_ARRAY -> parser.getBinaryValue();
                        case LOCAL_DATE_TIME -> dateTimeFormatter.parse(parser.getText());
                        case LOCAL_DATE -> dateFormatter.parse(parser.getText());
                        case BOOLEAN -> parser.getBooleanValue();
                    };
                    collection.add(value);
                }
                continue;
            }
            var mapDescription = provider.getMap(tagName);
            if(mapDescription != null){
                var map = ((Map<Object,Object>)provider.getMap(result, tagName));
                parser.nextToken();
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    parser.nextToken();
                    var keyValue = readJsonValue(mapDescription.keyType(), mapDescription.keyClassName(), parser, params);
                    parser.nextToken();
                    var valueValue = readJsonValue(mapDescription.valueType(), mapDescription.valueClassName(), parser, params);
                    map.put(keyValue, valueValue);
                    parser.nextToken();
                }
            }
        }
        return result;
    }

    private Object readJsonValue(SerializablePropertyType type, String className,  JsonParser parser, SerializationParameters params) throws Exception {
        return switch (type) {
            case STRING -> {
                parser.nextToken();
                yield parser.getText();
            }
            case ENUM -> {
                parser.nextToken();
                yield  switch (params.getEnumSerializationStrategy()){
                    case ID -> reflectionFactory.safeGetEnum(className, enumMapper.getName(parser.getIntValue(), reflectionFactory.getClass(className)));
                    case NAME -> reflectionFactory.safeGetEnum(className, parser.getText());
                };
            }
            case CLASS -> {
                parser.nextToken();
                yield  switch (params.getClassSerializationStrategy()){
                    case ID -> reflectionFactory.getClass(classMapper.getName(parser.getIntValue()));
                    case NAME -> reflectionFactory.getClass(parser.getText());
                };
            }
            case ENTITY_REFERENCE -> {
                parser.nextToken();
                yield readEntityReference(parser, className, params);
            }
            case ENTITY -> {
                parser.nextToken();
                yield unmarshal(parser, className, params);
            }
            case BIG_DECIMAL -> {
                parser.nextToken();
                yield BigDecimal.valueOf(parser.getDoubleValue());
            }
            case INT -> {
                parser.nextToken();
                yield parser.getIntValue();
            }
            case LONG -> {
                parser.nextToken();
                yield parser.getLongValue();
            }
            case BYTE_ARRAY -> {
                parser.nextToken();
                yield parser.getBinaryValue();
            }
            case LOCAL_DATE_TIME -> {
                parser.nextToken();
                yield LocalDateTime.parse(parser.getText(), dateTimeFormatter);
            }
            case LOCAL_DATE -> {
                parser.nextToken();
                yield LocalDate.parse(parser.getText(), dateFormatter);
            }
            case BOOLEAN -> {
                parser.nextToken();
                yield parser.getBooleanValue();
            }
        };
    }

    private EntityReference<?> readEntityReference(JsonParser parser, String className, SerializationParameters params) throws IOException {
        var result = new EntityReference<>();
        while(parser.nextToken() != JsonToken.END_OBJECT){
            var tagName = parser.currentName();
            parser.nextToken();
            switch (tagName){
                case BaseIdentity.Fields.id -> result.setId(parser.getLongValue());
                case EntityReference.Fields.type -> {
                    switch (params.getEntityReferenceTypeSerializationStrategy()){
                        case ALL_CLASS_NAME -> result.setType(reflectionFactory.getClass(parser.getText()));
                        case ABSTRACT_CLASS_ID -> result.setType(reflectionFactory.getClass(classMapper.getName(parser.getIntValue())));
                    }
                }
                case EntityReference.Fields.caption -> result.setCaption(parser.getText());
            }
        }
        if(result.getType() == null){
            if(metadataProvidersFactory.getProvider(className).isAbstract()) {
                throw Xeption.forDeveloper("no classname is provided for abstract type %s".formatted(className));
            } else {
                result.setType(reflectionFactory.getClass(className));
            }
        }
        var dd = domainMetaRegistry.getDocuments().get(result.getType().getName());
        var ad = domainMetaRegistry.getAssets().get(result.getType().getName());
        if((dd != null && (dd.isCacheCaption() || dd.isCacheResolve()))||(ad != null && (ad.isCacheCaption()|| ad.isCacheResolve()))){
            result.setCaption(captionProvider.getCaption(result));
        }
        return result;
    }

}
