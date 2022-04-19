/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.domain;

import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.CachedObject;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;

public class _CachedDemoUserAccount extends DemoUserAccount implements CachedObject{

	private boolean allowChanges = false;

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
	public void setName(String value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setName(value);
	}

	@Override
	public void setLogin(String value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setLogin(value);
	}

	@Override
	public void setValue(String propertyName, Object value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setValue(propertyName, value);
	}
}