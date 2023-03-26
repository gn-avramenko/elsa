/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.test.model.domain;

import com.gridnine.elsa.common.search.ArgumentType;
import com.gridnine.elsa.common.search.ComparisonSupport;
import com.gridnine.elsa.common.search.EqualitySupport;
import com.gridnine.elsa.common.search.FieldNameSupport;
import com.gridnine.elsa.common.search.SortSupport;
import com.gridnine.elsa.common.search.StringOperationsSupport;
import java.time.LocalDateTime;

public class TestDomainAssetFields{

	public final static _stringPropertyField stringProperty = new _stringPropertyField();

	public final static _dateTimePropertyField dateTimeProperty = new _dateTimePropertyField();

	private static class _stringPropertyField extends FieldNameSupport implements StringOperationsSupport, SortSupport, EqualitySupport, ComparisonSupport, ArgumentType<String>{
		_stringPropertyField(){
			super("stringProperty");
		}
	}

	private static class _dateTimePropertyField extends FieldNameSupport implements SortSupport, ComparisonSupport, ArgumentType<LocalDateTime>{
		_dateTimePropertyField(){
			super("dateTimeProperty");
		}
	}
}