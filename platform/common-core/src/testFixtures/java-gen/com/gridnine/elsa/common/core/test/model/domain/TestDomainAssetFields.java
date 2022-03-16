/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.search.ComparisonSupport;
import com.gridnine.elsa.common.core.search.EqualitySupport;
import com.gridnine.elsa.common.core.search.FieldNameSupport;
import com.gridnine.elsa.common.core.search.SortSupport;
import com.gridnine.elsa.common.core.search.StringOperationsSupport;

public class TestDomainAssetFields{

	public final static _stringPropertyField stringProperty = new _stringPropertyField("stringProperty");

	public final static _datePropertyField dateProperty = new _datePropertyField("dateProperty");

	private static class _stringPropertyField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport{
		_stringPropertyField(String name){
			super(name);
		}
	}

	private static class _datePropertyField extends FieldNameSupport implements ComparisonSupport, SortSupport{
		_datePropertyField(String name){
			super(name);
		}
	}
}