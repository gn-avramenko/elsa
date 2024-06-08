/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.platform.elsa.core.remoting;

import com.gridnine.platform.elsa.common.meta.remoting.RemotingDescription;
import com.gridnine.platform.elsa.common.meta.remoting.RemotingGroupDescription;
import com.gridnine.platform.elsa.common.meta.remoting.RemotingSubscriptionDescription;
import com.gridnine.platform.elsa.common.meta.remoting.ServiceDescription;

public class StandardRemotingParameters {

    public static final ContextParameterId<RemotingDescription> REMOTING_DESCRIPTION = () -> "REMOTING_DESCRIPTION";
    public static final ContextParameterId<ServiceDescription> SERVICE_DESCRIPTION = () -> "SERVICE_DESCRIPTION";

    public static final ContextParameterId<RemotingSubscriptionDescription> SUBSCRIPTION_DESCRIPTION = () -> "SUBSCRIPTION_DESCRIPTION";

    public static final ContextParameterId<RemotingGroupDescription> GROUP_DESCRIPTION = () -> "GROUP_DESCRIPTION";


}
