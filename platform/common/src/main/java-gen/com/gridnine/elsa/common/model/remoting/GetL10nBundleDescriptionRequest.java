/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class GetL10nBundleDescriptionRequest extends BaseIntrospectableObject{

	private String bundleId;

	public String getBundleId(){
		return bundleId;
	}

	public void setBundleId(String value){
		this.bundleId = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("bundleId".equals(propertyName)){
			return bundleId;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("bundleId".equals(propertyName)){
			this.bundleId = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}