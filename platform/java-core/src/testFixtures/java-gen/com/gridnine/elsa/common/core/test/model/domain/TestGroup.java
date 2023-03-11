/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.core.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class TestGroup extends BaseIntrospectableObject{

	private String name;

	private final ArrayList<TestItem> items = new ArrayList<>();

	public String getName(){
		return name;
	}

	public void setName(String value){
		this.name = value;
	}

	public ArrayList<TestItem> getItems(){
		return items;
	}

	@Override
	public Object getValue(String propertyName){

		if("name".equals(propertyName)){
			return name;
		}

		if("items".equals(propertyName)){
			return items;
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