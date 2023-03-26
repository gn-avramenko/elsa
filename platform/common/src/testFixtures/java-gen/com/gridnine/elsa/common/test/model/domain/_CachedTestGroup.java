/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.test.model.domain;

import com.gridnine.elsa.common.model.common.ReadOnlyArrayList;
import com.gridnine.elsa.common.model.common.Xeption;
import com.gridnine.elsa.common.model.domain.CachedObject;
import java.util.ArrayList;

public class _CachedTestGroup extends TestGroup implements CachedObject{

	private boolean allowChanges = false;

	private final ArrayList<TestItem> items = new ReadOnlyArrayList<>();

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
	public ArrayList<TestItem> getItems(){
		return this.items;
	}
	@Override
	public Object getValue(String propertyName){

		if("items".equals(propertyName)){
			return items;
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