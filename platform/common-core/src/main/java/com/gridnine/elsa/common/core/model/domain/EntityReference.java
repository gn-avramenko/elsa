/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.domain;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;

import java.util.Objects;

public class EntityReference<T extends BaseIdentity>{

    public static class Fields{
        public final static String id ="id";
        public final static String type ="type";
        public final static String caption ="caption";
    }
    private Class<T> type;

    private String caption;

    private long id;

    public EntityReference() {}

    public EntityReference(long id, Class<T> type, String caption) {
        this.id = id;
        this.type = type;
        this.caption = caption;
    }

    public EntityReference(T document) {
       this(document.getId(), (Class<T>) document.getClass(), document.toString());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof _CachedEntityReference<?> cer){
            return Objects.equals(type.getName(), cer.getType().getName().replace("_Cached", ""))
                    && Objects.equals(id, cer.getId());
        }
        if (o == null || getClass() != o.getClass()) return false;
        EntityReference<?> that = (EntityReference<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "EntityReference{}";
    }
}
