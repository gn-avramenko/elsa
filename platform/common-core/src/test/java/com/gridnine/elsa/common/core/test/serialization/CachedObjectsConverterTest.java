/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.test.serialization;

import com.gridnine.elsa.common.core.common.TestBase;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.serialization.CachedObjectConverter;
import com.gridnine.elsa.common.core.test.model.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
public class CachedObjectsConverterTest extends TestBase {

    @Autowired
    private CachedObjectConverter converter;

    @Test
    public void testDocumentConverter(){
        var document = new TestDomainDocument();
        document.setId(2);
        document.setStringProperty("123");
        document.getStringCollection().add("321");
        document.setEntityReference(new EntityReference<>(document));
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
        var doc2 = converter.toCachedObject(document);
        compareDocuments(document, doc2);
        var doc3 = converter.toStandardObject(doc2);
        compareDocuments(document, doc3);
    }

    private void compareDocuments(TestDomainDocument document, TestDomainDocument doc2){
        Assertions.assertEquals(document.getStringProperty(), doc2.getStringProperty());
        Assertions.assertEquals(1, doc2.getStringCollection().size());
        Assertions.assertEquals(document.getEntityReference(), doc2.getEntityReference());
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
        asset.setDateProperty(LocalDateTime.now());
        asset.setStringProperty("test");
        var asset2 = converter.toCachedObject(asset);
        compareAssets(asset, asset2);
        var asset3 = converter.toStandardObject(asset2);
        compareAssets(asset, asset3);
    }

    private void compareAssets(TestDomainAsset asset, TestDomainAsset asset2) {
        Assertions.assertEquals(asset.getStringProperty(), asset2.getStringProperty());
        Assertions.assertEquals(asset.getDateProperty(), asset2.getDateProperty());
    }

//
//    @Test
//    fun testAssetConverter(){
//        val asset = TestDomainAsset()
//        asset.dateProperty = LocalDateTime.now()
//        asset.stringProperty = "test"
//        val asset2 = CachedObjectsConverter.get().toCachedObject(asset)
//        Assert.assertEquals(asset.dateProperty, asset2.dateProperty)
//        Assert.assertEquals(asset.stringProperty, asset2.stringProperty)
//    }
}
