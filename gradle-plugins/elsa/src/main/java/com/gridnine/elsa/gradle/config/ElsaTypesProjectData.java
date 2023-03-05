/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElsaTypesProjectData {
    public Project project;
    public File destDir;
    public final List<ElsaTypesFileData> serializableTypes = new ArrayList<>();
    public final List<ElsaTypesFileData> domainTypes = new ArrayList<>();

    public final List<ElsaTypesFileData> customTypes = new ArrayList<>();

    public final List<ElsaTypesFileData> l10nTypes = new ArrayList<>();
}
