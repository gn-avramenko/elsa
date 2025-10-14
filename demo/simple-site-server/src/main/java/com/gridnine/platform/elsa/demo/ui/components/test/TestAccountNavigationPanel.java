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

import com.gridnine.platform.elsa.webApp.BaseTestWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class TestAccountNavigationPanel extends BaseTestWebAppUiElement {

    private final TestAccountNavigationLink organizationsLink;
    private final TestAccountNavigationLink managersLink;
    private final TestAccountNavigationLink doctorsLink;
    private final TestAccountNavigationLink clientsLink;
    private final TestAccountNavigationLink accountLink;
    private final TestAccountNavigationLink securityLink;
    private final TestAccountNavigationLink exitLink;


    public TestAccountNavigationPanel(String tag, OperationUiContext ctx) {
        super("account.AccountNavigation", tag, ctx);
        setInitParam("flexDirection", "COLUMN");
        var config = this.createConfiguration(ctx);
        organizationsLink = new TestAccountNavigationLink("organizationsLink", config.getOrganizationsLink(),ctx);
        addChild(ctx, organizationsLink, 0);
        managersLink = new TestAccountNavigationLink("managersLink", config.getManagersLink(),ctx);
        addChild(ctx, managersLink, 0);
        doctorsLink = new TestAccountNavigationLink("doctorsLink", config.getDoctorsLink(),ctx);
        addChild(ctx, doctorsLink, 0);
        clientsLink = new TestAccountNavigationLink("clientsLink", config.getClientsLink(),ctx);
        addChild(ctx, clientsLink, 0);
        accountLink = new TestAccountNavigationLink("accountLink", config.getAccountLink(),ctx);
        addChild(ctx, accountLink, 0);
        securityLink = new TestAccountNavigationLink("securityLink", config.getSecurityLink(),ctx);
        addChild(ctx, securityLink, 0);
        exitLink = new TestAccountNavigationLink("exitLink", config.getExitLink(),ctx);
        addChild(ctx, exitLink, 0);
        decorateWithListeners();
    }

    private TestAccountNavigationPanelConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestAccountNavigationPanelConfiguration();
        result.setAccountLink(new TestAccountNavigationLinkConfiguration());
        result.getAccountLink().setTitle("Account");
        result.getAccountLink().setVisible(true);
        result.setClientsLink(new TestAccountNavigationLinkConfiguration());
        result.getClientsLink().setTitle("Clients");
        result.getClientsLink().setVisible(true);
        result.setDoctorsLink(new TestAccountNavigationLinkConfiguration());
        result.getDoctorsLink().setTitle("Doctors");
        result.getDoctorsLink().setVisible(true);
        result.setExitLink(new TestAccountNavigationLinkConfiguration());
        result.getExitLink().setTitle("Exit");
        result.getExitLink().setVisible(true);
        result.setManagersLink(new TestAccountNavigationLinkConfiguration());
        result.getManagersLink().setTitle("Managers");
        result.getManagersLink().setVisible(true);
        result.setOrganizationsLink(new TestAccountNavigationLinkConfiguration());
        result.getOrganizationsLink().setTitle("Organizations");
        result.getOrganizationsLink().setVisible(true);
        result.setSecurityLink(new TestAccountNavigationLinkConfiguration());
        result.getSecurityLink().setTitle("Security");
        result.getSecurityLink().setVisible(true);
        return result;
    }

    private void decorateWithListeners() {
        organizationsLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/account/organizations", ctx);
        });
        managersLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/account/managers", ctx);
        });
        doctorsLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/account/doctors", ctx);
        });
        clientsLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/account/clients", ctx);
        });
        accountLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/account/account", ctx);
        });
        securityLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/account/security", ctx);
        });
        exitLink.setClickListener((ctx) ->{
            TestWebApp.lookup(this).navigate("/main", ctx);
        });
    }
}
