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
import com.gridnine.platform.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.platform.elsa.common.core.search.AggregationQuery;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchQuery;
import com.gridnine.platform.elsa.common.core.search.SimpleCriterion;
import com.gridnine.platform.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.platform.elsa.core.storage.database.DatabaseAssetWrapper;
import com.gridnine.platform.elsa.core.storage.database.DatabaseSearchableProjectionWrapper;
import com.mongodb.client.MongoCursor;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MongoDatabase {

    private final MongoStorageCustomizer customizer;
    private final MongoTemplate mongoTemplate;

    private final MongoConverter converter;

    private final Map<String, String> collectionsMapping = new ConcurrentHashMap<>();

    public MongoDatabase(Map<String, Object> customParameters, MongoTemplate mongoTemplate,  ObjectMetadataProvidersFactory providersFactory, ReflectionFactory reflectionFactory) {
        this.mongoTemplate = mongoTemplate;
        this.customizer = (MongoStorageCustomizer) customParameters.get(MongoStorageCustomizer.KEY);
        this.converter = new MongoConverter(providersFactory, reflectionFactory);
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

    public void deleteDocument(Class<? extends BaseDocument> aClass, UUID id) {
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("id").is(id.toString())), getCollectionName(aClass));
    }

    public List<com.gridnine.platform.elsa.common.core.model.domain.VersionInfo> getVersionsMetadata(Class<?> aClass, UUID id) {
        throw new UnsupportedOperationException();
    }

    public void deleteVersion(Class<?> aClass, UUID id, int versionNumber) {
        throw new UnsupportedOperationException();
    }

    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> void deleteProjections(Class<I> projectionClass, UUID id) {
        var query = new Query();
        query.addCriteria(Criteria.where("document.id").is(id));
        mongoTemplate.remove(query, getCollectionName(projectionClass));
    }

    public <D extends BaseDocument> void saveDocument(D doc, boolean update) {
        if (update) {
            Query query = new Query().addCriteria(Criteria.where("id").is(doc.getId()));
            mongoTemplate.findAndReplace(query, converter.toDocument(doc, mongoTemplate.findOne(query, Document.class, getCollectionName(doc.getClass()))), getCollectionName(doc.getClass()));
        } else {
            mongoTemplate.insert(converter.toDocument(doc, null), getCollectionName(doc.getClass()));
        }
    }

    public <D extends BaseDocument> void saveDocumentVersion(D oldDocument) {
        throw new UnsupportedOperationException();
    }

    public <D extends BaseDocument> D loadDocument(Class<D> aClass, UUID id) {
        return converter.fromDocument(mongoTemplate.findOne(new Query().addCriteria(Criteria.where("id").is(id)), Document.class, getCollectionName(aClass)), aClass);
    }

    public void updateProjections(Class<BaseSearchableProjection<BaseDocument>> projectionClass, UUID id, ArrayList<DatabaseSearchableProjectionWrapper<BaseDocument, BaseSearchableProjection<BaseDocument>>> wrappers, boolean update) {
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("document.id").is(id)), getCollectionName(projectionClass));
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

    public <A extends BaseAsset> A loadAsset(Class<A> cls, UUID id) {
        return converter.fromDocument(mongoTemplate.findOne(new Query().addCriteria(Criteria.where("id").is(id.toString())), Document.class, getCollectionName(cls)), cls);
    }

    public <A extends BaseIdentity> A loadVersion(Class<A> cls, UUID id, int version) {
        throw new UnsupportedOperationException();
    }

    public void deleteAsset(Class<? extends BaseAsset> aClass, UUID id) {
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("id").is(id.toString())), getCollectionName(aClass));
    }

    public <A extends BaseAsset> void saveAsset(DatabaseAssetWrapper<A> aDatabaseAssetWrapper, A oldAsset) {
        if (oldAsset != null) {
            Query query = new Query().addCriteria(Criteria.where("id").is(aDatabaseAssetWrapper.getAsset().getId().toString()));
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
}
