package com.gridnine.elsa.demo.boot;

import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class TestComponent {

//    @Autowired
//    public TestComponent(DomainMetaRegistry registry){
//        System.out.println(registry);
//    }
public TestComponent(){
    System.out.println("test component");
}
    private final Logger log = LoggerFactory.getLogger(getClass());
    @PreDestroy
    public void preDestroy(){
        log.info("test component is destroyed");
    }
}
