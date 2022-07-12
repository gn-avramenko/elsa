/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.server.ui;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;

public class TestGridEditorVV extends BaseIntrospectableObject{

	private String textField1;

	private String textField2;

	private String numberField1;

	private String textField3;

	public String getTextField1(){
		return textField1;
	}

	public void setTextField1(String value){
		this.textField1 = value;
	}

	public String getTextField2(){
		return textField2;
	}

	public void setTextField2(String value){
		this.textField2 = value;
	}

	public String getNumberField1(){
		return numberField1;
	}

	public void setNumberField1(String value){
		this.numberField1 = value;
	}

	public String getTextField3(){
		return textField3;
	}

	public void setTextField3(String value){
		this.textField3 = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("textField1".equals(propertyName)){
			return textField1;
		}

		if("textField2".equals(propertyName)){
			return textField2;
		}

		if("numberField1".equals(propertyName)){
			return numberField1;
		}

		if("textField3".equals(propertyName)){
			return textField3;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("textField1".equals(propertyName)){
			this.textField1 = (String) value;
			return;
		}

		if("textField2".equals(propertyName)){
			this.textField2 = (String) value;
			return;
		}

		if("numberField1".equals(propertyName)){
			this.numberField1 = (String) value;
			return;
		}

		if("textField3".equals(propertyName)){
			this.textField3 = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}