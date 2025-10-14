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

package com.gridnine.platform.elsa.demo.ui.account;

import com.gridnine.platform.elsa.demo.ui.app.WebApp;
import com.gridnine.platform.elsa.webApp.common.FlexDirection;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class AccountNavigation extends AccountNavigationSkeleton{

	public AccountNavigation(String tag, OperationUiContext ctx){
		super(tag, ctx);
	}

    @Override
    protected AccountNavigationConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new AccountNavigationConfiguration();
        result.setFlexDirection(FlexDirection.COLUMN);
        {
            var organizationsLink = new AccountNavigationButtonConfiguration();
            organizationsLink.setTitle("Organizations");
            organizationsLink.setClickListener((ct) ->{
                WebApp.lookup(AccountNavigation.this).navigate("/account/organizations", ct);
            });
            result.setOrganizationsLink(organizationsLink);
        }
        {
            var managersLink = new AccountNavigationButtonConfiguration();
            managersLink.setTitle("Managers");
            managersLink.setClickListener((ct) ->{
                WebApp.lookup(AccountNavigation.this).navigate("/account/managers", ct);
            });
            result.setManagersLink(managersLink);
        }
        {
            var doctorsLink = new AccountNavigationButtonConfiguration();
            doctorsLink.setTitle("Doctors");
            doctorsLink.setClickListener((ct) ->{
                WebApp.lookup(AccountNavigation.this).navigate("/account/doctors", ct);
            });
            result.setDoctorsLink(doctorsLink);
        }
        {
            var clientsLink = new AccountNavigationButtonConfiguration();
            clientsLink.setTitle("Clients");
            clientsLink.setClickListener((ct) ->{
                WebApp.lookup(AccountNavigation.this).navigate("/account/clients", ct);
            });
            result.setClientsLink(clientsLink);
        }
        {
            var accountLink = new AccountNavigationButtonConfiguration();
            accountLink.setTitle("Account");
            accountLink.setClickListener((ct) ->{
                WebApp.lookup(AccountNavigation.this).navigate("/account/account", ct);
            });
            result.setAccountLink(accountLink);
        }
        {
            var securityLink = new AccountNavigationButtonConfiguration();
            securityLink.setTitle("Security");
            securityLink.setClickListener((ct) ->{
                WebApp.lookup(AccountNavigation.this).navigate("/account/security", ct);
            });
            result.setSecurityLink(securityLink);
        }
        {
            var exitLink = new AccountNavigationButtonConfiguration();
            exitLink.setTitle("Exit");
            exitLink.setClickListener((ct) ->{
                System.out.println("exitLink clicked");
            });
            result.setExitLink(exitLink);
        }
        return result;
    }
}