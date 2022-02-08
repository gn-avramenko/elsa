/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.List;

public class TableBoxWidgetDescription extends BaseWidgetDescription {

    private boolean hideToolsColumn;

    private final List<TableColumnDescription> columns = new ArrayList<>();

    @Override
    public WidgetType getWidgetType() {
        return WidgetType.TABLE_BOX;
    }

    public List<TableColumnDescription> getColumns() {
        return columns;
    }

}
