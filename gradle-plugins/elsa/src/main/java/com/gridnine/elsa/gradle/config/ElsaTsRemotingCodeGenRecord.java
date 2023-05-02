/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import java.io.File;

public class ElsaTsRemotingCodeGenRecord extends BaseElsaCodeGenRecord {
    private File module;

    private boolean skipClientGeneration;

    public File getModule() {
        return module;
    }

    public void setModule(File module) {
        this.module = module;
    }

    public void setSkipClientGeneration(boolean skipClientGeneration) {
        this.skipClientGeneration = skipClientGeneration;
    }

    public boolean isSkipClientGeneration() {
        return skipClientGeneration;
    }
}
