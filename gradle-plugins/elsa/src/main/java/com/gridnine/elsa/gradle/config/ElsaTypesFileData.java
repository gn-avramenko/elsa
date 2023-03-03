/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import java.io.File;

public class ElsaTypesFileData {
    public final File metadataFile;
    public final String configuratorClassName;

    public ElsaTypesFileData(File metadataFile, String configuratorClassName) {
        this.metadataFile = metadataFile;
        this.configuratorClassName = configuratorClassName;
    }
}
