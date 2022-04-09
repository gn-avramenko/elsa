/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.auth;

public class AuthUtils {
    private static ThreadLocal<String> users = new ThreadLocal<>();
    public static void setCurrentUser(String user){
        users.set(user);
    }
    public static void resetCurrentUser() {
        users.remove();
    }
    public static String  getCurrentUser(){
        return users.get();
    }
}
