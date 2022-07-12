/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;

public class WebUiCodeGenRecord extends BaseCodeGenRecord {

    private String tsFileName;

    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.WEB_UI;
    }


    public String getTsFileName() {
        return tsFileName;
    }

    public void setTsFileName(String tsFileName) {
        this.tsFileName = tsFileName;
    }
}
