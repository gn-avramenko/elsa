/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.test.serialization;

import com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainNestedDocumentImpl;
import com.gridnine.elsa.common.core.test.model.domain.TestEnum;
import com.gridnine.elsa.core.model.domain.EntityReference;
import com.gridnine.elsa.core.serialization.JsonMarshaller;
import com.gridnine.elsa.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.core.serialization.StandardSerializationParameters;
import com.gridnine.elsa.core.test.common.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DomainSerializationTest extends TestBase {

    @Test
    public void testDomainSerization(){
        var document = new TestDomainDocument();
        document.setEnumProperty(TestEnum.ITEM1);
        document.setStringProperty("string property");
        document.setId(1000);
        var entityRef = new EntityReference<TestDomainDocument>();
        entityRef.setType(TestDomainDocument.class);
        entityRef.setId(1001);
        entityRef.setCaption("entity reference caption");
        document.setEntityReferenceProperty(entityRef);
        document.getStringCollection().add("value");
        document.getEnumCollection().add(TestEnum.ITEM2);
        document.getEntityRefCollection().add(entityRef);
        var entity = new TestDomainNestedDocumentImpl();
        entity.setName("nested name");
        entity.setValue("nested value");
        document.setEntityProperty(entity);

        var entity2 = new TestDomainNestedDocumentImpl();
        entity2.setName("nested name 2");
        entity2.setValue("nested value 2");
        document.getEntityCollection().add(entity2);

        var params = new HashMap<String,Object>();
        StandardSerializationParameters.setPrettyPrint(true, params);
        StandardSerializationParameters.setEntityReferenceTypeSerializationStrategy(StandardSerializationParameters.EntityReferenceTypeSerializationStrategy.ALL_CLASS_NAME, params);
        StandardSerializationParameters.setEntityReferenceCaptionSerializationStrategy(StandardSerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL, params);
        StandardSerializationParameters.setEnumSerializationStrategy(StandardSerializationParameters.EnumSerializationStrategy.NAME, params);
        StandardSerializationParameters.setClassSerializationStrategy(StandardSerializationParameters.ClassSerializationStrategy.NAME, params);
        var content = JsonMarshaller.get().marshalToString(document, false, params);
        println(content);
        var document2 = JsonUnmarshaller.get().unmarshal(TestDomainDocument.class, new ByteArrayInputStream((content.getBytes(StandardCharsets.UTF_8))), params);
        Assertions.assertEquals(document.getStringProperty(), document2.getStringProperty());
        Assertions.assertEquals(document.getEnumProperty(), document2.getEnumProperty());
        Assertions.assertEquals(document.getEntityReferenceProperty(), document2.getEntityReferenceProperty());
        Assertions.assertEquals(document.getEntityReferenceProperty().getCaption(), document2.getEntityReferenceProperty().getCaption());
        Assertions.assertEquals(document.getEnumCollection(), document2.getEnumCollection());
        Assertions.assertEquals(document.getStringCollection(), document2.getStringCollection());
        Assertions.assertEquals(document.getEntityRefCollection(), document2.getEntityRefCollection());
        var ne1 = (TestDomainNestedDocumentImpl)document.getEntityCollection().get(0);
        var ne2 = (TestDomainNestedDocumentImpl)document2.getEntityCollection().get(0);
        Assertions.assertEquals(ne1.getName(), ne2.getName());
        Assertions.assertEquals(ne1.getValue(), ne2.getValue());
    }
}
