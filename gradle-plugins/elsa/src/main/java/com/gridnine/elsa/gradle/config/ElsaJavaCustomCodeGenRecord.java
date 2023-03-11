/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

public class ElsaJavaCustomCodeGenRecord extends BaseElsaCodeGenRecord {

    @Override
    public ElsaGeneratorType getGeneratorType() {
        return ElsaGeneratorType.JAVA_CUSTOM;
    }

}
