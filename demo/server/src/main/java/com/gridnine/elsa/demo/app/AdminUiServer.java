/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.gridnine.platform.elsa.config","com.gridnine.elsa.demo.config"})
public class AdminUiServer {
    public static void main(String[] args) {
        SpringApplication.run(AdminUiServer.class, args);
    }
}