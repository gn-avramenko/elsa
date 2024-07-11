package com.gridnine.platform.elsa.demo.app;

import com.gridnine.platform.elsa.config.ElsaDemoRemotingMetaRegistryConfigurator;
import com.gridnine.platform.elsa.core.remoting.RemotingHttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan(basePackages = "com.gridnine.platform.elsa.config")
@Configuration
public class DemoApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }

    @Bean
    public ServletRegistrationBean<RemotingHttpServlet> remotingServletBean(@Autowired RemotingHttpServlet servlet) {
        var bean = new ServletRegistrationBean<>(
                servlet, "/api/*");
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Bean
    public TestRestHandler testRestHandler(){
        return new TestRestHandler();
    }

    @Bean
    public ElsaDemoRemotingMetaRegistryConfigurator elsaDemoRemotingMetaRegistryConfigurator(){
        return new ElsaDemoRemotingMetaRegistryConfigurator();
    }
}
