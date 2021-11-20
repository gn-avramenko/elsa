package com.gridnine.elsa.demo.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ElsaDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(ElsaDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ElsaDemoApplication.class, args);
        log.info("application started");
    }
}
