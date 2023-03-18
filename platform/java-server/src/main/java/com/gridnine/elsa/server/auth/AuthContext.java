/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.auth;

public class AuthContext {
    private static final ThreadLocal<String> users = new ThreadLocal<>();

    public static void setCurrentUser(String user) {
        users.set(user);
    }

    public static void resetCurrentUser() {
        users.remove();
    }

    public static String getCurrentUser() {
        return users.get();
    }
}
