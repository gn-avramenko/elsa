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

package com.gridnine.platform.elsa.core.storage.database;

import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.core.model.domain.*;
import com.gridnine.platform.elsa.common.core.search.AggregationQuery;
import com.gridnine.platform.elsa.common.core.search.SearchQuery;

import java.util.*;

public interface Database {
    <A extends BaseAsset> DatabaseAssetWrapper<A> loadAssetWrapper(Class<A> aClass, UUID id) throws Exception;

    <A extends BaseAsset> void saveAsset(DatabaseAssetWrapper<A> aDatabaseAssetWrapper, DatabaseAssetWrapper<A> oldAsset) throws Exception;

    <A extends BaseAsset> void saveAssetVersion(Class<A> aClass, UUID id, DatabaseBinaryData data, VersionInfo vi) throws Exception;

    <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery updateQuery) throws Exception;

    List<VersionInfo> getVersionsMetadata(Class<?> cls, UUID id) throws Exception;

    <A extends BaseAsset> void deleteAsset(Class<A> aClass, UUID id) throws Exception;

    DatabaseObjectData loadVersion(Class<?> cls, UUID id, int number) throws Exception;

    void deleteVersion(Class<?> cls, UUID id, int number) throws Exception;

    <A extends BaseAsset> A loadAsset(Class<A> cls, UUID id) throws Exception;

    void updateCaptions(Class<?> aClass, UUID id, LinkedHashMap<Locale, String> captions, boolean insert) throws Exception;

    void updateCaptions(Class<?> aClass, UUID id, String name, boolean insert) throws Exception;

    void deleteCaptions(Class<?> aClass, UUID id) throws Exception;

    <D extends BaseDocument> DatabaseObjectData loadDocumentData(Class<D> cls, UUID id) throws Exception;

    <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<I> searchDocuments(Class<I> projClass, SearchQuery query) throws Exception;

    <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery updateQuery) throws Exception;

    <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery updateQuery) throws Exception;

    void updateProjections(Class<BaseSearchableProjection<BaseDocument>> projectionClass, UUID id, ArrayList<DatabaseSearchableProjectionWrapper<BaseDocument, BaseSearchableProjection<BaseDocument>>> wrappers, boolean update) throws Exception;

    <D extends BaseDocument> void saveDocument(UUID id, Class<D> aClass, DatabaseObjectData obj, DatabaseObjectData oldDocument) throws Exception;

    <D extends BaseDocument> void saveDocumentVersion(Class<D> aClass, UUID id, DatabaseObjectData version, Long oldVersionDataId) throws Exception;

    <D extends BaseDocument> void deleteDocument(Class<D> aClass, UUID id, Long oid) throws Exception;

    <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit, Locale locale) throws Exception;

    <D extends BaseDocument, I extends BaseSearchableProjection<D>> void deleteProjections(Class<I> projectionClass, UUID id) throws Exception;

    <I extends BaseIdentity> String getCaption(Class<I> type, UUID id, Locale locale) throws Exception;

    <I extends BaseIdentity> String getCaption(Class<I> type, UUID id) throws Exception;

    <VA extends BaseVirtualAsset> List<VA> searchVirtualAssets(Class<VA> cls, SearchQuery updateQuery) throws Exception;

    <VA extends BaseVirtualAsset> List<List<Object>> searchVirtualAssets(Class<VA> cls, AggregationQuery updateQuery) throws Exception;
}
