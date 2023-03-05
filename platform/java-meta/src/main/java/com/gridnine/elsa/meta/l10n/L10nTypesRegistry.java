/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.l10n;

import com.gridnine.elsa.meta.common.TagDescription;

import java.util.LinkedHashMap;
import java.util.Map;

public class L10nTypesRegistry {

    private final  Map<String, TagDescription> parameterTypeTags = new LinkedHashMap<>();

    public Map<String, TagDescription> getParameterTypeTags() {
        return parameterTypeTags;
    }
}
