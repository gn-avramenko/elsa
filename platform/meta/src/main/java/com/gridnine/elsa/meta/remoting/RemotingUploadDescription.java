/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.remoting;

import com.gridnine.elsa.meta.common.BaseElement;

public class RemotingUploadDescription extends BaseElement {

    private String requestClassName;

    public RemotingUploadDescription() {
    }

    public RemotingUploadDescription(String id) {
        super(id);
    }

    public String getRequestClassName() {
        return requestClassName;
    }

    public void setRequestClassName(String requestClassName) {
        this.requestClassName = requestClassName;
    }

}
