package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.platform.elsa.admin.domain.Workspace;

public interface WorkspaceProvider {
    Workspace getWorkspace() throws Exception;
}
