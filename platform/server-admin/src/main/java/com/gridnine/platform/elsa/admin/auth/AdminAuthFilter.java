package com.gridnine.platform.elsa.admin.auth;

import com.gridnine.platform.elsa.admin.domain.LoginToken;
import com.gridnine.platform.elsa.admin.domain.LoginTokenFields;
import com.gridnine.platform.elsa.admin.utils.HttpUtils;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.search.SearchQuery;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.platform.elsa.core.auth.AuthContext;
import com.gridnine.platform.elsa.core.storage.Storage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AdminAuthFilter extends HttpFilter {
    private final Map<String, TokenData> authIds = new ConcurrentHashMap<>();

    private final static String AUTH_COOKIE_ID =  "admin-auth-cookie";

    @Autowired
    private Storage storage;

    @Value("${elsa.auth.tokenPeriod:24}")
    private int authTokenPeriod;

    @Value("${elsa.auth.developmentUserName:#{null}}")
    private String developmentUserName;

    private final Timer cleanupTimer;

    public AdminAuthFilter() {
        cleanupTimer = new Timer("login-tokens-cleanup");
    }

    @PreDestroy
    public void destroy() {
        cleanupTimer.cancel();
    }
    @PostConstruct
    public void init() {
        cleanupTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                var logins = storage.searchAssets(LoginToken.class, new SearchQuery(), true);
                var now = Instant.now();
                logins.forEach(login -> {
                    var tokenData = authIds.get(login.getToken());
                    if(tokenData != null) {
                        if(tokenData.created().plusSeconds(TimeUnit.HOURS.toSeconds(authTokenPeriod)).isBefore(now)) {
                            authIds.remove(login.getToken());
                            storage.deleteAsset(login);
                        } else if(!tokenData.created().equals(login.getCreated())) {
                            login.setCreated(tokenData.created());
                            storage.saveAsset(login, false, "cleanup-task");
                        }
                        return;
                    }
                    if(login.getCreated().plusSeconds(TimeUnit.HOURS.toSeconds(authTokenPeriod)).isBefore(now)) {
                        storage.deleteAsset(login);
                    }
                });
            }
        }, 10_000, 3600_000);
    }


    public void logout(HttpServletResponse resp) {
        var login = AuthContext.getCurrentUser();
        var entry = authIds.entrySet().stream().filter(it -> it.getValue().equals(login)).findFirst().orElse(null);
        if(entry != null){
            authIds.remove(entry.getKey());
            var token = storage.findUniqueAsset(LoginToken.class, LoginTokenFields.token, entry.getKey(), false);
            if(token != null){
                storage.deleteAsset(token);
            }
        }
        var cookie = new Cookie(AUTH_COOKIE_ID, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public void registerSuccessfulLogin(String login, HttpServletResponse resp) {
        var entry = authIds.entrySet().stream().filter(it -> it.getValue().login.equals(login)).findFirst().orElse(null);
        var authId = UUID.randomUUID().toString();
        if (entry != null) {
            authId =  entry.getKey();
        }
        authIds.put(authId, new TokenData(login, Instant.now()));
        var token = storage.findUniqueAsset(LoginToken.class, LoginTokenFields.token, authId, true);
        if (token != null) {
            token.setCreated(Instant.now());
        } else {
            token = new LoginToken();
            token.setCreated(Instant.now());
            token.setToken(authId);
            token.setLogin(login);
        }
        storage.saveAsset(token, false, "login");
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
                var tokenData = authIds.get(cookieId.getValue());
                if (tokenData != null) {
                    AuthContext.setCurrentUser(tokenData.login);
                    authIds.put(cookieId.getValue(), new TokenData(tokenData.login, Instant.now()));
                    chain.doFilter(request, res);
                    return;
                }
                var storageToken = storage.findUniqueAsset(LoginToken.class, LoginTokenFields.token, cookieId.getValue(), false);
                if(storageToken != null){
                    AuthContext.setCurrentUser(storageToken.getLogin());
                    authIds.put(storageToken.getToken(), new TokenData(storageToken.getLogin(), Instant.now()));
                    chain.doFilter(request, res);
                    return;
                }
            }
            var authToken = HttpUtils.getQueryParameter(request, "token");
            if (authToken != null) {
                String login = getLoginByToken(authToken);
                var entry = authIds.entrySet().stream().filter(it -> it.getValue().login.equals(login)).findFirst().orElse(null);
                var authId = UUID.randomUUID().toString();
                if (entry != null) {
                    authId =  entry.getKey();
                }
                authIds.put(authId, new TokenData(login, Instant.now()));
                var storageToken = storage.findUniqueAsset(LoginToken.class, LoginTokenFields.token, authId, true);
                if(storageToken != null){
                   storageToken.setCreated(Instant.now());
                } else {
                    storageToken = new LoginToken();
                    storageToken.setCreated(Instant.now());
                    storageToken.setToken(authId);
                    storageToken.setLogin(login);
                }
                storage.saveAsset(storageToken, false,"login");
                AuthContext.setCurrentUser(login);
                var cookie = new Cookie(AUTH_COOKIE_ID, authId);
                cookie.setPath("/");
                res.addCookie(cookie);
                chain.doFilter(request, res);
                return;
            }
            if(TextUtils.isNotBlank(developmentUserName)){
                AuthContext.setCurrentUser(developmentUserName);
                try{
                    chain.doFilter(request,res);
                } finally {
                    AuthContext.resetCurrentUser();
                }
                return;
            }
            res.setStatus(401);
            res.sendRedirect("/login?redirectUrl=/");
        } finally {
            AuthContext.resetCurrentUser();
        }
    }

    protected String getLoginByToken(String authToken) {
        throw Xeption.forDeveloper("not implemented yet");
    }

    protected String getRedirectUrl(){
        return "/login";
    }

    record TokenData(String login, Instant created){}
}
