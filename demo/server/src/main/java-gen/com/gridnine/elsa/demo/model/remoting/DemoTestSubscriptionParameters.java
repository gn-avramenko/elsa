/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.remoting;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class DemoTestSubscriptionParameters extends BaseIntrospectableObject{

	private String param;

	public String getParam(){
		return param;
	}

	public void setParam(String value){
		this.param = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("param".equals(propertyName)){
			return param;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("param".equals(propertyName)){
			this.param = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}