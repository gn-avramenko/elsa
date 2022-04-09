package com.gridnine.elsa.demo.boot;

import com.gridnine.elsa.common.core.lock.LockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.gridnine"} )
public class ElsaDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(ElsaDemoApplication.class);

    public static void main(String[] args) {
        var ctx = SpringApplication.run(ElsaDemoApplication.class, args);
        var lockManager = ctx.getBeanFactory().getBean(LockManager.class);
        lockManager.withLock("lock", () ->{
            log.info("inside lock");
            return null;
        });
        log.info("application started");
    }
}
