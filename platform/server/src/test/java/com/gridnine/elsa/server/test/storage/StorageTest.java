/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test.storage;

import com.gridnine.elsa.common.test.model.domain.TestDomainAsset;
import com.gridnine.elsa.common.test.model.domain.TestDomainAssetFields;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocumentProjection;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocumentProjectionFields;
import com.gridnine.elsa.common.test.model.domain.TestEnum;
import com.gridnine.elsa.common.model.common.IdGenerator;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.search.AggregationQueryBuilder;
import com.gridnine.elsa.common.search.SearchCriterion;
import com.gridnine.elsa.common.search.SearchQuery;
import com.gridnine.elsa.common.search.SearchQueryBuilder;
import com.gridnine.elsa.server.auth.AuthContext;
import com.gridnine.elsa.server.storage.Storage;
import com.gridnine.elsa.server.test.ServerTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

public class StorageTest extends ServerTestBase {


    @Test
    public void testDocumentCRUD() {
        AuthContext.setCurrentUser("system");
        var storage = Storage.get();
        var doc = new TestDomainDocument();
        doc.setId(IdGenerator.get().nextId());
        doc.setStringProperty("test");
        doc.setEntityReferenceProperty(new EntityReference<>(doc));
        doc.getStringCollection().addAll(Arrays.asList("test1", "test2"));
        doc.getEnumCollection().addAll(Arrays.asList(TestEnum.ITEM1, TestEnum.ITEM2));
        doc.getEntityRefCollection().addAll(Arrays.asList(new EntityReference<>(doc), new EntityReference<>(doc)));
        doc.setEnumProperty(TestEnum.ITEM1);
        storage.saveDocument(doc,"version1");
        doc = storage.loadDocument(TestDomainDocument.class, doc.getId(), true);
        Assertions.assertEquals("test", doc.getStringProperty());
        doc.setStringProperty("test2");
        storage.saveDocument(doc, true, "version2");
        doc = storage.loadDocument(TestDomainDocument.class, doc.getId(), true);
        Assertions.assertEquals("test2", doc.getStringProperty());
        doc = storage.loadDocumentVersion(TestDomainDocument.class, doc.getId(), 0);
        Assertions.assertEquals("test", doc.getStringProperty());
        var metadataItems = storage.getVersionsMetadata(TestDomainDocument.class, doc.getId());
        Assertions.assertEquals(2, metadataItems.size());
        var query = new SearchQueryBuilder().where(SearchCriterion.eq(TestDomainDocumentProjectionFields.stringProperty,
                "test2")).build();
        var indexes = storage.searchDocuments(TestDomainDocumentProjection.class, query);
        Assertions.assertEquals(1, indexes.size());
        query = new SearchQueryBuilder().freeText("test2").build();
        indexes = storage.searchDocuments(TestDomainDocumentProjection.class, query);
        Assertions.assertEquals(1, indexes.size());
        indexes = storage.searchDocuments(TestDomainDocumentProjection.class, new SearchQuery());
        Assertions.assertEquals(1, indexes.size());
        var idx = indexes.get(0);
        Assertions.assertEquals(2, idx.getStringCollection().size());
        Assertions.assertEquals(2, idx.getEnumCollection().size());
        Assertions.assertEquals(2, idx.getEntityRefCollection().size());
        query = new SearchQueryBuilder().preferredFields(TestDomainDocumentProjectionFields.entityReferenceProperty)
                        .where(SearchCriterion.eq(TestDomainDocumentProjectionFields.entityReferenceProperty,
                                doc.getEntityReferenceProperty())).build();
        indexes = storage.searchDocuments(TestDomainDocumentProjection.class, query);
        Assertions.assertEquals(1, indexes.size());
        var aggregationQuery = new AggregationQueryBuilder().count().where(
                SearchCriterion.eq(TestDomainDocumentProjectionFields.stringProperty, "test2")
        ).build();
        var count = (long) storage.searchDocuments(TestDomainDocumentProjection.class, aggregationQuery).get(0).get(0);
        Assertions.assertEquals(1, count);
        var refs = storage.searchCaptions(TestDomainDocument.class, "test", 10);
        Assertions.assertEquals(1, refs.size());
    }

    @Test
    public void testAssetCRUD() {
        AuthContext.setCurrentUser("system");
        var asset = new TestDomainAsset();
        var storage = Storage.get();
        asset.setStringProperty("test");
        asset.setDateTimeProperty(LocalDateTime.now());
        storage.saveAsset(asset,"init");
        asset = storage.loadAsset(TestDomainAsset.class, asset.getId(), true);
        var metadataItems = storage.getVersionsMetadata(TestDomainAsset.class, asset.getId());
        asset.setStringProperty("test2");
        storage.saveAsset(asset, true, "version2");
        asset = storage.loadAsset(TestDomainAsset.class, asset.getId(), true);
        asset = storage.loadAssetVersion(TestDomainAsset.class, asset.getId(), 0);
        Assertions.assertEquals("test", asset.getStringProperty());
        var query = new SearchQueryBuilder().
                where(SearchCriterion.eq(TestDomainAssetFields.stringProperty, "test2")).build();
        var assets = storage.searchAssets(TestDomainAsset.class, query);
        Assertions.assertEquals(1, assets.size());
        query = new SearchQueryBuilder().freeText("test2").build();
        assets = storage.searchAssets(TestDomainAsset.class, query) ;
        Assertions.assertEquals(1, assets.size());
        assets = storage.searchAssets(TestDomainAsset.class, new SearchQuery());
        Assertions.assertEquals(1, assets.size());
        metadataItems = storage.getVersionsMetadata(TestDomainAsset.class, asset.getId());
        Assertions.assertEquals(2, metadataItems.size());
        var aggregationQuery = new AggregationQueryBuilder().count().where(
                SearchCriterion.eq(TestDomainAssetFields.stringProperty, "test2")
        ).build();
        var count = (long) storage.searchAssets(TestDomainAsset.class, aggregationQuery).get(0).get(0);
        Assertions.assertEquals(1, count);
    }
}
