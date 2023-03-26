/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.test.common;

import com.gridnine.elsa.common.activator.CommonActivator;
import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.common.config.Configuration;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.common.utils.IoUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class TestBase {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final List<Activator> activators = new ArrayList<>();
    @BeforeEach
    protected void initialize() throws Exception {
        setUp();
    }
    protected void setUp() throws Exception{
        configureEnvironment();
        var configuration = new TestConfigurationImpl();
        populateConfiguration(configuration);
        Environment.publish(Configuration.class, configuration);
        registerActivators(activators);
        activators.sort((a,b) -> a.getOrder()-b.getOrder() > 0? 1: (a.getOrder()-b.getOrder() == 0? 0: -1));
        for(Activator activator: activators){
            activator.configure();
        }
        for(Activator activator: activators){
            activator.activate();
        }
    }

    protected void populateConfiguration(TestConfigurationImpl configuration) {
    }

    protected void registerActivators(List<Activator> activators) {
        activators.add(new CommonActivator());
        activators.add(new TestCoreActivator());
    }

    protected void configureEnvironment() {
        var rootFolder = new File("temp");
        if(rootFolder.exists()){
            for(File file: rootFolder.listFiles()){
                if(file.isDirectory()){
                    if("temp".equals(file.getName())){
                        continue;
                    }
                    IoUtils.deleteDirectory(file);
                    continue;
                }
                file.delete();
            }
        }
        if(!rootFolder.exists()){
            rootFolder.mkdirs();
        }
        Environment.configure(rootFolder);
    }

    @AfterEach
    protected void dispose(){
        Environment.dispose();
    }


    public void println(Object value){
        log.debug(value.toString());
    }

}
