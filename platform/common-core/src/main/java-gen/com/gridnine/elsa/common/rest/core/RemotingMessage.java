/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class RemotingMessage extends BaseIntrospectableObject{

	private RemotingMessageType type;

	private String subscriptionId;

	private String callId;

	private String groupId;

	private String methodId;

	private String data;

	public RemotingMessageType getType(){
		return type;
	}

	public void setType(RemotingMessageType value){
		this.type = value;
	}

	public String getSubscriptionId(){
		return subscriptionId;
	}

	public void setSubscriptionId(String value){
		this.subscriptionId = value;
	}

	public String getCallId(){
		return callId;
	}

	public void setCallId(String value){
		this.callId = value;
	}

	public String getGroupId(){
		return groupId;
	}

	public void setGroupId(String value){
		this.groupId = value;
	}

	public String getMethodId(){
		return methodId;
	}

	public void setMethodId(String value){
		this.methodId = value;
	}

	public String getData(){
		return data;
	}

	public void setData(String value){
		this.data = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("type".equals(propertyName)){
			return type;
		}

		if("subscriptionId".equals(propertyName)){
			return subscriptionId;
		}

		if("callId".equals(propertyName)){
			return callId;
		}

		if("groupId".equals(propertyName)){
			return groupId;
		}

		if("methodId".equals(propertyName)){
			return methodId;
		}

		if("data".equals(propertyName)){
			return data;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("type".equals(propertyName)){
			this.type = (RemotingMessageType) value;
			return;
		}

		if("subscriptionId".equals(propertyName)){
			this.subscriptionId = (String) value;
			return;
		}

		if("callId".equals(propertyName)){
			this.callId = (String) value;
			return;
		}

		if("groupId".equals(propertyName)){
			this.groupId = (String) value;
			return;
		}

		if("methodId".equals(propertyName)){
			this.methodId = (String) value;
			return;
		}

		if("data".equals(propertyName)){
			this.data = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}