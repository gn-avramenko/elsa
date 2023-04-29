/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocument;

public class DemoDocumentChangedEvent extends BaseIntrospectableObject{

	private EntityReference<DemoDomainDocument> document;

	public EntityReference<DemoDomainDocument> getDocument(){
		return document;
	}

	public void setDocument(EntityReference<DemoDomainDocument> value){
		this.document = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("document".equals(propertyName)){
			return document;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("document".equals(propertyName)){
			this.document = (EntityReference<DemoDomainDocument>) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}