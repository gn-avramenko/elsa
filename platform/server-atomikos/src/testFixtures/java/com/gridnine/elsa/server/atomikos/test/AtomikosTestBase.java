/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.atomikos.test;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.server.atomikos.ServerAtomikosActivator;
import com.gridnine.elsa.server.test.ServerTestBase;

import java.io.File;
import java.util.List;

public class AtomikosTestBase extends ServerTestBase {

    @Override
    protected void setUp() throws Exception {
        System.setProperty("com.atomikos.icatch.file", new File("src/testFixtures/java/com/gridnine/elsa/server/atomikos/test/jta.properties").getAbsolutePath());
        System.setProperty("atomikos-log-base-dir", new File("temp/atomikos").getAbsolutePath());
        super.setUp();
    }

    @Override
    protected void registerActivators(List<Activator> activators) {
        super.registerActivators(activators);
        activators.add(new ServerAtomikosActivator());
    }
}
