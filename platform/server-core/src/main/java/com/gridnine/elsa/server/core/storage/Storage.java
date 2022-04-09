/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.core.lock.LockManager;
import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.core.model.common.Localizable;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import com.gridnine.elsa.common.core.search.DynamicCriterionHandler;
import com.gridnine.elsa.common.core.search.SearchQuery;
import com.gridnine.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.core.serialization.SerializationParameters;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.elsa.common.core.utils.RunnableWithException;
import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DatabaseCollectionDescription;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.server.core.auth.AuthUtils;
import com.gridnine.elsa.server.core.storage.transaction.TransactionContext;
import com.gridnine.elsa.server.core.storage.transaction.TransactionManager;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unchecked")
public class Storage {

    @Autowired
    private ListableBeanFactory factory;

    private volatile List<StorageAdvice> advices;

    private List<StorageInterceptor> interceptors;

    @Autowired
    private TransactionManager transactionManager;

    @Autowired
    private LockManager lockManager;

    @Autowired
    private Database database;

    @Autowired
    private JsonUnmarshaller unmarshaller;

    @Autowired
    private JsonMarshaller marshaller;

    private SerializationParameters serializationParameters;

    @Autowired
    private DomainMetaRegistry metaRegistry;

    private final ThreadLocal<GlobalOperationContext> contexts = new ThreadLocal<>();

    @Autowired
    private SupportedLocalesProvider localesProvider;

    private DynamicCriterionsUpdater criterionsUpdater;
    public void init() {
        if (advices == null) {
            advices = factory.getBeansOfType(StorageAdvice.class).values().stream().sorted(Comparator.comparing(StorageAdvice::getPriority)).toList();
            interceptors = factory.getBeansOfType(StorageInterceptor.class).values().stream().sorted(Comparator.comparing(StorageInterceptor::getPriority)).toList();
            serializationParameters = new SerializationParameters();
            serializationParameters.setPrettyPrint(false);
            serializationParameters.setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ONLY_NOT_CACHED);
            serializationParameters.setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.ID);
            serializationParameters.setEntityReferenceTypeSerializationStrategy(SerializationParameters.EntityReferenceTypeSerializationStrategy.ABSTRACT_CLASS_ID);
            serializationParameters.setClassSerializationStrategy(SerializationParameters.ClassSerializationStrategy.ID);
            criterionsUpdater = new DynamicCriterionsUpdater((List) factory.getBeansOfType(DynamicCriterionHandler.class).values().stream().toList());
        }
    }

    public <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment) {
        init();
        lockManager.withLock(asset, () -> transactionManager.withTransaction((tc) -> withGlobalContext(() ->
                saveAsset(asset, createNewVersion, comment, advices, tc, 0))));
    }

    public <A extends BaseAsset> void deleteAsset(A asset) {
        init();
        lockManager.withLock(asset,() -> transactionManager.withTransaction((tc) -> withGlobalContext(() ->
                        deleteAsset(asset, advices, tc, 0))));
    }

    public List<VersionMetadata> getVersionsMetadata(Class<?> cls, long id) {
        init();
        return ExceptionUtils.wrapException(() ->getVersionsMetadata(cls, id, advices, 0));
    }

    public <A extends BaseAsset> void saveAsset(A asset, String comment) {
        saveAsset(asset, false, comment);
    }

    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query, boolean ignoreCache) {
        return ExceptionUtils.wrapException(() ->searchAssets(cls, query, ignoreCache, advices, 0));
    }

    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query) {
        return searchAssets(cls, query, false);
    }

    public <A extends BaseAsset> A loadAssetVersion(Class<A> cls, long id, int version) {
        return  ExceptionUtils.wrapException(() ->loadAssetVersion(cls, id, version, advices, 0));
    }

    <A extends BaseAsset> A loadAsset(Class<A> cls, long id, boolean ignoreCache) {
        return ExceptionUtils.wrapException(() -> loadAsset(cls, id, ignoreCache, advices,  0));
    }

    private <A extends BaseAsset> A loadAsset(Class<A> cls, long id, boolean ignoreCache, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.loadAsset(cls, id);
        }
        return advices.get(0).onLoadAsset(cls, id, ignoreCache ,  ( cls2, id2, ignoreCache2) ->
                loadAsset(cls2, id2, ignoreCache2, advices, idx + 1)
        );
    }

    private <A extends BaseAsset> A loadAssetVersion(Class<A> cls, long id, int version, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var data = database.loadVersion(cls, id ,version);
            var is = new GZIPInputStream(new ByteArrayInputStream(data.getData().content()));
            return unmarshaller.unmarshal(cls, is, serializationParameters);
        }
        return advices.get(idx).onLoadAssetVersion(cls, id, version , (cls2, uid2, version2) ->
                loadAssetVersion(cls2, uid2,  version2, advices, idx + 1)
        );
    }

    private<A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query, boolean ignoreCache, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.searchAssets(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return advices.get(idx).onSearchAssets(cls, query, ignoreCache, (cls2, query2, ignoreCache2)
                -> searchAssets(cls2, query2, ignoreCache2, advices, idx + 1));
    }

    private List<VersionMetadata> getVersionsMetadata(Class<?> cls, long id, List<StorageAdvice> storageAdvices, int idx) throws Exception {
        init();
        if (idx == storageAdvices.size()) {
            return database.getVersionsMetadata(cls, id).stream().sorted(Comparator.comparing(VersionMetadata::getVersionNumber)).toList();
        }
        return advices.get(idx).onGetVersionsMetadata(cls, () -> getVersionsMetadata(cls,id,advices, idx + 1));
    }

    private <A extends BaseAsset> void deleteAsset(A asset, List<StorageAdvice> storageAdvices, TransactionContext ctx, int idx) throws Exception {
        init();
        if (idx == storageAdvices.size()) {
            var uac  = getUpdateAssetContext(asset, ctx);
            if(uac.oldAsset == null){
                throw  Xeption.forDeveloper("asset %s id = %s is absent in db".formatted(asset.getClass().getName(), asset.getId()));
            }
            if(asset.getVersionInfo().getRevision() != uac.oldAsset.getAsset().getVersionInfo().getRevision()){
                throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                        uac.oldAsset.getAsset().getVersionInfo().getRevision(),   asset.getVersionInfo().getRevision()
                ));
            }
            for (var interceptor : interceptors) {
                interceptor.onDelete(uac.oldAsset.getAsset(), uac.operationContext);
            }
            database.deleteAsset(asset.getClass(), asset.getId());
            for(var metadata: database.getVersionsMetadata(asset.getClass(), asset.getId())){
                database.deleteVersion(asset.getClass(), asset.getId(), metadata.getVersionNumber());
            }
            database.deleteCaptions(asset.getClass(), asset.getId());
            return;
        }
        advices.get(idx).onDeleteAsset(asset, (asset2) -> deleteAsset(asset2, advices, ctx, idx + 1));
    }

    private <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment, List<StorageAdvice> storageAdvices, TransactionContext ctx, int idx) throws Exception {
        init();
        if (idx == storageAdvices.size()) {
            var uc = getUpdateAssetContext(asset, ctx);
            for (var interceptor : interceptors) {
                interceptor.onSave(asset, uc.operationContext());
            }
            if (asset.getVersionInfo() == null) {
                asset.setVersionInfo(new VersionInfo());
            }
            var oldAsset = uc.oldAsset();
            if (oldAsset == null) {
                asset.getVersionInfo().setValue(VersionInfo.Properties.revision, 0);
            } else {
                if(asset.getVersionInfo().getRevision() != oldAsset.getAsset().getVersionInfo().getRevision()){
                    throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                            oldAsset.getAsset().getVersionInfo().getRevision(),   asset.getVersionInfo().getRevision()
                    ));
                }
                asset.getVersionInfo().setValue(VersionInfo.Properties.revision, oldAsset.getAsset().getVersionInfo().getRevision() + 1);
            }
            var description = metaRegistry.getAssets().get(asset.getClass().getName());
            AggregatedData data = buildAggreagatedData(asset, description);
            String aggregatedData = data.getAggregatedData();
            var now = LocalDateTime.now();
            asset.getVersionInfo().setModified(now);
            asset.getVersionInfo().setModifiedBy(AuthUtils.getCurrentUser());
            asset.getVersionInfo().setComment(comment);
            asset.getVersionInfo().setVersionNumber(oldAsset == null ? 0 : oldAsset.getAsset().getVersionInfo().getVersionNumber() + 1);
            database.saveAsset(new DatabaseAssetWrapper<>(asset, aggregatedData), uc.oldAsset);
            if (createNewVersion && oldAsset != null) {
                var vi = new VersionInfo();
                vi.setVersionNumber(oldAsset.getAsset().getVersionInfo().getVersionNumber());
                vi.setModifiedBy(AuthUtils.getCurrentUser());
                vi.setComment(comment);
                vi.setModified(now);
                var baos = new ByteArrayOutputStream();
                var gzipos = new GZIPOutputStream(baos);
                try {
                    marshaller.marshal(oldAsset.getAsset(), gzipos, false, serializationParameters);
                } finally {
                    gzipos.flush();
                    gzipos.close();
                }
                database.saveAssetVersion((Class<A>) asset.getClass(), asset.getId(), new BlobWrapper(null, baos.toByteArray()), vi);
            }
            if(asset instanceof Localizable lz){
                var names = new LinkedHashMap<Locale, String>();
                for(var locale : localesProvider.getSupportedLocales()){
                    names.put(locale, lz.toString(locale));
                }
                database.updateCaptions(asset.getClass(), asset.getId(), names, oldAsset == null);
            } else {
                database.updateCaptions(asset.getClass(), asset.getId(), asset.toString(), oldAsset == null);
            }
            return;
        }
        advices.get(idx).onSave(asset, (asset2) -> saveAsset(asset2, createNewVersion, comment, advices, ctx, idx + 1));
    }


    private <A extends BaseIntrospectableObject> AggregatedData buildAggreagatedData(A object, BaseSearchableDescription description) {
        var result = new AggregatedData();
        for(DatabasePropertyDescription prop: description.getProperties().values()){
            if(prop.isUseInTextSearch()){
                result.aggregate(object.getValue(prop.getId()));
            }
        }
        for(DatabaseCollectionDescription coll: description.getCollections().values()){
            if(coll.isUseInTextSearch()){
                result.aggregate(object.getValue(coll.getId()));
            }
        }
        return result;
    }

    private <A extends BaseAsset> UpdateAssetContext<A> getUpdateAssetContext(A asset, TransactionContext ctx) throws Exception {
        if(asset.getVersionInfo() == null){
            asset.setVersionInfo(new VersionInfo());
        }
        var oldAssetWrapperData = database.loadAssetWrapper((Class<A>) asset.getClass(), asset.getId());
        var revision = asset.getVersionInfo().getRevision();
        if (revision == -1) {
            revision =  oldAssetWrapperData == null? 0: oldAssetWrapperData.getAsset().getVersionInfo().getRevision()+1;
        }
        var oldAsset = oldAssetWrapperData == null? null: oldAssetWrapperData.getAsset();
        if (oldAsset != null && revision != oldAsset.getVersionInfo().getRevision()) {
            throw Xeption.forDeveloper("revision conflict with asset %s %s, db revision = %s, operation revision %s"
                    .formatted(asset.getClass().getName(), asset.getId(),
                            oldAsset.getVersionInfo().getRevision(), revision));
        }
        var globalContext = contexts.get();
        if (globalContext == null) {
            globalContext = new GlobalOperationContext(asset, oldAsset, ctx);
            contexts.set(globalContext);
        }
        var localContext = new LocalOperationContext<>(oldAsset);
        var operationContext = new OperationContext<>(globalContext, localContext);
        return new UpdateAssetContext<>(oldAssetWrapperData, operationContext);
    }

    private <T> T withGlobalContext(Callable<T> callable) {
        return ExceptionUtils.wrapException(() -> {
                    var owner = contexts.get() == null;
                    try {
                        return callable.call();
                    } finally {
                        if (owner) {
                            contexts.remove();
                        }
                    }
                }
        );
    }

    private void withGlobalContext(RunnableWithException runnable) {
        withGlobalContext(() -> {
            runnable.run();
            return null;
        });
    }


    private record UpdateAssetContext<A extends BaseAsset>(DatabaseAssetWrapper<A> oldAsset,
                                                           OperationContext<A> operationContext) {
    }

}
