package com.gridnine.elsa.demo.boot;

import com.gridnine.elsa.common.core.boot.ElsaActivator;
import com.gridnine.elsa.config.ElsaCommonCoreConfiguration;
import com.gridnine.elsa.config.ElsaCommonMetaConfiguration;
import com.gridnine.elsa.demo.activator.ElsaDemoActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication(scanBasePackages = {"com.gridnine.elsa.config","com.gridnine.elsa.demo.config" })
public class ElsaDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(ElsaDemoApplication.class);

    public static void main(String[] args){
        var ctx = SpringApplication.run(ElsaDemoApplication.class, args);
        ElsaActivator.performActivation(ctx);
    }

}
