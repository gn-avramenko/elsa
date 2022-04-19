/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.common;

import com.gridnine.elsa.common.core.common.TestBase;
import com.gridnine.elsa.config.ElsaServerCoreConfiguration;
import com.gridnine.elsa.config.ElsaServerCoreDatabaseFactoryConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ElsaServerCoreDatabaseFactoryConfiguration.class, ElsaServerCoreConfiguration.class, ElsaServerCoreTestConfiguration.class})
public abstract class ServerCoreTestBase extends TestBase {

}
