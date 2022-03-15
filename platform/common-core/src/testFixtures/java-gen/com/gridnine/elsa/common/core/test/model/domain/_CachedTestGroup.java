/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.common.ReadOnlyArrayList;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.CachedObject;
import java.util.*;

public class _CachedTestGroup extends TestGroup implements CachedObject{

	private boolean allowChanges = false;

	private final List<TestItem> items = new ReadOnlyArrayList<>();

	@Override
	public void setAllowChanges(boolean allowChanges){
		this.allowChanges = allowChanges;
	}

	@Override
	public void setName(String value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setName(value);
	}

	@Override
	public List<TestItem> getItems(){
		return items;
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

		if("items".equals(collectionName)){
			return items;
		}

		return super.getCollection(collectionName);
	}
}