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

package com.gridnine.platform.elsa.demo.ui.components.test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

public class TestWebApp extends BaseWebAppUiElement {

    private final TestNavigationPanel navigation;

    private final TestWebAppRouter router;

    public TestWebApp(String tag, OperationUiContext ctx) {
        super("app.WebApp", tag, ctx);
        var config = createConfiguration(ctx);
        setInitParam("flexDirection", "COLUMN");
        navigation = new TestNavigationPanel("navigation", ctx);
        addChild(ctx, navigation, 0);
        router = new TestWebAppRouter("router", ctx);
        addChild(ctx, router, 0);
    }

    public static TestWebApp lookup(BaseWebAppUiElement elm) {
        return lookupInternal(elm);
    }

    public void navigate(String path, OperationUiContext ctx) {
        router.navigate(path, false, ctx);
    }

    private static TestWebApp lookupInternal(BaseUiElement elm) {
        if(elm instanceof TestWebApp) {
            return (TestWebApp) elm;
        }
        return lookupInternal(elm.getParent());
    }

    public void notify(String message, OperationUiContext ctx) {
        var command = new JsonObject();
        command.addProperty("message", message);
        this.sendCommand(ctx, "notify", command);
    }

    public void confirm(String message, long elementId, String commandId, JsonElement commandData, OperationUiContext ctx) {
        var command = new JsonObject();
        command.addProperty("message", message);
        command.addProperty("id", String.valueOf(elementId));
        command.addProperty("cmd", commandId);
        WebPeerUtils.addProperty(command, "data",commandData);
        sendCommand(ctx, "confirm", command);
    }

    private TestWebAppConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestWebAppConfiguration();
        return result;
    }

}
