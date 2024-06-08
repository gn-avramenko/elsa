/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.elsa.demo.config;

import com.gridnine.platform.elsa.admin.AdminUiServlet;
import com.gridnine.platform.elsa.core.remoting.RemotingHttpServlet;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminUiConfiguration {

    @Value("${elsa.remoting.apiPrefix:api}")
    private String apiPrefix;

    @Autowired
    private RemotingHttpServlet remotingHttpServlet;

    @Autowired
    private AdminUiServlet adminUiServlet;

    @Bean
    public ServletRegistrationBean<RemotingHttpServlet> createRestServicesServlet() {
        ServletRegistrationBean<RemotingHttpServlet> result = new ServletRegistrationBean<>(
                remotingHttpServlet, "/%s/*".formatted(apiPrefix));
        var fsThreshold = 1024 * 1024 * 10; // 10 MiB
        result.setMultipartConfig(new MultipartConfigElement("/tmp", -1, -1, fsThreshold));
        result.setLoadOnStartup(1);
        return result;
    }
    @Bean
    public ServletRegistrationBean<AdminUiServlet> createAdminUiServlet() {
        ServletRegistrationBean<AdminUiServlet> result = new ServletRegistrationBean<>(
                adminUiServlet, "","/admin/*");
        result.setLoadOnStartup(2);
        return result;
    }
}
