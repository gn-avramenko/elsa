/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui.template;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;

public class JavaUiTemplateCodeGenRecord extends BaseCodeGenRecord {

    private String xsdFileName;

    private String targetNameSpace;

    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.JAVA_UI_TEMPLATE;
    }

    public String getXsdFileName() {
        return xsdFileName;
    }

    public void setXsdFileName(String xsdFileName) {
        this.xsdFileName = xsdFileName;
    }

    public String getTargetNameSpace() {
        return targetNameSpace;
    }

    public void setTargetNameSpace(String targetNameSpace) {
        this.targetNameSpace = targetNameSpace;
    }
}
