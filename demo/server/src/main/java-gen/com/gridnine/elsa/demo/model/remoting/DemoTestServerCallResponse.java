/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.remoting;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class DemoTestServerCallResponse extends BaseIntrospectableObject{

	private String stringProperty;

	public String getStringProperty(){
		return stringProperty;
	}

	public void setStringProperty(String value){
		this.stringProperty = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("stringProperty".equals(propertyName)){
			return stringProperty;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("stringProperty".equals(propertyName)){
			this.stringProperty = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}