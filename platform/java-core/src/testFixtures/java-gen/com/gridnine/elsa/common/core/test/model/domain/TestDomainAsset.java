/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.core.model.domain.BaseAsset;
import java.time.LocalDateTime;

public class TestDomainAsset extends BaseAsset{

	private String stringProperty;

	private LocalDateTime dateTimeProperty;

	public String getStringProperty(){
		return stringProperty;
	}

	public void setStringProperty(String value){
		this.stringProperty = value;
	}

	public LocalDateTime getDateTimeProperty(){
		return dateTimeProperty;
	}

	public void setDateTimeProperty(LocalDateTime value){
		this.dateTimeProperty = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("stringProperty".equals(propertyName)){
			return stringProperty;
		}

		if("dateTimeProperty".equals(propertyName)){
			return dateTimeProperty;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("stringProperty".equals(propertyName)){
			this.stringProperty = (String) value;
			return;
		}

		if("dateTimeProperty".equals(propertyName)){
			this.dateTimeProperty = (LocalDateTime) value;
			return;
		}

		super.setValue(propertyName, value);
	}

	@Override
	public String toString(){
		return stringProperty;
	}
}