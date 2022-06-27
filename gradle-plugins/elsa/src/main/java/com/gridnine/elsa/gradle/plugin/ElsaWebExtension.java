/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import org.gradle.api.Action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElsaWebExtension {

    private final List<BaseCodeGenRecord> codegenRecords= new ArrayList<>();

    private File projectDir;

    public void codegen(Action<ElsaWebCodeGenExtension> action){
        var ext = new ElsaWebCodeGenExtension(codegenRecords, projectDir);
        action.execute(ext);
    }

    public List<BaseCodeGenRecord> getCodegenRecords() {
        return codegenRecords;
    }

    public void setProjectDir(File projectDir) {
        this.projectDir = projectDir;
    }

}
