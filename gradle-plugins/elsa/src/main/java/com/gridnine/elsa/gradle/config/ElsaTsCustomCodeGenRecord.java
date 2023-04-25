/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import java.io.File;

public class ElsaTsCustomCodeGenRecord extends BaseElsaCodeGenRecord {
    private File module;

    public File getModule() {
        return module;
    }

    public void setModule(File module) {
        this.module = module;
    }

}
