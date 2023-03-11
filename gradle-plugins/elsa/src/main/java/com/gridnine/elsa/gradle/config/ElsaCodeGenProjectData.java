/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElsaCodeGenProjectData {

    public Project project;

    public final List<ElsaCodeGenFolderData> folders = new ArrayList<>();

}
