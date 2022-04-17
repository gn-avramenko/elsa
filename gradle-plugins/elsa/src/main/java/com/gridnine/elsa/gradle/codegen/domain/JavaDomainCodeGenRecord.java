/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;

public class JavaDomainCodeGenRecord extends BaseCodeGenRecord {

    private String registryConfigurator;

    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.JAVA_DOMAIN;
    }

    public String getRegistryConfigurator() {
        return registryConfigurator;
    }

    public void setRegistryConfigurator(String registryConfigurator) {
        this.registryConfigurator = registryConfigurator;
    }
}
