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

import com.gridnine.platform.elsa.webApp.common.FlexDirection;
import com.gridnine.platform.elsa.webApp.common.Option;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class NavigationPanel extends NavigationPanelSkeleton{

	public NavigationPanel(String tag, OperationUiContext ctx){
		super(tag, ctx);
	}

    @Override
    protected NavigationPanelConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new NavigationPanelConfiguration();
        result.setFlexDirection(FlexDirection.ROW);
        {
            var mainLinkConfig = new StandardLinkConfiguration();
            mainLinkConfig.setTitle("Main");
            mainLinkConfig.setClickListener(ct ->{
                WebApp.lookup(NavigationPanel.this).navigate("/main", ct);
            });
            result.setMainLink(mainLinkConfig);
        }
        {
            var historyLinkConfig = new StandardLinkConfiguration();
            historyLinkConfig.setTitle("History");
            historyLinkConfig.setClickListener(ct ->{
                WebApp.lookup(NavigationPanel.this).navigate("/history", ct);
            });
            result.setHistoryLink(historyLinkConfig);
        }
        {
            var accountLinkConfig = new AccountLinkConfiguration();
            accountLinkConfig.setTitle("Account");
            accountLinkConfig.setClickListener(ct ->{
                WebApp.lookup(NavigationPanel.this).navigate("/account/organizations", ct);
            });
            result.setAccountLink(accountLinkConfig);
        }
        {
            var languageSelector = new LanguageSelectorConfiguration();
            {
                var option = new Option();
                option.setId("ru");
                option.setDisplayName("Русский");
                languageSelector.getOptions().add(option);
            }
            {
                var option = new Option();
                option.setId("en");
                option.setDisplayName("English");
                languageSelector.getOptions().add(option);
            }
            {
                var value = new LanguageSelectorInputValue();
                value.getValues().add("en");
                languageSelector.setValue(value);
            }
            languageSelector.setValueChangeListener(((oldValue, newValue, context) ->
              System.out.println("new value = %s".formatted(newValue.getValues().get(0)))
            ));
            result.setLanguageSelector(languageSelector);
        }
        return result;
    }
}