/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class RemotingEntityPropertyDescription extends BaseIntrospectableObject{

	private RemotingEntityValueType type;

	private String id;

	private String className;

	private boolean isAbstract;

	public RemotingEntityValueType getType(){
		return type;
	}

	public void setType(RemotingEntityValueType value){
		this.type = value;
	}

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public String getClassName(){
		return className;
	}

	public void setClassName(String value){
		this.className = value;
	}

	public boolean getIsAbstract(){
		return isAbstract;
	}

	public void setIsAbstract(boolean value){
		this.isAbstract = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("type".equals(propertyName)){
			return type;
		}

		if("id".equals(propertyName)){
			return id;
		}

		if("className".equals(propertyName)){
			return className;
		}

		if("isAbstract".equals(propertyName)){
			return isAbstract;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("type".equals(propertyName)){
			this.type = (RemotingEntityValueType) value;
			return;
		}

		if("id".equals(propertyName)){
			this.id = (String) value;
			return;
		}

		if("className".equals(propertyName)){
			this.className = (String) value;
			return;
		}

		if("isAbstract".equals(propertyName)){
			this.isAbstract = (boolean) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}