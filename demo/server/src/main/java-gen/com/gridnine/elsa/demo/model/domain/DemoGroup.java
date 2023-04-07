/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.domain;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class DemoGroup extends BaseIntrospectableObject{

	private String name;

	private final ArrayList<DemoItem> items = new ArrayList<>();

	public String getName(){
		return name;
	}

	public void setName(String value){
		this.name = value;
	}

	public ArrayList<DemoItem> getItems(){
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