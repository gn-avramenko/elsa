/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.config;

import com.gridnine.elsa.meta.config.Environment;

import java.util.List;

public interface Configuration {

    List<String> getValues(String propertyName);

    String getValue(String propertyName);

    String getValue(String propertyName,String defaultValue);

    Configuration getSubConfiguration(String propertyName);

    List<Configuration> getSubConfigurations(String propertyName);

    List<String> getPropertyNames();

    public static Configuration get(){
        return Environment.getPublished(Configuration.class);
    }

}
