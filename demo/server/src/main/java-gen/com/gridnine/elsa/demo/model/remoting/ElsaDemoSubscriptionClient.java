/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.remoting;

import com.gridnine.elsa.server.remoting.RemotingChannels;

public class ElsaDemoSubscriptionClient{


	public void elsa_demo_remoting_test_demo_document_changed_subscription_send_event(DemoDocumentChangedEvent event){
		RemotingChannels.get().sendSubscriptionEvent("elsa-demo-remoting","test","demo-document-changed-subscription", event);
	}
}