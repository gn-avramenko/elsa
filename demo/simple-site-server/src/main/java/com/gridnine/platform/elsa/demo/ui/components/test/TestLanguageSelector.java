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
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAnd2Arguments;
import com.gridnine.platform.elsa.webApp.BaseTestWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.util.List;
//codegen:import:end

//codegen:class:start
public class TestLanguageSelector extends BaseTestWebAppUiElement {

    private RunnableWithExceptionAnd2Arguments<OperationUiContext, String> selectionListener;

    public TestLanguageSelector(String tag, TestLanguageSelectorConfiguration config, OperationUiContext ctx) {
        super("app.LanguageSelector", tag, ctx);
         setOptions(config.getOptions(), ctx);
         setSelectedId(config.getSelectedId(), ctx);
    }

    public void setOptions(List<TestOption> options, OperationUiContext ctx) {
        setProperty("options", options, ctx);
    }

    public List<TestOption> getOptions(){
        //noinspection unchecked
        return (List<TestOption>) getProperty("options", List.class);
    }

    public void setSelectedId(String selectedId, OperationUiContext ctx) {
        setProperty("selectedId", selectedId, ctx);
    }

    public String getSelectedId(){
        return getProperty("selectedId", String.class);
    }

    public void setSelectionListener(RunnableWithExceptionAnd2Arguments<OperationUiContext, String> selectionListener) {
        this.selectionListener = selectionListener;
    }

    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if("select".equals(commandId)){
            this.selectionListener.run(ctx, WebPeerUtils.getString(data.getAsJsonObject(), "selectedId"));
            return;
        }
        super.processCommand(ctx, commandId, data);
    }
//codegen:class:end
}
