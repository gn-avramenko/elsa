/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo;


import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.platform.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.platform.elsa.common.core.model.domain.BaseDocument;
import com.gridnine.platform.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.platform.elsa.common.core.model.domain.CaptionProvider;
import com.gridnine.platform.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.platform.elsa.common.core.search.AggregationQuery;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchQuery;
import com.gridnine.platform.elsa.common.core.search.SimpleCriterion;
import com.gridnine.platform.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.storage.database.DatabaseAssetWrapper;
import com.gridnine.platform.elsa.core.storage.database.DatabaseSearchableProjectionWrapper;
import com.mongodb.client.MongoCursor;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MongoDatabase {

    private final MongoStorageCustomizer customizer;
    private final MongoTemplate mongoTemplate;

    private final MongoConverter converter;

    private final Map<String, String> collectionsMapping = new ConcurrentHashMap<>();



    public MongoDatabase(Map<String, Object> customParameters, MongoTemplate mongoTemplate, CaptionProvider captionProvider, ObjectMetadataProvidersFactory providersFactory, ReflectionFactory reflectionFactory, DomainMetaRegistry domainMetaRegistry) {
        this.mongoTemplate = mongoTemplate;
        this.customizer = (MongoStorageCustomizer) customParameters.get(MongoStorageCustomizer.KEY);
        this.converter = new MongoConverter(providersFactory,captionProvider, reflectionFactory);
        var types =  new ArrayList<String>(domainMetaRegistry.getAssets().keySet());
        types.addAll(domainMetaRegistry.getDocuments().keySet());
        types.addAll(domainMetaRegistry.getSearchableProjections().keySet());
        var collectionNames = types.stream().map(it -> customizer == null? reflectionFactory.getClass(it).getSimpleName().toLowerCase():
                customizer.getCollectionName(reflectionFactory.getClass(it))).filter(Objects::nonNull).toList();
        ensureAllCollectionsExists(collectionNames);
    }

    public void ensureAllCollectionsExists(List<String> collections) {
        var toCreate = new  ArrayList<String>(collections);
        toCreate.removeAll(mongoTemplate.getCollectionNames());
        toCreate.forEach(mongoTemplate::createCollection);
    }

    private String getCollectionName(Class<?> cls) {
        String className = cls.getName();
        String collection = collectionsMapping.get(className);
        if (collection != null) {
            return collection;
        }
        return collectionsMapping.computeIfAbsent(className, (name) -> {
            if (customizer != null) {
                String collectionName = customizer.getCollectionName(cls);
                if (collectionName != null) {
                    return collectionName;
                }
            }
            return cls.getSimpleName().toLowerCase();
        });
    }

    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery query) {
        if(isFindByMongoIdRequest(query)){
            return searchById(cls, query);
        }
        Query mongoQuery = converter.toMongoQuery(query, cls);
        List<A> result = new ArrayList<>();
        mongoTemplate.executeQuery(mongoQuery, getCollectionName(cls), (doc) -> result.add(converter.fromDocument(doc, cls)));
        return result;
    }

    public void deleteDocument(Class<? extends BaseDocument> aClass, String id) {
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("_id").is(new BsonString(id))), getCollectionName(aClass));
    }

    public List<com.gridnine.platform.elsa.common.core.model.domain.VersionInfo> getVersionsMetadata(Class<?> aClass, String id) {
        throw new UnsupportedOperationException();
    }

    public void deleteVersion(Class<?> aClass, String id, int versionNumber) {
        throw new UnsupportedOperationException();
    }

    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> void deleteProjections(Class<I> projectionClass, String id) {
        var query = new Query();
        query.addCriteria(Criteria.where("document").is(id));
        mongoTemplate.remove(query, getCollectionName(projectionClass));
    }

    public <D extends BaseDocument> void saveDocument(D doc, boolean update) {
        if (update) {
            Query query = new Query().addCriteria(Criteria.where("_id").is(new BsonString(doc.getId())));
            mongoTemplate.findAndReplace(query, converter.toDocument(doc, mongoTemplate.findOne(query, Document.class, getCollectionName(doc.getClass()))), getCollectionName(doc.getClass()));
        } else {
            mongoTemplate.insert(converter.toDocument(doc, null), getCollectionName(doc.getClass()));
        }
    }

    public <D extends BaseDocument> void saveDocumentVersion(D oldDocument) {
        throw new UnsupportedOperationException();
    }

    public <D extends BaseDocument> D loadDocument(Class<D> aClass, String id) {
        return converter.fromDocument(mongoTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(new BsonString(id))), Document.class, getCollectionName(aClass)), aClass);
    }

    public void updateProjections(Class<BaseSearchableProjection<BaseDocument>> projectionClass, String id, ArrayList<DatabaseSearchableProjectionWrapper<BaseDocument, BaseSearchableProjection<BaseDocument>>> wrappers, boolean update) {
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("document").is(id)), getCollectionName(projectionClass));
        for(var wrapper : wrappers){
            Document doc = converter.toDocument(wrapper.getProjection(), null);
            doc.put("aggregatedData", wrapper.getAggregatedData());
            mongoTemplate.insert(doc, getCollectionName(wrapper.getProjection().getClass()));
        }
    }

    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<I> searchDocuments(Class<I> cls, SearchQuery updateQuery) {
        if(isFindByMongoIdRequest(updateQuery)){
            return searchById(cls, updateQuery);
        }
        Query mongoQuery = converter.toMongoQuery(updateQuery, cls);
        List<I> result = new ArrayList<>();
        mongoTemplate.executeQuery(mongoQuery, getCollectionName(cls), (doc) -> result.add(converter.fromDocument(doc, cls)));
        return result;
    }

    private <C extends BaseIntrospectableObject> List<C> searchById(Class<C> cls, SearchQuery updateQuery) {
        SimpleCriterion sc = (SimpleCriterion) updateQuery.getCriterions().get(0);
        var filter = new BsonDocument();
        filter.put("_id", new BsonString((String) sc.value));
        try(MongoCursor<Document> docs = mongoTemplate.getDb().getCollection(getCollectionName(cls)).find(filter).iterator()){
            if(docs.hasNext()){
                return List.of(converter.fromDocument(docs.next(), cls));
            }
            return new ArrayList<>();
        }
    }

    private boolean isFindByMongoIdRequest(SearchQuery updateQuery) {
        if(updateQuery.getCriterions().size() != 1){
            return false;
        }
        SearchCriterion sc = updateQuery.getCriterions().get(0);
        if(sc instanceof SimpleCriterion scc){
            return "_id".equals(scc.property);
        }
        return false;
    }

    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery updateQuery) {
        throw new UnsupportedOperationException();
    }

    public <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery updateQuery) {
        throw new UnsupportedOperationException();
    }

    public <A extends BaseAsset> A loadAsset(Class<A> cls, String id) {
        return converter.fromDocument(mongoTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(new BsonString(id))), Document.class, getCollectionName(cls)), cls);
    }

    public <A extends BaseIdentity> A loadVersion(Class<A> cls, String id, int version) {
        throw new UnsupportedOperationException();
    }

    public void deleteAsset(Class<? extends BaseAsset> aClass, String id) {
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("_id").is(new BsonString(id))), getCollectionName(aClass));
    }

    public <A extends BaseAsset> void saveAsset(DatabaseAssetWrapper<A> aDatabaseAssetWrapper, A oldAsset) {
        if (oldAsset != null) {
            Query query = new Query().addCriteria(Criteria.where("_id").is(new BsonString(aDatabaseAssetWrapper.getAsset().getId())));
            Document doc = converter.toDocument(aDatabaseAssetWrapper.getAsset(), mongoTemplate.findOne(query, Document.class, getCollectionName(aDatabaseAssetWrapper.getAsset().getClass())));
            doc.put("aggregatedData", aDatabaseAssetWrapper.getAggregatedData());
            mongoTemplate.findAndReplace(query,
                    doc, getCollectionName(aDatabaseAssetWrapper.getAsset().getClass()));
        } else {
            Document doc = converter.toDocument(aDatabaseAssetWrapper.getAsset(), null);
            doc.put("aggregatedData", aDatabaseAssetWrapper.getAggregatedData());
            mongoTemplate.insert(doc, getCollectionName(aDatabaseAssetWrapper.getAsset().getClass()));
        }
    }

    public <A extends BaseAsset> void saveAssetVersion(A oldAsset) {
        throw new UnsupportedOperationException();
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void deleteCaptions(Class<? extends BaseIdentity> aClass, String id) {
        var collName = "%s-captions".formatted(getCollectionName(aClass));
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("_id").is(new BsonString(id))), collName);
    }

    public void updateCaptions(Class<? extends BaseIdentity> aClass, String id, LinkedHashMap<Locale, String> names) {
        var collName = "%s-captions".formatted(getCollectionName(aClass));
        var doc = new Document();
        for(Map.Entry<Locale, String> entry : names.entrySet()) {
            doc.put("caption%s".formatted(TextUtils.capitalize(entry.getKey().getLanguage())), entry.getValue());
        }
        doc.put("_id", new BsonString(id));
        mongoTemplate.upsert(new Query().addCriteria(Criteria.where("_id").is(new BsonString(id))), Update.fromDocument(doc), collName);
    }

    public void updateCaptions(Class<? extends BaseIdentity> aClass, String id, String caption) {
        var collName = "%s-captions".formatted(getCollectionName(aClass));
        var doc = new Document();
        doc.put("_id", new BsonString(id));
        doc.put("caption", caption);
        mongoTemplate.upsert(new Query().addCriteria(Criteria.where("_id").is(new BsonString(id))), Update.fromDocument(doc), collName);
    }
}
