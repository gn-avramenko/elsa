/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.core.model.domain.EntityReference;
import com.gridnine.elsa.core.search.ArgumentType;
import com.gridnine.elsa.core.search.CollectionSupport;
import com.gridnine.elsa.core.search.ComparisonSupport;
import com.gridnine.elsa.core.search.FieldNameSupport;
import com.gridnine.elsa.core.search.SortSupport;
import com.gridnine.elsa.core.search.StringOperationsSupport;

public class TestDomainDocumentProjectionFields{

	public final static _stringPropertyField stringProperty = new _stringPropertyField();

	public final static _getAllPropertyField getAllProperty = new _getAllPropertyField();

	public final static _stringCollectionField stringCollection = new _stringCollectionField();

	public final static _enumPropertyField enumProperty = new _enumPropertyField();

	public final static _entityReferencePropertyField entityReferenceProperty = new _entityReferencePropertyField();

	public final static _enumCollectionField enumCollection = new _enumCollectionField();

	public final static _entityRefCollectionField entityRefCollection = new _entityRefCollectionField();

	private static class _stringPropertyField extends FieldNameSupport implements StringOperationsSupport, SortSupport, ComparisonSupport, ArgumentType<String>{
		_stringPropertyField(){
			super("stringProperty");
		}
	}

	private static class _getAllPropertyField extends FieldNameSupport implements StringOperationsSupport, SortSupport, ComparisonSupport, ArgumentType<String>{
		_getAllPropertyField(){
			super("getAllProperty");
		}
	}

	private static class _stringCollectionField extends FieldNameSupport implements CollectionSupport, ArgumentType<String>{
		_stringCollectionField(){
			super("stringCollection");
		}
	}

	private static class _enumPropertyField extends FieldNameSupport implements SortSupport, ArgumentType<TestEnum>{
		_enumPropertyField(){
			super("enumProperty");
		}
	}

	private static class _entityReferencePropertyField extends FieldNameSupport implements SortSupport{
		_entityReferencePropertyField(){
			super("entityReferenceProperty");
		}
	}

	private static class _enumCollectionField extends FieldNameSupport implements CollectionSupport, ArgumentType<TestEnum>{
		_enumCollectionField(){
			super("enumCollection");
		}
	}

	private static class _entityRefCollectionField extends FieldNameSupport implements CollectionSupport, ArgumentType<EntityReference<TestDomainDocument>>{
		_entityRefCollectionField(){
			super("entityRefCollection");
		}
	}
}