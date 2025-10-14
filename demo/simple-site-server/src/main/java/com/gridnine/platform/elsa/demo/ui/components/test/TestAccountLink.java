//codegen:header:start
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
//codegen:header:end
//codegen:import:start
import com.google.gson.JsonElement;
import com.gridnine.platform.elsa.webApp.BaseTestWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.RunnableWithExceptionAndArgument;
//codegen:import:end

//codegen:class:start
public class TestAccountLink extends BaseTestWebAppUiElement {

    private RunnableWithExceptionAndArgument<OperationUiContext> clickListener;

    public TestAccountLink(String tag, TestAccountLinkConfiguration config, OperationUiContext ctx) {
        super("app.AccountLink", tag, ctx);
        setTitle(config.getTitle(), ctx);
        setInsideAccountSection(config.isInsideAccountSection(), ctx);
        setLoggedIn(config.isLoggedIn(), ctx);
    }

    public void setInsideAccountSection(boolean loggedIn, OperationUiContext ctx) {
        setProperty("insideAccountSection", loggedIn, ctx);
    }

    public boolean isInsideAccountSection(){
        return getProperty("insideAccountSection", Boolean.class);
    }

    public void setLoggedIn(boolean loggedIn, OperationUiContext ctx) {
        setProperty("loggedIn", loggedIn, ctx);
    }

    public boolean isLoggedIn(){
        return getProperty("loggedIn", Boolean.class);
    }

    public String getTitle() {
        return getProperty("title", String.class);
    }

    public void setClickListener(RunnableWithExceptionAndArgument<OperationUiContext> clickListener) {
        this.clickListener = clickListener;
    }

    public void setTitle(String title, OperationUiContext ctx) {
        setProperty("title", title, ctx);
    }
    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if("click".equals(commandId)){
            this.clickListener.run(ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }
//codegen:class:end
}
