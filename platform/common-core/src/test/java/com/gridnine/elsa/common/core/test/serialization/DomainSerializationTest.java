/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.test.serialization;

import com.gridnine.elsa.common.core.common.TestBase;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.core.serialization.SerializationParameters;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainNestedDocumentImpl;
import com.gridnine.elsa.common.core.test.model.domain.TestEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
public class DomainSerializationTest extends TestBase {

    @Autowired
    private JsonMarshaller jsonMarshaller;

    @Autowired
    private JsonUnmarshaller jsonUnmarshaller;


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
        document.setEntityReference(entityRef);
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

        var params = new SerializationParameters()
                .setEntityReferenceTypeSerializationStrategy(SerializationParameters.EntityReferenceTypeSerializationStrategy.ALL_CLASS_NAME).setPrettyPrint(true)
                .setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL).
                setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.NAME).
                setClassSerializationStrategy(SerializationParameters.ClassSerializationStrategy.NAME);
        var baos = new ByteArrayOutputStream();
        jsonMarshaller.marshal(document, baos, false, params);
        println(baos.toString(StandardCharsets.UTF_8));
        var document2 =jsonUnmarshaller.unmarshal(TestDomainDocument.class, new ByteArrayInputStream((baos.toByteArray())), params);
        Assertions.assertEquals(document.getStringProperty(), document2.getStringProperty());
        Assertions.assertEquals(document.getEnumProperty(), document2.getEnumProperty());
        Assertions.assertEquals(document.getEntityReference(), document2.getEntityReference());
        Assertions.assertEquals(document.getEntityReference().getCaption(), document2.getEntityReference().getCaption());
        Assertions.assertEquals(document.getEnumCollection(), document2.getEnumCollection());
        Assertions.assertEquals(document.getStringCollection(), document2.getStringCollection());
        Assertions.assertEquals(document.getEntityRefCollection(), document2.getEntityRefCollection());
        var ne1 = (TestDomainNestedDocumentImpl)document.getEntityCollection().get(0);
        var ne2 = (TestDomainNestedDocumentImpl)document2.getEntityCollection().get(0);
        Assertions.assertEquals(ne1.getName(), ne2.getName());
        Assertions.assertEquals(ne1.getValue(), ne2.getValue());
    }
}
