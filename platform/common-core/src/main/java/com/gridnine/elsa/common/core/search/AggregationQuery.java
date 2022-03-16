/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AggregationQuery extends BaseQuery{
    private final List<Aggregation> aggregations = new ArrayList<>();

    private final List<String> groupBy = new ArrayList<>();

    public List<Aggregation> getAggregations() {
        return aggregations;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    @Override
    public String toString() {
        var buf = new StringBuilder("SELECT %s".formatted(aggregations.isEmpty()? "???" : StringUtils.join(aggregations, ", ")));
        if(!getCriterions().isEmpty()){
            buf.append(" WHERE %s".formatted(StringUtils.join(getCriterions(), " AND ")));
        }
        if (groupBy.size() > 0) {
            buf.append(" GROUP BY %s".formatted(StringUtils.join(groupBy, ", ")));
        }
        return buf.toString();
    }

}
