/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.domain;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

import java.time.LocalDateTime;

public class VersionInfo extends BaseIntrospectableObject {
    public static class Fields {
        public static final String revision ="revision";
        public static final String modifiedBy="modifiedBy";
        public static final String modified="modified";
        public static final String comment="comment";
        public static final String versionNumber ="versionNumber";
    }
    private int revision;

    private String modifiedBy;

    private LocalDateTime modified;

    private String comment;

    private int versionNumber;

    public int getRevision() {
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

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Object getValue(String propertyName) {
        if(Fields.comment.equals(propertyName)){
            return comment;
        }
        if(Fields.modified.equals(propertyName)){
            return modified;
        }
        if(Fields.modifiedBy.equals(propertyName)){
            return modifiedBy;
        }
        if(Fields.revision.equals(propertyName)){
            return revision;
        }
        if(Fields.versionNumber.equals(propertyName)){
            return versionNumber;
        }
        return super.getValue(propertyName);
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if(Fields.comment.equals(propertyName)){
            comment = (String) value;
            return;
        }
        if(Fields.modified.equals(propertyName)){
            modified = (LocalDateTime) value;
            return;
        }
        if(Fields.modifiedBy.equals(propertyName)){
            modifiedBy = (String) value;
            return;
        }
        if(Fields.revision.equals(propertyName)){
            revision = (int) value;
            return;
        }
        if(Fields.versionNumber.equals(propertyName)){
            versionNumber = (int) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
