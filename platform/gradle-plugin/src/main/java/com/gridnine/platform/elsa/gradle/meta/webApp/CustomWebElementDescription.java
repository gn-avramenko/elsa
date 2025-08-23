/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

public class CustomWebElementDescription extends BaseWebElementDescription {

    public CustomWebElementDescription(String id, String className) {
        super(id, className);
    }

    @Override
    public WebElementType getType() {
        return WebElementType.CUSTOM;
    }


}
