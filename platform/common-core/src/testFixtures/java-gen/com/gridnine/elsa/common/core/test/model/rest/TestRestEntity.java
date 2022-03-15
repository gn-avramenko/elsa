/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.rest;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import java.util.*;

public class TestRestEntity extends BaseIntrospectableObject{

	private String stringField;

	private TestRestEnum enumField;

	private final Set<String> stringCollection = new HashSet<>();

	private final Map<String,String> stringMap = new HashMap<>();

	public String getStringField(){
		return stringField;
	}

	public void setStringField(String value){
		this.stringField = value;
	}

	public TestRestEnum getEnumField(){
		return enumField;
	}

	public void setEnumField(TestRestEnum value){
		this.enumField = value;
	}

	public Set<String> getStringCollection(){
		return stringCollection;
	}

	public Map<String,String> getStringMap(){
		return stringMap;
	}

	@Override
	public Object getValue(String propertyName){

		if("stringField".equals(propertyName)){
			return stringField;
		}

		if("enumField".equals(propertyName)){
			return enumField;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("stringField".equals(propertyName)){
			this.stringField = (String) value;
			return;
		}

		if("enumField".equals(propertyName)){
			this.enumField = (TestRestEnum) value;
			return;
		}

		super.setValue(propertyName, value);
	}

	@Override
	public Collection<?> getCollection(String collectionName){

		if("stringCollection".equals(collectionName)){
			return stringCollection;
		}

		return super.getCollection(collectionName);
	}

	@Override
	public Map<?,?> getMap(String mapName){

		if("stringMap".equals(mapName)){
			return stringMap;
		}

		return super.getMap(mapName);
	}
}