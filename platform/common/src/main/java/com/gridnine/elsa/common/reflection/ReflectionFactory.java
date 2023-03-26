/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.reflection;

import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.meta.config.Environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ReflectionFactory {

    private final Map<String, Class<?>> cache = new ConcurrentHashMap<>();

    public <T> T newInstance(Class<T> cls){
        return ExceptionUtils.wrapException(() ->cls.getDeclaredConstructor().newInstance());
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstance(String className){
        return (T) ExceptionUtils.wrapException(() ->getClass(className).getDeclaredConstructor().newInstance());
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getClass(String className){
        var cleanClassName = className;
        var idx = className.indexOf("<");
        if( idx != -1){
            cleanClassName = className.substring(0, idx);
        }
        return (Class<T>) cache.computeIfAbsent(cleanClassName, (key) -> ExceptionUtils.wrapException(() -> Class.forName(key)));
    }

    public Enum<?> safeGetEnum(Class<Enum<?>> cls, String item){
        for(Object constant : cls.getEnumConstants()){
            if(((Enum<?>) constant).name().equals(item)){
                return (Enum<?>) constant;
            }
        }
        return null;
    }

    public Enum<?> safeGetEnum(String className, String item){
        return safeGetEnum(getClass(className), item);
    }

    public static ReflectionFactory get(){
        return Environment.getPublished(ReflectionFactory.class);
    }
}
