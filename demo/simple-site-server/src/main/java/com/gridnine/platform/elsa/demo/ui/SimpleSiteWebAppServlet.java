/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.demo.ui;

import com.gridnine.platform.elsa.demo.ui.app.WebApp;
import com.gridnine.platform.elsa.demo.ui.components.test.TestWebApp;
import com.gridnine.webpeer.core.servlet.BaseWebAppServlet;
import com.gridnine.webpeer.core.servlet.WebAppModule;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.TypedParameter;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.env.Environment;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class SimpleSiteWebAppServlet extends BaseWebAppServlet<WebApp> {

    public static final TypedParameter<ListableBeanFactory> BEAN_FACTORY = new TypedParameter<>("BEAN_FACTORY");
    @Autowired
    private ListableBeanFactory factory;

    @Override
    protected WebApp createRootElement(OperationUiContext operationUiContext) throws Exception {
        operationUiContext.setParameter(BEAN_FACTORY, factory);
        return new WebApp("root", operationUiContext);
    }

    @Override
    protected List<WebAppModule> getAllModules() throws Exception {
        return List.of(new SimpleSiteWebModule());
    }

    @Override
    protected URL getFaviconUrl() {
        return getClass().getClassLoader().getResource("simpleSite/favicon.ico");
    }

    @Override
    protected Map<String, String> getWebAppParameters() {
        return Map.of( "restPath", "/_ui", "webSocketUrl", "/websocket");
    }

    @Override
    protected String getTitle() {
        return "Simple Site";
    }

}
