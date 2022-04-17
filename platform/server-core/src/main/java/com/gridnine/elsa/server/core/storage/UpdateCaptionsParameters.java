/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

public class UpdateCaptionsParameters {
    private boolean skipInterceptors;

    public UpdateCaptionsParameters skipInterceptors(boolean value){
        skipInterceptors =value;
        return this;
    }
    public boolean isSkipInterceptors() {
        return skipInterceptors;
    }
}
