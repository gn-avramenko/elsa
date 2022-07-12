/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.server.ui;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class TestGridEditor2VC extends BaseIntrospectableObject{

	private TestGridEditorVC embeddedEditor;

	public TestGridEditorVC getEmbeddedEditor(){
		return embeddedEditor;
	}

	public void setEmbeddedEditor(TestGridEditorVC value){
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
			this.embeddedEditor = (TestGridEditorVC) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}