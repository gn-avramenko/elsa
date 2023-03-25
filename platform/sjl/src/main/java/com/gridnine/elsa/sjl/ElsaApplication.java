/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.sjl;

import com.gridnine.elsa.core.config.Activator;
import com.gridnine.elsa.core.config.Configuration;
import com.gridnine.elsa.meta.config.Environment;
import com.vga.sjl.Application;
import com.vga.sjl.ApplicationCallback;
import com.vga.sjl.config.AppConfiguration;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class ElsaApplication implements Application {
    @Override
    public void start(AppConfiguration appConfiguration, ApplicationCallback applicationCallback) throws Exception {
        Environment.configure(new File("."));
        Environment.publish(Configuration.class, new ElsaConfigurationImpl(appConfiguration));
        var activators = ServiceLoader.load(Activator.class, getClass().getClassLoader()).stream().map(ServiceLoader.Provider::get).sorted(Comparator.comparingDouble(Activator::getOrder)).toList();
        for(Activator activator: activators){
            activator.configure();
        }
        for(Activator activator: activators){
            activator.activate();
        }
    }

    @Override
    public void stop() throws Exception {
        Environment.dispose();
    }
}
