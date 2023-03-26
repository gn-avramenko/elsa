/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class RGenericDescription extends BaseIntrospectableObject{

	private String id;

	private String type;

	private String objectIdAttributeName;

	private final ArrayList<RGenericDescription> nestedGenerics = new ArrayList<>();

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public String getType(){
		return type;
	}

	public void setType(String value){
		this.type = value;
	}

	public String getObjectIdAttributeName(){
		return objectIdAttributeName;
	}

	public void setObjectIdAttributeName(String value){
		this.objectIdAttributeName = value;
	}

	public ArrayList<RGenericDescription> getNestedGenerics(){
		return nestedGenerics;
	}

	@Override
	public Object getValue(String propertyName){

		if("id".equals(propertyName)){
			return id;
		}

		if("type".equals(propertyName)){
			return type;
		}

		if("objectIdAttributeName".equals(propertyName)){
			return objectIdAttributeName;
		}

		if("nestedGenerics".equals(propertyName)){
			return nestedGenerics;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("id".equals(propertyName)){
			this.id = (String) value;
			return;
		}

		if("type".equals(propertyName)){
			this.type = (String) value;
			return;
		}

		if("objectIdAttributeName".equals(propertyName)){
			this.objectIdAttributeName = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}