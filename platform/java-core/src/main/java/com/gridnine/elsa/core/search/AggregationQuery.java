/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.search;

import com.gridnine.elsa.core.utils.TextUtils;

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
        var buf = new StringBuilder("SELECT %s".formatted(aggregations.isEmpty()? "???" : TextUtils.join(aggregations, ", ")));
        if(!getCriterions().isEmpty()){
            buf.append(" WHERE %s".formatted(TextUtils.join(getCriterions(), " AND ")));
        }
        if (groupBy.size() > 0) {
            buf.append(" GROUP BY %s".formatted(TextUtils.join(groupBy, ", ")));
        }
        return buf.toString();
    }

}
