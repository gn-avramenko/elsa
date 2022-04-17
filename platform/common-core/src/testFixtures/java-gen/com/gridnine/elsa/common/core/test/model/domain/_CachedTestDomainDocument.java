/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.common.ReadOnlyArrayList;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.CachedObject;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import java.util.*;

public class _CachedTestDomainDocument extends TestDomainDocument implements CachedObject{

	private boolean allowChanges = false;

	private final List<String> stringCollection = new ReadOnlyArrayList<>();

	private final List<BaseTestDomainNestedDocument> entityCollection = new ReadOnlyArrayList<>();

	private final List<TestGroup> groups = new ReadOnlyArrayList<>();

	private final List<TestEnum> enumCollection = new ReadOnlyArrayList<>();

	private final List<EntityReference<TestDomainDocument>> entityRefCollection = new ReadOnlyArrayList<>();

	@Override
	public void setAllowChanges(boolean allowChanges){
		this.allowChanges = allowChanges;
	}

	@Override
	public void setId(long value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setId(value);
	}

	@Override
	public void setVersionInfo(VersionInfo value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setVersionInfo(value);
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
	public void setEntityProperty(BaseTestDomainNestedDocument value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEntityProperty(value);
	}

	@Override
	public void setEnumProperty(TestEnum value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEnumProperty(value);
	}

	@Override
	public void setEntityReference(EntityReference<TestDomainDocument> value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEntityReference(value);
	}

	@Override
	public List<String> getStringCollection(){
		return stringCollection;
	}

	@Override
	public List<BaseTestDomainNestedDocument> getEntityCollection(){
		return entityCollection;
	}

	@Override
	public List<TestGroup> getGroups(){
		return groups;
	}

	@Override
	public List<TestEnum> getEnumCollection(){
		return enumCollection;
	}

	@Override
	public List<EntityReference<TestDomainDocument>> getEntityRefCollection(){
		return entityRefCollection;
	}

	@Override
	public void setValue(String propertyName, Object value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setValue(propertyName, value);
	}

	@Override
	public Collection<?> getCollection(String collectionName){

		if("stringCollection".equals(collectionName)){
			return stringCollection;
		}

		if("entityCollection".equals(collectionName)){
			return entityCollection;
		}

		if("groups".equals(collectionName)){
			return groups;
		}

		if("enumCollection".equals(collectionName)){
			return enumCollection;
		}

		if("entityRefCollection".equals(collectionName)){
			return entityRefCollection;
		}

		return super.getCollection(collectionName);
	}
}