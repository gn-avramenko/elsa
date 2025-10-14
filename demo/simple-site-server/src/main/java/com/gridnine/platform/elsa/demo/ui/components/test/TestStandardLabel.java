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
import com.gridnine.platform.elsa.webApp.BaseTestWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
//codegen:import:end

//codegen:class:start
public class TestStandardLabel extends BaseTestWebAppUiElement {


    public TestStandardLabel(String tag, TestStandardLabelConfiguration config, OperationUiContext ctx) {
        super("common.StandardLabel", tag, ctx);
        setLabel(config.getLabel(), ctx);
    }

    public String getLabel() {
        return getProperty("label", String.class);
    }


    public void setLabel(String label, OperationUiContext ctx) {
        setProperty("label", label, ctx);
    }
//codegen:class:end
}
