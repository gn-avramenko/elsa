package com.gridnine.platform.elsa.admin.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.admin.auth.AdminAuthFilter;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.IoUtils;
import com.gridnine.webpeer.core.utils.WebPeerUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class AdminLoginServlet extends HttpServlet {

    @Autowired
    private AdminAuthFilter adminAuthFilter;

    private volatile String loginHtmlContent;
    private volatile String loginCssContent;
    private volatile String loginJsContent;
    private volatile byte[] favicon;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var redirectUrl = req.getParameter("redirectUrl");
        initContent(redirectUrl);
        String pathInfo = req.getPathInfo();
        String content = loginHtmlContent;
        if (pathInfo != null) {
            if (pathInfo.endsWith(".css")) {
                content = loginCssContent;
            } else if (pathInfo.endsWith(".js")) {
               content = loginJsContent;
            } else if (pathInfo.endsWith("favicon.svg")) {
                resp.setContentType("image/svg+xml");
                try(var os = resp.getOutputStream()){
                    IoUtils.copy(new ByteArrayInputStream(favicon), os);
                    os.flush();
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }
        resp.setCharacterEncoding("UTF-8");
        try(var writer = resp.getWriter()){
            writer.write(content);
            writer.flush();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void initContent(String redirectUrl) throws IOException {
        if(loginHtmlContent==null){
            synchronized (this){
                if(loginHtmlContent==null){
                    var cnt2 = readContent(getLoginCssResource());
                    var cnt3 = readContent(getLoginJsResource()).replace("${context}", getContextPath());
                    var loginJsName = "%s/login-%s.js".formatted(getContextPath(), IoUtils.sha1sum(cnt3));
                    var loginCssName = "%s/login-%s.css".formatted(getContextPath(),IoUtils.sha1sum(cnt2));
                    var faviconSvg = "%s/favicon.svg".formatted(getContextPath());
                    var cnt1 = readContent(getLoginHtmlResource()).replace("${title}",getTitle())
                            .replace("${usernameLabel}", getUserNameLabel())
                            .replace("${passwordLabel}", getPasswordLabel())
                            .replace("${login_js}", loginJsName)
                            .replace("${favicon_svg}", faviconSvg)
                            .replace("${login_css}", loginCssName)
                            .replace("${submitButtonLabel}", getSubmitButtonLabel()).replace("${redirectUrl}", redirectUrl == null?"":redirectUrl);
                    loginCssContent = cnt2;
                    loginJsContent = cnt3;
                    var baos = new ByteArrayOutputStream();
                    try(var is= getFaviconResource().openStream()){
                        IoUtils.copy(is, baos);
                    }
                    favicon = baos.toByteArray();
                    loginHtmlContent = cnt1;

                }
            }
        }
    }

    private String readContent(URL url) throws IOException {
        var baos = new ByteArrayOutputStream();
        try(var is = url.openStream()){
            IoUtils.copy(is, baos);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login;
        String password;
        try(var is = req.getInputStream()){
            var gson = new Gson();
            var res = gson.fromJson(new InputStreamReader(is,  StandardCharsets.UTF_8), JsonObject.class);
            login = WebPeerUtils.getString(res, "login");
            password = WebPeerUtils.getString(res, "password");
        }
        var  checkResult = ExceptionUtils.wrapException(()-> checkCredentials(login, password));
        adminAuthFilter.registerSuccessfulLogin(login, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("Content-Type", "application/json");
        resp.setCharacterEncoding("UTF-8");
        try(var writer = resp.getWriter();) {
            var gson = new Gson();
            var jsonObj = new JsonObject();
            jsonObj.addProperty("errorMessage", checkResult.errorMessage);
            jsonObj.addProperty("success", checkResult.success);
            gson.toJson(jsonObj, writer);
            writer.flush();
        }
    }

    protected abstract CredentialCheckResult checkCredentials(String login, String password) throws Exception;

    protected URL getLoginHtmlResource() {
        return getClass().getClassLoader().getResource("admin/login/login.html");
    }

    protected URL getLoginCssResource() {
        return getClass().getClassLoader().getResource("admin/login/login.css");
    }
    protected URL getLoginJsResource() {
        return getClass().getClassLoader().getResource("admin/login/login.js");
    }

    protected URL getFaviconResource() {
        return getClass().getClassLoader().getResource("admin/login/favicon.svg");
    }

    protected String getContextPath(){
        return "/login";
    }

    protected String getTitle(){
        return "en:Authorization|ru:Авторизация";
    }

    protected String getUserNameLabel(){
        return "en:Login|ru:Имя пользователя";
    }
    protected String getPasswordLabel(){
        return "en:Password|ru:Пароль";
    }

    protected String getSubmitButtonLabel(){
        return "en:Login|ru:Войти";
    }

    public record CredentialCheckResult(boolean success, String errorMessage) {}
}
