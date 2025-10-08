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
 *
 *****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.platform.elsa.demo.ui.app;

import com.gridnine.platform.elsa.demo.ui.components.test.TestWebApp;
import com.gridnine.platform.elsa.webApp.BaseTestWebAppUiElement;
import com.gridnine.platform.elsa.webApp.common.FlexDirection;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class WebApp extends WebAppSkeleton{

	public WebApp(String tag, OperationUiContext ctx){
		super(tag, ctx);
	}

    @Override
    protected WebAppConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new WebAppConfiguration();
        result.setFlexDirection(FlexDirection.COLUMN);
        return result;
    }

    public void navigate(String path, OperationUiContext ctx) {
        getMainRouter().navigate(path, false, ctx);
    }

    public static WebApp lookup(BaseUiElement elm) {
        return lookupInternal(elm);
    }

    private static WebApp lookupInternal(BaseUiElement elm) {
        if (elm instanceof WebApp) {
            return (WebApp) elm;
        }
        return lookupInternal(elm.getParent());
    }

}