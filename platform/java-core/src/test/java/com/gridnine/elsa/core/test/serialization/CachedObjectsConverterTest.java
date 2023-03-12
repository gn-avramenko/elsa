/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.test.serialization;

import com.gridnine.elsa.common.core.test.model.domain.TestDomainAsset;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainNestedDocumentImpl;
import com.gridnine.elsa.common.core.test.model.domain.TestEnum;
import com.gridnine.elsa.common.core.test.model.domain.TestGroup;
import com.gridnine.elsa.common.core.test.model.domain.TestItem;
import com.gridnine.elsa.core.model.domain.EntityReference;
import com.gridnine.elsa.core.serialization.CachedObjectConverter;
import com.gridnine.elsa.core.test.common.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CachedObjectsConverterTest extends TestBase {

    @Test
    public void testDocumentConverter(){
        var document = new TestDomainDocument();
        document.setId(2);
        document.setStringProperty("123");
        document.getStringCollection().add("321");
        document.setEntityReferenceProperty(new EntityReference<>(document));
        document.setEnumProperty(TestEnum.ITEM1);
        var nestedEntity = new TestDomainNestedDocumentImpl();
        nestedEntity.setName("test1");
        nestedEntity.setValue("test2");
        document.setEntityProperty(nestedEntity);
        var collEntity = new TestDomainNestedDocumentImpl();
        collEntity.setName("coll1");
        collEntity.setValue("coll2");
        document.getEntityCollection().add(collEntity);
        var group1 = new TestGroup();
        group1.setName("group1");
        document.getGroups().add(group1);
        var group2 = new TestGroup();
        group2.setName("group2");
        document.getGroups().add(group2);
        var item = new TestItem();
        item.setName("item");
        group2.getItems().add(item);
        var doc2 = CachedObjectConverter.get().toCachedObject(document);
        compareDocuments(document, doc2);
        var doc3 = CachedObjectConverter.get().toStandardObject(doc2);
        compareDocuments(document, doc3);
    }

    private void compareDocuments(TestDomainDocument document, TestDomainDocument doc2){
        Assertions.assertEquals(document.getStringProperty(), doc2.getStringProperty());
        Assertions.assertEquals(1, doc2.getStringCollection().size());
        Assertions.assertEquals(document.getEntityReferenceProperty(), doc2.getEntityReferenceProperty());
        Assertions.assertEquals(document.getStringCollection().get(0), doc2.getStringCollection().get(0));
        Assertions.assertEquals(document.getId(), doc2.getId());
        Assertions.assertEquals(document.getEntityProperty().getName(), doc2.getEntityProperty().getName());
        Assertions.assertEquals(((TestDomainNestedDocumentImpl) document.getEntityProperty()).getValue(),((TestDomainNestedDocumentImpl) doc2.getEntityProperty()).getValue());
        Assertions.assertEquals(1, doc2.getEntityCollection().size());
        Assertions.assertEquals(document.getEntityCollection().get(0).getName(), doc2.getEntityCollection().get(0).getName());
        Assertions.assertEquals(((TestDomainNestedDocumentImpl) document.getEntityCollection().get(0)).getValue(),
                ((TestDomainNestedDocumentImpl) doc2.getEntityCollection().get(0)).getValue());
        Assertions.assertEquals(2, doc2.getGroups().size());
        Assertions.assertEquals(1, doc2.getGroups().get(1).getItems().size());
    }

    @Test
    public void testAssetConverter() {
        var asset = new TestDomainAsset();
        asset.setDateTimeProperty(LocalDateTime.now());
        asset.setStringProperty("test");
        var asset2 = CachedObjectConverter.get().toCachedObject(asset);
        compareAssets(asset, asset2);
        var asset3 = CachedObjectConverter.get().toStandardObject(asset2);
        compareAssets(asset, asset3);
    }

    private void compareAssets(TestDomainAsset asset, TestDomainAsset asset2) {
        Assertions.assertEquals(asset.getStringProperty(), asset2.getStringProperty());
        Assertions.assertEquals(asset.getDateTimeProperty(), asset2.getDateTimeProperty());
    }

}
