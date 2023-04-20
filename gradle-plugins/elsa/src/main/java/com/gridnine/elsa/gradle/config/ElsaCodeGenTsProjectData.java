/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

public class ElsaCodeGenTsProjectData {

    public Project project;

    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public final List<ElsaCodeGenTsFolderData> folders = new ArrayList<>();

}
