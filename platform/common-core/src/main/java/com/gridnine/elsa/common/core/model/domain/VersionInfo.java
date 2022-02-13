/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.domain;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

import java.time.LocalDateTime;

public class VersionInfo extends BaseIntrospectableObject {
    public static class Properties{
        public static final String revision ="revision";
        public static final String modifiedBy="modifiedBy";
        public static final String modified="modified";
        public static final String comment="comment";
    }
    private long revision;

    private String modifiedBy;

    private LocalDateTime modified;

    private String comment;

    public long getRevision() {
        return revision;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getValue(String propertyName) {
        if(Properties.comment.equals(propertyName)){
            return comment;
        }
        if(Properties.modified.equals(propertyName)){
            return modified;
        }
        if(Properties.modifiedBy.equals(propertyName)){
            return modifiedBy;
        }
        if(Properties.revision.equals(propertyName)){
            return revision;
        }
        return super.getValue(propertyName);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if(Properties.comment.equals(propertyName)){
            comment = (String) value;
            return;
        }
        if(Properties.modified.equals(propertyName)){
            modified = (LocalDateTime) value;
            return;
        }
        if(Properties.modifiedBy.equals(propertyName)){
            modifiedBy = (String) value;
            return;
        }
        if(Properties.revision.equals(propertyName)){
            revision = (long) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
