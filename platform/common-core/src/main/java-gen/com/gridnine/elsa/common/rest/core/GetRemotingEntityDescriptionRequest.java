/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class GetRemotingEntityDescriptionRequest extends BaseIntrospectableObject{

	private String entityId;

	public String getEntityId(){
		return entityId;
	}

	public void setEntityId(String value){
		this.entityId = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("entityId".equals(propertyName)){
			return entityId;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("entityId".equals(propertyName)){
			this.entityId = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}