/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.common;

import com.gridnine.elsa.config.ElsaCommonCoreConfiguration;
import com.gridnine.elsa.config.ElsaCommonMetaConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ElsaCommonMetaConfiguration.class,
        ElsaCommonCoreConfiguration.class,
        ElsaCommonCoreTestConfiguration.class })
@TestPropertySource(properties = "")
public abstract class TestBase {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @BeforeEach
    protected void setUp(){

    }

    @AfterEach
    protected void dispose(){
    }


    public void println(Object value){
        log.debug(value.toString());
    }

}
