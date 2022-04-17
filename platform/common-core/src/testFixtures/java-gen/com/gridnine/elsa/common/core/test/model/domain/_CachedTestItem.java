/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.CachedObject;

public class _CachedTestItem extends TestItem implements CachedObject{

	private boolean allowChanges = false;

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
	public void setValue(String propertyName, Object value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setValue(propertyName, value);
	}
}