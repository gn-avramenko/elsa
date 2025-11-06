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

package com.gridnine.platform.elsa.demo.admin.config;

import com.gridnine.platform.elsa.demo.admin.organization.OrganizationUiListHandler;
import com.gridnine.platform.elsa.demo.admin.boot.AdminActivator;
import com.gridnine.platform.elsa.demo.admin.country.CountryUiListHandler;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoAdminConfiguration {

    @Bean
    public AdminWebAppServlet demoRootWebAppServlet() {
        return new AdminWebAppServlet();
    }
    @Bean
    public ServletRegistrationBean<AdminWebAppServlet> demoRootWebAppServletServletRegistrationBean(){
        return new ServletRegistrationBean<>(demoRootWebAppServlet(), "/*");
    }

    @Bean
    public AdminActivator demoRootActivator() {
        return new AdminActivator();
    }
    @Bean
    public WsContextListener wsContextListener(){
        return new WsContextListener();
    }

    @Bean
    public CountryUiListHandler countryUiListHandler(){
        return new CountryUiListHandler();
    }

    @Bean
    public OrganizationUiListHandler  organizationUiListHandler(){
        return new OrganizationUiListHandler();
    }

}
