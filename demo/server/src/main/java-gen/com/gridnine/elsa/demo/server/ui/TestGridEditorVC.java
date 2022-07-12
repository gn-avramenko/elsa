/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.server.ui;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.demo.server.ui.template.BigDecimalBoxWidgetConfiguration;
import com.gridnine.elsa.demo.server.ui.template.TextBoxWidgetConfiguration;

public class TestGridEditorVC extends BaseIntrospectableObject{

	private TextBoxWidgetConfiguration textField1;

	private TextBoxWidgetConfiguration textField2;

	private BigDecimalBoxWidgetConfiguration numberField1;

	private TextBoxWidgetConfiguration textField3;

	public TextBoxWidgetConfiguration getTextField1(){
		return textField1;
	}

	public void setTextField1(TextBoxWidgetConfiguration value){
		this.textField1 = value;
	}

	public TextBoxWidgetConfiguration getTextField2(){
		return textField2;
	}

	public void setTextField2(TextBoxWidgetConfiguration value){
		this.textField2 = value;
	}

	public BigDecimalBoxWidgetConfiguration getNumberField1(){
		return numberField1;
	}

	public void setNumberField1(BigDecimalBoxWidgetConfiguration value){
		this.numberField1 = value;
	}

	public TextBoxWidgetConfiguration getTextField3(){
		return textField3;
	}

	public void setTextField3(TextBoxWidgetConfiguration value){
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
			this.textField1 = (TextBoxWidgetConfiguration) value;
			return;
		}

		if("textField2".equals(propertyName)){
			this.textField2 = (TextBoxWidgetConfiguration) value;
			return;
		}

		if("numberField1".equals(propertyName)){
			this.numberField1 = (BigDecimalBoxWidgetConfiguration) value;
			return;
		}

		if("textField3".equals(propertyName)){
			this.textField3 = (TextBoxWidgetConfiguration) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}