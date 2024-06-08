/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.config;

import com.gridnine.platform.elsa.admin.AdminUiServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerAdminConfiguration {
    @Bean
    public AdminUiServlet adminUiServlet(){
        return new AdminUiServlet();
    }
}
