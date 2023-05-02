/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection;
import java.util.ArrayList;

public class GetIndexesResponse extends BaseIntrospectableObject{

	private final ArrayList<DemoDomainDocumentProjection> indexes = new ArrayList<>();

	public ArrayList<DemoDomainDocumentProjection> getIndexes(){
		return indexes;
	}

	@Override
	public Object getValue(String propertyName){

		if("indexes".equals(propertyName)){
			return indexes;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		super.setValue(propertyName, value);
	}
}