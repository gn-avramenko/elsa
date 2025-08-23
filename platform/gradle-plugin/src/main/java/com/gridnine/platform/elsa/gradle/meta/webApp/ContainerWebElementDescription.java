/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

import java.util.ArrayList;
import java.util.List;

public class ContainerWebElementDescription extends BaseWebElementDescription {

    private ContainerFlexDirection flexDirection;

    private final List<BaseWebElementDescription> children = new ArrayList<>();

    public ContainerWebElementDescription(String id, String className) {
        super(id, className);
    }
    public ContainerFlexDirection getFlexDirection() {
        return flexDirection;
    }

    public List<BaseWebElementDescription> getChildren() {
        return children;
    }

    public void setFlexDirection(ContainerFlexDirection flexDirection) {
        this.flexDirection = flexDirection;
    }

    @Override
    public WebElementType getType() {
        return WebElementType.CONTAINER;
    }


}
