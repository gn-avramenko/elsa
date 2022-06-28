/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class GetRemotingEntityDescriptionResponse extends BaseIntrospectableObject{

	private RemotingEntityDescription description;

	public RemotingEntityDescription getDescription(){
		return description;
	}

	public void setDescription(RemotingEntityDescription value){
		this.description = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("description".equals(propertyName)){
			return description;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("description".equals(propertyName)){
			this.description = (RemotingEntityDescription) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}