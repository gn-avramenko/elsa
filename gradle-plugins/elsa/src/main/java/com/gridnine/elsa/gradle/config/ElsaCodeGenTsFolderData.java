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

    public final List<ElsaTsRemotingCodeGenRecord> remotingCodeGenRecords = new ArrayList<>();
}
