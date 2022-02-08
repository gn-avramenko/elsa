/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.rest;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class RestOperationDescription extends BaseModelElementDescription {

    private String groupId;
    private String requestEntity;
    private String responseEntity;
    private String handler;
    private boolean validatable;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(String requestEntity) {
        this.requestEntity = requestEntity;
    }

    public String getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(String responseEntity) {
        this.responseEntity = responseEntity;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public boolean isValidatable() {
        return validatable;
    }

    public void setValidatable(boolean validatable) {
        this.validatable = validatable;
    }
}


