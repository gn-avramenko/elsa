/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import java.util.*;

public abstract class BaseObjectMetadataProvider<T> {

    private final Map<String, SerializablePropertyDescription> propertiesMap = new LinkedHashMap<>();

    private final Map<String, SerializableCollectionDescription> collectionsMap = new LinkedHashMap<>();

    private final Map<String, SerializableMapDescription> mapsMap = new LinkedHashMap<>();

    private final List<SerializablePropertyDescription> allProperties = new ArrayList<>();

    private final List<SerializableCollectionDescription> allCollections = new ArrayList<>();

    private final List<SerializableMapDescription> allMaps = new ArrayList<>();

    private boolean isAbstract;

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public SerializablePropertyDescription getProperty(String id){
        return propertiesMap.get(id);
    }

    public SerializableCollectionDescription getCollection(String id){
        return collectionsMap.get(id);
    }

    public SerializableMapDescription getMap(String id){
        return mapsMap.get(id);
    }

    public List<SerializablePropertyDescription> getAllProperties() {
        return allProperties;
    }

    public List<SerializableCollectionDescription> getAllCollections() {
        return allCollections;
    }

    public List<SerializableMapDescription> getAllMaps() {
        return allMaps;
    }

    void addProperty(SerializablePropertyDescription prop){
        propertiesMap.put(prop.id(), prop);
        allProperties.add(prop);
    }

    void addCollection(SerializableCollectionDescription prop){
        collectionsMap.put(prop.id(), prop);
        allCollections.add(prop);
    }

    void addMap(SerializableMapDescription prop){
        mapsMap.put(prop.id(), prop);
        allMaps.add(prop);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public abstract Object getPropertyValue(T obj, String id);

    public abstract void setPropertyValue(T obj, String id, Object value);

    public abstract Collection<?> getCollection(T obj, String id);

    public abstract Map<?,?> getMap(T obj, String id);

}
