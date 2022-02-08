/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.rest;

import com.gridnine.elsa.common.meta.common.BaseElementWitId;

public class RestGroupDescription extends BaseElementWitId {

    private String restId;

    public RestGroupDescription() {
    }

    public RestGroupDescription(String id) {
        super(id);
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }
}



