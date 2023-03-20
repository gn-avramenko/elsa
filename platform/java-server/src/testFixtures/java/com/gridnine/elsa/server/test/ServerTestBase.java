/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test;

import com.gridnine.elsa.core.config.Activator;
import com.gridnine.elsa.core.test.common.TestBase;

import java.util.List;

public abstract class ServerTestBase extends TestBase {

    @Override
    protected void registerActivators(List<Activator> activators) {
        super.registerActivators(activators);
        activators.add(new ServerTestActivator());
    }
}
