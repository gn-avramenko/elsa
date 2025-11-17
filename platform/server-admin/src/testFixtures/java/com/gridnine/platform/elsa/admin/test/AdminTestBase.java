package com.gridnine.platform.elsa.admin.test;

import com.gridnine.platform.elsa.config.AdminBasicConfiguration;
import com.gridnine.platform.elsa.config.AdminConfiguration;
import com.gridnine.platform.elsa.server.atomikos.test.AtomikosTestBase;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AdminBasicConfiguration.class, AdminConfiguration.class})
public class AdminTestBase extends AtomikosTestBase {

}
