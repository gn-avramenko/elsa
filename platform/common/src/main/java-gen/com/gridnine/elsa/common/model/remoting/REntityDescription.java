/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class REntityDescription extends BaseIntrospectableObject{

	private String id;

	private final ArrayList<RPropertyDescription> properties = new ArrayList<>();

	private final ArrayList<RAttribute> attributes = new ArrayList<>();

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public ArrayList<RPropertyDescription> getProperties(){
		return properties;
	}

	public ArrayList<RAttribute> getAttributes(){
		return attributes;
	}

	@Override
	public Object getValue(String propertyName){

		if("id".equals(propertyName)){
			return id;
		}

		if("properties".equals(propertyName)){
			return properties;
		}

		if("attributes".equals(propertyName)){
			return attributes;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("id".equals(propertyName)){
			this.id = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}