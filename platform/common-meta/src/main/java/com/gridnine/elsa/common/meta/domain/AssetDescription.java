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

    private boolean isAbstract;

    private String extendsId;

    public AssetDescription() {
    }

    public AssetDescription(String id) {
        super(id);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public String getExtendsId() {
        return extendsId;
    }

    public void setExtendsId(String extendsId) {
        this.extendsId = extendsId;
    }

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
