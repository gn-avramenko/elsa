/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.test.serialization;

import com.gridnine.elsa.common.core.common.TestBase;
import com.gridnine.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.core.serialization.SerializationParameters;
import com.gridnine.elsa.common.core.test.model.rest.TestRestEntity;
import com.gridnine.elsa.common.core.test.model.rest.TestRestEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
public class RestSerializationTest extends TestBase {

    @Autowired
    private JsonMarshaller jsonMarshaller;

    @Autowired
    private JsonUnmarshaller jsonUnmarshaller;

    @Test
    public void testRestSerization(){
        var document = new TestRestEntity();
        document.setEnumField(TestRestEnum.ELEMENT1);
        document.setStringField("string value");
        document.getStringCollection().add("element 1");
        document.getStringCollection().add("element 2");
        document.getStringMap().put("mapKey1", "mapValue1");
        document.getStringMap().put("mapKey2", "mapValue2");
        var params = new SerializationParameters()
                .setEntityReferenceTypeSerializationStrategy(SerializationParameters.EntityReferenceTypeSerializationStrategy.ALL_CLASS_NAME).setPrettyPrint(true)
                .setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL).
                setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.NAME);
        var baos = new ByteArrayOutputStream();
        jsonMarshaller.marshal(document, baos, false, params);
        println(baos.toString(StandardCharsets.UTF_8));
        var doc2 = jsonUnmarshaller.unmarshal(TestRestEntity.class, new ByteArrayInputStream(baos.toByteArray()), params);
        Assertions.assertEquals(document.getStringField(), doc2.getStringField());
        Assertions.assertEquals(document.getEnumField(), doc2.getEnumField());
        Assertions.assertEquals(document.getStringCollection(), doc2.getStringCollection());
        Assertions.assertEquals(document.getStringMap(), doc2.getStringMap());
    }
}
