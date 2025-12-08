/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo.test;

import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.server.mongo.MongoFacade;
import com.gridnine.platform.elsa.server.mongo.test.model.domain.TestMongoDomainAsset;
import com.gridnine.platform.elsa.server.mongo.test.model.domain.TestMongoDomainAssetFields;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class MongoBasicTest extends ServerMongoTestBase{

    @Autowired
    private Storage storage;

    @Autowired
    private MongoFacade mongoFacade;

    @Test
    public void testBasicOperations(){
        TestMongoDomainAsset asset = new TestMongoDomainAsset();
        asset.setStringProperty("test");
        asset.setBooleanProperty(true);
        asset.setBigDecimalProperty(BigDecimal.TEN);
        asset.setLongProperty(10L);
        storage.saveAsset(asset, "initialization");
        var assetRef = asset.toReference();
        Assertions.assertNotNull(storage.loadAsset(assetRef, false));
        var doc = storage.loadAsset(TestMongoDomainAsset.class, asset.getId(), false);
        Assertions.assertEquals("test",doc.getStringProperty());
        List<TestMongoDomainAsset> assets = storage.searchAssets(TestMongoDomainAsset.class,
                new SearchQueryBuilder().where(SearchCriterion.eq(TestMongoDomainAssetFields.stringProperty, "test")).build());
        Assertions.assertEquals(1, assets.size());
        Assertions.assertEquals("test", assets.get(0).getStringProperty());
        Assertions.assertEquals(true, assets.get(0).getBooleanProperty());
        assertEquals(10 , assets.get(0).getBigDecimalProperty());
        Assertions.assertEquals(10L, assets.get(0).getLongProperty());
        storage.deleteAsset(asset);
        Assertions.assertNull(storage.loadAsset(assetRef, false));
    }
}
