/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;

import java.util.Collection;
import java.util.Map;

class EntityMetadataProvider extends BaseObjectMetadataProvider<BaseIntrospectableObject> {


    EntityMetadataProvider(EntityDescription entityDescription,
                           DomainMetaRegistry dr, CustomMetaRegistry cr,
                           SerializablePropertyDescription... additionalProperties) {
        for (SerializablePropertyDescription ap : additionalProperties) {
            addProperty(ap);
        }
        var extendsId = entityDescription.getExtendsId();
        while (extendsId != null) {
            var parentDescr =  dr.getEntities().get(extendsId);
            if (parentDescr == null) {
                parentDescr = dr.getDocuments().get(extendsId);
            }
            if (parentDescr == null) {
                parentDescr = cr.getEntities().get(extendsId);
            }
            if (parentDescr == null) {
                throw Xeption.forDeveloper("no entity found for id %s".formatted(extendsId));
            }
            fillProperties(parentDescr, dr,  cr);
            fillCollections(parentDescr, dr,  cr);
            fillMaps(parentDescr, dr,  cr);
            extendsId = parentDescr.getExtendsId();
        }
        fillProperties(entityDescription, dr,  cr);
        fillCollections(entityDescription, dr,  cr);
        fillMaps(entityDescription, dr,  cr);
        setAbstract(entityDescription.isAbstract());
    }

    private boolean isAbstractClass(String className, DomainMetaRegistry dr,  CustomMetaRegistry cr) {
        if (className == null) {
            return false;
        }
        var domainDocument = dr.getDocuments().get(className);
        if (domainDocument != null) {
            return domainDocument.isAbstract();
        }
        var entity = dr.getEntities().get(className);
        if (entity != null) {
            return entity.isAbstract();
        }

        var customEntity = cr.getEntities().get(className);
        if (customEntity != null) {
            return customEntity.isAbstract();
        }
        return false;
    }

    private SerializablePropertyType toSerializableType(StandardValueType type) {
        return switch (type) {
            case LONG -> SerializablePropertyType.LONG;
            case LOCAL_DATE_TIME -> SerializablePropertyType.LOCAL_DATE_TIME;
            case LOCAL_DATE -> SerializablePropertyType.LOCAL_DATE;
            case INT -> SerializablePropertyType.INT;
            case ENUM -> SerializablePropertyType.ENUM;
            case ENTITY_REFERENCE -> SerializablePropertyType.ENTITY_REFERENCE;
            case BOOLEAN -> SerializablePropertyType.BOOLEAN;
            case BIG_DECIMAL -> SerializablePropertyType.BIG_DECIMAL;
            case BYTE_ARRAY -> SerializablePropertyType.BYTE_ARRAY;
            case STRING -> SerializablePropertyType.STRING;
            case ENTITY -> SerializablePropertyType.ENTITY;
            case CLASS -> SerializablePropertyType.CLASS;
        };
    }

    private String toClassName(StandardValueType type, String className) {
        return className;
    }

    private void fillMaps(EntityDescription desc, DomainMetaRegistry dr,  CustomMetaRegistry cr) {
        desc.getMaps().values().forEach((map) -> addMap(new SerializableMapDescription(map.getId(), toSerializableType(map.getKeyType()),
                toClassName(map.getKeyType(), map.getKeyClassName()),
                toSerializableType(map.getValueType()), toClassName(map.getValueType(), map.getValueClassName()),
                isAbstractClass(map.getKeyClassName(), dr,  cr), isAbstractClass(map.getValueClassName(), dr, cr))));
    }

    private void fillCollections(EntityDescription desc, DomainMetaRegistry dr, CustomMetaRegistry cr) {
        desc.getCollections().values().forEach((coll) -> addCollection(new SerializableCollectionDescription(coll.getId(), toSerializableType(coll.getElementType()),
                        toClassName(coll.getElementType(), coll.getElementClassName()), isAbstractClass(
                        coll.getElementClassName(), dr,  cr)
                ))
        );
    }

    private void fillProperties(EntityDescription desc, DomainMetaRegistry dr, CustomMetaRegistry cr) {
        desc.getProperties().values().forEach((prop) -> addProperty(new SerializablePropertyDescription(prop.getId(), toSerializableType(prop.getType()),
                        toClassName(prop.getType(), prop.getClassName()), isAbstractClass(
                        prop.getClassName(), dr,  cr)
                ))
        );
    }

    @Override
    public Object getPropertyValue(BaseIntrospectableObject obj, String id) {
        return obj.getValue(id);
    }

    public @Override
    Collection<?> getCollection(BaseIntrospectableObject obj, String id) {
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
