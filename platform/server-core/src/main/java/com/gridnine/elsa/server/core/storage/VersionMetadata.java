/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import java.time.LocalDateTime;

public class VersionMetadata {
    private String modifiedBy;
    private LocalDateTime modified;
    private String comment;
    private int versionNumber;

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
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

    public void setVersionNumber(int version) {
        this.versionNumber = version;
    }
}
