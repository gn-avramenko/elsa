/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

/* eslint-disable max-classes-per-file,no-unused-vars,max-len,lines-between-class-members  */
import {
  GridLayoutContainer,
} from '../src/feature/widgets';
import {
  TestGridEditor,
  TestGridEditorVM,
  TestGridEditorVC,
  TestGridEditorVV,
} from './demo-ui';

export type TestUiEnum=
'ENUM1'
| 'ENUM2';

export type TestGridEditor2VM={
  embeddedEditor?: TestGridEditorVM,
};

export type TestGridEditor2VC={
  embeddedEditor?: TestGridEditorVC,
};

export type TestGridEditor2VV={
  embeddedEditor?: TestGridEditorVV,
};

export class TestGridEditor2 extends GridLayoutContainer<TestGridEditor2VM, TestGridEditor2VC, TestGridEditor2VV> {
  // @ts-ignore
  embeddedEditor: TestGridEditor;
}
