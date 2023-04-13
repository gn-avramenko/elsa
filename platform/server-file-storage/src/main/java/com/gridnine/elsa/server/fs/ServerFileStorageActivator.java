/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.fs;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.meta.config.Environment;

public class ServerFileStorageActivator implements Activator {
    @Override
    public double getOrder() {
        return 5;
    }

    @Override
    public void configure() throws Exception {
        Environment.publish(new FileStorage());

    }

    @Override
    public void activate() throws Exception {

    }
}
