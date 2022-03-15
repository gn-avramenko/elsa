/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.core.serialization.meta.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Component
public class Cloner {

    @Autowired
    private ObjectMetadataProvidersFactory metadataProvidersFactory;

    @Autowired
    private ReflectionFactory reflectionFactory;

    @SuppressWarnings("unchecked")
    public <T extends BaseIntrospectableObject> T clone(T source) {
        var result = (T) reflectionFactory.newInstance(source.getClass().getName());
        copy(source, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public<T extends BaseIntrospectableObject>  void copy (T source, T result){
        var provider = metadataProvidersFactory.getProvider(source.getClass().getName());
        for(SerializablePropertyDescription prop: provider.getAllProperties()){
            result.setValue(prop.id(), getCopy(source.getValue(prop.id()), prop.type()));
        }
        for(SerializableCollectionDescription coll: provider.getAllCollections()){
            Collection<Object> rc = (Collection<Object>) result.getCollection(coll.id());
            rc.clear();
            for(Object value : source.getCollection(coll.id())){
                rc.add(getCopy(value, coll.elementType()));
            }
        }
        for(SerializableMapDescription map: provider.getAllMaps()){
            Map<Object, Object> rm = (Map<Object, Object>) result.getMap(map.id());
            rm.clear();
            for(Map.Entry<?, ?> entry : source.getMap(map.id()).entrySet()){
                rm.put(getCopy(entry, map.keyType()), getCopy(entry.getValue(), map.valueType()));
            }
        }
    }

    private Object getCopy(Object value, SerializablePropertyType type){
        if(value == null){
            return null;
        }
        return switch (type){
            case STRING, ENUM,  LONG, INT, BIG_DECIMAL, LOCAL_DATE_TIME ,LOCAL_DATE,
                    BOOLEAN,CLASS -> value;
            case BYTE_ARRAY -> {
                var ba = (byte[]) value;
                yield Arrays.copyOf(ba, ba.length);
            }
            case ENTITY_REFERENCE -> {
                var er = (EntityReference<?>) value;
                yield new EntityReference<>(er.getId(), er.getType(), er.getCaption());
            }
            case ENTITY -> {
                var qualifiedName = value.getClass().getName();
                var ett = (BaseIntrospectableObject) reflectionFactory.newInstance(qualifiedName);
                metadataProvidersFactory.getProvider(qualifiedName);
                copy((BaseIntrospectableObject) value, ett);
                yield ett;
            }
        };
    }
}
