/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElsaCodeGenFolderData {

    public File folder;

    public String serializableTypesConfigurator;
    public String domainTypesConfigurator;
    public String l10nTypesConfigurator;

    public String customTypesConfigurator;

    public String domainMetaRegistryConfigurator;
    public String customMetaRegistryConfigurator;
    public String l10nMetaRegistryConfigurator;

    public final List<ElsaTypesRecord> serializableTypes = new ArrayList<>();

    public final List<ElsaTypesRecord> domainTypes = new ArrayList<>();

    public final List<ElsaTypesRecord> customTypes = new ArrayList<>();

    public final List<ElsaTypesRecord> l10nTypes = new ArrayList<>();

    public final List<ElsaJavaDomainCodeGenRecord> domainCodeGenRecords = new ArrayList<>();

    public final List<ElsaJavaCustomCodeGenRecord> customCodeGenRecords = new ArrayList<>();

    public final List<ElsaJavaL10nCodeGenRecord> l10nCodeGenRecords = new ArrayList<>();
}
