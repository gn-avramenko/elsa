/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CodeGenerator<T extends BaseCodeGenRecord> {
    void generate(List<T> records, File destDir, Set<File> generatedFiles, Map<Object, Object> context) throws Exception;
}
