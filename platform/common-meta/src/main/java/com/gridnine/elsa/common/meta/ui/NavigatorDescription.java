/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.List;

public class NavigatorDescription extends BaseViewDescription{
    private final List<NavigatorVariantDescription> variants = new ArrayList<>();

    public List<NavigatorVariantDescription> getVariants() {
        return variants;
    }

    @Override
    ViewType getViewType() {
        return ViewType.NAVIGATOR;
    }

}
