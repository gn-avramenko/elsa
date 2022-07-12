/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.parser.ui.ViewTemplateParserHandler;
import org.gradle.api.Action;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElsaWebExtension {

    private final List<BaseCodeGenRecord> codegenRecords= new ArrayList<>();

    private File projectDir;

    private final Map<String, ViewTemplateParserHandler> templatesHandlers = new LinkedHashMap<>();

    private final Map<String, File> imports = new LinkedHashMap<>();

    public void codegen(Action<ElsaWebCodeGenExtension> action){
        var ext = new ElsaWebCodeGenExtension(codegenRecords, projectDir, imports);
        action.execute(ext);
    }

    public List<BaseCodeGenRecord> getCodegenRecords() {
        return codegenRecords;
    }

    public void setProjectDir(File projectDir) {
        this.projectDir = projectDir;
    }

    public Map<String, ViewTemplateParserHandler> getTemplatesHandlers() {
        return templatesHandlers;
    }

    public Map<String, File> getImports() {
        return imports;
    }
}
