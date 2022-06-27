/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;

public class WebRemotingCodeGenRecord extends BaseCodeGenRecord {

    private String remotingFacade;

    private String typesDefinition;

    private String constants;

    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.WEB_REMOTING;
    }

    public String getRemotingFacade() {
        return remotingFacade;
    }

    public void setRemotingFacade(String remotingFacade) {
        this.remotingFacade = remotingFacade;
    }

    public String getTypesDefinition() {
        return typesDefinition;
    }

    public void setTypesDefinition(String typesDefinition) {
        this.typesDefinition = typesDefinition;
    }

    public String getConstants() {
        return constants;
    }

    public void setConstants(String constants) {
        this.constants = constants;
    }
}
