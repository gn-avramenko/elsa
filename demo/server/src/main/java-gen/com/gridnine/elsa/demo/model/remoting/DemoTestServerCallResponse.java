/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.remoting;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import java.time.LocalDate;
import java.util.*;

public class DemoTestServerCallResponse extends BaseIntrospectableObject{

	private String stringProperty;

	private LocalDate dateProperty;

	private DemoTestEnum enumProperty;

	private final List<String> stringCollection = new ArrayList<>();

	private final List<LocalDate> dateCollection = new ArrayList<>();

	private final List<DemoTestEntity> entityCollection = new ArrayList<>();

	private final Map<String,String> stringMap = new HashMap<>();

	private final Map<LocalDate,LocalDate> dateMap = new HashMap<>();

	private final Map<DemoTestEntity,DemoTestEntity> eneityMap = new HashMap<>();

	public String getStringProperty(){
		return stringProperty;
	}

	public void setStringProperty(String value){
		this.stringProperty = value;
	}

	public LocalDate getDateProperty(){
		return dateProperty;
	}

	public void setDateProperty(LocalDate value){
		this.dateProperty = value;
	}

	public DemoTestEnum getEnumProperty(){
		return enumProperty;
	}

	public void setEnumProperty(DemoTestEnum value){
		this.enumProperty = value;
	}

	public List<String> getStringCollection(){
		return stringCollection;
	}

	public List<LocalDate> getDateCollection(){
		return dateCollection;
	}

	public List<DemoTestEntity> getEntityCollection(){
		return entityCollection;
	}

	public Map<String,String> getStringMap(){
		return stringMap;
	}

	public Map<LocalDate,LocalDate> getDateMap(){
		return dateMap;
	}

	public Map<DemoTestEntity,DemoTestEntity> getEneityMap(){
		return eneityMap;
	}

	@Override
	public Object getValue(String propertyName){

		if("stringProperty".equals(propertyName)){
			return stringProperty;
		}

		if("dateProperty".equals(propertyName)){
			return dateProperty;
		}

		if("enumProperty".equals(propertyName)){
			return enumProperty;
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
			this.dateProperty = (LocalDate) value;
			return;
		}

		if("enumProperty".equals(propertyName)){
			this.enumProperty = (DemoTestEnum) value;
			return;
		}

		super.setValue(propertyName, value);
	}

	@Override
	public Collection<?> getCollection(String collectionName){

		if("stringCollection".equals(collectionName)){
			return stringCollection;
		}

		if("dateCollection".equals(collectionName)){
			return dateCollection;
		}

		if("entityCollection".equals(collectionName)){
			return entityCollection;
		}

		return super.getCollection(collectionName);
	}

	@Override
	public Map<?,?> getMap(String mapName){

		if("stringMap".equals(mapName)){
			return stringMap;
		}

		if("dateMap".equals(mapName)){
			return dateMap;
		}

		if("eneityMap".equals(mapName)){
			return eneityMap;
		}

		return super.getMap(mapName);
	}
}