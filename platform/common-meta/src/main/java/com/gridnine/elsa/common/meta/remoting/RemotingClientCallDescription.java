/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.remoting;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

public class RemotingClientCallDescription extends BaseElementWithId {
    private String requestClassName;

    private String responseClassName;

    public RemotingClientCallDescription() {
    }

    public RemotingClientCallDescription(String id) {
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
