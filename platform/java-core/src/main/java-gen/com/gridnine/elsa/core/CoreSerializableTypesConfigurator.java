/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.core;

import com.gridnine.elsa.core.config.Environment;
import com.gridnine.elsa.meta.serialization.GenericDeclaration;
import com.gridnine.elsa.meta.serialization.SerializableType;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import com.gridnine.elsa.meta.serialization.SingleGenericDeclaration;
import java.util.ArrayList;

public class CoreSerializableTypesConfigurator{

	public static final String STRING = "STRING";

	public static final String LONG = "LONG";

	public static final String INT = "INT";

	public static final String LOCAL_DATE_TIME = "LOCAL-DATE-TIME";

	public static final String LOCAL_DATE = "LOCAL-DATE";

	public static final String BOOLEAN = "BOOLEAN";

	public static final String BYTE_ARRAY = "BYTE-ARRAY";

	public static final String BIG_DECIMAL = "BIG-DECIMAL";

	public static final String ENTITY_REFERENCE = "ENTITY-REFERENCE";

	public static final String ARRAY_LIST = "ARRAY-LIST";

	public static final String LINKED_HASH_MAP = "LINKED-HASH-MAP";

	public void configure(){
		var registry = Environment.getPublished(SerializableTypesRegistry.class);

		{
			var type = new SerializableType();
			var id = "STRING";
			type.setId(id);
			type.setJavaQualifiedName("String");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "LONG";
			type.setId(id);
			type.setJavaQualifiedName("long");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "INT";
			type.setId(id);
			type.setJavaQualifiedName("int");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "LOCAL-DATE-TIME";
			type.setId(id);
			type.setJavaQualifiedName("java.time.LocalDateTime");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "LOCAL-DATE";
			type.setId(id);
			type.setJavaQualifiedName("java.time.LocalDate");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "BOOLEAN";
			type.setId(id);
			type.setJavaQualifiedName("boolean");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "BYTE-ARRAY";
			type.setId(id);
			type.setJavaQualifiedName("byte[]");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "BIG-DECIMAL";
			type.setId(id);
			type.setJavaQualifiedName("java.math.BigDecimal");
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "ENTITY-REFERENCE";
			type.setId(id);
			type.setJavaQualifiedName("com.gridnine.elsa.core.domain.EntityReference");
			var genererics_0 = new ArrayList<GenericDeclaration>();
			{
				var generic = new SingleGenericDeclaration();
				generic.setId("class-name");
				genererics_0.add(generic);
			}
			type.getGenerics().addAll(genererics_0);
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "ARRAY-LIST";
			type.setId(id);
			type.setJavaQualifiedName("java.util.ArrayList");
			var genererics_0 = new ArrayList<GenericDeclaration>();
			{
				var generic = new SingleGenericDeclaration();
				generic.setId("element-class-name");
				genererics_0.add(generic);
			}
			type.getGenerics().addAll(genererics_0);
			registry.getTypes().put(id, type);
		}

		{
			var type = new SerializableType();
			var id = "LINKED-HASH-MAP";
			type.setId(id);
			type.setJavaQualifiedName("java.util.LinkedHashMap");
			var genererics_0 = new ArrayList<GenericDeclaration>();
			{
				var generic = new SingleGenericDeclaration();
				generic.setId("key-class-name");
				genererics_0.add(generic);
			}
			{
				var generic = new SingleGenericDeclaration();
				generic.setId("value-class-name");
				genererics_0.add(generic);
			}
			type.getGenerics().addAll(genererics_0);
			registry.getTypes().put(id, type);
		}
	}
}