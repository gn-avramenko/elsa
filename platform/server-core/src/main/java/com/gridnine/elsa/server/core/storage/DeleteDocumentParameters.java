/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

public class DeleteDocumentParameters {
    private boolean skipInterceptors;

    public DeleteDocumentParameters skipInterceptors(boolean value){
        skipInterceptors =value;
        return this;
    }
    public boolean isSkipInterceptors() {
        return skipInterceptors;
    }
}
