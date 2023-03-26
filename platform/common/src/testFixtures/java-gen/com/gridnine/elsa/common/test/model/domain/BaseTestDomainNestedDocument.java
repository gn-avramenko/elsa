/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.test.model.domain;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public abstract class BaseTestDomainNestedDocument extends BaseIntrospectableObject{

	private String name;

	public String getName(){
		return name;
	}

	public void setName(String value){
		this.name = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("name".equals(propertyName)){
			return name;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("name".equals(propertyName)){
			this.name = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}