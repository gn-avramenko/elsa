/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DatabaseCollectionType;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyType;

import java.util.Collection;
import java.util.Map;

abstract class BaseSearchableMetadataProvider extends BaseObjectMetadataProvider<BaseIntrospectableObject> {
    BaseSearchableMetadataProvider(BaseSearchableDescription description) {
        description.getProperties().values().forEach(prop ->
                addProperty(new SerializablePropertyDescription(prop.getId(), toSerializableType(prop.getType()),
                        toClassName(prop.getType(), prop.getClassName()), false)));
        description.getCollections().values().forEach(coll ->
                addCollection(new SerializableCollectionDescription(coll.getId(), toSerializableType(coll.getElementType()),
                        toClassName(coll.getElementType(), coll.getElementClassName()), false)));

    }

    private SerializablePropertyType toSerializableType(DatabasePropertyType type) {
        return switch (type) {
            case LONG -> SerializablePropertyType.LONG;
            case LOCAL_DATE_TIME -> SerializablePropertyType.LOCAL_DATE_TIME;
            case LOCAL_DATE -> SerializablePropertyType.LOCAL_DATE;
            case INT -> SerializablePropertyType.INT;
            case ENUM -> SerializablePropertyType.ENUM;
            case ENTITY_REFERENCE -> SerializablePropertyType.ENTITY;
            case BOOLEAN -> SerializablePropertyType.BOOLEAN;
            case BIG_DECIMAL -> SerializablePropertyType.BIG_DECIMAL;
            case STRING, TEXT -> SerializablePropertyType.STRING;
        };
    }

    private SerializablePropertyType toSerializableType(DatabaseCollectionType type) {
        return switch (type) {
            case ENTITY_REFERENCE -> SerializablePropertyType.ENTITY;
            case ENUM -> SerializablePropertyType.ENUM;
            case STRING -> SerializablePropertyType.STRING;
        };
    }

    private String toClassName(DatabasePropertyType type, String className) {
        if (type == DatabasePropertyType.ENTITY_REFERENCE) {
            return EntityReference.class.getName();
        }
        return className;
    }

    private String toClassName(DatabaseCollectionType type, String className) {
        if (type == DatabaseCollectionType.ENTITY_REFERENCE) {
            return EntityReference.class.getName();
        }
        return className;
    }

    @Override
    public Object getPropertyValue(BaseIntrospectableObject obj, String id) {
        return obj.getValue(id);
    }

    @Override
    public Collection<?> getCollection(BaseIntrospectableObject obj, String id) {
        return obj.getCollection(id);
    }

    @Override
    public Map<?, ?> getMap(BaseIntrospectableObject obj, String id) {
        return obj.getMap(id);
    }

    @Override
    public void setPropertyValue(BaseIntrospectableObject obj, String id, Object value) {
        obj.setValue(id, value);
    }
}





