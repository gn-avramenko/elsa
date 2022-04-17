/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.domain.*;
import com.gridnine.elsa.common.core.search.AggregationQuery;
import com.gridnine.elsa.common.core.search.SearchQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public interface Database {
    <A extends BaseAsset> DatabaseAssetWrapper<A> loadAssetWrapper(Class<A> aClass, long id) throws Exception;

    <A extends BaseAsset> void saveAsset(DatabaseAssetWrapper<A> aDatabaseAssetWrapper, DatabaseAssetWrapper<A> oldAsset) throws Exception;

    <A extends BaseAsset> void saveAssetVersion(Class<A> aClass, long id, BlobWrapper data, VersionInfo vi) throws Exception;

    <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery updateQuery) throws Exception;

    List<VersionInfo> getVersionsMetadata(Class<?> cls, long id) throws Exception;

    <A extends BaseAsset> void deleteAsset(Class<A> aClass, long id) throws Exception;

    ObjectData loadVersion(Class<?> cls, long id, int number) throws Exception;

    void deleteVersion(Class<?> cls, long id, int number) throws Exception;

    <A extends BaseAsset> A loadAsset(Class<A> cls, long id) throws Exception;

    void updateCaptions(Class<?> aClass, long id, LinkedHashMap<Locale, String> captions, boolean insert) throws Exception;

    void updateCaptions(Class<?> aClass, long id, String name, boolean insert) throws Exception;

    void deleteCaptions(Class<?> aClass, long id) throws Exception;

    <D extends BaseDocument> ObjectData loadDocumentData(Class<D> cls, long id) throws  Exception;

    <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<I> searchDocuments(Class<I> projClass, SearchQuery query) throws Exception;

    <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery updateQuery) throws  Exception;

    <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery updateQuery) throws Exception;

    void updateProjections(Class<BaseSearchableProjection<BaseDocument>> projectionClass, long id, ArrayList<DatabaseSearchableProjectionWrapper<BaseDocument, BaseSearchableProjection<BaseDocument>>> wrappers, boolean update) throws Exception;

    <D extends BaseDocument> void saveDocument(long id, Class<D> aClass, ObjectData obj, ObjectData oldDocument) throws Exception;

    <D extends BaseDocument> void saveDocumentVersion(Class<D> aClass, long id, ObjectData version, Long oldVersionDataId) throws Exception;

    <D extends BaseDocument> void deleteDocument(Class<D> aClass, long id, Long oid) throws Exception;

    <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit, Locale locale) throws Exception;

    <D extends BaseDocument, I extends BaseSearchableProjection<D>> void deleteProjections(Class<I> projectionClass, long id) throws Exception;
}
