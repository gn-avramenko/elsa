package com.gridnine.platform.elsa.admin.web.entityEditor;

import java.io.Serializable;
import java.util.UUID;

public record WriteDataResult(boolean success, String errorMessage, String id) implements Serializable {
}
