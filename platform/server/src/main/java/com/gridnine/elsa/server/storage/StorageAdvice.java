/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.CallableWithExceptionAnd2Arguments;
import com.gridnine.elsa.common.model.common.CallableWithExceptionAnd3Arguments;
import com.gridnine.elsa.common.model.common.CallableWithExceptionAnd4Arguments;
import com.gridnine.elsa.common.model.common.HasPriority;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAnd2Arguments;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAnd4Arguments;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAndArgument;
import com.gridnine.elsa.common.model.domain.BaseAsset;
import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.BaseProjection;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.model.domain.VersionInfo;
import com.gridnine.elsa.common.search.AggregationQuery;
import com.gridnine.elsa.common.search.ArgumentType;
import com.gridnine.elsa.common.search.EqualitySupport;
import com.gridnine.elsa.common.search.FieldNameSupport;
import com.gridnine.elsa.common.search.SearchQuery;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public interface StorageAdvice extends HasPriority {
    default <A extends BaseAsset> void onSave(A asset, RunnableWithExceptionAndArgument<A> callback) throws Exception {
        callback.run(asset);
    }

    default <A extends BaseAsset> List<A> onSearchAssets(Class<A> cls, SearchQuery query, boolean forModification, CallableWithExceptionAnd3Arguments<List<A>, Class<A>, SearchQuery, Boolean> callback) throws Exception {
        return callback.call(cls, query, forModification);
    }

    default List<VersionInfo> onGetVersionsMetadata(Class<?> cls, Callable<List<VersionInfo>> callback) throws Exception {
        return callback.call();
    }

    default <A extends BaseAsset> void onDeleteAsset(A asset, RunnableWithExceptionAndArgument<A> callback) throws Exception {
        callback.run(asset);
    }

    default <A extends BaseAsset> A onLoadAssetVersion(Class<A> cls, long id, int version, CallableWithExceptionAnd3Arguments<A, Class<A>, Long, Integer> callback) throws Exception {
        return callback.call(cls, id, version);
    }

    default <A extends BaseAsset> A onLoadAsset(Class<A> cls, long id, boolean forModification, CallableWithExceptionAnd3Arguments<A, Class<A>, Long, Boolean> callback) throws Exception {
        return callback.call(cls, id, forModification);
    }

    default <D extends BaseDocument> D onLoadDocument(Class<D> cls, long id, boolean forModification, CallableWithExceptionAnd3Arguments<D, Class<D>, Long, Boolean> callback) throws Exception {
        return callback.call(cls, id, forModification);
    }

    default <D extends BaseDocument> D onLoadDocumentVersion(Class<D> cls, long objectId, int versionNumber, CallableWithExceptionAnd3Arguments<D, Class<D>, Long, Integer> callback) throws Exception {
        return callback.call(cls, objectId, versionNumber);
    }

    default <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport& ArgumentType<T>> EntityReference<D>
    onFindUniqueDocumentReference(Class<I> projClass, E property, T propertyValue, CallableWithExceptionAnd3Arguments<EntityReference<D>,
            Class<I>, E, T> callback) throws Exception {
        return callback.call(projClass, property, propertyValue);
    }

    default <T, D extends BaseDocument, I extends BaseProjection<D>, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> Set<EntityReference<D>>
    onGetAllDocumentReferences(Class<I> projClass, E property, T propertyValue, CallableWithExceptionAnd3Arguments<Set<EntityReference<D>>,
            Class<I>, E, T> callback) throws Exception {
        return callback.call(projClass, property, propertyValue);
    }

    default <A extends BaseAsset> List<List<Object>> onSearchAssets(Class<A> cls, AggregationQuery query, CallableWithExceptionAnd2Arguments<List<List<Object>>, Class<A>, AggregationQuery> callback) throws Exception {
        return callback.call(cls, query);
    }

    default <D extends BaseDocument, I extends BaseProjection<D>> List<I> onSearchDocuments(Class<I> cls, SearchQuery query,
                                                                                                      CallableWithExceptionAnd2Arguments<List<I>, Class<I>, SearchQuery> callback) throws Exception {
        return callback.call(cls, query);
    }

    default <D extends BaseDocument, I extends BaseProjection<D>> List<List<Object>> onSearchDocuments(Class<I> cls, AggregationQuery query,
                                                                                                      CallableWithExceptionAnd2Arguments<List<List<Object>>, Class<I>, AggregationQuery> callback) throws Exception {
        return callback.call(cls, query);
    }

    default <D extends BaseDocument> void onSaveDocument(D doc, boolean createNewVersion, String comment, SaveDocumentParameters params,
                                                    RunnableWithExceptionAnd4Arguments<D, Boolean , String, SaveDocumentParameters> callback) throws Exception {
        callback.run(doc, createNewVersion, comment, params);
    }

    default <D extends BaseDocument> void onDeleteDocument(D document, DeleteDocumentParameters params,
                                                           RunnableWithExceptionAnd2Arguments<D,DeleteDocumentParameters> callback) throws Exception {
        callback.run(document, params);
    }

    default<D extends BaseIdentity> void onUpdateCaptions(D entity, UpdateCaptionsParameters params, RunnableWithExceptionAnd2Arguments<D,UpdateCaptionsParameters> callback) throws Exception {
        callback.run(entity, params);
    }

    default <T,A extends BaseAsset, E extends FieldNameSupport & EqualitySupport&ArgumentType<T>> A onFindUniqueAsset(Class<A> cls, E property, T propertyValue, boolean forModification,
                                                                                                    CallableWithExceptionAnd4Arguments<A,
                                                                                                                                                                                                                Class<A>, E, T, Boolean> callbackObject) throws Exception {
        return callbackObject.call(cls, property, propertyValue, forModification);
    }

    default <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport& ArgumentType<T>> Set<A> onGetAllAssets(Class<A> cls, E property, T propertyValue, boolean forModification, CallableWithExceptionAnd4Arguments<Set<A>,
            Class<A>, E, T, Boolean> callbackObject) throws Exception {
        return callbackObject.call(cls, property, propertyValue, forModification);
    }
}
