/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.web;

import java.util.Arrays;
import java.util.List;

public record VirtualWebApplication(String path, List<HttpServletDescription<?>> servlets) {
    public VirtualWebApplication(String path, HttpServletDescription<?>... servlets) {
        this(path, Arrays.asList(servlets));
    }
}
