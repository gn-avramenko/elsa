/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.search.SearchQuery;
import com.gridnine.elsa.common.core.utils.CallableWithExceptionAnd3Arguments;
import com.gridnine.elsa.common.core.utils.HasPriority;
import com.gridnine.elsa.common.core.utils.RunnableWithExceptionAndArgument;

import java.util.List;
import java.util.concurrent.Callable;

public interface StorageAdvice extends HasPriority {
    default <A extends BaseAsset>  void onSave(A asset, RunnableWithExceptionAndArgument<A> callback) throws Exception{
        callback.run(asset);
    }

    default <A extends BaseAsset> List<A> onSearchAssets(Class<A> cls, SearchQuery query, boolean ignoreCache, CallableWithExceptionAnd3Arguments<List<A>, Class<A>, SearchQuery, Boolean> callback) throws Exception {
       return callback.call(cls, query, ignoreCache);
    }

    default List<VersionMetadata> onGetVersionsMetadata(Class<?> cls, Callable<List<VersionMetadata>> callback) throws Exception {
        return callback.call();
    }

    default <A extends BaseAsset> void onDeleteAsset(A asset, RunnableWithExceptionAndArgument<A> callback) throws Exception {
        callback.run(asset);
    }

    default <A extends BaseAsset> A  onLoadAssetVersion(Class<A> cls, long id, int version, CallableWithExceptionAnd3Arguments<A, Class<A>,Long, Integer> callback) throws Exception {
        return callback.call(cls, id, version);
    }

    default <A extends BaseAsset> A onLoadAsset(Class<A> cls, long id, boolean ignoreCache, CallableWithExceptionAnd3Arguments<A, Class<A>,Long, Boolean> callback) throws Exception{
        return callback.call(cls, id, ignoreCache);
    }
}
