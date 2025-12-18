package com.gridnine.platform.elsa.admin.common;

import com.gridnine.platform.elsa.admin.domain.Restriction;
import com.gridnine.platform.elsa.admin.web.common.Option;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;

public interface RestrictionRenderer<P,E extends BaseUiElement> {
    String getId();
    List<Option> getConditions();
    E createValueUiElement(P restrictionParameters, Restriction value, String tag, OperationUiContext context) throws Exception;
    Restriction getData(String propertyId, String conditionId, E uiElement) throws Exception;
    boolean validate(String conditionId, E valueComp, OperationUiContext ctx);

    boolean match(Object propValue, Object restrValue, String conditionId);

    SearchCriterion getSearchCriterion(String propertyId, String conditionId, Object value);
}
