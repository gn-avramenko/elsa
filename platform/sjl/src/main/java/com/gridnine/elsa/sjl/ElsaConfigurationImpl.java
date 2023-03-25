/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.sjl;

import com.gridnine.elsa.core.config.Configuration;
import com.vga.sjl.config.model.ConfigurationNode;

import java.util.Collections;
import java.util.List;

public class ElsaConfigurationImpl implements Configuration {

    private final ConfigurationNode delegate;

    public ElsaConfigurationImpl(ConfigurationNode delegate) {
        this.delegate = delegate;
    }


    @Override
    public List<String> getValues(String propertyName) {
        return delegate.getValues(propertyName);
    }

    @Override
    public String getValue(String propertyName) {
        return delegate == null? null: delegate.getValue(propertyName);
    }

    @Override
    public String getValue(String propertyName, String defaultValue) {
        String value = delegate == null? null: delegate.getValue(propertyName);
        return value != null ? value: defaultValue;
    }

    @Override
    public Configuration getSubConfiguration(String propertyName) {
        return new ElsaConfigurationImpl(delegate.getSubConfiguration(propertyName));
    }

    @Override
    public List<Configuration> getSubConfigurations(String propertyName) {
        return delegate == null? Collections.emptyList(): delegate.getSubConfigurations(propertyName).stream().map(it -> (Configuration) new ElsaConfigurationImpl(it)).toList();
    }

    @Override
    public List<String> getPropertyNames() {
        return delegate == null? Collections.emptyList(): delegate.getPropertyNames();
    }
}
