/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class RTagDescription extends BaseIntrospectableObject{

	private String tagName;

	private String type;

	private String objectIdAttributeName;

	private final ArrayList<RGenericDescription> generics = new ArrayList<>();

	public String getTagName(){
		return tagName;
	}

	public void setTagName(String value){
		this.tagName = value;
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

	public ArrayList<RGenericDescription> getGenerics(){
		return generics;
	}

	@Override
	public Object getValue(String propertyName){

		if("tagName".equals(propertyName)){
			return tagName;
		}

		if("type".equals(propertyName)){
			return type;
		}

		if("objectIdAttributeName".equals(propertyName)){
			return objectIdAttributeName;
		}

		if("generics".equals(propertyName)){
			return generics;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("tagName".equals(propertyName)){
			this.tagName = (String) value;
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