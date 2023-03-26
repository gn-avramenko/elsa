/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class GetServerCallDescriptionRequest extends BaseIntrospectableObject{

	private String remotingId;

	private String groupId;

	private String methodId;

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

	public String getMethodId(){
		return methodId;
	}

	public void setMethodId(String value){
		this.methodId = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("remotingId".equals(propertyName)){
			return remotingId;
		}

		if("groupId".equals(propertyName)){
			return groupId;
		}

		if("methodId".equals(propertyName)){
			return methodId;
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

		if("methodId".equals(propertyName)){
			this.methodId = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}