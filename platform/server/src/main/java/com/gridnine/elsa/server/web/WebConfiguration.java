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

    private final List<WebApplication> webApplications = new ArrayList<>();

    public void register(VirtualWebApplication app){
        virtualApplications.add(app);
    }

    public void register(WebApplication app){
        webApplications.add(app);
    }

    public List<VirtualWebApplication> getVirtualApplications() {
        return virtualApplications;
    }

    public List<WebApplication> getWebApplications() {
        return webApplications;
    }

    public static WebConfiguration get(){
        return Environment.getPublished(WebConfiguration.class);
    }
}
