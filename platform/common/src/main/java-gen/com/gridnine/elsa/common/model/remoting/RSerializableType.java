/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class RSerializableType extends BaseIntrospectableObject{

	private String id;

	private final ArrayList<RGenericDeclaration> generics = new ArrayList<>();

	public String getId(){
		return id;
	}

	public void setId(String value){
		this.id = value;
	}

	public ArrayList<RGenericDeclaration> getGenerics(){
		return generics;
	}

	@Override
	public Object getValue(String propertyName){

		if("id".equals(propertyName)){
			return id;
		}

		if("generics".equals(propertyName)){
			return generics;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("id".equals(propertyName)){
			this.id = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}