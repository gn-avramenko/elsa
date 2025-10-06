/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.meta.webApp;

import com.gridnine.platform.elsa.gradle.meta.common.BaseElementWithId;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public abstract class BaseWebElementDescription {

    private String className;

    private final WebAppEntity serverManagedState = new  WebAppEntity();

    private final List<WebElementCommandDescription> commandsFromServer = new LinkedList<>();

    private final List<WebElementCommandDescription> commandsFromClient = new LinkedList<>();

    private final Map<String, InputDescription> inputs = new LinkedHashMap<>();

    public abstract WebElementType getType();

    public BaseWebElementDescription() {
    }

    public BaseWebElementDescription(String className) {
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

    public Map<String, InputDescription> getInputs() {
        return inputs;
    }
}
