/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.web;

import com.gridnine.elsa.meta.config.Environment;

import java.util.ArrayList;
import java.util.List;

public class WebConfiguration {

    private final List<VirtualWebApplication> virtualApplications = new ArrayList<>();

    public void register(VirtualWebApplication app){
        virtualApplications.add(app);
    }

    public List<VirtualWebApplication> getVirtualApplications() {
        return virtualApplications;
    }

    public static WebConfiguration get(){
        return Environment.getPublished(WebConfiguration.class);
    }
}
