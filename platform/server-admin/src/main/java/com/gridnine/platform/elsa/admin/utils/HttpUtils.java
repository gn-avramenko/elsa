package com.gridnine.platform.elsa.admin.utils;

import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    public static String getQueryParameter(HttpServletRequest request, String name) {
        var queryString = request.getQueryString();
        if(TextUtils.isBlank(queryString)) {
            return null;
        }
        queryString = queryString.substring(queryString.indexOf("?")+1);
        var tokens = queryString.split("\\&");
        if(tokens.length > 0) {
            var value = Arrays.stream(tokens).filter(it -> it.startsWith("%s=".formatted(name))).findFirst().orElse(null);
            if(value != null) {
                return value.substring(name.length()+1);
            }
        }
        return null;
    }

    public static void disableSSLVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {;
            logger.error("unable to disable ssl verification", e);
        }
    }
}
