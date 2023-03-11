/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.search;

import com.gridnine.elsa.core.utils.TextUtils;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SearchQuery extends BaseQuery{
    private final Map<String, SortOrder> orders = new LinkedHashMap<>();
    private final Set<String> preferredFields = new LinkedHashSet<>();
    private int limit = 200;
    private int offset = 0;

    public Map<String, SortOrder> getOrders() {
        return orders;
    }

    public Set<String> getPreferredFields() {
        return preferredFields;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        var buf = new StringBuilder("SELECT %s".formatted(preferredFields.isEmpty()? "*" : TextUtils.join(preferredFields, ", ")));
        if(!getCriterions().isEmpty()){
            buf.append(" WHERE %s".formatted(TextUtils.join(getCriterions(), " AND ")));
        }
        if (limit > 0) {
            buf.append(" LIMIT %s OFFSET %s".formatted(limit, offset));
        }
        return buf.toString();
    }
}
