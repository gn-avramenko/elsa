/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.core.search.ArgumentType;
import com.gridnine.elsa.core.search.ComparisonSupport;
import com.gridnine.elsa.core.search.FieldNameSupport;
import com.gridnine.elsa.core.search.SortSupport;
import com.gridnine.elsa.core.search.StringOperationsSupport;
import java.time.LocalDateTime;

public class TestDomainAssetFields{

	public final static _stringPropertyField stringProperty = new _stringPropertyField();

	public final static _dateTimePropertyField dateTimeProperty = new _dateTimePropertyField();

	private static class _stringPropertyField extends FieldNameSupport implements StringOperationsSupport, SortSupport, ComparisonSupport, ArgumentType<String>{
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