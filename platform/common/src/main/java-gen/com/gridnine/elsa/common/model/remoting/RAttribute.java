/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class RAttribute extends BaseIntrospectableObject{

	private String name;

	private String value;

	public String getName(){
		return name;
	}

	public void setName(String value){
		this.name = value;
	}

	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("name".equals(propertyName)){
			return name;
		}

		if("value".equals(propertyName)){
			return value;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("name".equals(propertyName)){
			this.name = (String) value;
			return;
		}

		if("value".equals(propertyName)){
			this.value = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}