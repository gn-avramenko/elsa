/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.common;

import com.gridnine.elsa.common.core.utils.TextUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ElsaCoreTestConfiguration.class })
public abstract class TestBase {
    private File appHome = new File(
            String.format("./test/app-home/%s/", TextUtils.generateUUID()));

    @Autowired
    private Environment environment;

    @BeforeEach
    protected void setUp(){
        setUpEnvironment();
    }

    @AfterEach
    protected void dispose(){
        FileSystemUtils.deleteRecursively(appHome);
    }

    protected void setUpEnvironment() {
        if (appHome.exists()) {
            FileSystemUtils.deleteRecursively(appHome);
        }
        assert appHome.mkdirs();
        environment.configure(appHome, true);
    }

}
