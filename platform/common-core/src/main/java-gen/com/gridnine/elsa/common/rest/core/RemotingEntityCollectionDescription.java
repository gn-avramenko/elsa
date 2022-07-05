/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class RemotingEntityCollectionDescription extends BaseIntrospectableObject{

	private RemotingEntityValueType elementType;

	private String id;

	private String elementClassName;

	private boolean elementIsAbstract;

	public RemotingEntityValueType getElementType(){
		return elementType;
	}

	public void setElementType(RemotingEntityValueType value){
		this.elementType = value;
	}

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public String getElementClassName(){
		return elementClassName;
	}

	public void setElementClassName(String value){
		this.elementClassName = value;
	}

	public boolean getElementIsAbstract(){
		return elementIsAbstract;
	}

	public void setElementIsAbstract(boolean value){
		this.elementIsAbstract = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("elementType".equals(propertyName)){
			return elementType;
		}

		if("id".equals(propertyName)){
			return id;
		}

		if("elementClassName".equals(propertyName)){
			return elementClassName;
		}

		if("elementIsAbstract".equals(propertyName)){
			return elementIsAbstract;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("elementType".equals(propertyName)){
			this.elementType = (RemotingEntityValueType) value;
			return;
		}

		if("id".equals(propertyName)){
			this.id = (String) value;
			return;
		}

		if("elementClassName".equals(propertyName)){
			this.elementClassName = (String) value;
			return;
		}

		if("elementIsAbstract".equals(propertyName)){
			this.elementIsAbstract = (boolean) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}