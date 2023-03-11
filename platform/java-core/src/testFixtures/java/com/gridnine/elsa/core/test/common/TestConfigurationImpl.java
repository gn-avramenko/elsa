/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.test.common;

import com.gridnine.elsa.core.config.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestConfigurationImpl implements Configuration {
    private final Map<String, List<String>> values = new LinkedHashMap<>();
    private final Map<String, List<Configuration>> subconfigurations = new LinkedHashMap<>();

    public void setValue(String propertyName, String value){
        var lst = values.computeIfAbsent(propertyName, (name)-> new ArrayList<>());
        lst.clear();
        lst.add(value);
    }
    @Override
    public List<String> getValues(String propertyName) {
        return values.getOrDefault(propertyName, Collections.emptyList());
    }

    @Override
    public String getValue(String propertyName) {
        var values =  getValues(propertyName);
        return values.size()> 0? values.get(0): null;
    }

    @Override
    public Configuration getSubConfiguration(String propertyName) {
        var values =  getSubConfigurations(propertyName);
        return values.size()> 0? values.get(0): null;
    }

    @Override
    public List<Configuration> getSubConfigurations(String propertyName) {
        return subconfigurations.getOrDefault(propertyName, Collections.emptyList());
    }

    @Override
    public List<String> getPropertyNames() {
        return null;
    }
}
