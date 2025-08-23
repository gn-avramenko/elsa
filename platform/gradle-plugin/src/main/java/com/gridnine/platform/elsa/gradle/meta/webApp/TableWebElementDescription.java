/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

import java.util.ArrayList;
import java.util.List;

public class TableWebElementDescription extends BaseWebElementDescription {

    private ElementExtension columnDescriptionExtension;

    private final List<TableDataFieldDescription> dataFields = new ArrayList<>();

    public TableWebElementDescription(String id, String className) {
        super(id, className);
    }

    public void setColumnDescriptionExtension(ElementExtension columnDescriptionExtension) {
        this.columnDescriptionExtension = columnDescriptionExtension;
    }

    public ElementExtension getColumnDescriptionExtension() {
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
