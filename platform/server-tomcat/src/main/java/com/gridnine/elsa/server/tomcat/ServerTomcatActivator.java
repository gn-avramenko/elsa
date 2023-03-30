/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.tomcat;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.meta.config.Environment;

public class ServerTomcatActivator implements Activator {
    @Override
    public double getOrder() {
        return 5;
    }

    @Override
    public void configure() throws Exception {
        //noops
    }

    @Override
    public void activate() throws Exception {
        Environment.publish(new TomcatWebServer());
    }
}
