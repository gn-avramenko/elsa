/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Configuration(value = "elsaRegistry")
public class Registry {
    private final Map<String, Map<String, RegistryItem<?>>> registry = new HashMap<>();
    private final Map<String, List<RegistryItem<?>>> allItems = new HashMap<>();

    @Autowired
    public void initialize(Collection<RegistryItem<?>> items) {
        items.forEach(it -> {
            registry.computeIfAbsent(it.getType().id(), k -> new HashMap<>()).put(it.getId(), it);
            allItems.computeIfAbsent(it.getType().id(), k -> new ArrayList<>()).add(it);
        });
    }

    public <T> List<T> allOff(RegistryItemType<T> type) {
        return allItems.containsKey(type.id()) ? (java.util.List<T>) allItems.get(type.id()) : Collections.emptyList();
    }

    public <T> T get(RegistryItemType<T> type, String id){
        return registry.containsKey(type.id())? (T) registry.get(type.id()).get(id) : null;
    }

}
