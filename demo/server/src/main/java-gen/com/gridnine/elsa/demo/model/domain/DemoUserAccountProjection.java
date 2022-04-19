/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.domain;

import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;

public class DemoUserAccountProjection extends BaseSearchableProjection<DemoUserAccount>{

	private String name;

	private String login;

	public String getName(){
		return name;
	}

	public void setName(String value){
		this.name = value;
	}

	public String getLogin(){
		return login;
	}

	public void setLogin(String value){
		this.login = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("name".equals(propertyName)){
			return name;
		}

		if("login".equals(propertyName)){
			return login;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("name".equals(propertyName)){
			this.name = (String) value;
			return;
		}

		if("login".equals(propertyName)){
			this.login = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}