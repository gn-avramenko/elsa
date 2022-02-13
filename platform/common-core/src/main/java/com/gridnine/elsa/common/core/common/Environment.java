/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.common;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Environment {

    private boolean test;

    private File rootFolder;

    private File tempFolder;

    public void configure(File rootFolder, boolean test){
        this.rootFolder = rootFolder;
        this.test = test;
        this.tempFolder = new File(rootFolder, "temp/");
        assert this.tempFolder.exists() || this.tempFolder.mkdirs();
    }

    public File getRootFolder() {
        return rootFolder;
    }

    public File getTempFolder() {
        return tempFolder;
    }

    public boolean isTest() {
        return test;
    }
}
