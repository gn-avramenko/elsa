/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class RemotingEntityMapDescription extends BaseIntrospectableObject{

	private String id;

	private RemotingEntityValueType keyType;

	private boolean keyIsAbsctract;

	private String keyClassName;

	private RemotingEntityValueType valueType;

	private String valueClassName;

	private boolean valueIsAbsctract;

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public RemotingEntityValueType getKeyType(){
		return keyType;
	}

	public void setKeyType(RemotingEntityValueType value){
		this.keyType = value;
	}

	public boolean getKeyIsAbsctract(){
		return keyIsAbsctract;
	}

	public void setKeyIsAbsctract(boolean value){
		this.keyIsAbsctract = value;
	}

	public String getKeyClassName(){
		return keyClassName;
	}

	public void setKeyClassName(String value){
		this.keyClassName = value;
	}

	public RemotingEntityValueType getValueType(){
		return valueType;
	}

	public void setValueType(RemotingEntityValueType value){
		this.valueType = value;
	}

	public String getValueClassName(){
		return valueClassName;
	}

	public void setValueClassName(String value){
		this.valueClassName = value;
	}

	public boolean getValueIsAbsctract(){
		return valueIsAbsctract;
	}

	public void setValueIsAbsctract(boolean value){
		this.valueIsAbsctract = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("id".equals(propertyName)){
			return id;
		}

		if("keyType".equals(propertyName)){
			return keyType;
		}

		if("keyIsAbsctract".equals(propertyName)){
			return keyIsAbsctract;
		}

		if("keyClassName".equals(propertyName)){
			return keyClassName;
		}

		if("valueType".equals(propertyName)){
			return valueType;
		}

		if("valueClassName".equals(propertyName)){
			return valueClassName;
		}

		if("valueIsAbsctract".equals(propertyName)){
			return valueIsAbsctract;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("id".equals(propertyName)){
			this.id = (String) value;
			return;
		}

		if("keyType".equals(propertyName)){
			this.keyType = (RemotingEntityValueType) value;
			return;
		}

		if("keyIsAbsctract".equals(propertyName)){
			this.keyIsAbsctract = (boolean) value;
			return;
		}

		if("keyClassName".equals(propertyName)){
			this.keyClassName = (String) value;
			return;
		}

		if("valueType".equals(propertyName)){
			this.valueType = (RemotingEntityValueType) value;
			return;
		}

		if("valueClassName".equals(propertyName)){
			this.valueClassName = (String) value;
			return;
		}

		if("valueIsAbsctract".equals(propertyName)){
			this.valueIsAbsctract = (boolean) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}