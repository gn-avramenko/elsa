/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import java.io.File;

public class ElsaTsRemotingCodeGenRecord extends BaseElsaCodeGenRecord {
    private File modelsFile;

    public File getModelsFile() {
        return modelsFile;
    }

    public void setModelsFile(File modelsFile) {
        this.modelsFile = modelsFile;
    }

    @Override
    public ElsaGeneratorType getGeneratorType() {
        return ElsaGeneratorType.TS_REMOTING;
    }


}
