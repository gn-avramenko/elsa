/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo;

import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.model.domain.*;
import com.gridnine.platform.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.platform.elsa.common.core.search.*;
import com.gridnine.platform.elsa.common.core.serialization.meta.*;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import org.bson.*;
import org.bson.types.Decimal128;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings("unchecked")
public class MongoConverter {

    private final static String CLASS_NAME_PROPERTY = "_cn";

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final DateTimeFormatter instantFormatter = DateTimeFormatter.ISO_INSTANT;

    private final ObjectMetadataProvidersFactory metadataProvidersFactory;

    private final ReflectionFactory reflectionFactory;

    private final CaptionProvider captionProvider;

    public MongoConverter(ObjectMetadataProvidersFactory metadataProvidersFactory, CaptionProvider captionProvider, ReflectionFactory reflectionFactory) {
        this.metadataProvidersFactory = metadataProvidersFactory;
        this.reflectionFactory = reflectionFactory;
        this.captionProvider = captionProvider;
    }

    public Query toMongoQuery(SearchQuery query, Class<?> entityType) {
        var result = new Query();
        String className = entityType.getName();
        if (!query.getPreferredFields().isEmpty()) {
            result.fields().include(query.getPreferredFields().toArray(new String[]{}));
        }
        if (!TextUtils.isBlank(query.getFreeText())) {
            result.addCriteria(Criteria.where("aggregatedData").regex(query.getFreeText(), "i"));
        }
        result.skip(query.getOffset());
        result.limit(query.getLimit() == 0 ? 200 : query.getLimit());
        var whereParts = new HashMap<String, Map<String, Criteria>>();
        if (!query.getOrders().isEmpty()) {
            var orders = query.getOrders().entrySet().stream().map((k) -> new Sort.Order(k.getValue() == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, k.getKey())).toArray(Sort.Order[]::new);
            result.with(Sort.by(orders));
        }
        if (query.getCriterions().size() == 1) {
            result.addCriteria(toCriteria(query.getCriterions().get(0), className));
        } else if (!query.getCriterions().isEmpty()) {
            result.addCriteria(new Criteria().andOperator(query.getCriterions().stream().map(it -> toCriteria(it, className)).toList()));
        }
        return result;
    }

    private Criteria toCriteria(SearchCriterion searchCriterion, String className) {
        if (searchCriterion instanceof SimpleCriterion sc) {
            BaseObjectMetadataProvider<?> provider = metadataProvidersFactory.getProvider(className);
            if (sc.operation == SimpleCriterion.Operation.CONTAINS) {
                SerializableCollectionDescription coll = provider.getCollection(sc.property);
                switch (sc.operation) {
                    case CONTAINS -> {
                        return Criteria.where(sc.property).is(sc.value);
                    }
                    default -> throw Xeption.forDeveloper("unsupported operation " + sc.operation);
                }
            } else {
                String propertyName;
                Object value;
                if (sc.property.contains(".")) {//NestedProperty
                    propertyName = sc.property;
                    value = sc.value;
                } else {
                    SerializablePropertyDescription property = provider.getProperty(sc.property);
                    propertyName = toQueryPropertyName(property.id(), property);
                    value = toQueryValue(sc.value, property);
                }
                switch (sc.operation) {
                    case EQ -> {
                        if (value == null) {
                            return Criteria.where(propertyName).isNull();
                        }
                        return Criteria.where(propertyName).is(value);
                    }
                    case NE -> {
                        return Criteria.where(propertyName).ne(value);
                    }
                    case LIKE -> {
                        return Criteria.where(propertyName).regex((String) value);
                    }
                    case ILIKE -> {
                        return Criteria.where(propertyName).regex((String) value, "i");
                    }
                    case GT -> {
                        return Criteria.where(propertyName).gt(value);
                    }
                    case LT -> {
                        return Criteria.where(propertyName).lt(value);
                    }
                    case GE -> {
                        return Criteria.where(propertyName).gte(value);
                    }
                    case LE -> {
                        return Criteria.where(propertyName).lte(value);
                    }
                    default -> throw Xeption.forDeveloper("unsupported operation " + sc.operation);
                }
            }
        }
        if (searchCriterion instanceof JunctionCriterion sc) {
            if (sc.disjunction) {
                return new Criteria().orOperator(sc.criterions.stream().map(it -> toCriteria(it, className)).toArray(Criteria[]::new));
            }
            return new Criteria().andOperator(sc.criterions.stream().map(it -> toCriteria(it, className)).toArray(Criteria[]::new));
        }
        if (searchCriterion instanceof BetweenCriterion sc) {
            throw Xeption.forDeveloper("unsupported criterion type: between");
        }
        if (searchCriterion instanceof NotCriterion nc) {
            throw Xeption.forDeveloper("unsupported criterion type: not");
        }
        if (searchCriterion instanceof CheckCriterion cc) {
            switch (cc.check) {
                case IS_EMPTY -> {
                    return Criteria.where(cc.property + ".0").exists(false);
                }
                case NOT_EMPTY -> {
                    return Criteria.where(cc.property + ".0").exists(true);
                }
                case IS_NULL -> {
                    return Criteria.where(cc.property).isNull();
                }
                case IS_NOT_NULL -> {
                    return Criteria.where(cc.property).not().isNull();
                }
            }
        }
        if (searchCriterion instanceof InCriterion<?> ic) {
            BaseObjectMetadataProvider<?> provider = metadataProvidersFactory.getProvider(className);
            SerializablePropertyDescription property = provider.getProperty(ic.property);

            return Criteria.where(toQueryPropertyName(ic.property, property)).in(ic.values.stream().map(it -> toQueryValue(it, property)).toList());
        }
        throw Xeption.forDeveloper("unsupported criterion type " + searchCriterion.getClass());
    }

    private String toQueryPropertyName(String id, SerializablePropertyDescription propertyDescription) {
        switch (propertyDescription.type()) {
            case STRING, ENUM, ENTITY_REFERENCE,LONG, INT, BIG_DECIMAL, LOCAL_DATE_TIME, LOCAL_DATE, BOOLEAN, CLASS, INSTANT -> {
                return id;
            }
            case ENTITY -> throw Xeption.forDeveloper("unsupported property type ENTITY");
            case BYTE_ARRAY -> throw Xeption.forDeveloper("unsupported property type BYTE_ARRAY");
        }
        throw Xeption.forDeveloper("unsupported id " + id);
    }

    @SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
    private Object toQueryValue(Object value, SerializablePropertyDescription property) {
        if (value == null) {
            return null;
        }
        switch (property.type()) {
            case STRING, LONG, INT, BIG_DECIMAL, BOOLEAN -> {
                return value;
            }
            case ENUM -> {
                return ((Enum<?>) value).name();
            }
            case ENTITY -> {
                throw Xeption.forDeveloper("unsupported property type ENTITY");
            }
            case ENTITY_REFERENCE -> {
                return ((EntityReference<?>) value).getId();
            }
            case LOCAL_DATE_TIME -> {
                return dateTimeFormatter.format((LocalDateTime) value);
            }
            case LOCAL_DATE -> {
                return dateFormatter.format((LocalDateTime) value);
            }
            case CLASS -> {
                return ((Class<?>) value).getName();
            }
            case BYTE_ARRAY -> {
                throw Xeption.forDeveloper("unsupported property type BYTE_ARRAY");
            }
            case INSTANT -> {
                return Date.from((Instant) value);
            }
        }
        throw Xeption.forDeveloper("unsupported property type " + property.type());
    }

    public <A extends BaseIntrospectableObject> A fromDocument(Document doc, Class<A> cls) {
        if (doc == null) {
            return null;
        }
        return ExceptionUtils.wrapException(() -> unmarshal(doc, cls.getName()));
    }

    private <A extends BaseIntrospectableObject> A unmarshal(Map<String, Object> doc, String className) {
        var realClassName = (String) doc.getOrDefault(CLASS_NAME_PROPERTY, null);
        if (realClassName == null) {
            realClassName = className;
        }
        A result = reflectionFactory.newInstance(realClassName);
        BaseObjectMetadataProvider<?> provider = metadataProvidersFactory.getProvider(realClassName);
        for (SerializablePropertyDescription prop : provider.getAllProperties()) {
            if(prop.id().equals("id") && (result instanceof BaseAsset || result instanceof BaseDocument || result instanceof BaseSearchableProjection<?>)) {
                result.setValue("id", doc.get("_id"));
                continue;
            }
            result.setValue(prop.id(), getModelValue(doc.getOrDefault(prop.id(), null), prop.type(), prop.isAbstract(), prop.className()));
        }
        for (SerializableCollectionDescription coll : provider.getAllCollections()) {
            List<?> array = (ArrayList<?>) doc.getOrDefault(coll.id(), null);
            if (array != null) {
                var modelColl = (Collection<Object>) result.getCollection(coll.id());
                array.forEach(item -> modelColl.add(getModelValue(item, coll.elementType(), coll.isAbstract(), coll.elementClassName())));
            }
        }
        for (SerializableMapDescription mapDescription : provider.getAllMaps()) {
            BsonArray array = (BsonArray) doc.getOrDefault(mapDescription.id(), null);
            if (array != null) {
                var modelMap = (Map<Object, Object>) result.getMap(mapDescription.id());
                array.forEach(item -> modelMap.put(getModelValue(item.asDocument().get("key"), mapDescription.keyType(), mapDescription.keyIsAbstract(),
                                mapDescription.keyClassName()),
                        getModelValue(item.asDocument().get("value"), mapDescription.valueType(), mapDescription.valueIsAbstract(),
                                mapDescription.valueClassName())));
            }
        }
        return result;
    }

    private Object getModelValue(Object o, SerializablePropertyType type, boolean anAbstract, String className) {
        if (o == null && type != SerializablePropertyType.INT && type != SerializablePropertyType.LONG && type != SerializablePropertyType.BOOLEAN) {
            return null;
        }
        switch (type) {
            case STRING -> {
                return o;
            }
            case BOOLEAN -> {
                return Boolean.TRUE.equals(o);
            }
            case INT -> {
                return o == null ? 0 : ((Number) o).intValue();
            }
            case LONG -> {
                return o == null ? 0 : ((Number) o).longValue();
            }
            case CLASS -> {
                return reflectionFactory.getClass((String) o);
            }
            case ENUM -> {
                return reflectionFactory.safeGetEnum(className, (String) o);
            }
            case ENTITY -> {
                return unmarshal((Map<String, Object>) o, className);
            }
            case BIG_DECIMAL -> {
                return (o instanceof Decimal128) ? ((Decimal128) o).bigDecimalValue() : BigDecimal.valueOf(((Number) o).doubleValue());
            }
            case BYTE_ARRAY -> {
                return ((BsonBinary) o).getData();
            }
            case LOCAL_DATE_TIME -> {
                return LocalDateTime.parse((String) o, dateTimeFormatter);
            }
            case LOCAL_DATE -> {
                return LocalDate.parse((String) o, dateFormatter);
            }
            case INSTANT -> {
                return o instanceof Date ? ((Date) o).toInstant() : null;
            }
            case ENTITY_REFERENCE -> {
                var id = (String) o;
                var ref = new EntityReference<>(id, reflectionFactory.getClass(className), null);
                ref.setCaption(captionProvider.getCaption(ref));
                return ref;
            }
        }
        throw Xeption.forDeveloper("unsupported property type " + type);
    }

    public <A> Document toDocument(A object, Document existingDocument) {
        Document document = existingDocument != null ? existingDocument : new Document();
        ExceptionUtils.wrapException(() ->
                marshal(document, object, false, existingDocument, new HashSet<>()));
        return document;
    }

    private void marshal(Map<String, Object> document, Object obj, boolean isAbstract, Map<String, Object> existingDocument, Set<Object> processed) throws Exception {
        if (!(obj instanceof EntityReference<?>) && processed.contains(obj)) {
            throw Xeption.forDeveloper("object %s is serialized multiple times".formatted(obj));
        }
        processed.add(obj);
        var key = obj.getClass().getName();
        var index = key.indexOf("_Cached");
        if (index != -1) {
            key = key.substring(0, key.lastIndexOf(".")) + "." + key.substring(index + 7);
        }
        @SuppressWarnings("unchecked") var provider = (BaseObjectMetadataProvider<Object>) metadataProvidersFactory.getProvider(key);
        if (existingDocument != null) {
            document.putAll(existingDocument);
        } else if(obj instanceof BaseAsset || obj instanceof BaseDocument || obj instanceof BaseSearchableProjection<?>) {
            document.put("_id", provider.getPropertyValue(obj, "id"));
        }
        if (isAbstract) {
            document.put(CLASS_NAME_PROPERTY, key);
        }
        for (SerializablePropertyDescription prop : provider.getAllProperties()) {
            if(prop.id().equals("id")){
                continue;
            }
            var value = provider.getPropertyValue(obj, prop.id());
            if (value != null) {

                document.put(prop.id(), getBsonValue(value, prop.type(), prop.isAbstract(),
                        Optional.ofNullable(existingDocument)
                                .map(ed -> ed.get(prop.id())).orElse(null), processed));
            } else {
                document.remove(prop.id());
            }
        }
        for (SerializableCollectionDescription coll : provider.getAllCollections()) { // todo: add support for collections of nested entities
            document.remove(coll.id());
            Collection<?> values = provider.getCollection(obj, coll.id());
            if (!values.isEmpty()) {
                var array = new BsonArray();
                document.put(coll.id(), array);
                for (Object value : values) {
                    array.add(getBsonValue(value, coll.elementType(), coll.isAbstract(), processed));
                }
            }
        }
        for (SerializableMapDescription mapDescription : provider.getAllMaps()) { // todo: add support for maps of nested entities
            document.remove(mapDescription.id());
            var map = provider.getMap(obj, mapDescription.id());
            if (!map.isEmpty()) {
                var array = new BsonArray();
                document.put(mapDescription.id(), array);
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    BsonDocument entryValue = new BsonDocument();
                    entryValue.put("key", getBsonValue(entry.getKey(), mapDescription.keyType(), mapDescription.keyIsAbstract(), processed));
                    entryValue.put("value", getBsonValue(entry.getKey(), mapDescription.keyType(), mapDescription.keyIsAbstract(), processed));
                    array.add(entryValue);
                }
            }
        }
    }

    private BsonValue getBsonValue(Object value, SerializablePropertyType type, boolean isAbstract,
                                   Set<Object> processed) throws Exception {
        return getBsonValue(value, type, isAbstract, null, processed);
    }

    private BsonValue getBsonValue(Object value, SerializablePropertyType type, boolean isAbstract,
                                   Object existingValue, Set<Object> processed) throws Exception {
        switch (type) {
            case STRING -> {
                return new BsonString((String) value);
            }
            case CLASS -> {
                return new BsonString(((Class<?>) value).getName());
            }
            case ENUM -> {
                return new BsonString(((Enum<?>) value).name());
            }
            case ENTITY -> {
                var document = new Document();
                marshal(document, value, isAbstract, (Map<String, Object>) existingValue, processed);
                return document.toBsonDocument();
            }
            case BIG_DECIMAL -> {
                return new BsonDouble(((BigDecimal) value).doubleValue());
            }
            case INT -> {
                return new BsonInt32((Integer) value);
            }
            case LONG -> {
                return new BsonInt64((Long) value);
            }
            case BOOLEAN -> {
                return new BsonBoolean((Boolean) value);
            }
            case BYTE_ARRAY -> {
                return new BsonBinary((byte[]) value);
            }
            case LOCAL_DATE_TIME -> {
                return new BsonString(dateTimeFormatter.format((LocalDateTime) value));
            }
            case LOCAL_DATE -> {
                return new BsonString(dateFormatter.format((LocalDate) value));
            }
            case INSTANT -> {
                return new BsonDateTime(((Instant) value).toEpochMilli());
            }
            case ENTITY_REFERENCE -> {
                var erv = (EntityReference<?>) value;
                return new BsonString(erv.getId());
            }
        }
        throw Xeption.forDeveloper("unsupported property type " + type);
    }


}
