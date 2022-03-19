/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.l10n;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;

public class JavaL10nCodeGenRecord extends BaseCodeGenRecord {

    private String registryConfigurator;

    private String factory;

    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.JAVA_L10N;
    }

    public String getRegistryConfigurator() {
        return registryConfigurator;
    }

    public void setRegistryConfigurator(String registryConfigurator) {
        this.registryConfigurator = registryConfigurator;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }
}
