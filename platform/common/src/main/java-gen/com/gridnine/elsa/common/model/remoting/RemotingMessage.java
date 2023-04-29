/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class RemotingMessage extends BaseIntrospectableObject{

	private RemotingMessageType type;

	private String callId;

	private String methodId;

	private String groupId;

	private String remotingId;

	private String data;

	public RemotingMessageType getType(){
		return type;
	}

	public void setType(RemotingMessageType value){
		this.type = value;
	}

	public String getCallId(){
		return callId;
	}

	public void setCallId(String value){
		this.callId = value;
	}

	public String getMethodId(){
		return methodId;
	}

	public void setMethodId(String value){
		this.methodId = value;
	}

	public String getGroupId(){
		return groupId;
	}

	public void setGroupId(String value){
		this.groupId = value;
	}

	public String getRemotingId(){
		return remotingId;
	}

	public void setRemotingId(String value){
		this.remotingId = value;
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

		if("callId".equals(propertyName)){
			return callId;
		}

		if("methodId".equals(propertyName)){
			return methodId;
		}

		if("groupId".equals(propertyName)){
			return groupId;
		}

		if("remotingId".equals(propertyName)){
			return remotingId;
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

		if("callId".equals(propertyName)){
			this.callId = (String) value;
			return;
		}

		if("methodId".equals(propertyName)){
			this.methodId = (String) value;
			return;
		}

		if("groupId".equals(propertyName)){
			this.groupId = (String) value;
			return;
		}

		if("remotingId".equals(propertyName)){
			this.remotingId = (String) value;
			return;
		}

		if("data".equals(propertyName)){
			this.data = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}