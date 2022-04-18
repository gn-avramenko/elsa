/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.search.ArgumentType;
import com.gridnine.elsa.common.core.search.CollectionSupport;
import com.gridnine.elsa.common.core.search.EqualitySupport;
import com.gridnine.elsa.common.core.search.FieldNameSupport;
import com.gridnine.elsa.common.core.search.SortSupport;
import com.gridnine.elsa.common.core.search.StringOperationsSupport;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.core.test.model.domain.TestEnum;

public class TestDomainDocumentProjectionFields{

	public final static _stringPropertyField stringProperty = new _stringPropertyField();

	public final static _getAllPropertyField getAllProperty = new _getAllPropertyField();

	public final static _enumPropertyField enumProperty = new _enumPropertyField();

	public final static _entityReferenceField entityReference = new _entityReferenceField();

	public final static _stringCollectionField stringCollection = new _stringCollectionField();

	public final static _enumCollectionField enumCollection = new _enumCollectionField();

	public final static _entityRefCollectionField entityRefCollection = new _entityRefCollectionField();

	private static class _stringPropertyField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport, ArgumentType<String>{
		_stringPropertyField(){
			super("stringProperty");
		}
	}

	private static class _getAllPropertyField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport, ArgumentType<String>{
		_getAllPropertyField(){
			super("getAllProperty");
		}
	}

	private static class _enumPropertyField extends FieldNameSupport implements EqualitySupport, SortSupport, ArgumentType<TestEnum>{
		_enumPropertyField(){
			super("enumProperty");
		}
	}

	private static class _entityReferenceField extends FieldNameSupport implements SortSupport, EqualitySupport, ArgumentType<EntityReference<TestDomainDocument>>{
		_entityReferenceField(){
			super("entityReference");
		}
	}

	private static class _stringCollectionField extends FieldNameSupport implements CollectionSupport, ArgumentType<String>{
		_stringCollectionField(){
			super("stringCollection");
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