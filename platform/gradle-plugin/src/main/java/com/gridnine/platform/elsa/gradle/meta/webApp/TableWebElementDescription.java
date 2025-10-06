/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

import java.util.ArrayList;
import java.util.List;

public class TableWebElementDescription extends BaseWebElementDescription {

    private WebAppEntity columnDescriptionExtension;

    private final List<TableDataFieldDescription> dataFields = new ArrayList<>();

    public TableWebElementDescription(String className) {
        super(className);
    }

    public void setColumnDescriptionExtension(WebAppEntity columnDescriptionExtension) {
        this.columnDescriptionExtension = columnDescriptionExtension;
    }

    public WebAppEntity getColumnDescriptionExtension() {
        return columnDescriptionExtension;
    }

    @Override
    public WebElementType getType() {
        return WebElementType.TABLE;
    }


    public List<TableDataFieldDescription> getDataFields() {
        return dataFields;
    }
}
