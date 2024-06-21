/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.core.web;

import com.gridnine.platform.elsa.common.core.utils.IoUtils;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class BaseWebAppServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathInfo = req.getPathInfo();
        if(TextUtils.isBlank(pathInfo)|| "/".equals(pathInfo) || "/index.html".equals(pathInfo) || "index.html".equals(pathInfo)){
            processIndexHtml(req, resp);
            return;
        }
        if(pathInfo.startsWith("/scripts")){
            processScript(req, resp);
            return;
        }
        throw new IllegalArgumentException("unable to serve path info \"%s\"".formatted(pathInfo));
    }

    private void processScript(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathInfo = req.getPathInfo();
        var scriptName = pathInfo.substring(8);
        var scriptId = scriptName.substring(1, scriptName.lastIndexOf("_"));
        var script = getScripts().stream().filter(it -> it.getId().equals(scriptId)).findFirst().get();
        var content = IoUtils.readText(script.getUrl());
        resp.setCharacterEncoding("utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.setHeader("cache-control", "max-age=2592000");
        try(var w = resp.getWriter()){
            w.write(content);
            w.flush();
        }
    }

    protected void processIndexHtml(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var template = IoUtils.readText(getIndexHtmlTemplate());
        template = insert(template, "title", "<title>%s</title>".formatted(getTitle()));
        template = insert(template, "scripts", TextUtils.join(getScripts().stream().filter(it -> !it.isLazy())
                .map(sc -> "<script type=\"text/javascript\" src=\"%s/scripts/%s\"></script>".formatted(req.getServletPath(),getScriptUrl(sc))).toList(), "\n"));
        resp.setCharacterEncoding("utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html");
        resp.setHeader("Cache-Control", "no-cache");
        try(var w = resp.getWriter()){
            w.write(template);
            w.flush();
        }
    }

    private String getScriptUrl(JsScript sc) {
        var content = IoUtils.readBytes(sc.getUrl());
        var hash = IoUtils.getMd5Hash(content);
        return "%s_%s.js".formatted(sc.getId(), hash.replace("_","-").replace("/","-"));
    }

    protected String insert(String template, String tag, String content) {
        var startTag = "<!--%s-start-->".formatted(tag);
        return template.replace(startTag, "%s\n%s".formatted(startTag, content));
    }

    protected abstract String getTitle();

    protected abstract List<JsScript> getScripts();

    protected URL getIndexHtmlTemplate(){
        return getClass().getClassLoader().getResource("webAppServlet/index.html");
    }
}
