/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.config;

public interface Activator {
    double getOrder();
    void configure() throws Exception;
    void activate() throws Exception;
}
