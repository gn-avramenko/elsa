/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import org.gradle.api.Action;

import java.util.ArrayList;
import java.util.List;

public class ElsaJavaExtension {

    private final List<BaseCodeGenRecord> codegenRecords= new ArrayList<>();

    public void codegen(Action<ElsaJavaCodeGenExtension> action){
        var ext = new ElsaJavaCodeGenExtension(codegenRecords);
        action.execute(ext);
    }

    public List<BaseCodeGenRecord> getCodegenRecords() {
        return codegenRecords;
    }

}
