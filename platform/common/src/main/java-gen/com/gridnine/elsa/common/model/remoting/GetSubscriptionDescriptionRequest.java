/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class GetSubscriptionDescriptionRequest extends BaseIntrospectableObject{

	private String remotingId;

	private String groupId;

	private String subscriptionId;

	public String getRemotingId(){
		return remotingId;
	}

	public void setRemotingId(String value){
		this.remotingId = value;
	}

	public String getGroupId(){
		return groupId;
	}

	public void setGroupId(String value){
		this.groupId = value;
	}

	public String getSubscriptionId(){
		return subscriptionId;
	}

	public void setSubscriptionId(String value){
		this.subscriptionId = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("remotingId".equals(propertyName)){
			return remotingId;
		}

		if("groupId".equals(propertyName)){
			return groupId;
		}

		if("subscriptionId".equals(propertyName)){
			return subscriptionId;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("remotingId".equals(propertyName)){
			this.remotingId = (String) value;
			return;
		}

		if("groupId".equals(propertyName)){
			this.groupId = (String) value;
			return;
		}

		if("subscriptionId".equals(propertyName)){
			this.subscriptionId = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}