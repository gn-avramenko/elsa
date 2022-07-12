/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

/* eslint-disable max-classes-per-file,no-unused-vars,max-len,lines-between-class-members  */
import {
  TextBoxWidgetConfiguration,
  BigDecimalBoxWidgetConfiguration,
} from './demo-ui-template';
import {
  GridLayoutContainer,
  TextBoxWidget,
  BigDecimalBoxWidget,
} from '../src/feature/widgets';

export type TestUiEnum=
'ENUM1'
| 'ENUM2';

export type TestGridEditorVM={
  textField1?: string,
  textField2?: string,
  numberField1?: number,
  textField3?: string,
};

export type TestGridEditorVC={
  textField1?: TextBoxWidgetConfiguration,
  textField2?: TextBoxWidgetConfiguration,
  numberField1?: BigDecimalBoxWidgetConfiguration,
  textField3?: TextBoxWidgetConfiguration,
};

export type TestGridEditorVV={
  textField1?: string,
  textField2?: string,
  numberField1?: string,
  textField3?: string,
};

export class TestGridEditor extends GridLayoutContainer<TestGridEditorVM, TestGridEditorVC, TestGridEditorVV> {
  // @ts-ignore
  textField1: TextBoxWidget;
  // @ts-ignore
  textField2: TextBoxWidget;
  // @ts-ignore
  numberField1: BigDecimalBoxWidget;
  // @ts-ignore
  textField3: TextBoxWidget;
}
