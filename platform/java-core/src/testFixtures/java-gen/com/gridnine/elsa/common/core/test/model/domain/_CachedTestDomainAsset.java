/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.core.model.common.Xeption;
import com.gridnine.elsa.core.model.domain.CachedObject;
import com.gridnine.elsa.core.model.domain.VersionInfo;
import java.time.LocalDateTime;

public class _CachedTestDomainAsset extends TestDomainAsset implements CachedObject{

	private boolean allowChanges = false;

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
	public void setDateTimeProperty(LocalDateTime value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setDateTimeProperty(value);
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
	public void setValue(String propertyName, Object value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setValue(propertyName, value);
	}
}