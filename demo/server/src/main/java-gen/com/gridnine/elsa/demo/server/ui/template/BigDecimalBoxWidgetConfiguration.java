/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.server.ui.template;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class BigDecimalBoxWidgetConfiguration extends BaseIntrospectableObject{

	private Boolean notEditable;

	public Boolean getNotEditable(){
		return notEditable;
	}

	public void setNotEditable(Boolean value){
		this.notEditable = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("notEditable".equals(propertyName)){
			return notEditable;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("notEditable".equals(propertyName)){
			this.notEditable = (Boolean) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}