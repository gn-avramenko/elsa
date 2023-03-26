/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.search;

public abstract class BaseQueryBuilder<T, Q extends BaseQuery> {
    protected final Q query;

    public BaseQueryBuilder(Q query){
        this.query = query;
    }

    public T freeText(String freeText){
        query.setFreeText(freeText);
        return getBilder();
    }

    public T where(SearchCriterion... criterions){
        query.getCriterions().clear();
        for(SearchCriterion crit: criterions){
            query.getCriterions().add(crit);
        }
        return getBilder();
    }

    @SuppressWarnings("unchecked")
    private T getBilder() {
        return (T) this;
    }

    public Q build(){
        return query;
    }

}
