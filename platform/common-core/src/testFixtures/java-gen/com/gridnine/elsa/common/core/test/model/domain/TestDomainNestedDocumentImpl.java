/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

public class TestDomainNestedDocumentImpl extends BaseTestDomainNestedDocument{

	private String value;

	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("value".equals(propertyName)){
			return value;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("value".equals(propertyName)){
			this.value = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}