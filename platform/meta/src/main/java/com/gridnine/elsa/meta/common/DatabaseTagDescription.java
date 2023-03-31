/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

public class DatabaseTagDescription extends TagDescription {

    public DatabaseTagDescription() {
    }

    private boolean hasCollectionSupport;

    private boolean hasComparisonSupport;

    private boolean hasEqualitySupport;

    private String searchQueryArgumentType;

    public boolean isHasCollectionSupport() {
        return hasCollectionSupport;
    }

    public void setHasCollectionSupport(boolean hasCollectionSupport) {
        this.hasCollectionSupport = hasCollectionSupport;
    }

    public boolean isHasComparisonSupport() {
        return hasComparisonSupport;
    }

    public void setHasComparisonSupport(boolean hasComparisonSupport) {
        this.hasComparisonSupport = hasComparisonSupport;
    }

    public boolean isHasEqualitySupport() {
        return hasEqualitySupport;
    }

    public void setHasEqualitySupport(boolean hasEqualitySupport) {
        this.hasEqualitySupport = hasEqualitySupport;
    }

    public boolean isHasNumberOperationsSupport() {
        return hasNumberOperationsSupport;
    }

    public void setHasNumberOperationsSupport(boolean hasNumberOperationsSupport) {
        this.hasNumberOperationsSupport = hasNumberOperationsSupport;
    }

    public boolean isHasSortSupport() {
        return hasSortSupport;
    }

    public void setHasSortSupport(boolean hasSortSupport) {
        this.hasSortSupport = hasSortSupport;
    }

    public boolean isHasStringOperationsSupport() {
        return hasStringOperationsSupport;
    }

    public void setHasStringOperationsSupport(boolean hasStringOperationsSupport) {
        this.hasStringOperationsSupport = hasStringOperationsSupport;
    }

    private boolean hasNumberOperationsSupport;

    private boolean hasSortSupport;

    private boolean hasStringOperationsSupport;


    public String getSearchQueryArgumentType() {
        return searchQueryArgumentType;
    }

    public void setSearchQueryArgumentType(String searchQueryArgumentType) {
        this.searchQueryArgumentType = searchQueryArgumentType;
    }
}
