/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.remoting;

import com.gridnine.elsa.meta.common.BaseElement;

public class RemotingServerSubscriptionDescription extends BaseElement {

    private String parameterClassName;

    private String eventClassName;

    public RemotingServerSubscriptionDescription() {
    }

    public RemotingServerSubscriptionDescription(String id) {
        super(id);
    }

    public String getParameterClassName() {
        return parameterClassName;
    }

    public void setParameterClassName(String parameterClassName) {
        this.parameterClassName = parameterClassName;
    }

    public String getEventClassName() {
        return eventClassName;
    }

    public void setEventClassName(String eventClassName) {
        this.eventClassName = eventClassName;
    }
}
