/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.search;

public class AggregationQueryBuilder extends BaseQueryBuilder<AggregationQueryBuilder, AggregationQuery> {

    public AggregationQueryBuilder(){
        super(new AggregationQuery());
    }

    public AggregationQueryBuilder count(){
        query.getAggregations().add(new Aggregation("*", Aggregation.Operation.COUNT));
        return this;
    }

    public<T extends FieldNameSupport & NumberOperationsSupport> AggregationQueryBuilder sum(T property){
        query.getAggregations().add(new Aggregation(property.name, Aggregation.Operation.SUM));
        return this;
    }

    public<T extends FieldNameSupport & NumberOperationsSupport> AggregationQueryBuilder min(T property){
        query.getAggregations().add(new Aggregation(property.name, Aggregation.Operation.MIN));
        return this;
    }

    public<T extends FieldNameSupport & NumberOperationsSupport> AggregationQueryBuilder max(T property){
        query.getAggregations().add(new Aggregation(property.name, Aggregation.Operation.MAX));
        return this;
    }

    public<T extends FieldNameSupport & NumberOperationsSupport> AggregationQueryBuilder avg(T property){
        query.getAggregations().add(new Aggregation(property.name, Aggregation.Operation.AVG));
        return this;
    }

}
