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
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.RunnableWithExceptionAndArgument;
//codegen:import:end

//codegen:class:start
public class TestSearchField extends BaseWebAppUiElement {

    private RunnableWithExceptionAndArgument<OperationUiContext> valueChangeListener;

    public TestSearchField(String tag, TestSearchFieldConfiguration config, OperationUiContext ctx) {
        super("common.SearchField", tag, ctx);
        setValue(config.getValue(), ctx);
        setHidden(config.isHidden(), ctx);
        setDebounceTime(config.getDebounceTime(), ctx);
        setDisabled(config.isDisabled(), ctx);
    }

    public void setValue(String value, OperationUiContext context) {
        setProperty("value", value, context);
    }

    public void setHidden(boolean value, OperationUiContext context) {
        setProperty("hidden", value, context);
    }
    public void setDisabled(boolean value, OperationUiContext context) {
        setProperty("disabled", value, context);
    }
    public void setDebounceTime(Integer value, OperationUiContext context) {
        setProperty("debounceTime", value, context);
    }

    public Integer getDebounceTime() {
        return getProperty("debounceTime", Integer.class);
    }

    public boolean isHidden() {
        return getProperty("hidden", Boolean.class);
    }

    public boolean isDisabled() {
        return getProperty("disabled", Boolean.class);
    }

    public String getValue() {
        return getProperty("value", String.class);
    }

    public void setValueChangeListener(RunnableWithExceptionAndArgument<OperationUiContext> valueChangeListener, OperationUiContext context) {
        this.valueChangeListener = valueChangeListener;
        setProperty("trackValueChange", this.valueChangeListener != null, context);
    }


    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if("value-changed".equals(commandId)){
            this.valueChangeListener.run(ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }
//codegen:class:end
}
