/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.elsa.demo.web;

import com.gridnine.platform.elsa.core.web.BaseWebAppServlet;
import com.gridnine.platform.elsa.core.web.JsScript;

import java.util.List;

public class AdminUiServlet extends BaseWebAppServlet {

    private final TestJsScript testJsScript = new TestJsScript();

    @Override
    protected String getTitle() {
        return "Demo admin app";
    }

    @Override
    protected List<JsScript> getScripts() {
        return List.of(testJsScript);
    }
}
