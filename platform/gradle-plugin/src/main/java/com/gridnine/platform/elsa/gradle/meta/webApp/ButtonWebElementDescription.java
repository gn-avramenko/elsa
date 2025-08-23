/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

public class ButtonWebElementDescription extends BaseWebElementDescription {

    public ButtonWebElementDescription(String id, String className) {
        super(id, className);
    }

    @Override
    public WebElementType getType() {
        return WebElementType.BUTTON;
    }


}
