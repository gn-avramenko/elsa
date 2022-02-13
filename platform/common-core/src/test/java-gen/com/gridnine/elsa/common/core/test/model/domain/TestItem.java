/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class TestItem extends BaseIntrospectableObject{

	private String name;

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