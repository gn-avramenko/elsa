/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.elsa.demo.web;

import com.gridnine.platform.elsa.core.web.JsScript;

import java.net.URL;

public class TestJsScript implements JsScript {
    @Override
    public boolean isLazy() {
        return false;
    }

    @Override
    public URL getUrl() {
        return getClass().getClassLoader().getResource("test.js");
    }

    @Override
    public String getId() {
        return "test";
    }
}
