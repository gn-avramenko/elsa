/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseElsaCodeGenRecord {
    private final List<File> sources = new ArrayList<>();

    public List<File> getSources() {
        return sources;
    }

    public abstract ElsaGeneratorType getGeneratorType();
}
