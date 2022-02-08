/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EntityDescription extends BaseModelElementDescription{
    private boolean hasId;

    private boolean isAbstract;

    private String extendsId;

    private final Map<String, StandardPropertyDescription> properties = new LinkedHashMap<>();

    private final Map<String, StandardCollectionDescription> collections = new LinkedHashMap<>();

    private final Map<String, StandardMapDescription> maps = new LinkedHashMap<>();

    private final List<String> codeInjections = new ArrayList<>();

    public EntityDescription() {
    }

    public EntityDescription(String id) {
        super(id);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public String getExtendsId() {
        return extendsId;
    }

    public void setExtendsId(String extendsId) {
        this.extendsId = extendsId;
    }

    public Map<String, StandardPropertyDescription> getProperties() {
        return properties;
    }

    public Map<String, StandardCollectionDescription> getCollections() {
        return collections;
    }

    public Map<String, StandardMapDescription> getMaps() {
        return maps;
    }

    public List<String> getCodeInjections() {
        return codeInjections;
    }

    public boolean isHasId() {
        return hasId;
    }

    public void setHasId(boolean hasId) {
        this.hasId = hasId;
    }
}
