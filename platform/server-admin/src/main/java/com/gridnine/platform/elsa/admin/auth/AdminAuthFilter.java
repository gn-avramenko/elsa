package com.gridnine.platform.elsa.admin.auth;

import com.gridnine.platform.elsa.admin.utils.HttpUtils;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.core.auth.AuthContext;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AdminAuthFilter extends HttpFilter {
    private final Map<String, String> authIds = new ConcurrentHashMap<>();

    private final static String AUTH_COOKIE_ID =  "admin-auth-cookie";

    public void logout(HttpServletResponse resp) {
        var login = AuthContext.getCurrentUser();
        var entry = authIds.entrySet().stream().filter(it -> it.getValue().equals(login)).findFirst().orElse(null);
        if(entry != null){
            authIds.remove(entry.getKey());
        } else {
            var cookie = new Cookie(AUTH_COOKIE_ID, entry.getKey());
            cookie.setPath("/");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }
    }

    public void registerSuccessfulLogin(String login, HttpServletResponse resp) {
        var entry = authIds.entrySet().stream().filter(it -> it.getValue().equals(login)).findFirst().orElse(null);
        var authId = UUID.randomUUID().toString();
        if (entry != null) {
            authId =  entry.getKey();
        } else {
            authIds.put(authId, login);
        }
        var cookie = new Cookie(AUTH_COOKIE_ID, authId);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(request.getRequestURI() != null && request.getRequestURI().startsWith("/login")){
            chain.doFilter(request,res);
            return;
        }
        if(request.getPathInfo() != null && (request.getPathInfo().contains("favicon.svg")||request.getPathInfo().contains("favicon.ico")||request.getPathInfo().contains("fav.ico"))){
            chain.doFilter(request,res);
            return;
        }
        if(request.getQueryString() != null && request.getQueryString().contains("action=destroy")){
            chain.doFilter(request,res);
            return;
        }
        try {
            var cookieId = request.getCookies() == null? null: Arrays.stream(request.getCookies()).filter(it -> AUTH_COOKIE_ID.equals(it.getName())).findFirst().orElse(null);
            if (cookieId != null) {
                String userName = authIds.get(cookieId.getValue());
                if (userName != null) {
                    AuthContext.setCurrentUser(userName);
                    chain.doFilter(request, res);
                    return;
                }
            }
            var authToken = HttpUtils.getQueryParameter(request, "token");
            if (authToken != null) {
                String login = getLoginByToken(authToken);
                var entry = authIds.entrySet().stream().filter(it -> it.getValue().equals(login)).findFirst().orElse(null);
                var authId = UUID.randomUUID().toString();
                if (entry != null) {
                    authId =  entry.getKey();
                } else {
                    authIds.put(authId, login);
                }
                AuthContext.setCurrentUser(login);
                var cookie = new Cookie(AUTH_COOKIE_ID, authId);
                cookie.setPath("/");
                res.addCookie(cookie);
                chain.doFilter(request, res);
                return;
            }
            res.setStatus(401);
            res.sendRedirect("/login?redirectUrl=/");
        } finally {
            AuthContext.resetCurrentUser();
            LocaleUtils.resetCurrentLocale();
        }
    }

    protected String getLoginByToken(String authToken) {
        throw Xeption.forDeveloper("not implemented yet");
    }

    protected String getRedirectUrl(){
        return "/login";
    }
}
