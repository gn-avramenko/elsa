/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.search.SearchCriterion;
import com.gridnine.elsa.common.core.search.SearchQuery;
import com.gridnine.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainAsset;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainAssetFields;
import com.gridnine.elsa.server.core.auth.AuthUtils;
import com.gridnine.elsa.server.core.common.ServerCoreTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
public class StorageTest extends ServerCoreTestBase {

    @Autowired
    private Storage storage;

    @Test
    public void testAssetCRUD() {
        AuthUtils.setCurrentUser("system");
        var asset = new TestDomainAsset();
        asset.setStringProperty("test");
        asset.setDateProperty(LocalDateTime.now());
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
    }
}
