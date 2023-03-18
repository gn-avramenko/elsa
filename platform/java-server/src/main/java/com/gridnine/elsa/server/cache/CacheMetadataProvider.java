/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.cache;

import com.gridnine.elsa.core.model.common.BaseIdentity;
import com.gridnine.elsa.meta.common.BaseElementWithId;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CacheMetadataProvider {

    private final Map<String, Boolean> resolveCache = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Boolean>> findCache = new ConcurrentHashMap<>();

    private final Map<String, Map<String, Boolean>> getAllCache = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> findProperties = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> getAllProperties = new ConcurrentHashMap<>();
    private final Map<String, Boolean> cacheCaption = new ConcurrentHashMap<>();
    private final Map<String, Boolean> cacheLocalizedCaption = new ConcurrentHashMap<>();

    public <I extends BaseIdentity> boolean isCacheResolve(Class<I> cls) {
        var className = cls.getName();
        var res = resolveCache.get(className);
        if (res != null) {
            return res;
        }
        var descr = SerializableMetaRegistry.get().getEntities().get(className);
        var attr = descr.getAttributes().get("cache-resolve");
        resolveCache.put(className, "true".equals(attr));
        return resolveCache.get(className);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCacheFind(Class<?> cls, String propertyName) {
        var className = cls.getName();
        var c1 = findCache.get(className);
        if (c1 == null) {
            c1 = new ConcurrentHashMap<>();
            findCache.put(className, c1);
        }
        var res = c1.get(propertyName);
        if (res != null) {
            return res;
        }
        var attr = SerializableMetaRegistry.get().getEntities().get(className).getProperties().get(propertyName).getAttributes().get("cache-find");
        c1.put(propertyName, "true".equals(attr));
        return c1.get(propertyName);
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCacheGetAll(Class<?> cls, String propertyName) {
        var className = cls.getName();
        var c1 = getAllCache.get(className);
        if (c1 == null) {
            c1 = new ConcurrentHashMap<>();
            getAllCache.put(className, c1);
        }
        var res = c1.get(propertyName);
        if (res != null) {
            return res;
        }
        var attr = SerializableMetaRegistry.get().getEntities().get(className).getProperties().get(propertyName).getAttributes().get("cache-get-all");
        c1.put(propertyName, "true".equals(attr));
        return c1.get(propertyName);
    }

    public Set<String> getGetAllCacheProperties(Class<?> cls) {
        var className = cls.getName();
        var res = getAllProperties.get(className);
        if (res != null) {
            return res;
        }
        var propsIds = SerializableMetaRegistry.get().getEntities().get(className)
                .getProperties().values().stream()
                .filter(it -> "true".equals(it.getAttributes().get("cache-get-all")))
                .map(BaseElementWithId::getId).collect(Collectors.toSet());
        getAllProperties.put(className, propsIds);
        return getAllProperties.get(className);
    }

    public Set<String> getFindCacheProperties(Class<?> cls) {
        var className = cls.getName();
        var res = findProperties.get(className);
        if (res != null) {
            return res;
        }
        var propsIds = SerializableMetaRegistry.get().getEntities().get(className)
                .getProperties().values().stream()
                .filter(it -> "true".equals(it.getAttributes().get("cache-find")))
                .map(BaseElementWithId::getId).collect(Collectors.toSet());
        findProperties.put(className, propsIds);
        return findProperties.get(className);
    }

    public boolean isCacheCaption(Class<?> cls){
        var className = cls.getName();
        var res = cacheCaption.get(className);
        if(res != null){
            return res;
        }
        var attrs = SerializableMetaRegistry.get().getEntities().get(className).getAttributes();
        cacheCaption.put(className, "true".equals(attrs.get("cache-caption")) && attrs.containsKey("caption-expression"));
        return cacheCaption.get(className);
    }

    public boolean isCacheLocalizedCaption(Class<?> cls){
        var className = cls.getName();
        var res = cacheLocalizedCaption.get(className);
        if(res != null){
            return res;
        }
        var attrs = SerializableMetaRegistry.get().getEntities().get(className).getAttributes();
        cacheLocalizedCaption.put(className, "true".equals(attrs.get("cache-caption")) && attrs.containsKey("localizable-caption-expression"));
        return cacheLocalizedCaption.get(className);
    }
}
