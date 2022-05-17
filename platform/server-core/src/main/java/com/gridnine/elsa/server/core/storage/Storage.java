/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.core.lock.LockManager;
import com.gridnine.elsa.common.core.model.common.*;
import com.gridnine.elsa.common.core.model.domain.*;
import com.gridnine.elsa.common.core.search.*;
import com.gridnine.elsa.common.core.serialization.CachedObjectConverter;
import com.gridnine.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.core.serialization.SerializationParameters;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.elsa.common.core.utils.IoUtils;
import com.gridnine.elsa.common.core.utils.LocaleUtils;
import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DatabaseCollectionDescription;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.server.core.CoreL10nMessagesRegistryFactory;
import com.gridnine.elsa.server.core.auth.AuthContext;
import com.gridnine.elsa.server.core.storage.database.*;
import com.gridnine.elsa.server.core.storage.transaction.ElsaTransactionContext;
import com.gridnine.elsa.server.core.storage.transaction.ElsaTransactionManager;
import com.nothome.delta.Delta;
import com.nothome.delta.GDiffPatcher;
import com.nothome.delta.GDiffWriter;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unchecked")
public class Storage {

    @Autowired
    private ListableBeanFactory factory;

    private volatile List<StorageAdvice> advices;

    private List<StorageInterceptor> interceptors;

    @Autowired
    private ElsaTransactionManager transactionManager;

    @Autowired
    private LockManager lockManager;

    private Database database;


    private JsonUnmarshaller unmarshaller;

    private JsonMarshaller marshaller;

    private SerializationParameters serializationParameters;

    @Autowired
    private DomainMetaRegistry metaRegistry;

    private final ThreadLocal<GlobalOperationContext> contexts = new ThreadLocal<>();

    @Autowired
    private SupportedLocalesProvider localesProvider;

    @Autowired
    private CachedObjectConverter cachedObjectConverter;

    private final GDiffPatcher patcher = new GDiffPatcher();
    private final Delta delta = new Delta();

    private DynamicCriterionsUpdater criterionsUpdater;

    @Autowired
    private DatabaseFactory databaseFactory;

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    private Map<String, List<SearchableProjectionHandler<BaseDocument, BaseSearchableProjection<BaseDocument>>>> projectionHandlers;

    public <D extends BaseDocument> D loadDocument(EntityReference<D> ref, boolean forModification) {
        init();
        if (ref == null) {
            return null;
        }
        return loadDocument(ref.getType(), ref.getId(), forModification);
    }

    public <D extends BaseDocument> D loadDocument(Class<D> cls, long id, boolean forModification) {
        init();
        return ExceptionUtils.wrapException(() -> loadDocument(cls, id, forModification, advices, 0));
    }

    public <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment) {
        init();
        lockManager.withLock(asset, () -> transactionManager.withTransaction((tc) -> withGlobalContext(() ->
                saveAsset(asset, createNewVersion, comment, advices, tc, 0))));
    }

    public <A extends BaseAsset> void deleteAsset(A asset) {
        init();
        lockManager.withLock(asset, () -> transactionManager.withTransaction((tc) -> withGlobalContext(() ->
                deleteAsset(asset, advices, tc, 0))));
    }

    public List<VersionInfo> getVersionsMetadata(Class<?> cls, long id) {
        init();
        return ExceptionUtils.wrapException(() -> getVersionsMetadata(cls, id, advices, 0));
    }

    public <A extends BaseAsset> void saveAsset(A asset, String comment) {
        init();
        saveAsset(asset, false, comment);
    }

    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query, boolean forModification) {
        init();
        return ExceptionUtils.wrapException(() -> searchAssets(cls, query, forModification, advices, 0));
    }

    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query) {
        init();
        return searchAssets(cls, query, false);
    }

    public <A extends BaseAsset> A loadAssetVersion(Class<A> cls, long id, int version) {
        init();
        return ExceptionUtils.wrapException(() -> loadAssetVersion(cls, id, version, advices, 0));
    }

    public <A extends BaseAsset> A loadAsset(Class<A> cls, long id, boolean forModification) {
        init();
        return ExceptionUtils.wrapException(() -> loadAsset(cls, id, forModification, advices, 0));
    }

    public <D extends BaseDocument> D loadDocumentVersion(Class<D> cls, long id, int version) {
        init();
        return ExceptionUtils.wrapException(() -> loadDocumentVersion(cls, id, version, advices, 0));
    }

    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    EntityReference<D> findUniqueDocumentReference(Class<I> projClass, E property, T propertyValue) {
        init();
        return ExceptionUtils.wrapException(() -> findUniqueDocumentReference(projClass, property, propertyValue, advices, 0));
    }

    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    D findUniqueDocument(Class<I> projClass, E property, T propertyValue, boolean forModification) {
        init();
        return loadDocument(findUniqueDocumentReference(projClass, property, propertyValue), forModification);
    }

    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    Set<EntityReference<D>> getAllDocumentReferences(Class<I> projClass, E property, T propertyValue) {
        init();
        return ExceptionUtils.wrapException(() -> getAllDocumentReferences(projClass, property, propertyValue, advices, 0));
    }

    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    Set<D> getAllDocuments(Class<I> projClass, E property, T propertyValue, boolean forModification) {
        init();
        return getAllDocumentReferences(projClass, property, propertyValue).stream().map((it) -> loadDocument(it, forModification)).collect(Collectors.toSet());
    }

    public <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery query) {
        init();
        return ExceptionUtils.wrapException(() -> searchAssets(cls, query, advices, 0));
    }

    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<I> searchDocuments(Class<I> cls, SearchQuery query) {
        init();
        return ExceptionUtils.wrapException(() -> searchDocuments(cls, query, advices, 0));
    }

    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery query) {
        init();
        return ExceptionUtils.wrapException(() -> searchDocuments(cls, query, advices, 0));
    }

    public <D extends BaseDocument> void updateProjections(D doc, Class<?>... projectionClasses) {
        init();
        transactionManager.withTransaction((tc) -> updateProjectionsInternal(doc, true, projectionClasses));
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
        init();
        lockManager.withLock(doc, () -> transactionManager.withTransaction((tx) ->
                withGlobalContext(() -> saveDocument(doc, createNewVersion, comment, params, advices, tx, 0))));
    }

    public <D extends BaseDocument> void deleteDocument(D doc) {
        deleteDocument(doc, new DeleteDocumentParameters());
    }

    public <D extends BaseDocument> void deleteDocument(D doc, DeleteDocumentParameters params) {
        init();
        lockManager.withLock(doc, () -> transactionManager.withTransaction((tx) ->
                withGlobalContext(() -> deleteDocument(doc, params, advices, tx, 0))));
    }
    public<D extends BaseIdentity> void updateCaptions(D entity){

        updateCaptions(entity, new UpdateCaptionsParameters());
    }
    public<D extends BaseIdentity> void updateCaptions(D entity,UpdateCaptionsParameters params){
        init();
        lockManager.withLock(entity, () -> transactionManager.withTransaction((tx) ->
                withGlobalContext(() -> updateCaptions(entity, params, advices, tx, 0))));
    }

    public <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit){
        init();
        return ExceptionUtils.wrapException( () -> database.searchCaptions(cls, pattern, limit, LocaleUtils.getCurrentLocale()));
    }

    public <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> A findUniqueAsset(Class<A> cls, E property,
                                                                                                                    T propertyValue,
                                                                                                                    boolean forModification) {
        return ExceptionUtils.wrapException(() -> findUniqueAsset(cls, property, propertyValue, forModification, advices, 0));
    }

    public <T,A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> Set<A> getAllAssets(Class<A> cls, E property, T propertyValue, boolean forModification){
        return ExceptionUtils.wrapException(() ->getAllAssets(cls, property, propertyValue, forModification, advices, 0));
    }

    private <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> Set<A> getAllAssets(Class<A> cls, E property, T propertyValue, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQuery();
            query.getPreferredFields().add(BaseSearchableProjection.Fields.document);
            if (propertyValue != null) {
                query.getCriterions().add(SearchCriterion.eq(property, propertyValue));
            } else {
                query.getCriterions().add(SearchCriterion.isNull(property));
            }
            return new HashSet<>(database.searchAssets(cls, query));
        }
        return advices.get(idx).onGetAllAssets(cls, property, propertyValue, forModification, (asset2, property2, propertyValue2, forModification2) ->
                getAllAssets(asset2, property2, propertyValue2, forModification2, advices, idx + 1));
    }

    private <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> A findUniqueAsset(Class<A> cls, E property,
                                                                                                                     T propertyValue,
                                                                                                                     boolean forModification, List<StorageAdvice> advices,int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQueryBuilder().preferredFields(property).where(propertyValue == null?
                    SearchCriterion.isNull(property):
                    SearchCriterion.eq(property, propertyValue)).build();
            var lst = database.searchAssets(cls, query);
            var size = lst.size();
            if (size == 0) {
                return null;
            } else if (size == 1) {
                return lst.get(0);
            } else {
                throw Xeption.forAdmin(CoreL10nMessagesRegistryFactory
                        .Found_several_recordsMessage(cls.getName(), property.name, propertyValue == null ? null : propertyValue.toString()));
            }
        }
        return advices.get(idx).onFindUniqueAsset(cls, property, propertyValue, forModification, (cls2, property2, propertyValue2, forModification2) ->
                findUniqueAsset(cls2, property2, propertyValue2, forModification2, advices, idx + 1)
        );
    }

    private <D extends BaseIdentity> void updateCaptions(D entity, UpdateCaptionsParameters params, List<StorageAdvice> advices, ElsaTransactionContext tx, int idx) throws Exception {
        if (idx == advices.size()) {
            if (entity instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : localesProvider.getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                database.updateCaptions(entity.getClass(), entity.getId(), names, false);
            } else {
                database.updateCaptions(entity.getClass(), entity.getId(), entity.toString(), false);
            }
            return;
        }
        advices.get(idx).onUpdateCaptions(entity, params, (entity2, params2) -> updateCaptions(entity2, params2, advices, tx, idx + 1));
    }

    private <D extends BaseDocument> void deleteDocument(D document, DeleteDocumentParameters params, List<StorageAdvice> storageAdvices, ElsaTransactionContext ctx, int idx) throws Exception {
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
                for (var interceptor : interceptors) {
                    interceptor.onDelete(document, udc.operationContext);
                }
            }
            database.deleteDocument(document.getClass(), document.getId(), udc.oldDocument.getData().id());
            for (var metadata : database.getVersionsMetadata(document.getClass(), document.getId())) {
                database.deleteVersion(document.getClass(), document.getId(), metadata.getVersionNumber());
            }
            database.deleteCaptions(document.getClass(), document.getId());
            var handlers = projectionHandlers.get(document.getClass().getName());
            if (handlers != null) {
                for (var projectionHandler : handlers) {
                    database.deleteProjections(projectionHandler.getProjectionClass(), document.getId());
                }
            }
            return;
        }
        advices.get(idx).onDeleteDocument(document, params, (document2, params2) -> deleteDocument(document2, params2, advices, ctx, idx + 1));
    }

    private <D extends BaseDocument> void saveDocument(D doc, boolean createNewVersion, String comment, SaveDocumentParameters params,
                                                       List<StorageAdvice> advices, ElsaTransactionContext ctx, int idx) throws Exception {
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
                for (var interceptor : interceptors) {
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
            marshaller.marshal(doc, baos, false, serializationParameters);
            var data = baos.toByteArray();
            var baos3 = new ByteArrayOutputStream();
            try (var os = new GZIPOutputStream(baos3)) {
                IoUtils.copy(new ByteArrayInputStream(data), os);
            }
            var obj = new DatabaseObjectData();
            obj.setData(new DatabaseBinaryData(null, baos3.toByteArray()));
            obj.setComment(doc.getVersionInfo().getComment());
            obj.setModified(doc.getVersionInfo().getModified());
            obj.setModifiedBy(doc.getVersionInfo().getModifiedBy());
            obj.setRevision(doc.getVersionInfo().getRevision());
            obj.setVersionNumber(doc.getVersionInfo().getVersionNumber());
            database.saveDocument(doc.getId(), doc.getClass(), obj, context.oldDocument);
            if ((createNewVersion && context.oldDocument != null) || (context.oldDocument != null && updatePreviousVersion)) {
                var baos2 = new ByteArrayOutputStream();
                try(var writer = new GDiffWriter(baos2) ){
                    delta.compute(data, new GZIPInputStream(new ByteArrayInputStream(updatePreviousVersion? previousVersionContent
                            : context.oldDocument.getData().content())), writer);
                    writer.flush();
                }
                var version = new DatabaseObjectData();
                version.setVersionNumber(updatePreviousVersion? context.oldDocument.getVersionNumber()-1: context.oldDocument.getVersionNumber());
                version.setRevision(context.oldDocument.getRevision());
                version.setModifiedBy(context.oldDocument.getModifiedBy());
                version.setModified(context.oldDocument.getModified());
                version.setComment(context.oldDocument.getComment());
                version.setData(new DatabaseBinaryData(null, IoUtils.gzip(baos2.toByteArray())));
                database.saveDocumentVersion((Class<D>) doc.getClass(), doc.getId(), version,
                        updatePreviousVersion? context.oldDocument.getData().id() : null );
            }
            if (doc instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : localesProvider.getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                database.updateCaptions(doc.getClass(), doc.getId(), names, context.oldDocument == null);
            } else {
                database.updateCaptions(doc.getClass(), doc.getId(), doc.toString(), context.oldDocument == null);
            }
            updateProjectionsInternal(doc, context.oldDocument != null);
            return;
        }
        advices.get(idx).onSaveDocument(doc, createNewVersion, comment, params,  (doc2, createNewVersion2, comment2, params2 ) -> saveDocument(doc2, createNewVersion2, comment2, params2, advices, ctx, idx + 1));
    }

    private <D extends BaseDocument> UpdateDocumentContext<D> getUpdateDocumentContext(D doc, ElsaTransactionContext tc) throws Exception {
        var oldDocument = database.loadDocumentData((Class<D>) doc.getClass(), doc.getId());
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
                oldObject = unmarshaller.unmarshal((Class<D>) doc.getClass(), is, serializationParameters);
            }
        }
        var previousVersionContentFactory = new ObjectFactoryWithException<DatabaseObjectData>() {

            DatabaseObjectData result;

            @Override
            public DatabaseObjectData createObject() throws Exception {
                if (oldDocument == null || oldDocument.getVersionNumber() == 0) {
                    return null;
                }
                if (result == null) {
                    result = database.loadVersion(doc.getClass(), doc.getId(),
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
            standardObject = (D) cachedObjectConverter.toStandardObject(co);
        }
        var handlers = projectionHandlers.get(standardObject.getClass().getName());
        if (handlers == null) {
            return;
        }
        for (var projectionHandler : handlers) {
            if (indexClasses.length == 0 || Arrays.stream(indexClasses).anyMatch(it -> it == projectionHandler.getProjectionClass())) {
                var description = metaRegistry.getSearchableProjections().get(projectionHandler.getProjectionClass().getName());
                var projections = projectionHandler.createProjections(doc, Collections.emptySet());
                var wrappers = new ArrayList<DatabaseSearchableProjectionWrapper<BaseDocument, BaseSearchableProjection<BaseDocument>>>();
                for (var proj : projections) {
                    proj.setDocument(new EntityReference<>(doc));
                    AggregatedData data = buildAggreagatedData(proj, description);
                    wrappers.add(new DatabaseSearchableProjectionWrapper<>(
                            proj, metaRegistry, data.getAggregatedData()));
                }
                database.updateProjections(projectionHandler.getProjectionClass(), doc.getId(), wrappers, update);
            }
        }

    }

    private <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<I> searchDocuments(Class<I> cls, SearchQuery query, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.searchDocuments(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return advices.get(idx).onSearchDocuments(cls, query, (cls2, query2) ->
                searchDocuments(cls2, query2, advices, idx + 1));
    }

    private <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery query, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.searchDocuments(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return advices.get(idx).onSearchDocuments(cls, query, (cls2, query2) ->
                searchDocuments(cls2, query2, advices, idx + 1));
    }

    private <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery query, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.searchAssets(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return advices.get(idx).onSearchAssets(cls, query, (cls2, query2) ->
                searchAssets(cls2, query2, advices, idx + 1));
    }

    private <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    Set<EntityReference<D>> getAllDocumentReferences(Class<I> projClass, E property, T propertyValue, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQuery();
            query.getPreferredFields().add(BaseSearchableProjection.Fields.document);
            if (propertyValue != null) {
                query.getCriterions().add(SearchCriterion.eq(property, propertyValue));
            } else {
                query.getCriterions().add(SearchCriterion.isNull(property));
            }
            return database.searchDocuments(projClass, query).stream().map(BaseSearchableProjection::getDocument).collect(Collectors.toSet());
        }
        return advices.get(idx).onGetAllDocumentReferences(projClass, property, propertyValue, (projClass2, property2, propertyValue2) ->
                getAllDocumentReferences(projClass2, property2, propertyValue2, advices, idx + 1)
        );
    }

    private <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>>
    EntityReference<D> findUniqueDocumentReference(Class<I> projClass, E property, T propertyValue, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQuery();
            query.getPreferredFields().add(BaseSearchableProjection.Fields.document);
            if (propertyValue != null) {
                query.getCriterions().add(SearchCriterion.eq(property, propertyValue));
            } else {
                query.getCriterions().add(SearchCriterion.isNull(property));
            }
            var lst = database.searchDocuments(projClass, query);
            var size = lst.size();
            if (size == 0) {
                return null;
            } else if (size == 1) {
                return lst.get(0).getDocument();
            } else {
                throw Xeption.forAdmin(CoreL10nMessagesRegistryFactory
                        .Found_several_recordsMessage(projClass.getName(), property.name, propertyValue == null ? null : propertyValue.toString()));
            }
        }
        return advices.get(idx).onFindUniqueDocumentReference(projClass, property, propertyValue, (projClass2, property2, propertyValue2) ->
                findUniqueDocumentReference(projClass2, property2, propertyValue2, advices, idx + 1));
    }

    private <D extends BaseDocument> D loadDocumentVersion(Class<D> cls, long objectId, int versionNumber,
                                                           List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var lastVersion = database.loadDocumentData(cls, objectId);
            if (lastVersion == null) {
                throw new IllegalArgumentException("document of type %s with id %s is not found".formatted(cls.getName(), objectId));
            }
            var data = IoUtils.gunzip(lastVersion.getData().content());
            for (int n = lastVersion.getVersionNumber() - 1; n >= versionNumber; n--) {
                var versionData = database.loadVersion(cls, objectId, n);
                data = patcher.patch(data, IoUtils.gunzip(versionData.getData().content()));
            }
            return unmarshaller.unmarshal(cls, new ByteArrayInputStream(data), serializationParameters);
        }
        return advices.get(idx).onLoadDocumentVersion(cls, objectId, versionNumber, (cls2, id2, versionNumber2) ->
                loadDocumentVersion(cls2, id2, versionNumber2, advices, idx + 1));
    }

    private <A extends BaseAsset> A loadAsset(Class<A> cls, long id, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.loadAsset(cls, id);
        }
        return advices.get(0).onLoadAsset(cls, id, forModification, (cls2, id2, forModification2) ->
                loadAsset(cls2, id2, forModification2, advices, idx + 1)
        );
    }

    private <A extends BaseAsset> A loadAssetVersion(Class<A> cls, long id, int version, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var data = database.loadVersion(cls, id, version);
            var is = new GZIPInputStream(new ByteArrayInputStream(data.getData().content()));
            return unmarshaller.unmarshal(cls, is, serializationParameters);
        }
        return advices.get(idx).onLoadAssetVersion(cls, id, version, (cls2, uid2, version2) ->
                loadAssetVersion(cls2, uid2, version2, advices, idx + 1)
        );
    }

    private <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.searchAssets(cls, criterionsUpdater.updateQuery(cls.getName(), query));
        }
        return advices.get(idx).onSearchAssets(cls, query, forModification, (cls2, query2, forModification2)
                -> searchAssets(cls2, query2, forModification2, advices, idx + 1));
    }

    private List<VersionInfo> getVersionsMetadata(Class<?> cls, long id, List<StorageAdvice> storageAdvices, int idx) throws Exception {
        init();
        if (idx == storageAdvices.size()) {
            return database.getVersionsMetadata(cls, id).stream().sorted(Comparator.comparing(VersionInfo::getVersionNumber)).toList();
        }
        return advices.get(idx).onGetVersionsMetadata(cls, () -> getVersionsMetadata(cls, id, advices, idx + 1));
    }

    private <A extends BaseAsset> void deleteAsset(A asset, List<StorageAdvice> storageAdvices, ElsaTransactionContext ctx, int idx) throws Exception {
        init();
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
            for (var interceptor : interceptors) {
                interceptor.onDelete(uac.oldAsset.getAsset(), uac.operationContext);
            }
            database.deleteAsset(asset.getClass(), asset.getId());
            for (var metadata : database.getVersionsMetadata(asset.getClass(), asset.getId())) {
                database.deleteVersion(asset.getClass(), asset.getId(), metadata.getVersionNumber());
            }
            database.deleteCaptions(asset.getClass(), asset.getId());
            return;
        }
        advices.get(idx).onDeleteAsset(asset, (asset2) -> deleteAsset(asset2, advices, ctx, idx + 1));
    }

    private <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment, List<StorageAdvice> storageAdvices, ElsaTransactionContext ctx, int idx) throws Exception {
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
                asset.getVersionInfo().setValue(VersionInfo.Fields.revision, 0);
            } else {
                if (asset.getVersionInfo().getRevision() != oldAsset.getAsset().getVersionInfo().getRevision()) {
                    throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                            oldAsset.getAsset().getVersionInfo().getRevision(), asset.getVersionInfo().getRevision()
                    ));
                }
                asset.getVersionInfo().setValue(VersionInfo.Fields.revision, oldAsset.getAsset().getVersionInfo().getRevision() + 1);
            }
            var description = metaRegistry.getAssets().get(asset.getClass().getName());
            AggregatedData data = buildAggreagatedData(asset, description);
            String aggregatedData = data.getAggregatedData();
            var now = LocalDateTime.now();
            asset.getVersionInfo().setModified(now);
            asset.getVersionInfo().setModifiedBy(AuthContext.getCurrentUser());
            asset.getVersionInfo().setComment(comment);
            asset.getVersionInfo().setVersionNumber(oldAsset == null ? 0 : oldAsset.getAsset().getVersionInfo().getVersionNumber() + 1);
            database.saveAsset(new DatabaseAssetWrapper<>(asset, aggregatedData), uc.oldAsset);
            if (createNewVersion && oldAsset != null) {
                var vi = new VersionInfo();
                vi.setVersionNumber(oldAsset.getAsset().getVersionInfo().getVersionNumber());
                vi.setModifiedBy(AuthContext.getCurrentUser());
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
                database.saveAssetVersion((Class<A>) asset.getClass(), asset.getId(), new DatabaseBinaryData(null, baos.toByteArray()), vi);
            }
            if (asset instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : localesProvider.getSupportedLocales()) {
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
        var result = new AggregatedData(metaRegistry);
        for (DatabasePropertyDescription prop : description.getProperties().values()) {
            if (prop.isUseInTextSearch()) {
                result.aggregate(object.getValue(prop.getId()));
            }
        }
        for (DatabaseCollectionDescription coll : description.getCollections().values()) {
            if (coll.isUseInTextSearch()) {
                result.aggregate(object.getCollection(coll.getId()));
            }
        }
        return result;
    }

    private <A extends BaseAsset> UpdateAssetContext<A> getUpdateAssetContext(A asset, ElsaTransactionContext ctx) throws Exception {
        if (asset.getVersionInfo() == null) {
            asset.setVersionInfo(new VersionInfo());
        }
        var oldAssetWrapperData = database.loadAssetWrapper((Class<A>) asset.getClass(), asset.getId());
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


    private void init() {
        if (advices == null) {
            synchronized (this){
                if (advices == null) {
                    database = databaseFactory.getPrimaryDatabase();
                    advices = factory.getBeansOfType(StorageAdvice.class).values().stream().sorted(Comparator.comparing(StorageAdvice::getPriority)).toList();
                    interceptors = factory.getBeansOfType(StorageInterceptor.class).values().stream().sorted(Comparator.comparing(StorageInterceptor::getPriority)).toList();
                    serializationParameters = new SerializationParameters();
                    serializationParameters.setPrettyPrint(false);
                    serializationParameters.setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ONLY_NOT_CACHED);
                    serializationParameters.setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.ID);
                    serializationParameters.setEntityReferenceTypeSerializationStrategy(SerializationParameters.EntityReferenceTypeSerializationStrategy.ABSTRACT_CLASS_ID);
                    serializationParameters.setClassSerializationStrategy(SerializationParameters.ClassSerializationStrategy.ID);
                    //noinspection rawtypes
                    criterionsUpdater = new DynamicCriterionsUpdater((List) factory.getBeansOfType(DynamicCriterionHandler.class).values().stream().toList());
                    projectionHandlers = new HashMap<>();
                    marshaller = factory.getBean(JsonMarshaller.class);
                    unmarshaller = factory.getBean(JsonUnmarshaller.class);
                    factory.getBeansOfType(SearchableProjectionHandler.class).values().forEach(h -> {
                        var lst = projectionHandlers.computeIfAbsent(h.getDocumentClass().getName(), k -> new ArrayList<>());
                        lst.add(h);
                    });
                }
            }
        }
    }

    private <D extends BaseDocument> D loadDocument(Class<D> cls, long id, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var documentData = database.loadDocumentData(cls, id);
            if (documentData == null) {
                return null;
            }
            try (var is = new GZIPInputStream(new ByteArrayInputStream(documentData.getData().content()))) {
                return unmarshaller.unmarshal(cls, is, serializationParameters);
            }
        }
        return advices.get(idx).onLoadDocument(cls, id, forModification, (cls2, id2, forModificationInt2) ->
                loadDocument(cls2, id2, forModificationInt2, advices, idx + 1));
    }

    public <I extends BaseIdentity> String getCaption(Class<I> type, long id, Locale currentLocale) {
        return ExceptionUtils.wrapException(() ->{
            if(isLocalizable(type)){
                return database.getCaption(type, id, currentLocale);
            }
            return database.getCaption(type, id);
        });
    }

    private boolean isLocalizable(Class<?> type){
        var docDescr = domainMetaRegistry.getDocuments().get(type.getName());
        if(docDescr != null) {
            return docDescr.getLocalizableCaptionExpression() != null;
        }
        var assetDescr = domainMetaRegistry.getAssets().get(type.getName());
        if(assetDescr != null) {
            return assetDescr.getLocalizableCaptionExpression() != null;
        }
        return false;
    }
    private record UpdateAssetContext<A extends BaseAsset>(DatabaseAssetWrapper<A> oldAsset,
                                                           OperationContext<A> operationContext) {
    }

    private record UpdateDocumentContext<D extends BaseDocument>(DatabaseObjectData oldDocument,
                                                                 ObjectFactoryWithException<DatabaseObjectData> previousVersionFactory,
                                                                 OperationContext<D> operationContext) {
    }

}
