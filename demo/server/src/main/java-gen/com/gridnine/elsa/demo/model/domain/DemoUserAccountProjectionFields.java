/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.domain;

import com.gridnine.elsa.common.core.search.ArgumentType;
import com.gridnine.elsa.common.core.search.EqualitySupport;
import com.gridnine.elsa.common.core.search.FieldNameSupport;
import com.gridnine.elsa.common.core.search.SortSupport;
import com.gridnine.elsa.common.core.search.StringOperationsSupport;

public class DemoUserAccountProjectionFields{

	public final static _nameField name = new _nameField();

	public final static _loginField login = new _loginField();

	private static class _nameField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport, ArgumentType<String>{
		_nameField(){
			super("name");
		}
	}

	private static class _loginField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport, ArgumentType<String>{
		_loginField(){
			super("login");
		}
	}
}