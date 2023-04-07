/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.domain;

import com.gridnine.elsa.common.model.common.ReadOnlyArrayList;
import com.gridnine.elsa.common.model.common.Xeption;
import com.gridnine.elsa.common.model.domain.CachedObject;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.model.domain.VersionInfo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class _CachedDemoDomainDocument extends DemoDomainDocument implements CachedObject{

	private boolean allowChanges = false;

	private final ArrayList<String> stringCollection = new ReadOnlyArrayList<>();

	private final ArrayList<BaseDemoDomainNestedDocument> entityCollection = new ReadOnlyArrayList<>();

	private final ArrayList<DemoGroup> groups = new ReadOnlyArrayList<>();

	private final ArrayList<DemoEnum> enumCollection = new ReadOnlyArrayList<>();

	private final ArrayList<EntityReference<DemoDomainDocument>> entityRefCollection = new ReadOnlyArrayList<>();

	public void setAllowChanges(boolean allowChanges){
		this.allowChanges = allowChanges;
	}
	@Override
	public void setStringProperty(String value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setStringProperty(value);
	}
	@Override
	public void setGetAllProperty(String value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setGetAllProperty(value);
	}

	@Override
	public ArrayList<String> getStringCollection(){
		return this.stringCollection;
	}
	@Override
	public void setDateTimeProperty(LocalDateTime value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setDateTimeProperty(value);
	}
	@Override
	public void setIntProperty(int value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setIntProperty(value);
	}
	@Override
	public void setLongProperty(long value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setLongProperty(value);
	}
	@Override
	public void setBigDecimalProprerty(BigDecimal value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setBigDecimalProprerty(value);
	}
	@Override
	public void setBooleanProperty(boolean value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setBooleanProperty(value);
	}
	@Override
	public void setDateProperty(LocalDate value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setDateProperty(value);
	}
	@Override
	public void setEntityProperty(BaseDemoDomainNestedDocument value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEntityProperty(value);
	}
	@Override
	public void setEnumProperty(DemoEnum value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEnumProperty(value);
	}
	@Override
	public void setEntityReferenceProperty(EntityReference<DemoDomainDocument> value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEntityReferenceProperty(value);
	}

	@Override
	public ArrayList<BaseDemoDomainNestedDocument> getEntityCollection(){
		return this.entityCollection;
	}

	@Override
	public ArrayList<DemoGroup> getGroups(){
		return this.groups;
	}

	@Override
	public ArrayList<DemoEnum> getEnumCollection(){
		return this.enumCollection;
	}

	@Override
	public ArrayList<EntityReference<DemoDomainDocument>> getEntityRefCollection(){
		return this.entityRefCollection;
	}
	@Override
	public void setVersionInfo(VersionInfo value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setVersionInfo(value);
	}
	@Override
	public void setId(long value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setId(value);
	}
	@Override
	public Object getValue(String propertyName){

		if("stringCollection".equals(propertyName)){
			return stringCollection;
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
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setValue(propertyName, value);
	}
}