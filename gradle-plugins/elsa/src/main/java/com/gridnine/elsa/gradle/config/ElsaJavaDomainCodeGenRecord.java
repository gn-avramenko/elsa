/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

public class ElsaJavaDomainCodeGenRecord extends BaseElsaCodeGenRecord {
    @Override
    public ElsaGeneratorType getGeneratorType() {
        return ElsaGeneratorType.JAVA_DOMAIN;
    }

}
