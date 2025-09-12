/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

import com.gridnine.platform.elsa.gradle.meta.common.BaseElementWithId;

import java.util.LinkedList;
import java.util.List;


public abstract class BaseWebElementDescription extends BaseElementWithId {

    private String className;

    private final WebAppEntity serverManagedState = new  WebAppEntity();

    private final List<WebElementCommandDescription> commandsFromServer = new LinkedList<>();

    private final List<WebElementCommandDescription> commandsFromClient = new LinkedList<>();

    public abstract WebElementType getType();

    public BaseWebElementDescription() {
    }

    public BaseWebElementDescription(String id, String className) {
        super(id);
        this.className = className;
    }

    public List<WebElementCommandDescription> getCommandsFromClient() {
        return commandsFromClient;
    }

    public List<WebElementCommandDescription> getCommandsFromServer() {
        return commandsFromServer;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public WebAppEntity getServerManagedState() {
        return serverManagedState;
    }

}
