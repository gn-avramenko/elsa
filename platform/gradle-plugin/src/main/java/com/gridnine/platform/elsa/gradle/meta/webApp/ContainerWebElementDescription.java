/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

import java.util.ArrayList;
import java.util.List;

public class ContainerWebElementDescription extends BaseWebElementDescription {

    private boolean managedConfiguration;

    private final List<BaseWebElementDescription> children = new ArrayList<>();

    public ContainerWebElementDescription(String id, String className) {
        super(id, className);
    }
    public List<BaseWebElementDescription> getChildren() {
        return children;
    }

    @Override
    public WebElementType getType() {
        return WebElementType.CONTAINER;
    }

    public void setManagedConfiguration(boolean managedConfiguration) {
        this.managedConfiguration = managedConfiguration;
    }

    public boolean isManagedConfiguration() {
        return managedConfiguration;
    }
}
