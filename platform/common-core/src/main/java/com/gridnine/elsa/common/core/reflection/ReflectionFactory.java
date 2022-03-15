/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.reflection;

import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
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

    public Enum<?> safeGetEnum(String className, String item){
        var cls = getClass(className);
        for(Object constant : cls.getEnumConstants()){
            if(((Enum<?>) constant).name().equals(item)){
                return (Enum<?>) constant;
            }
        }
        return null;
    }

    @PreDestroy
    public void destroy(){
        cache.clear();
    }

}
