/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

public interface RemotingSubscriptionHandler<P,E> {
    String getId();
    boolean isApplicable(E event, P parameters) throws Exception;
}
