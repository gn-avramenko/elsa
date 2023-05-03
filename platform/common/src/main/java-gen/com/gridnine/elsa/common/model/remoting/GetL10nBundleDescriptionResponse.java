/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class GetL10nBundleDescriptionResponse extends BaseIntrospectableObject{

	private final ArrayList<RL10nMessageDescription> messages = new ArrayList<>();

	public ArrayList<RL10nMessageDescription> getMessages(){
		return messages;
	}

	@Override
	public Object getValue(String propertyName){

		if("messages".equals(propertyName)){
			return messages;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		super.setValue(propertyName, value);
	}
}