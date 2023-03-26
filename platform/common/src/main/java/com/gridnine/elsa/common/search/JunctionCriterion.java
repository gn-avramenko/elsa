/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.search;

import com.gridnine.elsa.common.utils.TextUtils;

import java.util.List;

public class JunctionCriterion extends SearchCriterion{

    public final boolean disjunction;

    public final List<SearchCriterion> criterions;

    public JunctionCriterion(boolean disjunction, List<SearchCriterion> criterions) {
        this.disjunction = disjunction;
        this.criterions = criterions;
    }

    @Override
    public String toString() {
        return TextUtils.join(criterions.stream().map("(%s)"::formatted).toList(), disjunction? " OR ": " AND ");
    }

}
