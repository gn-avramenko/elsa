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

import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class TestManagersSection extends BaseWebAppUiElement {

    public TestManagersSection(String tag, OperationUiContext ctx) {
        super("account.manager.ManagersSection", tag, ctx);
        setInitParam("flexDirection", "ROW");
        var config = this.createConfiguration(ctx);
        decorateWithListeners();
    }

    private TestManagersSectionConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestManagersSectionConfiguration();
        return result;
    }

    private void decorateWithListeners() {
    }
}
