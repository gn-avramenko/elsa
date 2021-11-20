package com.gridnine.elsa.demo.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class TestComponent {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @PreDestroy
    public void preDestroy(){
        log.info("test component is destroyed");
    }
}
