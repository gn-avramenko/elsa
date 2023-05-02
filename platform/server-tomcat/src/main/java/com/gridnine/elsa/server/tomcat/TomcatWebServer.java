/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.tomcat;

import com.gridnine.elsa.common.config.Configuration;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.web.HttpServletDescription;
import com.gridnine.elsa.server.web.VirtualWebApplication;
import com.gridnine.elsa.server.web.WebConfiguration;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TomcatWebServer implements Disposable {

    private final Tomcat tomcat;

    private final Logger logger =  LoggerFactory.getLogger(getClass());

    TomcatWebServer() throws Exception {
        var webAppsDir = new File(Environment.getTempFolder(), "tomcat-workdir/webapps");
        if(!webAppsDir.exists()){
            webAppsDir.mkdirs();
        }
        tomcat = new Tomcat();
        tomcat
                .setBaseDir(new File(Environment.getTempFolder(), "tomcat-workdir").getCanonicalPath());
        var port = Integer.parseInt(Configuration.get().getValue("tomcat.port", "8080"));
        tomcat.setPort(port);
        tomcat.setHostname("localhost");
        tomcat.getConnector();
        for(VirtualWebApplication webApp: WebConfiguration.get().getVirtualApplications()){
            File appDir = new File(webAppsDir, webApp.path());
            File webInfDir = new File(appDir,"WEB-INF");
            if(!webInfDir.exists()){
                webInfDir.mkdirs();
            }
            var servletSection = new StringBuilder();
            for(HttpServletDescription<?> servlet: webApp.servlets()){
                servletSection.append("""
                        <servlet>
                                <servlet-name>%s</servlet-name>
                                <servlet-class>%s</servlet-class>
                                <async-supported>%s</async-supported>
                            </servlet>
                            <servlet-mapping>
                                <servlet-name>%s</servlet-name>
                                <url-pattern>%s</url-pattern>
                            </servlet-mapping>
                        """.formatted(servlet.servletClass().getSimpleName(), servlet.servletClass().getName(), servlet.asyncSupported(),
                        servlet.servletClass().getSimpleName(), servlet.path()));
            }
            var content = """
                    <web-app version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
                                        
                        <description><![CDATA[%s]]></description>
                        <display-name>%s</display-name>
                        %s
                    </web-app>
                    """.formatted(webApp.path(), webApp.path(), servletSection.toString());
            File webXml = new File(webInfDir, "web.xml");
            if(!webXml.exists() || !Files.readString(webXml.toPath(), StandardCharsets.UTF_8).equals(content)){
                Files.writeString(webXml.toPath(), content, StandardCharsets.UTF_8);
            }
            addContext(webApp.path(), appDir);
        }
        for(var webApp: WebConfiguration.get().getWebApplications()){
            addContext(webApp.path(), webApp.warOrDir());
        }

        tomcat.init();
        tomcat.start();
    }

    private void addContext(String path, File webappDir) throws LifecycleException {

        var ctx = tomcat.addWebapp(path, webappDir.getAbsolutePath());
        if (ctx instanceof StandardContext context) {
            context.setTldValidation(false);
            context.setXmlValidation(false);
            context.setClearReferencesObjectStreamClassCaches(false);
            context.setClearReferencesRmiTargets(false);
            context.setClearReferencesThreadLocals(false);
            context.getJarScanner().setJarScanFilter((a,b)->false);
//            if (context.getState() !=  LifecycleState.STARTED) {
//                ((Lifecycle) context).start();
//            }
        }
    }

    @Override
    public void dispose() throws Throwable {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            logger.error("unable to stop tomcat",e);
        }
    }
}
