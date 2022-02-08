/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.domain;

public class AssetDescription extends BaseSearchableDescription {

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

    public String getCaptionExpression() {
        return captionExpression;
    }

    public void setCaptionExpression(String captionExpression) {
        this.captionExpression = captionExpression;
    }

    public String getLocalizableCaptionExpression() {
        return localizableCaptionExpression;
    }

    public void setLocalizableCaptionExpression(String localizableCaptionExpression) {
        this.localizableCaptionExpression = localizableCaptionExpression;
    }
}
