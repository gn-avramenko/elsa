/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class GetRemotingEntityDescriptionResponse extends BaseIntrospectableObject{

	private REntityType type;

	private REntityDescription description;

	public REntityType getType(){
		return type;
	}

	public void setType(REntityType value){
		this.type = value;
	}

	public REntityDescription getDescription(){
		return description;
	}

	public void setDescription(REntityDescription value){
		this.description = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("type".equals(propertyName)){
			return type;
		}

		if("description".equals(propertyName)){
			return description;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("type".equals(propertyName)){
			this.type = (REntityType) value;
			return;
		}

		if("description".equals(propertyName)){
			this.description = (REntityDescription) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}