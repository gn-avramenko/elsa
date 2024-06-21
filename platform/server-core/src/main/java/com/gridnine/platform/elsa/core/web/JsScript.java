/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.core.web;

import java.net.URL;

public interface JsScript {
    boolean isLazy();

    URL getUrl();

    String getId();
}
