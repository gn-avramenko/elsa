/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class RPropertyDescription extends BaseIntrospectableObject{

	private String id;

	private String tagName;

	private final ArrayList<RAttribute> attributes = new ArrayList<>();

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public String getTagName(){
		return tagName;
	}

	public void setTagName(String value){
		this.tagName = value;
	}

	public ArrayList<RAttribute> getAttributes(){
		return attributes;
	}

	@Override
	public Object getValue(String propertyName){

		if("id".equals(propertyName)){
			return id;
		}

		if("tagName".equals(propertyName)){
			return tagName;
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

		if("tagName".equals(propertyName)){
			this.tagName = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}