/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.l10n;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;

public class L10nMessageParameterDescription extends BaseModelElementDescription {
    private StandardValueType type;
    private String className;
    private boolean collection;

    public L10nMessageParameterDescription() {
    }

    public L10nMessageParameterDescription(String id) {
        super(id);
    }

    public StandardValueType getType() {
        return type;
    }

    public void setType(StandardValueType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }
}
