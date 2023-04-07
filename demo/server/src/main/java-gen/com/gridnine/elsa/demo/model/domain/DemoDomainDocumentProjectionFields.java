/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.domain;

import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.search.ArgumentType;
import com.gridnine.elsa.common.search.CollectionSupport;
import com.gridnine.elsa.common.search.ComparisonSupport;
import com.gridnine.elsa.common.search.EqualitySupport;
import com.gridnine.elsa.common.search.FieldNameSupport;
import com.gridnine.elsa.common.search.SortSupport;
import com.gridnine.elsa.common.search.StringOperationsSupport;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DemoDomainDocumentProjectionFields{

	public final static _stringPropertyField stringProperty = new _stringPropertyField();

	public final static _getAllPropertyField getAllProperty = new _getAllPropertyField();

	public final static _stringCollectionField stringCollection = new _stringCollectionField();

	public final static _dateTimePropertyField dateTimeProperty = new _dateTimePropertyField();

	public final static _longPropertyField longProperty = new _longPropertyField();

	public final static _bigDecimalProprertyField bigDecimalProprerty = new _bigDecimalProprertyField();

	public final static _booleanPropertyField booleanProperty = new _booleanPropertyField();

	public final static _datePropertyField dateProperty = new _datePropertyField();

	public final static _enumPropertyField enumProperty = new _enumPropertyField();

	public final static _entityReferencePropertyField entityReferenceProperty = new _entityReferencePropertyField();

	public final static _enumCollectionField enumCollection = new _enumCollectionField();

	public final static _entityRefCollectionField entityRefCollection = new _entityRefCollectionField();

	private static class _stringPropertyField extends FieldNameSupport implements StringOperationsSupport, SortSupport, EqualitySupport, ComparisonSupport, ArgumentType<String>{
		_stringPropertyField(){
			super("stringProperty");
		}
	}

	private static class _getAllPropertyField extends FieldNameSupport implements StringOperationsSupport, SortSupport, EqualitySupport, ComparisonSupport, ArgumentType<String>{
		_getAllPropertyField(){
			super("getAllProperty");
		}
	}

	private static class _stringCollectionField extends FieldNameSupport implements CollectionSupport, ArgumentType<String>{
		_stringCollectionField(){
			super("stringCollection");
		}
	}

	private static class _dateTimePropertyField extends FieldNameSupport implements SortSupport, ComparisonSupport, ArgumentType<LocalDateTime>{
		_dateTimePropertyField(){
			super("dateTimeProperty");
		}
	}

	private static class _longPropertyField extends FieldNameSupport implements SortSupport, EqualitySupport, ComparisonSupport, ArgumentType<Long>{
		_longPropertyField(){
			super("longProperty");
		}
	}

	private static class _bigDecimalProprertyField extends FieldNameSupport implements SortSupport, EqualitySupport, ComparisonSupport, ArgumentType<BigDecimal>{
		_bigDecimalProprertyField(){
			super("bigDecimalProprerty");
		}
	}

	private static class _booleanPropertyField extends FieldNameSupport implements SortSupport, EqualitySupport, ArgumentType<Boolean>{
		_booleanPropertyField(){
			super("booleanProperty");
		}
	}

	private static class _datePropertyField extends FieldNameSupport implements SortSupport, EqualitySupport, ComparisonSupport, ArgumentType<LocalDateTime>{
		_datePropertyField(){
			super("dateProperty");
		}
	}

	private static class _enumPropertyField extends FieldNameSupport implements SortSupport, EqualitySupport, ArgumentType<DemoEnum>{
		_enumPropertyField(){
			super("enumProperty");
		}
	}

	private static class _entityReferencePropertyField extends FieldNameSupport implements SortSupport, EqualitySupport, ArgumentType<EntityReference<DemoDomainDocument>>{
		_entityReferencePropertyField(){
			super("entityReferenceProperty");
		}
	}

	private static class _enumCollectionField extends FieldNameSupport implements CollectionSupport, ArgumentType<DemoEnum>{
		_enumCollectionField(){
			super("enumCollection");
		}
	}

	private static class _entityRefCollectionField extends FieldNameSupport implements CollectionSupport, ArgumentType<EntityReference<DemoDomainDocument>>{
		_entityRefCollectionField(){
			super("entityRefCollection");
		}
	}
}