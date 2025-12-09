/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.server.mongo;


import com.gridnine.platform.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.platform.elsa.common.core.lock.LockManager;
import com.gridnine.platform.elsa.common.core.model.common.*;
import com.gridnine.platform.elsa.common.core.model.domain.*;
import com.gridnine.platform.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.platform.elsa.common.core.search.*;
import com.gridnine.platform.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.platform.elsa.common.meta.domain.DatabaseCollectionDescription;
import com.gridnine.platform.elsa.common.meta.domain.DatabasePropertyDescription;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.auth.AuthContext;
import com.gridnine.platform.elsa.core.storage.*;
import com.gridnine.platform.elsa.core.storage.database.DatabaseAssetWrapper;
import com.gridnine.platform.elsa.core.storage.database.DatabaseSearchableProjectionWrapper;
import com.gridnine.platform.elsa.core.storage.transaction.ElsaTransactionContext;
import com.gridnine.platform.elsa.core.storage.transaction.ElsaTransactionManager;
import com.gridnine.platform.elsa.server.core.CoreL10nMessagesRegistryFactory;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class MongoStorage implements Storage {

    private final  ListableBeanFactory factory;

    private final AbstractBeanFactory abstractBeanFactory;

    private final  LockManager lockManager;

    private final SupportedLocalesProvider localesProvider;

    private DynamicCriterionsUpdater criterionsUpdater;


    private final  DomainMetaRegistry domainMetaRegistry;


    private ElsaTransactionManager transactionManager;

    private ClassMapper classMapper;

    private EnumMapper enumMapper;

    private MongoDatabase database;

    private final Map<String,Object> customParameters;

    private final MongoFacade mongoFacade;


    private final ObjectMetadataProvidersFactory metadataProvidersFactory;

    private final ReflectionFactory reflectionFactory;

    public MongoStorage(ListableBeanFactory factory,
                        LockManager lockManager,
                        DomainMetaRegistry metaRegistry,
                        SupportedLocalesProvider localesProvider,
                        MongoTemplate mongoTemplate,
                        CaptionProvider captionProvider,
                        AbstractBeanFactory abstractBeanFactory, MongoFacade mongoFacade, ObjectMetadataProvidersFactory metadataProvidersFactory, ReflectionFactory reflectionFactory, Map<String,Object> customParameters) {
        this.factory = factory;
        this.lockManager = lockManager;
        this.localesProvider = localesProvider;
        this.domainMetaRegistry = metaRegistry;
        this.abstractBeanFactory = abstractBeanFactory;
        this.customParameters = customParameters;
        this.mongoFacade = mongoFacade;
        this.metadataProvidersFactory = metadataProvidersFactory;
        this.reflectionFactory = reflectionFactory;
        transactionManager = new MongoElsaTransactionManager();
        mongoFacade.setMongoTemplate(mongoTemplate);
        database = new MongoDatabase(customParameters, mongoTemplate,  captionProvider, metadataProvidersFactory, reflectionFactory, domainMetaRegistry);
        classMapper = new ClassMapper() {
            private final AtomicInteger counter = new AtomicInteger(0);
            private final Map<Integer, String> classNames = new ConcurrentHashMap<>();

            private final Map<String, Integer> inverseClassNames = new ConcurrentHashMap<>();

            @Override
            public int getId(String name) {
                Integer id = inverseClassNames.get(name);
                if(id != null){
                    return id;
                }
                synchronized (this){
                    id = inverseClassNames.get(name);
                    if(id != null){
                        return id;
                    }
                    id = counter.incrementAndGet();
                    inverseClassNames.put(name, id);
                    classNames.put(id, name);
                    return id;
                }
            }

            @Override
            public String getName(int id) {
                String name = classNames.get(id);
                if(name != null){
                    return name;
                }
                synchronized (this){
                    return classNames.get(id);
                }
            }
        };
        enumMapper = new EnumMapper() {
            @Override
            public int getId(Enum<?> value) {
                return 1000*classMapper.getId(value.getClass().getName())+value.ordinal();
            }

            @Override
            public String getName(int id, Class<Enum<?>> cls) {
                int id2 = classMapper.getId(cls.getName());
                return Arrays.stream(cls.getEnumConstants()).filter(it -> it.ordinal() == id - id2*1000).findFirst().orElseThrow().name();
            }
        };
    }
    private volatile List<StorageAdvice> advices;

    private List<StorageInterceptor> interceptors;

    private final ThreadLocal<GlobalOperationContext> contexts = new ThreadLocal<>();


    private Map<String, List<SearchableProjectionHandler<BaseDocument, BaseSearchableProjection<BaseDocument>>>> projectionHandlers;


    @Override
    public <D extends BaseDocument> D loadDocument(EntityReference<D> ref, boolean forModification) {
        init();
        if (ref == null) {
            return null;
        }
        return loadDocument(ref.getType(), ref.getId(), forModification);
    }

    @Override
    public <D extends BaseDocument> D loadDocument(Class<D> cls, String id, boolean forModification) {
        init();
        return (D) ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> loadDocument((Class<BaseDocument>)cls, id, forModification, advices, 0), false));
    }

    @Override
    public <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment) {
        init();
        lockManager.withLock(asset, () -> transactionManager.withTransaction((tc) -> withGlobalContext(() ->
                saveAsset(asset, createNewVersion, comment, advices, tc, 0))));
    }

    @Override
    public <A extends BaseAsset> void deleteAsset(A asset) {
        init();
        lockManager.withLock(asset, () -> transactionManager.withTransaction((tc) -> withGlobalContext(() ->
                deleteAsset(asset, advices, tc, 0))));
    }

    @Override
    public List<VersionInfo> getVersionsMetadata(Class<?> cls, String id) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> getVersionsMetadata(cls, id, advices, 0), false));
    }

    @Override
    public <A extends BaseAsset> void saveAsset(A asset, String comment) {
        init();
        saveAsset(asset, false, comment);
    }

    @Override
    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query, boolean forModification) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> searchAssets((Class)cls, query, forModification, advices, 0), true));
    }

    @Override
    public <VA extends BaseVirtualAsset> List<VA> searchVirtualAssets(Class<VA> cls, SearchQuery query) {
        throw Xeption.forDeveloper("virtual assets are not supported");
    }

    @Override
    public <VA extends BaseVirtualAsset> List<List<Object>> searchVirtualAssets(Class<VA> cls, AggregationQuery query) {
        throw Xeption.forDeveloper("virtual assets are not supported");
    }

    @Override
    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query) {
        init();
        return searchAssets(cls, query, false);
    }

    @Override
    public <A extends BaseAsset> A loadAssetVersion(Class<A> cls, String id, int version) {
        init();
        return (A) ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> loadAssetVersion((Class) cls, id, version, advices, 0), false));
    }

    @Override
    public <A extends BaseAsset> A loadAsset(Class<A> cls, String id, boolean forModification) {
        init();
        return (A) ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> loadAsset((Class) cls, id, forModification, advices, 0), false));
    }

    @Override
    public <D extends BaseDocument> D loadDocumentVersion(Class<D> cls, String id, int version) {
        init();
        return (D)ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> loadDocumentVersion((Class)cls, id, version, advices, 0), false));
    }

    @Override
    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
    EntityReference<D> findUniqueDocumentReference(Class<I> projClass, E property, T propertyValue) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> findUniqueDocumentReference((Class)projClass, property, propertyValue, advices, 0), false));
    }

    @Override
    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
    D findUniqueDocument(Class<I> projClass, E property, T propertyValue, boolean forModification) {
        init();
        return loadDocument(findUniqueDocumentReference(projClass, property, propertyValue), forModification);
    }

    @Override
    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
    D findUniqueDocument(Class<I> projClass, E property, T propertyValue) {
        return this.findUniqueDocument(projClass, property, propertyValue, false);
    }

    @Override
    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
    Set<EntityReference<D>> getAllDocumentReferences(Class<I> projClass, E property, T propertyValue) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> getAllDocumentReferences((Class)projClass, property, propertyValue, advices, 0), false));
    }

    @Override
    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
    Set<D> getAllDocuments(Class<I> projClass, E property, T propertyValue, boolean forModification) {
        init();
        return getAllDocumentReferences(projClass, property, propertyValue).stream().map((it) -> loadDocument(it, forModification)).collect(Collectors.toSet());
    }

    @Override
    public <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery query) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> searchAssets((Class)cls, query, advices, 0), false));
    }

    @Override
    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<I> searchDocuments(Class<I> cls, SearchQuery query) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> searchDocuments((Class)cls, query, advices, 0), false));
    }

    @Override
    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery query) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> searchDocuments((Class)cls, query, advices, 0), false));
    }

    @Override
    public <D extends BaseDocument> void updateProjections(D doc, Class<?>... projectionClasses) {
        init();
        transactionManager.withTransaction((tc) -> updateProjectionsInternal((BaseDocument) doc, true, projectionClasses));
    }

    @Override
    public <D extends BaseDocument> void saveDocument(D doc) {
        saveDocument(doc, false, null, new SaveDocumentParameters());
    }


    @Override
    public <D extends BaseDocument> void saveDocument(D doc, String comment) {
        saveDocument(doc, false, comment, new SaveDocumentParameters());
    }


    @Override
    public <D extends BaseDocument> void saveDocument(D doc, boolean createNewVersion, String comment) {
        saveDocument(doc, createNewVersion, comment, new SaveDocumentParameters());
    }


    @Override
    public <D extends BaseDocument> void saveDocument(D doc, boolean createNewVersion, String comment, SaveDocumentParameters params) {
        init();
        lockManager.withLock(doc, () -> transactionManager.withTransaction((tx) ->
                withGlobalContext(() -> saveDocument((BaseDocument) doc, createNewVersion, comment, params, advices, tx, 0))));
    }

    @Override
    public <D extends BaseDocument> void deleteDocument(D doc) {
        deleteDocument(doc, new DeleteDocumentParameters());
    }

    @Override
    public <D extends BaseDocument> void deleteDocument(D doc, DeleteDocumentParameters params) {
        init();
        lockManager.withLock(doc, () -> transactionManager.withTransaction((tx) ->
                withGlobalContext(() -> deleteDocument((BaseDocument) doc, params, advices, tx, 0))));
    }

    @Override
    public <D extends BaseIdentity> void updateCaptions(D entity) {
        updateCaptions(entity, new UpdateCaptionsParameters());
    }

    @Override
    public <D extends BaseIdentity> void updateCaptions(D entity, UpdateCaptionsParameters params) {
        init();
        ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) ->{
            updateCaptions(entity, params, advices, tx, 0);
        }));

    }

    private <D extends BaseIdentity> void updateCaptions(D entity, UpdateCaptionsParameters params, List<StorageAdvice> advices, ElsaTransactionContext tx, int idx) throws Exception {
        if (idx == advices.size()) {
            if (entity instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : localesProvider.getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                database.updateCaptions(entity.getClass(), entity.getId(), names);
            } else {
                database.updateCaptions(entity.getClass(), entity.getId(), entity.toString());
            }
            return;
        }
        advices.get(idx).onUpdateCaptions(entity, params, (entity2, params2) -> updateCaptions(entity2, params2, advices, tx, idx + 1));
    }

    @Override
    public <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit) {
        init();
        return database.searchCaptions(cls, pattern, limit);
    }

    @Override
    public <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>> A findUniqueAsset(Class<A> cls, E property,
                                                                                                                      T propertyValue,
                                                                                                                      boolean forModification) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> findUniqueAsset(cls, property, propertyValue, forModification, advices, 0), true));
    }

    @Override
    public <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>> Set<A>
    getAllAssets(Class<A> cls, E property, T propertyValue, boolean forModification) {
        init();
        return ExceptionUtils.wrapException(() -> transactionManager.withTransaction((tx) -> getAllAssets(cls, property, propertyValue, forModification, advices, 0), false));
    }

    private <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>> Set<A> getAllAssets(Class<A> cls, E property, T propertyValue, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQuery();
            query.getCriterions().add(SearchCriterion.eq(property, propertyValue));
            return new HashSet<>(database.searchAssets((Class) cls, query));
        }
        return advices.get(idx).onGetAllAssets(cls, property, propertyValue, forModification, (asset2, property2, propertyValue2, forModification2) ->
                getAllAssets(asset2, property2, propertyValue2, forModification2, advices, idx + 1));
    }

    private <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>> A findUniqueAsset(Class<A> cls, E property,
                                                                                                                       T propertyValue,
                                                                                                                       boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            var query = new SearchQueryBuilder().where(propertyValue == null ?
                    SearchCriterion.isNull(property) :
                    SearchCriterion.eq(property, propertyValue)).build();
            var lst = database.searchAssets((Class) cls, query);
            var size = lst.size();
            if (size == 0) {
                return null;
            } else if (size == 1) {
                return (A) lst.get(0);
            } else {
                throw Xeption.forAdmin(CoreL10nMessagesRegistryFactory
                        .Found_several_recordsMessage(cls.getName(), property.name, propertyValue == null ? null : propertyValue.toString()));
            }
        }
        return advices.get(idx).onFindUniqueAsset(cls, property, propertyValue, forModification, (cls2, property2, propertyValue2, forModification2) ->
                findUniqueAsset(cls2, property2, propertyValue2, forModification2, advices, idx + 1)
        );
    }

    private <D extends BaseDocument> void deleteDocument(D document, DeleteDocumentParameters params, List<StorageAdvice> storageAdvices, ElsaTransactionContext ctx, int idx) throws Exception {
        if (idx == storageAdvices.size()) {
            var udc = getUpdateDocumentContext(document, ctx);
            if (udc.oldDocument == null) {
                throw Xeption.forDeveloper("document %s id = %s is absent in db".formatted(document.getClass().getName(), document.getId()));
            }
            if (document.getVersionInfo().getRevision() != udc.oldDocument.getVersionInfo().getRevision()) {
                throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                        udc.oldDocument.getVersionInfo().getRevision(), document.getVersionInfo().getRevision()
                ));
            }
            if (!params.isSkipInterceptors()) {
                for (var interceptor : interceptors) {
                    interceptor.onDelete(document, udc.operationContext);
                }
            }
            database.deleteDocument((Class) document.getClass(), document.getId());
            for (var metadata : database.getVersionsMetadata(document.getClass(), document.getId())) {
                database.deleteVersion(document.getClass(), document.getId(), metadata.getVersionNumber());
            }
            var handlers = projectionHandlers.get(document.getClass().getName());
            if (handlers != null) {
                for (var projectionHandler : handlers) {
                    database.deleteProjections((Class) projectionHandler.getProjectionClass(), document.getId());
                }
            }
            return;
        }
        advices.get(idx).onDeleteDocument(document, params, (document2, params2) -> deleteDocument(document2, params2, advices, ctx, idx + 1));
    }

    private <D extends BaseDocument> void saveDocument(D doc, boolean createNewVersion, String comment, SaveDocumentParameters params,
                                                       List<StorageAdvice> advices, ElsaTransactionContext ctx, int idx) throws Exception {
        if (doc.getVersionInfo() == null) {
            doc.setVersionInfo(new VersionInfo());
        }
        if (idx == advices.size()) {
            var context = getUpdateDocumentContext(doc, ctx);
            if (!params.isSkipInterceptors()) {
                for (var interceptor : interceptors) {
                    interceptor.onSave(doc, context.operationContext);
                }
            }
            var now = Instant.now();
            doc.getVersionInfo().setValue(VersionInfo.Fields.revision, context.oldDocument == null ?
                    0 : context.oldDocument.getVersionInfo().getVersionNumber() + 1);
            doc.getVersionInfo().setComment(comment);
            doc.getVersionInfo().setModified(now);
            doc.getVersionInfo().setModifiedBy(AuthContext.getCurrentUser());
            doc.getVersionInfo().setValue(VersionInfo.Fields.revision, doc.getVersionInfo().getRevision());
            doc.getVersionInfo().setVersionNumber(
                    context.oldDocument == null ? 0 : (createNewVersion ? context.oldDocument.getVersionInfo().getVersionNumber() + 1 : context.oldDocument.getVersionInfo().getVersionNumber())
            );
            var updatePreviousVersion = !createNewVersion && (context.oldDocument != null && context.oldDocument.getVersionInfo().getVersionNumber() > 0);
            database.saveDocument(doc, context.oldDocument != null);
            if ((createNewVersion && context.oldDocument != null) || (context.oldDocument != null && updatePreviousVersion)) {
                database.saveDocumentVersion(context.oldDocument);
            }
            if (doc instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : localesProvider.getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                database.updateCaptions(doc.getClass(), doc.getId(), names);
            } else {
                database.updateCaptions(doc.getClass(), doc.getId(), doc.toString());
            }
            updateProjectionsInternal(doc, context.oldDocument != null);
            return;
        }
        advices.get(idx).onSaveDocument(doc, createNewVersion, comment, params, (doc2, createNewVersion2, comment2, params2) -> saveDocument(doc2, createNewVersion2, comment2, params2, advices, ctx, idx + 1));
    }

    private <D extends BaseDocument> UpdateDocumentContext<D> getUpdateDocumentContext(D doc, ElsaTransactionContext tc) throws Exception {
        var oldDocument = database.loadDocument((Class<D>) doc.getClass(), doc.getId());
        var docRevision = doc.getVersionInfo() == null ? -1 : doc.getVersionInfo().getRevision();
        if (docRevision == -1) {
            docRevision = oldDocument == null ? 0 : oldDocument.getVersionInfo().getRevision();
        }
        if (oldDocument != null && docRevision != oldDocument.getVersionInfo().getRevision()) {
            throw Xeption.forDeveloper("revision conflict with document %s %s, ".formatted(doc.getClass().getName(), doc.getId()) +
                    "db revision = %s, operation revision %s".formatted(oldDocument.getVersionInfo().getRevision(), doc.getVersionInfo().getRevision()));
        }
       var globalContext = contexts.get();
        if (globalContext == null) {
            globalContext = new GlobalOperationContext(doc, oldDocument, tc);
            contexts.set(globalContext);
        }
        var localContext = new LocalOperationContext<>(oldDocument);
        var operationContext = new OperationContext<>(globalContext, localContext);
        return new UpdateDocumentContext<>(oldDocument, operationContext);
    }

    private <D extends BaseDocument> void updateProjectionsInternal(D doc, boolean update, Class<?>... indexClasses) throws Exception {
        var standardObject = doc;
        var handlers = projectionHandlers.get(standardObject.getClass().getName());
        if (handlers == null) {
            return;
        }
        for (var projectionHandler : handlers) {
            if (indexClasses.length == 0 || Arrays.stream(indexClasses).anyMatch(it -> it == projectionHandler.getProjectionClass())) {
                var description = domainMetaRegistry.getSearchableProjections().get(projectionHandler.getProjectionClass().getName());
                var projections = projectionHandler.createProjections(doc, Collections.emptySet());
                var wrappers = new ArrayList<DatabaseSearchableProjectionWrapper<BaseDocument, BaseSearchableProjection<BaseDocument>>>();
                for (var proj : projections) {
                    proj.setDocument(new EntityReference<>(doc));
                    if(proj.getValue("_id") == null){
                        proj.setValue("_id", new ObjectId().toHexString());
                    }
                    AggregatedData data = buildAggregatedData(proj, description);
                    wrappers.add(new DatabaseSearchableProjectionWrapper<>(
                            proj, domainMetaRegistry, data.getAggregatedData()));
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

    private <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
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

    private <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
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

    private <D extends BaseDocument> D loadDocumentVersion(Class<D> cls, String objectId, int versionNumber,
                                                           List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            database.loadVersion(cls, objectId, versionNumber);
        }
        return advices.get(idx).onLoadDocumentVersion(cls, objectId, versionNumber, (cls2, id2, versionNumber2) ->
                loadDocumentVersion(cls2, id2, versionNumber2, advices, idx + 1));
    }

    private <A extends BaseAsset> A loadAsset(Class<A> cls, String id, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.loadAsset(cls, id);
        }
        return advices.get(0).onLoadAsset(cls, id, forModification, (cls2, id2, forModification2) ->
                loadAsset(cls2, id2, forModification2, advices, idx + 1)
        );
    }

    private <A extends BaseAsset> A loadAssetVersion(Class<A> cls, String id, int version, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.loadVersion(cls, id, version);
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

    private List<VersionInfo> getVersionsMetadata(Class<?> cls, String id, List<StorageAdvice> storageAdvices, int idx) throws Exception {
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
            if (asset.getVersionInfo().getRevision() != uac.oldAsset.getVersionInfo().getRevision()) {
                throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                        uac.oldAsset.getVersionInfo().getRevision(), asset.getVersionInfo().getRevision()
                ));
            }
            for (var interceptor : interceptors) {
                interceptor.onDelete(uac.oldAsset, uac.operationContext);
            }
            database.deleteAsset(asset.getClass(), asset.getId());
            return;
        }
        advices.get(idx).onDeleteAsset(asset, (asset2) -> deleteAsset(asset2, advices, ctx, idx + 1));
    }

    private <A extends BaseAsset> void saveAsset(A asset, boolean createNewVersion, String comment, List<StorageAdvice> storageAdvices, ElsaTransactionContext ctx, int idx) throws Exception {
        init();
        if (asset.getVersionInfo() == null) {
            asset.setVersionInfo(new VersionInfo());
        }
        if (idx == storageAdvices.size()) {
            var uc = getUpdateAssetContext(asset, ctx);
            for (var interceptor : interceptors) {
                interceptor.onSave(asset, uc.operationContext());
            }
            var oldAsset = uc.oldAsset();
            if (oldAsset == null) {
                asset.getVersionInfo().setValue(VersionInfo.Fields.revision, 0);
            } else {
                if (asset.getVersionInfo() != null && oldAsset.getVersionInfo() != null && asset.getVersionInfo().getRevision() != oldAsset.getVersionInfo().getRevision()) {
                    throw new IllegalArgumentException("revision conflict: expected = %s, actual: %s".formatted(
                            oldAsset.getVersionInfo().getRevision(), asset.getVersionInfo().getRevision()
                    ));
                }
                asset.getVersionInfo().setValue(VersionInfo.Fields.revision, oldAsset.getVersionInfo() == null? 0: (oldAsset.getVersionInfo().getRevision() + 1));
            }
            var description = domainMetaRegistry.getAssets().get(asset.getClass().getName());
            AggregatedData data = buildAggregatedData(asset, description);
            String aggregatedData = data.getAggregatedData();
            var now = Instant.now();
            asset.getVersionInfo().setModified(now);
            asset.getVersionInfo().setModifiedBy(AuthContext.getCurrentUser());
            asset.getVersionInfo().setComment(comment);
            asset.getVersionInfo().setVersionNumber(oldAsset == null || oldAsset.getVersionInfo() == null? 0 : oldAsset.getVersionInfo().getVersionNumber() + 1);
            database.saveAsset(new DatabaseAssetWrapper<>(asset, aggregatedData), uc.oldAsset);
            if (createNewVersion && oldAsset != null) {
                database.saveAssetVersion(oldAsset);
            }
            if (asset instanceof Localizable lz) {
                var names = new LinkedHashMap<Locale, String>();
                for (var locale : localesProvider.getSupportedLocales()) {
                    names.put(locale, lz.toString(locale));
                }
                database.updateCaptions(asset.getClass(), asset.getId(), names);
            } else {
                database.updateCaptions(asset.getClass(), asset.getId(), asset.toString());
            }
            return;
        }
        advices.get(idx).onSave(asset, (asset2) -> saveAsset(asset2, createNewVersion, comment, advices, ctx, idx + 1));
    }


    private <A extends BaseIntrospectableObject> AggregatedData buildAggregatedData(A object, BaseSearchableDescription description) {
        var result = new AggregatedData(domainMetaRegistry);
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
        var oldAsset = database.loadAsset((Class<A>) asset.getClass(), asset.getId());
        var revision = asset.getVersionInfo().getRevision();
        if (revision == -1) {
            revision = oldAsset == null || oldAsset.getVersionInfo() == null ? 0 : oldAsset.getVersionInfo().getRevision() + 1;
        }
        if (oldAsset != null && oldAsset.getVersionInfo()!= null && revision != oldAsset.getVersionInfo().getRevision()) {
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
        return new UpdateAssetContext<>(oldAsset, operationContext);
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
            synchronized (this) {
                if (advices == null) {
                    interceptors = factory.getBeansOfType(StorageInterceptor.class).values().stream().sorted(Comparator.comparing(StorageInterceptor::getPriority)).toList();
                     //noinspection rawtypes
                    criterionsUpdater = new DynamicCriterionsUpdater((List) factory.getBeansOfType(DynamicCriterionHandler.class).values().stream().toList());
                    projectionHandlers = new HashMap<>();
                    factory.getBeansOfType(SearchableProjectionHandler.class).values().forEach(h -> {
                        var lst = projectionHandlers.computeIfAbsent(h.getDocumentClass().getName(), k -> new ArrayList<>());
                        lst.add(h);
                    });
                    advices = factory.getBeansOfType(StorageAdvice.class).values().stream().sorted(Comparator.comparing(StorageAdvice::getPriority)).toList();

                }
            }
        }
    }

    private <D extends BaseDocument> D loadDocument(Class<D> cls, String id, boolean forModification, List<StorageAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            return database.loadDocument(cls, id);
        }
        return advices.get(idx).onLoadDocument(cls, id, forModification, (cls2, id2, forModificationInt2) ->
                loadDocument(cls2, id2, forModificationInt2, advices, idx + 1));
    }

    @Override
    public <I extends BaseIdentity> String getCaption(Class<I> type, String id, Locale currentLocale) {
        return null;
    }

    @Override
    public <RP> RP performNativeOperation(CallableWithExceptionAndArgument<RP, ElsaTransactionContext> operation) {
        throw Xeption.forDeveloper("unsupported operation");
    }

    public ClassMapper getClassMapper() {
        return classMapper;
    }

    public EnumMapper getEnumMapper() {
        return enumMapper;
    }

    public ElsaTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    private record UpdateAssetContext<A extends BaseAsset>(A oldAsset, OperationContext<A> operationContext) {
    }

    private record UpdateDocumentContext<D extends BaseDocument>(D oldDocument, OperationContext<D> operationContext) {
    }

    public MongoDatabase getDatabase() {
        return database;
    }

}
