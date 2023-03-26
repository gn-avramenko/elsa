/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.search;

public class SearchQueryBuilder extends BaseQueryBuilder<SearchQueryBuilder, SearchQuery> {

    public SearchQueryBuilder(){
        super(new SearchQuery());
    }

    public SearchQueryBuilder limit(int limit){
        query.setLimit(limit);
        return this;
    }

    public SearchQueryBuilder offset(int offset){
        query.setOffset(offset);
        return this;
    }

    public<T extends FieldNameSupport & SortSupport> SearchQueryBuilder orderBy(T property, SortOrder order){
        query.getOrders().clear();
        return addOrder(property, order);
    }

    public<T extends FieldNameSupport & SortSupport> SearchQueryBuilder addOrder(T property, SortOrder order){
        query.getOrders().put(property.name, order);
        return this;
    }

    public SearchQueryBuilder preferredFields(FieldNameSupport... fields){
        query.getPreferredFields().clear();
        for(FieldNameSupport field: fields){
            query.getPreferredFields().add(field.name);
        }
        return this;
    }

}
