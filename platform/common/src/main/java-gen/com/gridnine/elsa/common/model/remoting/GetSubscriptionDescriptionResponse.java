/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class GetSubscriptionDescriptionResponse extends BaseIntrospectableObject{

	private String parameterClassName;

	private String eventClassName;

	public String getParameterClassName(){
		return parameterClassName;
	}

	public void setParameterClassName(String value){
		this.parameterClassName = value;
	}

	public String getEventClassName(){
		return eventClassName;
	}

	public void setEventClassName(String value){
		this.eventClassName = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("parameterClassName".equals(propertyName)){
			return parameterClassName;
		}

		if("eventClassName".equals(propertyName)){
			return eventClassName;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("parameterClassName".equals(propertyName)){
			this.parameterClassName = (String) value;
			return;
		}

		if("eventClassName".equals(propertyName)){
			this.eventClassName = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}