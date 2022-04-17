/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.search.CollectionSupport;
import com.gridnine.elsa.common.core.search.EqualitySupport;
import com.gridnine.elsa.common.core.search.FieldNameSupport;
import com.gridnine.elsa.common.core.search.SortSupport;
import com.gridnine.elsa.common.core.search.StringOperationsSupport;

public class TestDomainDocumentProjectionFields{

	public final static _stringPropertyField stringProperty = new _stringPropertyField("stringProperty");

	public final static _getAllPropertyField getAllProperty = new _getAllPropertyField("getAllProperty");

	public final static _enumPropertyField enumProperty = new _enumPropertyField("enumProperty");

	public final static _entityReferenceField entityReference = new _entityReferenceField("entityReference");

	public final static _stringCollectionField stringCollection = new _stringCollectionField("stringCollection");

	public final static _enumCollectionField enumCollection = new _enumCollectionField("enumCollection");

	public final static _entityRefCollectionField entityRefCollection = new _entityRefCollectionField("entityRefCollection");

	private static class _stringPropertyField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport{
		_stringPropertyField(String name){
			super(name);
		}
	}

	private static class _getAllPropertyField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport{
		_getAllPropertyField(String name){
			super(name);
		}
	}

	private static class _enumPropertyField extends FieldNameSupport implements EqualitySupport, SortSupport{
		_enumPropertyField(String name){
			super(name);
		}
	}

	private static class _entityReferenceField extends FieldNameSupport implements SortSupport, EqualitySupport{
		_entityReferenceField(String name){
			super(name);
		}
	}

	private static class _stringCollectionField extends FieldNameSupport implements CollectionSupport{
		_stringCollectionField(String name){
			super(name);
		}
	}

	private static class _enumCollectionField extends FieldNameSupport implements CollectionSupport{
		_enumCollectionField(String name){
			super(name);
		}
	}

	private static class _entityRefCollectionField extends FieldNameSupport implements CollectionSupport{
		_entityRefCollectionField(String name){
			super(name);
		}
	}
}