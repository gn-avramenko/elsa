/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import java.time.LocalDateTime;

public class TestDomainAsset extends BaseAsset{

	private String stringProperty;

	private LocalDateTime dateProperty;

	public String getStringProperty(){
		return stringProperty;
	}

	public void setStringProperty(String value){
		this.stringProperty = value;
	}

	public LocalDateTime getDateProperty(){
		return dateProperty;
	}

	public void setDateProperty(LocalDateTime value){
		this.dateProperty = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("stringProperty".equals(propertyName)){
			return stringProperty;
		}

		if("dateProperty".equals(propertyName)){
			return dateProperty;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("stringProperty".equals(propertyName)){
			this.stringProperty = (String) value;
			return;
		}

		if("dateProperty".equals(propertyName)){
			this.dateProperty = (LocalDateTime) value;
			return;
		}

		super.setValue(propertyName, value);
	}

	@Override
	public String toString(){
		return stringProperty;
	}
}