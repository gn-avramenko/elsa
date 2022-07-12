/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.server.ui;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class TestGridEditor2VM extends BaseIntrospectableObject{

	private TestGridEditorVM embeddedEditor;

	public TestGridEditorVM getEmbeddedEditor(){
		return embeddedEditor;
	}

	public void setEmbeddedEditor(TestGridEditorVM value){
		this.embeddedEditor = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("embeddedEditor".equals(propertyName)){
			return embeddedEditor;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("embeddedEditor".equals(propertyName)){
			this.embeddedEditor = (TestGridEditorVM) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}