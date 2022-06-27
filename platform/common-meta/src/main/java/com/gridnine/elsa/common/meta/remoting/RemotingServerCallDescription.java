/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.remoting;

import com.gridnine.elsa.common.meta.common.BaseElementWitId;
import com.gridnine.elsa.common.meta.common.EntityDescription;

public class RemotingServerCallDescription extends BaseElementWitId {
    private boolean validatable;

    private String requestClassName;

    private String responseClassName;

    public RemotingServerCallDescription() {
    }

    public RemotingServerCallDescription(String id) {
        super(id);
    }

    public boolean isValidatable() {
        return validatable;
    }

    public void setValidatable(boolean validatable) {
        this.validatable = validatable;
    }

    public String getRequestClassName() {
        return requestClassName;
    }

    public void setRequestClassName(String requestClassName) {
        this.requestClassName = requestClassName;
    }

    public String getResponseClassName() {
        return responseClassName;
    }

    public void setResponseClassName(String responseClassName) {
        this.responseClassName = responseClassName;
    }
}
