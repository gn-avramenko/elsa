/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class RL10nMessageDescription extends BaseIntrospectableObject{

	private String id;

	private String displayName;

	private final ArrayList<RPropertyDescription> parameters = new ArrayList<>();

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setDisplayName(String value){
		this.displayName = value;
	}

	public ArrayList<RPropertyDescription> getParameters(){
		return parameters;
	}

	@Override
	public Object getValue(String propertyName){

		if("id".equals(propertyName)){
			return id;
		}

		if("displayName".equals(propertyName)){
			return displayName;
		}

		if("parameters".equals(propertyName)){
			return parameters;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("id".equals(propertyName)){
			this.id = (String) value;
			return;
		}

		if("displayName".equals(propertyName)){
			this.displayName = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}