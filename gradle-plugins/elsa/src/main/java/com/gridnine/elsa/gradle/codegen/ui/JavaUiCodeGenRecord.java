/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;

public class JavaUiCodeGenRecord extends BaseCodeGenRecord {

    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.JAVA_UI;
    }
}
