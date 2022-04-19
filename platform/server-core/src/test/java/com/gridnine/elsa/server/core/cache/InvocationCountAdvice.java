/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.cache;

import com.gridnine.elsa.common.core.model.common.CallableWithExceptionAnd3Arguments;
import com.gridnine.elsa.common.core.model.common.CallableWithExceptionAnd4Arguments;
import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.model.domain.BaseDocument;
import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.search.ArgumentType;
import com.gridnine.elsa.common.core.search.EqualitySupport;
import com.gridnine.elsa.common.core.search.FieldNameSupport;
import com.gridnine.elsa.server.core.storage.StorageAdvice;

import java.util.Set;

public class InvocationCountAdvice implements StorageAdvice {
    private int loadDocumentCount = 0;

    private int loadAssetCount = 0;

    private int findAssetCount = 0;

    private int findDocumentCount = 0;

    private int getAllDocumentsCount = 0;

    @Override
    public double getPriority() {
        return 30;
    }

    @Override
    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>>
    Set<EntityReference<D>> onGetAllDocumentReferences(Class<I> projClass, E property, T propertyValue,
                                                       CallableWithExceptionAnd3Arguments<Set<EntityReference<D>>, Class<I>, E, T> callback) throws Exception {
        getAllDocumentsCount++;
        return callback.call(projClass, property, propertyValue);
    }

    @Override
    public <T, D extends BaseDocument, I extends BaseSearchableProjection<D>, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>> EntityReference<D>
    onFindUniqueDocumentReference(Class<I> projClass, E property, T propertyValue, CallableWithExceptionAnd3Arguments<EntityReference<D>, Class<I>, E, T> callback) throws Exception {
        findDocumentCount++;
        return callback.call(projClass, property, propertyValue);
    }

    @Override
    public <T, A extends BaseAsset, E extends FieldNameSupport & EqualitySupport & ArgumentType<T>> A onFindUniqueAsset(Class<A> cls, E property, T propertyValue,
                                                                                                                        boolean forModification, CallableWithExceptionAnd4Arguments<A, Class<A>, E, T, Boolean> callbackObject) throws Exception {
        findAssetCount++;
        return callbackObject.call(cls, property, propertyValue, forModification);
    }

    @Override
    public <A extends BaseAsset> A onLoadAsset(Class<A> cls, long id, boolean forModification, CallableWithExceptionAnd3Arguments<A, Class<A>, Long, Boolean> callback) throws Exception {
        loadAssetCount++;
        return callback.call(cls, id, forModification);
    }

    @Override
    public <D extends BaseDocument> D onLoadDocument(Class<D> cls, long id, boolean forModification, CallableWithExceptionAnd3Arguments<D, Class<D>, Long, Boolean> callback) throws Exception {
        loadDocumentCount++;
        return callback.call(cls, id, forModification);
    }

    public int getLoadDocumentCount() {
        return loadDocumentCount;
    }

    public void setLoadDocumentCount(int loadDocumentCount) {
        this.loadDocumentCount = loadDocumentCount;
    }

    public int getLoadAssetCount() {
        return loadAssetCount;
    }

    public void setLoadAssetCount(int loadAssetCount) {
        this.loadAssetCount = loadAssetCount;
    }

    public int getFindAssetCount() {
        return findAssetCount;
    }

    public void setFindAssetCount(int findAssetCount) {
        this.findAssetCount = findAssetCount;
    }

    public int getFindDocumentCount() {
        return findDocumentCount;
    }

    public void setFindDocumentCount(int findDocumentCount) {
        this.findDocumentCount = findDocumentCount;
    }

    public int getGetAllDocumentsCount() {
        return getAllDocumentsCount;
    }

    public void setGetAllDocumentsCount(int getAllDocumentsCount) {
        this.getAllDocumentsCount = getAllDocumentsCount;
    }
}
