/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.restws;

import java.security.Principal;

public class StompPrincipal implements Principal {
    private final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
