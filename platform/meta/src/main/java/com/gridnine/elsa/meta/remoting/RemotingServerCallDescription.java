/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.remoting;

import com.gridnine.elsa.meta.common.BaseElement;

public class RemotingServerCallDescription extends BaseElement {
    private String requestClassName;
    private String responseClassName;

    public RemotingServerCallDescription() {
    }

    public RemotingServerCallDescription(String id) {
        super(id);
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
