/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.domain;

import com.gridnine.elsa.common.meta.common.EntityDescription;

public class DocumentDescription extends EntityDescription {

    private boolean cacheResolve;

    private boolean cacheCaption;

    private String captionExpression;

    private String localizableCaptionExpression;

    public boolean isCacheResolve() {
        return cacheResolve;
    }

    public void setCacheResolve(boolean cacheResolve) {
        this.cacheResolve = cacheResolve;
    }

    public boolean isCacheCaption() {
        return cacheCaption;
    }

    public void setCacheCaption(boolean cacheCaption) {
        this.cacheCaption = cacheCaption;
    }

}
