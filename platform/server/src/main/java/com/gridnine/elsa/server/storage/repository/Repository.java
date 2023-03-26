/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository;

import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.domain.BaseAsset;
import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.BaseProjection;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.model.domain.VersionInfo;
import com.gridnine.elsa.common.search.AggregationQuery;
import com.gridnine.elsa.common.search.SearchQuery;
import com.gridnine.elsa.meta.config.Environment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public interface Repository {

    <A extends BaseAsset> RepositoryAssetWrapper<A> loadAssetWrapper(Class<A> aClass, long id) throws Exception;

    <A extends BaseAsset> void saveAsset(RepositoryAssetWrapper<A> aDatabaseAssetWrapper, RepositoryAssetWrapper<A> oldAsset) throws Exception;

    <A extends BaseAsset> void saveAssetVersion(Class<A> aClass, long id, RepositoryBinaryData data, VersionInfo vi) throws Exception;

    <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery updateQuery) throws Exception;

    List<VersionInfo> getVersionsMetadata(Class<?> cls, long id) throws Exception;

    <A extends BaseAsset> void deleteAsset(Class<A> aClass, long id) throws Exception;

    RepositoryObjectData loadVersion(Class<?> cls, long id, int number) throws Exception;

    void deleteVersion(Class<?> cls, long id, int number) throws Exception;

    <A extends BaseAsset> A loadAsset(Class<A> cls, long id) throws Exception;

    void updateCaptions(Class<?> aClass, long id, LinkedHashMap<Locale, String> captions, boolean insert) throws Exception;

    void updateCaptions(Class<?> aClass, long id, String name, boolean insert) throws Exception;

    void deleteCaptions(Class<?> aClass, long id) throws Exception;

    <D extends BaseDocument> RepositoryObjectData loadDocumentData(Class<D> cls, long id) throws  Exception;

    <D extends BaseDocument, I extends BaseProjection<D>> List<I> searchDocuments(Class<I> projClass, SearchQuery query) throws Exception;

    <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery updateQuery) throws  Exception;

    <D extends BaseDocument, I extends BaseProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery updateQuery) throws Exception;

    void updateProjections(Class<BaseProjection<BaseDocument>> projectionClass, long id, ArrayList<RepositoryProjectionWrapper<BaseDocument, BaseProjection<BaseDocument>>> wrappers, boolean update) throws Exception;

    <D extends BaseDocument> void saveDocument(long id, Class<D> aClass, RepositoryObjectData obj, RepositoryObjectData oldDocument) throws Exception;

    <D extends BaseDocument> void saveDocumentVersion(Class<D> aClass, long id, RepositoryObjectData version, Long oldVersionDataId) throws Exception;

    <D extends BaseDocument> void deleteDocument(Class<D> aClass, long id, Long oid) throws Exception;

    <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit, Locale locale) throws Exception;

    <D extends BaseDocument, I extends BaseProjection<D>> void deleteProjections(Class<I> projectionClass, long id) throws Exception;

    <I extends BaseIdentity> String getCaption(Class<I> type, long id, Locale locale) throws Exception;

    <I extends BaseIdentity> String getCaption(Class<I> type, long id) throws Exception;

    static Repository get(){
        return LazyHolder.INSTANCE;
    }

    class LazyHolder {
        private static final Repository INSTANCE = Environment.getPublished(Repository.class);
    }

}

