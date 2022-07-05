/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class ClientResponse extends BaseIntrospectableObject{

	private String errorMessage;

	private String requestId;

	private String responseStr;

	public String getErrorMessage(){
		return errorMessage;
	}

	public void setErrorMessage(String value){
		this.errorMessage = value;
	}

	public String getRequestId(){
		return requestId;
	}

	public void setRequestId(String value){
		this.requestId = value;
	}

	public String getResponseStr(){
		return responseStr;
	}

	public void setResponseStr(String value){
		this.responseStr = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("errorMessage".equals(propertyName)){
			return errorMessage;
		}

		if("requestId".equals(propertyName)){
			return requestId;
		}

		if("responseStr".equals(propertyName)){
			return responseStr;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("errorMessage".equals(propertyName)){
			this.errorMessage = (String) value;
			return;
		}

		if("requestId".equals(propertyName)){
			this.requestId = (String) value;
			return;
		}

		if("responseStr".equals(propertyName)){
			this.responseStr = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}