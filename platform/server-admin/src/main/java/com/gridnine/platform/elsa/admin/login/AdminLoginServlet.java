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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        var resource = getLoginHtmlResource();
        var redirectUrl = req.getParameter("redirectUrl");
        if (pathInfo != null) {
            if (pathInfo.contains("login.css")) {
                resource = getLoginCssResource();
            } else if (pathInfo.contains("login.js")) {
                resource = getLoginJsResource();
            }
        }
        var baos =new ByteArrayOutputStream();
        try(var is = resource.openStream()){
            IoUtils.copy(is, baos);
        }
        var content = baos.toString(StandardCharsets.UTF_8).replace("${title}",getTitle()).replace("${context}", getContextPath())
                .replace("${username}", getUserNameLabel())
                .replace("${password}", getPasswordLabel())
                .replace("${submitButton}", getSubmitButtonLabel()).replace("${redirectUrl}", redirectUrl == null?"":redirectUrl);
        resp.setCharacterEncoding("UTF-8");
        try(var is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))){
            var os = resp.getOutputStream();
            IoUtils.copy(is, os);
            os.flush();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
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

    protected String getContextPath(){
        return "/login";
    }

    protected String getTitle(){
        return "Авторизация";
    }

    protected String getUserNameLabel(){
        return "Имя пользователя:";
    }
    protected String getPasswordLabel(){
        return "Имя пользователя:";
    }

    protected String getSubmitButtonLabel(){
        return "Войти";
    }

    public record CredentialCheckResult(boolean success, String errorMessage) {}
}
