package com.gridnine.platform.elsa.common.meta.adminUi.grid;


import com.gridnine.platform.elsa.common.meta.adminUi.AdminUiContainerType;
import com.gridnine.platform.elsa.common.meta.adminUi.BaseAdminUiContainerDescription;

import java.util.ArrayList;
import java.util.List;

public class GridContainerDescription extends BaseAdminUiContainerDescription {
    private final List<GridRowDescription> rows = new ArrayList<>();
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<GridRowDescription> getRows() {
        return rows;
    }

    @Override
    public AdminUiContainerType getType() {
        return AdminUiContainerType.GRID;
    }
}
