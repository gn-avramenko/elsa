/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.domain;

import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.EntityReference;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DemoDomainDocument extends BaseDocument{

	private String stringProperty;

	private String getAllProperty;

	private final ArrayList<String> stringCollection = new ArrayList<>();

	private LocalDateTime dateTimeProperty;

	private int intProperty;

	private long longProperty;

	private BigDecimal bigDecimalProprerty;

	private boolean booleanProperty;

	private LocalDate dateProperty;

	private BaseDemoDomainNestedDocument entityProperty;

	private DemoEnum enumProperty;

	private EntityReference<DemoDomainDocument> entityReferenceProperty;

	private final ArrayList<BaseDemoDomainNestedDocument> entityCollection = new ArrayList<>();

	private final ArrayList<DemoGroup> groups = new ArrayList<>();

	private final ArrayList<DemoEnum> enumCollection = new ArrayList<>();

	private final ArrayList<EntityReference<DemoDomainDocument>> entityRefCollection = new ArrayList<>();

	public String getStringProperty(){
		return stringProperty;
	}

	public void setStringProperty(String value){
		this.stringProperty = value;
	}

	public String getGetAllProperty(){
		return getAllProperty;
	}

	public void setGetAllProperty(String value){
		this.getAllProperty = value;
	}

	public ArrayList<String> getStringCollection(){
		return stringCollection;
	}

	public LocalDateTime getDateTimeProperty(){
		return dateTimeProperty;
	}

	public void setDateTimeProperty(LocalDateTime value){
		this.dateTimeProperty = value;
	}

	public int getIntProperty(){
		return intProperty;
	}

	public void setIntProperty(int value){
		this.intProperty = value;
	}

	public long getLongProperty(){
		return longProperty;
	}

	public void setLongProperty(long value){
		this.longProperty = value;
	}

	public BigDecimal getBigDecimalProprerty(){
		return bigDecimalProprerty;
	}

	public void setBigDecimalProprerty(BigDecimal value){
		this.bigDecimalProprerty = value;
	}

	public boolean getBooleanProperty(){
		return booleanProperty;
	}

	public void setBooleanProperty(boolean value){
		this.booleanProperty = value;
	}

	public LocalDate getDateProperty(){
		return dateProperty;
	}

	public void setDateProperty(LocalDate value){
		this.dateProperty = value;
	}

	public BaseDemoDomainNestedDocument getEntityProperty(){
		return entityProperty;
	}

	public void setEntityProperty(BaseDemoDomainNestedDocument value){
		this.entityProperty = value;
	}

	public DemoEnum getEnumProperty(){
		return enumProperty;
	}

	public void setEnumProperty(DemoEnum value){
		this.enumProperty = value;
	}

	public EntityReference<DemoDomainDocument> getEntityReferenceProperty(){
		return entityReferenceProperty;
	}

	public void setEntityReferenceProperty(EntityReference<DemoDomainDocument> value){
		this.entityReferenceProperty = value;
	}

	public ArrayList<BaseDemoDomainNestedDocument> getEntityCollection(){
		return entityCollection;
	}

	public ArrayList<DemoGroup> getGroups(){
		return groups;
	}

	public ArrayList<DemoEnum> getEnumCollection(){
		return enumCollection;
	}

	public ArrayList<EntityReference<DemoDomainDocument>> getEntityRefCollection(){
		return entityRefCollection;
	}

	@Override
	public Object getValue(String propertyName){

		if("stringProperty".equals(propertyName)){
			return stringProperty;
		}

		if("getAllProperty".equals(propertyName)){
			return getAllProperty;
		}

		if("stringCollection".equals(propertyName)){
			return stringCollection;
		}

		if("dateTimeProperty".equals(propertyName)){
			return dateTimeProperty;
		}

		if("intProperty".equals(propertyName)){
			return intProperty;
		}

		if("longProperty".equals(propertyName)){
			return longProperty;
		}

		if("bigDecimalProprerty".equals(propertyName)){
			return bigDecimalProprerty;
		}

		if("booleanProperty".equals(propertyName)){
			return booleanProperty;
		}

		if("dateProperty".equals(propertyName)){
			return dateProperty;
		}

		if("entityProperty".equals(propertyName)){
			return entityProperty;
		}

		if("enumProperty".equals(propertyName)){
			return enumProperty;
		}

		if("entityReferenceProperty".equals(propertyName)){
			return entityReferenceProperty;
		}

		if("entityCollection".equals(propertyName)){
			return entityCollection;
		}

		if("groups".equals(propertyName)){
			return groups;
		}

		if("enumCollection".equals(propertyName)){
			return enumCollection;
		}

		if("entityRefCollection".equals(propertyName)){
			return entityRefCollection;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("stringProperty".equals(propertyName)){
			this.stringProperty = (String) value;
			return;
		}

		if("getAllProperty".equals(propertyName)){
			this.getAllProperty = (String) value;
			return;
		}

		if("dateTimeProperty".equals(propertyName)){
			this.dateTimeProperty = (LocalDateTime) value;
			return;
		}

		if("intProperty".equals(propertyName)){
			this.intProperty = (int) value;
			return;
		}

		if("longProperty".equals(propertyName)){
			this.longProperty = (long) value;
			return;
		}

		if("bigDecimalProprerty".equals(propertyName)){
			this.bigDecimalProprerty = (BigDecimal) value;
			return;
		}

		if("booleanProperty".equals(propertyName)){
			this.booleanProperty = (boolean) value;
			return;
		}

		if("dateProperty".equals(propertyName)){
			this.dateProperty = (LocalDate) value;
			return;
		}

		if("entityProperty".equals(propertyName)){
			this.entityProperty = (BaseDemoDomainNestedDocument) value;
			return;
		}

		if("enumProperty".equals(propertyName)){
			this.enumProperty = (DemoEnum) value;
			return;
		}

		if("entityReferenceProperty".equals(propertyName)){
			this.entityReferenceProperty = (EntityReference<DemoDomainDocument>) value;
			return;
		}

		super.setValue(propertyName, value);
	}

	@Override
	public String toString(){
		return stringProperty;
	}
}