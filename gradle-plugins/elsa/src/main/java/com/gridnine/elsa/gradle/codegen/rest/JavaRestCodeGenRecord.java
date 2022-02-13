/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.rest;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;

public class JavaRestCodeGenRecord extends BaseCodeGenRecord {

    private String registryConfigurator;

    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.JAVA_REST;
    }

    public String getRegistryConfigurator() {
        return registryConfigurator;
    }

    public void setRegistryConfigurator(String registryConfigurator) {
        this.registryConfigurator = registryConfigurator;
    }
}
