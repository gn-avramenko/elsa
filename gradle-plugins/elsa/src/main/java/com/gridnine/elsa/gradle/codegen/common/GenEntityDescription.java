/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.common.meta.common.EntityDescription;

public class GenEntityDescription extends EntityDescription {
    private String toStringExpression;
    private String toLocalizableStringExpression;

    public String getToStringExpression() {
        return toStringExpression;
    }

    public void setToStringExpression(String toStringExpression) {
        this.toStringExpression = toStringExpression;
    }

    public String getToLocalizableStringExpression() {
        return toLocalizableStringExpression;
    }

    public void setToLocalizableStringExpression(String toLocalizableStringExpression) {
        this.toLocalizableStringExpression = toLocalizableStringExpression;
    }
}
