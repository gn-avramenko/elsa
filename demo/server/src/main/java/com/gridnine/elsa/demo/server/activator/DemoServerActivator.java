/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.activator;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.search.SearchQuery;
import com.gridnine.elsa.demo.ElsaDemoDomainMetaRegistryConfigurator;
import com.gridnine.elsa.demo.ElsaDemoRemotingMetaRegistryConfigurator;
import com.gridnine.elsa.demo.model.domain.DemoDomainAsset;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocument;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection;
import com.gridnine.elsa.demo.model.domain.DemoDomainNestedDocumentImpl;
import com.gridnine.elsa.demo.model.domain.DemoEnum;
import com.gridnine.elsa.demo.model.domain.DemoGroup;
import com.gridnine.elsa.demo.server.remoting.DemoEventSource;
import com.gridnine.elsa.demo.server.storage.DemoDomainDocumentProjectionHandler;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.auth.AuthContext;
import com.gridnine.elsa.server.storage.Storage;
import com.gridnine.elsa.server.storage.StorageRegistry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class DemoServerActivator implements Activator {
    @Override
    public double getOrder() {
        return 4;
    }

    @Override
    public void configure() throws Exception {
        new ElsaDemoDomainMetaRegistryConfigurator().configure();
        new ElsaDemoRemotingMetaRegistryConfigurator().configure();
        StorageRegistry.get().register(new DemoDomainDocumentProjectionHandler());
    }

    @Override
    public void activate() throws Exception {
        AuthContext.setCurrentUser("admin");
        Environment.publish(new DemoEventSource());
        if(Storage.get().searchDocuments(DemoDomainDocumentProjection.class, new SearchQuery()).size() != 0){
            return;
        }
        EntityReference<DemoDomainDocument> ref1 = null;
        EntityReference<DemoDomainDocument> ref2 = null;
        for(int n = 0; n < 50; n++){
            var doc = new DemoDomainDocument();
            {
                DemoDomainNestedDocumentImpl nd = new DemoDomainNestedDocumentImpl();
                nd.setName("nested %s".formatted(n));
                doc.setEntityProperty(nd);
            }
            doc.setEnumProperty(n%2 == 0? DemoEnum.ITEM1: DemoEnum.ITEM2);
            doc.setEntityReferenceProperty(ref1);
            doc.setStringProperty("string prop %s".formatted(n));
            doc.setGetAllProperty("get all prop %s".formatted(n));
            {
                DemoDomainNestedDocumentImpl nd = new DemoDomainNestedDocumentImpl();
                nd.setName("nested %s".formatted(n));
                doc.getEntityCollection().add(nd);
            }
            {
                DemoDomainNestedDocumentImpl nd = new DemoDomainNestedDocumentImpl();
                nd.setName("nested %s".formatted(n+1));
                doc.getEntityCollection().add(nd);
            }
            if(ref1 != null) {
                doc.getEntityRefCollection().add(ref1);
            }
            if(ref2 != null) {
                doc.getEntityRefCollection().add(ref2);
            }
            doc.getEnumCollection().add(DemoEnum.ITEM1);
            doc.getEnumCollection().add(DemoEnum.ITEM2);
            {
                var demoGroup = new DemoGroup();
                demoGroup.setName("group %s".formatted(n));
                doc.getGroups().add(demoGroup);
            }
            {
                var demoGroup = new DemoGroup();
                demoGroup.setName("group %s".formatted(n+1));
                doc.getGroups().add(demoGroup);
            }
            doc.getStringCollection().addAll(Arrays.asList("string 1", "string 2"));
            doc.setBooleanProperty(n % 2 == 0);
            doc.setBigDecimalProprerty(BigDecimal.valueOf(10));
            doc.setIntProperty(n %2 == 0? 1: 2);
            doc.setLongProperty(n %2 == 0? 100: 200);
            doc.setDateProperty(LocalDate.now());
            doc.setDateTimeProperty(LocalDateTime.now());
            Storage.get().saveDocument(doc);
            var asset = new DemoDomainAsset();
            asset.setDateTimeProperty(LocalDateTime.now());
            asset.setStringProperty("string %s".formatted(n));
            Storage.get().saveAsset(asset);
            if(ref1 == null) {
                ref1 = new EntityReference<>(doc);
            } else if (ref2 == null){
                ref2 = new EntityReference<>(doc);
            }
        }
    }
}
