/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.List;

public class GridContainerDescription extends BaseViewDescription{

    private int columnsCount = 1;

    private final List<GridContainerColumnDescription> columns = new ArrayList<>();
    private final List<GridContainerRowDescription> rows = new ArrayList<>();

    @Override
    ViewType getViewType() {
        return ViewType.GRID_CONTAINER;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    public List<GridContainerColumnDescription> getColumns() {
        return columns;
    }

    public List<GridContainerRowDescription> getRows() {
        return rows;
    }
}
