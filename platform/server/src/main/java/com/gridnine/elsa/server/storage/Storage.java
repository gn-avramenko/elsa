/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.common.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.server.lock.LockManager;
import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.common.Localizable;
import com.gridnine.elsa.common.model.common.ObjectFactoryWithException;
import com.gridnine.elsa.common.model.common.RunnableWithException;
import com.gridnine.elsa.common.model.common.Xeption;
import com.gridnine.elsa.common.model.domain.BaseAsset;
import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.BaseProjection;
import com.gridnine.elsa.common.model.domain.CachedObject;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.model.domain.VersionInfo;
import com.gridnine.elsa.common.search.AggregationQuery;
import com.gridnine.elsa.common.search.ArgumentType;
import com.gridnine.elsa.common.search.EqualitySupport;
import com.gridnine.elsa.common.search.FieldNameSupport;
import com.gridnine.elsa.common.search.SearchCriterion;
import com.gridnine.elsa.common.search.SearchQuery;
import com.gridnine.elsa.common.search.SearchQueryBuilder;
import com.gridnine.elsa.common.serialization.CachedObjectConverter;
import com.gridnine.elsa.common.serialization.JsonMarshaller;
import com.gridnine.elsa.common.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.common.utils.IoUtils;
import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.ServerL10nMessagesRegistryFactory;
import com.gridnine.elsa.server.auth.AuthContext;
import com.gridnine.elsa.server.storage.repository.Repository;
import com.gridnine.elsa.server.storage.repository.RepositoryAssetWrapper;
import com.gridnine.elsa.server.storage.repository.RepositoryBinaryData;
import com.gridnine.elsa.server.storage.repository.RepositoryObjectData;
import com.gridnine.elsa.server.storage.repository.RepositoryProjectionWrapper;
import com.gridnine.elsa.server.storage.transaction.TransactionContext;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;
import com.nothome.delta.Delta;
import com.nothome.delta.GDiffPatcher;
import com.nothome.delta.GDiffWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unchecked")
public class Storage {

    private final Map<String, Object> serializationParameters = new HashMap<>();

    private final ThreadLocal<GlobalOperationContext> contexts = new ThreadLocal<>();

    private final GDiffPatcher patcher = new GDiffPatcher();
    private final Delta delta = new Delta();

    private final DynamicCriterionsUpdater criterionsUpdater = new DynamicCriterionsUpdater();


    public <D extends BaseDocument> D loadDocument(EntityReference<D> ref, boolean forModification) {
        if (ref == null) {
            return null;
        }
        return loadDocument(ref.getType(), ref.getId(), forModification);
    }

    public <D extends BaseDocument> D loadDocument(Class<D> cls, long id, boolean forModification) {
        
        return ExceptionUtils.wrapException(() -> loadDocument(cls, id, forModification, StorageRegistry.get().getAdvices(), 0));
    }

    public <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment) {
        
        LockManager.get().withLock(asset, () -> TransactionManager.get().withTransaction((tc) -> withGlobalContext(() ->
                saveAsset(asset, createNewVersion, comment, StorageRegistry.get().getAdvices(), tc, 0))));
    }

    public <A extends BaseAsset> void deleteAsset(A asset) {
        
        LockManager.get().withLock(asset, () -> TransactionManager.get().withTransaction((tc) -> withGlobalContext(() ->
                deleteAsset(asset, StorageRegistry.get().getAdvices(), tc, 0))));
    }

    public List<VersionInfo> getVersionsMetadata(Class<?> cls, long id) {
        
        return ExceptionUtils.wrapException(() -> getVersionsMetadata(cls, id, StorageRegistry.get().getAdvices(), 0));
    }

    public <A extends BaseAsset> void saveAsset(A asset, String comment) {
        
        saveAsset(asset, false, comment);
    }

    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query, boolean forModification) {
        
        return ExceptionUtils.wrapException(() -> searchAssets(cls, query, forModification, StorageRegistry.get().getAdvices(), 0));
    }

    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query) {
        
        return searchAssets(cls, query, false);
    }

    public <A extends BaseAsset> A loadAssetVersion(Class<A> cls, long id, int version) {
        
        return ExceptionUtils.wrapException(() -> loadAssetVersion(cls, id, version, StorageRegistry.get().getAdvices(), 0));
    }

    public <A extends BaseAsset> A loadAsset(Class<A> cls, long id, boolean forModification) {
        
        return ExceptionUtils.wrapException(() -> loadAsset(cls, id, forModification, StorageRegistry.get().getAdvices(), 0));
    }

    public <D extends BaseDocument> D loadDocumentVersion(Class<D> cls, long id, int version) {
        
        return ExceptionUtils.wrapException(() -> loadDocumentVersion(cls, id, version, StorageRegistry.get().getAdvices(), 0));
    }

    public <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
    EntityReference<D> findUniqueDocumentReference(Class<I> projClass, E property, T propertyValue) {
        
        return ExceptionUtils.wrapException(() -> findUniqueDocumentReference(projClass, property, propertyValue, StorageRegistry.get().getAdvices(), 0));
    }

    public <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    D findUniqueDocument(Class<I> projClass, E property, T propertyValue, boolean forModification) {
        
        return loadDocument(findUniqueDocumentReference(projClass, property, propertyValue), forModification);
    }

    public <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    Set<EntityReference<D>> getAllDocumentReferences(Class<I> projClass, E property, T propertyValue) {
        
        return ExceptionUtils.wrapException(() -> getAllDocumentReferences(projClass, property, propertyValue, StorageRegistry.get().getAdvices(), 0));
    }

    public <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    Set<D> getAllDocuments(Class<I> projClass, E property, T propertyValue, boolean forModification) {
        
        return getAllDocumentReferences(projClass, property, propertyValue).stream().map((it) -> loadDocument(it, forModification)).collect(Collectors.toSet());
    }

    public <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery query) {
        
        return ExceptionUtils.wrapException(() -> searchAssets(cls, query, StorageRegistry.get().getAdvices(), 0));
    }

    public <D extends BaseDocument, I extends BaseProjection<D>> List<I> searchDocuments(Class<I> cls, SearchQuery query) {
        
        return ExceptionUtils.wrapException(() -> searchDocuments(cls, query, StorageRegistry.get().getAdvices(), 0));
    }

    public <D extends BaseDocument, I extends BaseProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery query) {
        
        return ExceptionUtils.wrapException(() -> searchDocuments(cls, query, StorageRegistry.get().getAdvices(), 0));
    }

    public <D extends BaseDocument> void updateProjections(D doc, Class<?>... projectionClasses) {
        
        TransactionManager.get().withTransaction((tc) -> updateProjectionsInternal(doc, true, projectionClasses));
    }

    public <D extends BaseDocument> void saveDocument(D doc) {
        saveDocument(doc, false, null, new SaveDocumentParameters());
    }


    public <D extends BaseDocument> void saveDocument(D doc, String comment) {
        saveDocument(doc, false, comment, new SaveDocumentParameters());
    }


    public <D extends BaseDocument> void saveDocument(D doc, boolean createNewVersion, String comment) {
        saveDocument(doc, createNewVersion, comment, new SaveDocumentParameters());
    }


    public <D extends BaseDocument> void saveDocument(D doc, boolean createNewVersion, String comment, SaveDocumentParameters params) {
        
        LockManager.get().withLock(doc, () -> TransactionManager.get().withTransaction((tx) ->
                withGlobalContext(() -> saveDocument(doc, createNewVersion, comment, params, StorageRegistry.get().getAdvices(), tx, 0))));
    }

    public <D extends BaseDocument> void deleteDocument(D doc) {
        deleteDocument(doc, new DeleteDocumentParameters());
    }

    public <D extends BaseDocument> void deleteDocument(D doc, DeleteDocumentParameters params) {
        
        LockManager.get().withLock(doc, () -> TransactionManager.get().withTransaction((tx) ->
                withGlobalContext(() -> deleteDocument(doc, params, StorageRegistry.get().getAdvices(), tx, 0))));
    }
    public<D extends BaseIdentity> void updateCaptions(D entity){

        updateCaptions(entity, new UpdateCaptionsParameters());
    }
    public<D extends BaseIdentity> void updateCaptions(D entity,UpdateCaptionsParameters params){
        
        LockManager.get().withLock(entity, () -> TransactionManager.get().withTransaction((tx) ->
                withGlobalContext(() -> updateCaptions(entity, params, StorageRegistry.get().getAdvices(), tx, 0))));
    }

    public <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit){
        
        return ExceptionUtils.wrapException( () -> Repository.get().searchCaptions(cls, pattern, limit, LocaleUtils.getCurrentLocale()));
    }

    public <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> A findUniqueAsset(Class<A> cls, E property,
                                                                                                                    T propertyValue,
                                                                                                                    boolean forModification) {
        return ExceptionUtils.wrapException(() -> findUniqueAsset(cls, property, propertyValue, forModification, StorageRegistry.get().getAdvices(), 0));
    }

    public <T,A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> Set<A> getAllAssets(Class<A> cls, E property, T propertyValue, boolean forModification){
        return ExceptionUtils.wrapException(() ->getAllAssets(cls, property, propertyValue, forModification, StorageRegistry.get().getAdvices(), 0));
    }

    private <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> Set<A> getAllAssets(Class<A> cls, E property, T propertyValue, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQuery();
            query.getPreferredFields().add(BaseProjection.Fields.document);
            if (propertyValue != null) {
                query.getCriterions().add(SearchCriterion.eq(property, propertyValue));
            } else {
                query.getCriterions().add(SearchCriterion.isNull(property));
            }
            return new HashSet<>(Repository.get().searchAssets(cls, query));
        }
        return StorageRegistry.get().getAdvices().get(idx).onGetAllAssets(cls, property, propertyValue, forModification, (asset2, property2, propertyValue2, forModification2) ->
                getAllAssets(asset2, property2, propertyValue2, forModification2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> A findUniqueAsset(Class<A> cls, E property,
                                                                                                                     T propertyValue,
                                                                                                                     boolean forModification, List<StorageAdvice> advices,int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQueryBuilder().preferredFields(property).where(propertyValue == null?
                    SearchCriterion.isNull(property):
                    SearchCriterion.eq(property, propertyValue)).build();
            var lst = Repository.get().searchAssets(cls, query);
            var size = lst.size();
            if (size == 0) {
                return null;
            } else if (size == 1) {
                return lst.get(0);
            } else {
                throw Xeption.forAdmin(ServerL10nMessagesRegistryFactory
                        .Found_several_recordsMessage(cls.getName(), property.name, propertyValue == null ? null : propertyValue.toString()));
            }
        }
        return StorageRegistry.get().getAdvices().get(idx).onFindUniqueAsset(cls, property, propertyValue, forModification, (cls2, property2, propertyValue2, forModification2) ->
                findUniqueAsset(cls2, property2, propertyValue2, forModification2, StorageRegistry.get().getAdvices(), idx + 1)
        );
    }

    private <D extends BaseIdentity> void updateCaptions(D entity, UpdateCaptionsParameters params, List<StorageAdvice> advices, TransactionContext tx, int idx) throws Exception {
        if (idx == advices.size()) {
            if (entity instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : SupportedLocalesProvider.get().getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                Repository.get().updateCaptions(entity.getClass(), entity.getId(), names, false);
            } else {
                Repository.get().updateCaptions(entity.getClass(), entity.getId(), entity.toString(), false);
            }
            return;
        }
        StorageRegistry.get().getAdvices().get(idx).onUpdateCaptions(entity, params, (entity2, params2) -> updateCaptions(entity2, params2, StorageRegistry.get().getAdvices(), tx, idx + 1));
    }

    private <D extends BaseDocument> void deleteDocument(D document, DeleteDocumentParameters params, List<StorageAdvice> storageAdvices, TransactionContext ctx, int idx) throws Exception {
        if (idx == storageAdvices.size()) {
            var udc = getUpdateDocumentContext(document, ctx);
            if (udc.oldDocument == null) {
                throw Xeption.forDeveloper("document %s id = %s is absent in db".formatted(document.getClass().getName(), document.getId()));
            }
            if (document.getVersionInfo().getRevision() != udc.oldDocument.getRevision()) {
                throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                        udc.oldDocument.getRevision(), document.getVersionInfo().getRevision()
                ));
            }
            if(!params.isSkipInterceptors()) {
                for (var interceptor : StorageRegistry.get().getInterceptors()) {
                    interceptor.onDelete(document, udc.operationContext);
                }
            }
            Repository.get().deleteDocument(document.getClass(), document.getId(), udc.oldDocument.getData().id());
            for (var metadata : Repository.get().getVersionsMetadata(document.getClass(), document.getId())) {
                Repository.get().deleteVersion(document.getClass(), document.getId(), metadata.getVersionNumber());
            }
            Repository.get().deleteCaptions(document.getClass(), document.getId());
            var handlers = StorageRegistry.get().getProjectionHandlers(document.getClass());
            if (handlers != null) {
                for (var projectionHandler : handlers) {
                    Repository.get().deleteProjections(projectionHandler.getProjectionClass(), document.getId());
                }
            }
            return;
        }
        StorageRegistry.get().getAdvices().get(idx).onDeleteDocument(document, params, (document2, params2) -> deleteDocument(document2, params2, StorageRegistry.get().getAdvices(), ctx, idx + 1));
    }

    private <D extends BaseDocument> void saveDocument(D doc, boolean createNewVersion, String comment, SaveDocumentParameters params,
                                                       List<StorageAdvice> advices, TransactionContext ctx, int idx) throws Exception {
        if (idx == advices.size()) {
            if (doc.getVersionInfo() == null) {
                doc.setVersionInfo(new VersionInfo());
            }
            var context = getUpdateDocumentContext(doc, ctx);
            var updatePreviousVersion = !createNewVersion && (context.oldDocument != null && context.oldDocument.getVersionNumber() > 0);
            byte[] previousVersionContent = null;
            if (updatePreviousVersion) {
                var os = new ByteArrayOutputStream();
                try (var is = new ByteArrayInputStream(IoUtils.gunzip(context.previousVersionFactory.createObject()
                        .getData().content()))) {
                    patcher.patch(IoUtils.gunzip(context.oldDocument.getData().content()), is, os);
                }
                previousVersionContent = IoUtils.gzip(os.toByteArray());
            }
            if (!params.isSkipInterceptors()) {
                for (var interceptor : StorageRegistry.get().getInterceptors()) {
                    interceptor.onSave(doc, context.operationContext);
                }
            }
            var now = LocalDateTime.now();
            doc.getVersionInfo().setValue(VersionInfo.Fields.revision, context.oldDocument == null ?
                    0 : context.oldDocument.getRevision() + 1);
            doc.getVersionInfo().setComment(comment);
            doc.getVersionInfo().setModified(now);
            doc.getVersionInfo().setModifiedBy(AuthContext.getCurrentUser());
            doc.getVersionInfo().setValue(VersionInfo.Fields.revision, doc.getVersionInfo().getRevision());
            doc.getVersionInfo().setVersionNumber(
                    context.oldDocument == null ? 0 : (createNewVersion ? context.oldDocument.getVersionNumber() + 1 : context.oldDocument.getVersionNumber())
            );

            var baos = new ByteArrayOutputStream();
            JsonMarshaller.get().marshal(doc, baos, false, serializationParameters);
            var data = baos.toByteArray();
            var baos3 = new ByteArrayOutputStream();
            try (var os = new GZIPOutputStream(baos3)) {
                IoUtils.copy(new ByteArrayInputStream(data), os);
            }
            var obj = new RepositoryObjectData();
            obj.setData(new RepositoryBinaryData(null, baos3.toByteArray()));
            obj.setComment(doc.getVersionInfo().getComment());
            obj.setModified(doc.getVersionInfo().getModified());
            obj.setModifiedBy(doc.getVersionInfo().getModifiedBy());
            obj.setRevision(doc.getVersionInfo().getRevision());
            obj.setVersionNumber(doc.getVersionInfo().getVersionNumber());
            Repository.get().saveDocument(doc.getId(), doc.getClass(), obj, context.oldDocument);
            if ((createNewVersion && context.oldDocument != null) || (context.oldDocument != null && updatePreviousVersion)) {
                var baos2 = new ByteArrayOutputStream();
                try(var writer = new GDiffWriter(baos2) ){
                    delta.compute(data, new GZIPInputStream(new ByteArrayInputStream(updatePreviousVersion? previousVersionContent
                            : context.oldDocument.getData().content())), writer);
                    writer.flush();
                }
                var version = new RepositoryObjectData();
                version.setVersionNumber(updatePreviousVersion? context.oldDocument.getVersionNumber()-1: context.oldDocument.getVersionNumber());
                version.setRevision(context.oldDocument.getRevision());
                version.setModifiedBy(context.oldDocument.getModifiedBy());
                version.setModified(context.oldDocument.getModified());
                version.setComment(context.oldDocument.getComment());
                version.setData(new RepositoryBinaryData(null, IoUtils.gzip(baos2.toByteArray())));
                Repository.get().saveDocumentVersion((Class<D>) doc.getClass(), doc.getId(), version,
                        updatePreviousVersion? context.oldDocument.getData().id() : null );
            }
            if (doc instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : SupportedLocalesProvider.get().getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                Repository.get().updateCaptions(doc.getClass(), doc.getId(), names, context.oldDocument == null);
            } else {
                Repository.get().updateCaptions(doc.getClass(), doc.getId(), doc.toString(), context.oldDocument == null);
            }
            updateProjectionsInternal(doc, context.oldDocument != null);
            return;
        }
        StorageRegistry.get().getAdvices().get(idx).onSaveDocument(doc, createNewVersion, comment, params,  (doc2, createNewVersion2, comment2, params2 ) -> saveDocument(doc2, createNewVersion2, comment2, params2, StorageRegistry.get().getAdvices(), ctx, idx + 1));
    }

    private <D extends BaseDocument> UpdateDocumentContext<D> getUpdateDocumentContext(D doc, TransactionContext tc) throws Exception {
        var oldDocument = Repository.get().loadDocumentData((Class<D>) doc.getClass(), doc.getId());
        var docRevision = doc.getVersionInfo() == null ? -1 : doc.getVersionInfo().getRevision();
        if (docRevision == -1) {
            docRevision = oldDocument == null ? 0 : oldDocument.getRevision();
        }
        if (oldDocument != null && docRevision != oldDocument.getRevision()) {
            throw Xeption.forDeveloper("revision conflict with document %s %s, ".formatted(doc.getClass().getName(), doc.getId()) +
                    "db revision = %s, operation revision %s".formatted(oldDocument.getRevision(), doc.getVersionInfo().getRevision()));
        }
        D oldObject = null;
        if (oldDocument != null) {
            try (var is = new GZIPInputStream(new ByteArrayInputStream(oldDocument.getData().content()))) {
                oldObject = JsonUnmarshaller.get().unmarshal((Class<D>) doc.getClass(), is, serializationParameters);
            }
        }
        var previousVersionContentFactory = new ObjectFactoryWithException<RepositoryObjectData>() {

            RepositoryObjectData result;

            @Override
            public RepositoryObjectData createObject() throws Exception {
                if (oldDocument == null || oldDocument.getVersionNumber() == 0) {
                    return null;
                }
                if (result == null) {
                    result = Repository.get().loadVersion(doc.getClass(), doc.getId(),
                            oldDocument.getVersionNumber() - 1);
                }
                return result;
            }
        };
        var globalContext = contexts.get();
        if (globalContext == null) {
            globalContext = new GlobalOperationContext(doc, oldObject, tc);
            contexts.set(globalContext);
        }
        var localContext = new LocalOperationContext<>(oldObject);
        var operationContext = new OperationContext<>(globalContext, localContext);
        return new UpdateDocumentContext<>(oldDocument, previousVersionContentFactory, operationContext);
    }

    private <D extends BaseDocument> void updateProjectionsInternal(D doc, boolean update, Class<?>... indexClasses) throws Exception {
        var standardObject = doc;
        if (doc instanceof CachedObject co) {
            standardObject = (D) CachedObjectConverter.get().toStandardObject(co);
        }
        var handlers = StorageRegistry.get().getProjectionHandlers(standardObject.getClass());
        if (handlers == null) {
            return;
        }
        for (var projectionHandler : handlers) {
            if (indexClasses.length == 0 || Arrays.stream(indexClasses).anyMatch(it -> it == projectionHandler.getProjectionClass())) {
                var projections = projectionHandler.createProjections(doc, Collections.emptySet());
                var wrappers = new ArrayList<RepositoryProjectionWrapper<BaseDocument, BaseProjection<BaseDocument>>>();
                for (var proj : projections) {
                    proj.setDocument(new EntityReference<>(doc));
                    AggregatedData data = buildAggreagatedData(proj);
                    wrappers.add(new RepositoryProjectionWrapper<>(proj, data.getAggregatedData()));
                }
                Repository.get().updateProjections(projectionHandler.getProjectionClass(), doc.getId(), wrappers, update);
            }
        }

    }

    private <D extends BaseDocument, I extends BaseProjection<D>> List<I> searchDocuments(Class<I> cls, SearchQuery query, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return Repository.get().searchDocuments(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return StorageRegistry.get().getAdvices().get(idx).onSearchDocuments(cls, query, (cls2, query2) ->
                searchDocuments(cls2, query2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private <D extends BaseDocument, I extends BaseProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery query, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return Repository.get().searchDocuments(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return StorageRegistry.get().getAdvices().get(idx).onSearchDocuments(cls, query, (cls2, query2) ->
                searchDocuments(cls2, query2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery query, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return Repository.get().searchAssets(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return StorageRegistry.get().getAdvices().get(idx).onSearchAssets(cls, query, (cls2, query2) ->
                searchAssets(cls2, query2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    Set<EntityReference<D>> getAllDocumentReferences(Class<I> projClass, E property, T propertyValue, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQuery();
            query.getPreferredFields().add(BaseProjection.Fields.document);
            if (propertyValue != null) {
                query.getCriterions().add(SearchCriterion.eq(property, propertyValue));
            } else {
                query.getCriterions().add(SearchCriterion.isNull(property));
            }
            return Repository.get().searchDocuments(projClass, query).stream().map(BaseProjection::getDocument).collect(Collectors.toSet());
        }
        return StorageRegistry.get().getAdvices().get(idx).onGetAllDocumentReferences(projClass, property, propertyValue, (projClass2, property2, propertyValue2) ->
                getAllDocumentReferences(projClass2, property2, propertyValue2, StorageRegistry.get().getAdvices(), idx + 1)
        );
    }

    private <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    EntityReference<D> findUniqueDocumentReference(Class<I> projClass, E property, T propertyValue, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQuery();
            query.getPreferredFields().add(BaseProjection.Fields.document);
            if (propertyValue != null) {
                query.getCriterions().add(SearchCriterion.eq(property, propertyValue));
            } else {
                query.getCriterions().add(SearchCriterion.isNull(property));
            }
            var lst = Repository.get().searchDocuments(projClass, query);
            var size = lst.size();
            if (size == 0) {
                return null;
            } else if (size == 1) {
                return lst.get(0).getDocument();
            } else {
                throw Xeption.forAdmin(ServerL10nMessagesRegistryFactory
                        .Found_several_recordsMessage(projClass.getName(), property.name, propertyValue == null ? null : propertyValue.toString()));
            }
        }
        return StorageRegistry.get().getAdvices().get(idx).onFindUniqueDocumentReference(projClass, property, propertyValue, (projClass2, property2, propertyValue2) ->
                findUniqueDocumentReference(projClass2, property2, propertyValue2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private <D extends BaseDocument> D loadDocumentVersion(Class<D> cls, long objectId, int versionNumber,
                                                           List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var lastVersion = Repository.get().loadDocumentData(cls, objectId);
            if (lastVersion == null) {
                throw new IllegalArgumentException("document of type %s with id %s is not found".formatted(cls.getName(), objectId));
            }
            var data = IoUtils.gunzip(lastVersion.getData().content());
            for (int n = lastVersion.getVersionNumber() - 1; n >= versionNumber; n--) {
                var versionData = Repository.get().loadVersion(cls, objectId, n);
                data = patcher.patch(data, IoUtils.gunzip(versionData.getData().content()));
            }
            return JsonUnmarshaller.get().unmarshal(cls, new ByteArrayInputStream(data), serializationParameters);
        }
        return StorageRegistry.get().getAdvices().get(idx).onLoadDocumentVersion(cls, objectId, versionNumber, (cls2, id2, versionNumber2) ->
                loadDocumentVersion(cls2, id2, versionNumber2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private <A extends BaseAsset> A loadAsset(Class<A> cls, long id, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return Repository.get().loadAsset(cls, id);
        }
        return StorageRegistry.get().getAdvices().get(idx).onLoadAsset(cls, id, forModification, (cls2, id2, forModification2) ->
                loadAsset(cls2, id2, forModification2, StorageRegistry.get().getAdvices(), idx + 1)
        );
    }

    private <A extends BaseAsset> A loadAssetVersion(Class<A> cls, long id, int version, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var data = Repository.get().loadVersion(cls, id, version);
            var is = new GZIPInputStream(new ByteArrayInputStream(data.getData().content()));
            return JsonUnmarshaller.get().unmarshal(cls, is, serializationParameters);
        }
        return StorageRegistry.get().getAdvices().get(idx).onLoadAssetVersion(cls, id, version, (cls2, uid2, version2) ->
                loadAssetVersion(cls2, uid2, version2, StorageRegistry.get().getAdvices(), idx + 1)
        );
    }

    private <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return Repository.get().searchAssets(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return StorageRegistry.get().getAdvices().get(idx).onSearchAssets(cls, query, forModification, (cls2, query2, forModification2)
                -> searchAssets(cls2, query2, forModification2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private List<VersionInfo> getVersionsMetadata(Class<?> cls, long id, List<StorageAdvice> storageAdvices, int idx) throws Exception {
        
        if (idx == storageAdvices.size()) {
            return Repository.get().getVersionsMetadata(cls, id).stream().sorted(Comparator.comparing(VersionInfo::getVersionNumber)).toList();
        }
        return StorageRegistry.get().getAdvices().get(idx).onGetVersionsMetadata(cls, () -> getVersionsMetadata(cls, id, StorageRegistry.get().getAdvices(), idx + 1));
    }

    private <A extends BaseAsset> void deleteAsset(A asset, List<StorageAdvice> storageAdvices, TransactionContext ctx, int idx) throws Exception {
        
        if (idx == storageAdvices.size()) {
            var uac = getUpdateAssetContext(asset, ctx);
            if (uac.oldAsset == null) {
                throw Xeption.forDeveloper("asset %s id = %s is absent in db".formatted(asset.getClass().getName(), asset.getId()));
            }
            if (asset.getVersionInfo().getRevision() != uac.oldAsset.getAsset().getVersionInfo().getRevision()) {
                throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                        uac.oldAsset.getAsset().getVersionInfo().getRevision(), asset.getVersionInfo().getRevision()
                ));
            }
            for (var interceptor : StorageRegistry.get().getInterceptors()) {
                interceptor.onDelete(uac.oldAsset.getAsset(), uac.operationContext);
            }
            Repository.get().deleteAsset(asset.getClass(), asset.getId());
            for (var metadata : Repository.get().getVersionsMetadata(asset.getClass(), asset.getId())) {
                Repository.get().deleteVersion(asset.getClass(), asset.getId(), metadata.getVersionNumber());
            }
            Repository.get().deleteCaptions(asset.getClass(), asset.getId());
            return;
        }
        StorageRegistry.get().getAdvices().get(idx).onDeleteAsset(asset, (asset2) -> deleteAsset(asset2, StorageRegistry.get().getAdvices(), ctx, idx + 1));
    }

    private <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment, List<StorageAdvice> storageAdvices, TransactionContext ctx, int idx) throws Exception {
        
        if (idx == storageAdvices.size()) {
            var uc = getUpdateAssetContext(asset, ctx);
            for (var interceptor : StorageRegistry.get().getInterceptors()) {
                interceptor.onSave(asset, uc.operationContext());
            }
            if (asset.getVersionInfo() == null) {
                asset.setVersionInfo(new VersionInfo());
            }
            var oldAsset = uc.oldAsset();
            if (oldAsset == null) {
                asset.getVersionInfo().setValue(VersionInfo.Fields.revision, 0);
            } else {
                if (asset.getVersionInfo().getRevision() != oldAsset.getAsset().getVersionInfo().getRevision()) {
                    throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                            oldAsset.getAsset().getVersionInfo().getRevision(), asset.getVersionInfo().getRevision()
                    ));
                }
                asset.getVersionInfo().setValue(VersionInfo.Fields.revision, oldAsset.getAsset().getVersionInfo().getRevision() + 1);
            }
            AggregatedData data = buildAggreagatedData(asset);
            String aggregatedData = data.getAggregatedData();
            var now = LocalDateTime.now();
            asset.getVersionInfo().setModified(now);
            asset.getVersionInfo().setModifiedBy(AuthContext.getCurrentUser());
            asset.getVersionInfo().setComment(comment);
            asset.getVersionInfo().setVersionNumber(oldAsset == null ? 0 : oldAsset.getAsset().getVersionInfo().getVersionNumber() + 1);
            Repository.get().saveAsset(new RepositoryAssetWrapper<>(asset, aggregatedData), uc.oldAsset);
            if (createNewVersion && oldAsset != null) {
                var vi = new VersionInfo();
                vi.setVersionNumber(oldAsset.getAsset().getVersionInfo().getVersionNumber());
                vi.setModifiedBy(AuthContext.getCurrentUser());
                vi.setComment(comment);
                vi.setModified(now);
                var baos = new ByteArrayOutputStream();
                var gzipos = new GZIPOutputStream(baos);
                try {
                    JsonMarshaller.get().marshal(oldAsset.getAsset(), gzipos, false, serializationParameters);
                } finally {
                    gzipos.flush();
                    gzipos.close();
                }
                Repository.get().saveAssetVersion((Class<A>) asset.getClass(), asset.getId(), new RepositoryBinaryData(null, baos.toByteArray()), vi);
            }
            if (asset instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : SupportedLocalesProvider.get().getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                Repository.get().updateCaptions(asset.getClass(), asset.getId(), names, oldAsset == null);
            } else {
                Repository.get().updateCaptions(asset.getClass(), asset.getId(), asset.toString(), oldAsset == null);
            }
            return;
        }
        StorageRegistry.get().getAdvices().get(idx).onSave(asset, (asset2) -> saveAsset(asset2, createNewVersion, comment, StorageRegistry.get().getAdvices(), ctx, idx + 1));
    }


    private <A extends BaseIntrospectableObject> AggregatedData buildAggreagatedData(A object) {
        var result = new AggregatedData();
        var descr = SerializableMetaRegistry.get().getEntities().get(object.getClass().getName());
        for (var prop : descr.getProperties().values()) {
            if ("true".equals(prop.getAttributes().get("use-in-text-search"))) {
                result.aggregate(object.getValue(prop.getId()));
            }
        }
        return result;
    }

    private <A extends BaseAsset> UpdateAssetContext<A> getUpdateAssetContext(A asset, TransactionContext ctx) throws Exception {
        if (asset.getVersionInfo() == null) {
            asset.setVersionInfo(new VersionInfo());
        }
        var oldAssetWrapperData = Repository.get().loadAssetWrapper((Class<A>) asset.getClass(), asset.getId());
        var revision = asset.getVersionInfo().getRevision();
        if (revision == -1) {
            revision = oldAssetWrapperData == null ? 0 : oldAssetWrapperData.getAsset().getVersionInfo().getRevision() + 1;
        }
        var oldAsset = oldAssetWrapperData == null ? null : oldAssetWrapperData.getAsset();
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


    private void withGlobalContext(RunnableWithException runnable) {
        ExceptionUtils.wrapException(() -> {
            var owner = contexts.get() == null;
            try {
                runnable.run();
            } finally {
                if (owner) {
                    contexts.remove();
                }
            }
        });
    }



    private <D extends BaseDocument> D loadDocument(Class<D> cls, long id, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var documentData = Repository.get().loadDocumentData(cls, id);
            if (documentData == null) {
                return null;
            }
            try (var is = new GZIPInputStream(new ByteArrayInputStream(documentData.getData().content()))) {
                return JsonUnmarshaller.get().unmarshal(cls, is, serializationParameters);
            }
        }
        return StorageRegistry.get().getAdvices().get(idx).onLoadDocument(cls, id, forModification, (cls2, id2, forModificationInt2) ->
                loadDocument(cls2, id2, forModificationInt2, StorageRegistry.get().getAdvices(), idx + 1));
    }

    public <I extends BaseIdentity> String getCaption(Class<I> type, long id, Locale currentLocale) {
        return ExceptionUtils.wrapException(() ->{
            if(isLocalizable(type)){
                return Repository.get().getCaption(type, id, currentLocale);
            }
            return Repository.get().getCaption(type, id);
        });
    }

    private boolean isLocalizable(Class<?> type){
        var docDescr = SerializableMetaRegistry.get().getEntities().get(type.getName());
        return docDescr.getAttributes().get("localizable-caption-expression")  != null;
    }
    private record UpdateAssetContext<A extends BaseAsset>(RepositoryAssetWrapper<A> oldAsset,
                                                           OperationContext<A> operationContext) {
    }

    private record UpdateDocumentContext<D extends BaseDocument>(RepositoryObjectData oldDocument,
                                                                 ObjectFactoryWithException<RepositoryObjectData> previousVersionFactory,
                                                                 OperationContext<D> operationContext) {
    }

    public static Storage get(){
        return Environment.getPublished(Storage.class);
    }
}
