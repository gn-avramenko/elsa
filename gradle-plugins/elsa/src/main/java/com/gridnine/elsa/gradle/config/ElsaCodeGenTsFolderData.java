/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElsaCodeGenTsFolderData {

    public File folder;

    public boolean dontCleanup;

    public final List<ElsaTsRemotingCodeGenRecord> remotingCodeGenRecords = new ArrayList<>();
    public final List<ElsaTsDomainCodeGenRecord> domainCodeGenRecords = new ArrayList<>();

    public final List<ElsaTsL10nCodeGenRecord> l10nCodeGenRecords = new ArrayList<>();

    public final List<ElsaTsCustomCodeGenRecord> customCodeGenRecords = new ArrayList<>();

    public boolean isDontCleanup() {
        return dontCleanup;
    }

    public void setDontCleanup(boolean dontCleanup) {
        this.dontCleanup = dontCleanup;
    }
}
