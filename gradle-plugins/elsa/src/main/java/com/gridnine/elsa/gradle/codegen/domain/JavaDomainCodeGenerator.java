/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;

import java.io.File;
import java.util.List;
import java.util.Set;

public class JavaDomainCodeGenerator implements CodeGenerator<JavaDomainCodeGenRecord> {
    @Override
    public void generate(List<JavaDomainCodeGenRecord> records, File destDir, Set<File> generatedFiles) {
        System.out.println("generating domain");
    }
}
