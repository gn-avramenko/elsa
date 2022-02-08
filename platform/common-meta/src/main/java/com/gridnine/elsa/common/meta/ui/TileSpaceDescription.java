/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.List;

public class TileSpaceDescription extends BaseViewDescription{
    private TileSpaceOverviewDescription overviewDescription;
    private final  List<TileDescription> tiles = new ArrayList<>();
    @Override
    ViewType getViewType() {
        return ViewType.TILE_SPACE;
    }

    public TileSpaceOverviewDescription getOverviewDescription() {
        return overviewDescription;
    }

    public void setOverviewDescription(TileSpaceOverviewDescription overviewDescription) {
        this.overviewDescription = overviewDescription;
    }

    public List<TileDescription> getTiles() {
        return tiles;
    }
}
