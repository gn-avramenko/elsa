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

public class TestNavigationPanel extends BaseWebAppUiElement {

    private final TestStandardLink historyLink;

    private final TestStandardLink mainLink;

    private final TestAccountLink accountLink;

    private final TestLanguageSelector languageSelector;

    public TestNavigationPanel(String tag, OperationUiContext ctx) {
        super("app.NavigationPanel", tag, ctx);
        setInitParam("flexDirection", "ROW");
        var config = this.createConfiguration(ctx);
        historyLink = new TestStandardLink("historyLink", config.getHistoryLink(), ctx);
        addChild(ctx, historyLink, 0);
        mainLink = new TestStandardLink("mainLink", config.getMainLink(), ctx);
        addChild(ctx, mainLink, 0);
        accountLink = new TestAccountLink("accountLink", config.getAccountLink(), ctx);
        addChild(ctx, accountLink, 0);
        languageSelector = new TestLanguageSelector("languageSelector", config.getLanguageSelector(), ctx) ;
        addChild(ctx, languageSelector, 0);
        decorateWithListeners();
    }

    private TestNavigationPanelConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestNavigationPanelConfiguration();
        result.setHistoryLink(new TestStandardLinkConfiguration());
        result.getHistoryLink().setTitle("History");
        result.setMainLink(new TestStandardLinkConfiguration());
        result.getMainLink().setTitle("Main");
        result.setAccountLink(new TestAccountLinkConfiguration());
        result.getAccountLink().setTitle("Account");
        result.getAccountLink().setInsideAccountSection(false);
        result.getAccountLink().setLoggedIn(false);
        result.setLanguageSelector(new TestLanguageSelectorConfiguration());
        result.getLanguageSelector().getOptions().add(new TestOption("ru", "Ru"));
        result.getLanguageSelector().getOptions().add(new TestOption("en", "En"));
        result.getLanguageSelector().setSelectedId("ru");
        return result;
    }

    private void decorateWithListeners() {
        historyLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/history", ctx);
        });
        mainLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/main", ctx);
        });
        accountLink.setClickListener((ctx) ->{
            System.out.println("Clicked on account link");
        });
        languageSelector.setSelectionListener((ctx, language) -> {
            System.out.println("Selected language: " + language);
            languageSelector.setSelectedId(language, ctx);
        });
    }
}
