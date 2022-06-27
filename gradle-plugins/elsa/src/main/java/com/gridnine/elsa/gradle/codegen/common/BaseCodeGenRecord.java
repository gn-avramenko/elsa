/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseCodeGenRecord {
    private final List<File> sources = new ArrayList<>();

    private File destinationDir;

    public List<File> getSources() {
        return sources;
    }

    public File getDestinationDir() {
        return destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }


    public abstract GeneratorType getGeneratorType();
}
