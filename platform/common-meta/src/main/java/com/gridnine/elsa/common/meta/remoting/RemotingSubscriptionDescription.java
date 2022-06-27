/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.remoting;

import com.gridnine.elsa.common.meta.common.BaseElementWitId;
import com.gridnine.elsa.common.meta.common.EntityDescription;

public class RemotingSubscriptionDescription extends BaseElementWitId {
    private String parameterClassName;

    private String eventClassName;

    public RemotingSubscriptionDescription() {
    }

    public RemotingSubscriptionDescription(String id) {
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
