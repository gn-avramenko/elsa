/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.standard;

import com.gridnine.elsa.common.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.Localizable;
import com.gridnine.elsa.common.model.domain.BaseAsset;
import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.BaseProjection;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.cache.CacheMetadataProvider;
import com.gridnine.elsa.server.storage.OperationContext;
import com.gridnine.elsa.server.storage.StorageInterceptor;
import com.gridnine.elsa.server.storage.StorageRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public class InvalidateCacheStorageInterceptor implements StorageInterceptor {

    private final CacheStorageAdvice cacheStorageAdvice;

    private final StorageCaptionProviderImpl captionProvider;

    public InvalidateCacheStorageInterceptor(CacheStorageAdvice advice, StorageCaptionProviderImpl captionProvider){
        this.captionProvider = captionProvider;
        this.cacheStorageAdvice = advice;
    }
    @Override
    public double getPriority() {
        return 100;
    }

    @Override
    public <A extends BaseAsset> void onDelete(A oldAssset, OperationContext<A> operationContext) {
        if (CacheMetadataProvider.get().isCacheResolve(oldAssset.getClass())) {
            operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(oldAssset.getClass(), oldAssset.getId()));
        }
        {
            var props = CacheMetadataProvider.get().getFindCacheProperties(oldAssset.getClass());
            if (!props.isEmpty()) {
                for (var prop : props) {
                    var value = oldAssset.getValue(prop);
                    operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                            -> cacheStorageAdvice.invalidateFindCache(oldAssset.getClass(), prop, value));
                }
            }
        }
        {
            var props = CacheMetadataProvider.get().getGetAllCacheProperties(oldAssset.getClass());
            if (!props.isEmpty()) {
                for (var prop : props) {
                    var value = oldAssset.getValue(prop);
                    operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                            -> cacheStorageAdvice.invalidateGetAllCache(oldAssset.getClass(), prop, value));
                }
            }
        }
        if(CacheMetadataProvider.get().isCacheCaption(oldAssset.getClass())){
            captionProvider.invalidateCaptionsCach(oldAssset.getClass(), oldAssset.getId());
        }
    }

    @Override
    public <D extends BaseDocument> void onDelete(D document, OperationContext<D> operationContext) throws Exception {
        if (CacheMetadataProvider.get().isCacheResolve(document.getClass())) {
            operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(document.getClass(), document.getId()));
        }
        for (var handler : StorageRegistry.get().projectionHandlers.get(document.getClass().getName())) {
            {
                var props = CacheMetadataProvider.get().getFindCacheProperties(handler.getProjectionClass());
                if (!props.isEmpty()) {
                    for (var proj : handler.createProjections(document, props)) {
                        for (var prop : props) {
                            var value = proj.getValue(prop);
                            operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                                    -> cacheStorageAdvice.invalidateFindCache(handler.getProjectionClass(), prop, value));
                        }
                    }
                }
            }
            {
                var props = CacheMetadataProvider.get().getGetAllCacheProperties(handler.getProjectionClass());
                if (!props.isEmpty()) {
                    for (var proj : handler.createProjections(document, props)) {
                        for (var prop : props) {
                            var value = proj.getValue(prop);
                            operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                                    -> cacheStorageAdvice.invalidateGetAllCache(handler.getProjectionClass(), prop, value));
                        }
                    }
                }
            }
        }
        if(CacheMetadataProvider.get().isCacheCaption(document.getClass())){
            captionProvider.invalidateCaptionsCach(document.getClass(), document.getId());
        }
    }

    @Override
    public <A extends BaseAsset> void onSave(A asset, OperationContext<A> context) {
        if (CacheMetadataProvider.get().isCacheResolve(asset.getClass())) {
            context.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(asset.getClass(), asset.getId()));
        }
        {
            var props = CacheMetadataProvider.get().getFindCacheProperties(asset.getClass());
            if (!props.isEmpty()) {
                for (var prop : props) {
                    var oldValue = context.localContext().getOldObject() == null ? null :
                            context.localContext().getOldObject().getValue(prop);
                    var newValue = asset.getValue(prop);
                    if (!Objects.equals(oldValue, newValue)) {
                        cacheStorageAdvice.invalidateFindCache(asset.getClass(), prop, oldValue);
                        cacheStorageAdvice.invalidateFindCache(asset.getClass(), prop, newValue);
                    }
                }
            }
        }
        {
            var props = CacheMetadataProvider.get().getGetAllCacheProperties(asset.getClass());
            if (!props.isEmpty()) {
                for (var prop : props) {
                    var oldValue = context.localContext().getOldObject() == null ? null :
                            context.localContext().getOldObject().getValue(prop);
                    var newValue = asset.getValue(prop);
                    if (!Objects.equals(oldValue, newValue)) {
                        cacheStorageAdvice.invalidateGetAllCache(asset.getClass(), prop, oldValue);
                        cacheStorageAdvice.invalidateGetAllCache(asset.getClass(), prop, newValue);
                    }
                }
            }
        }
        if(CacheMetadataProvider.get().isCacheCaption(asset.getClass())){
            checkCaptionChanged(asset, context.localContext().getOldObject());
        }
    }

    private <A extends BaseIdentity> void checkCaptionChanged(A asset, A oldObject) {
        if(oldObject == null){
            return;
        }
        if(SupportedLocalesProvider.get().getSupportedLocales().stream().anyMatch(it ->
                !Objects.equals(getCaption(asset, it), getCaption(oldObject, it)))){
            captionProvider.invalidateCaptionsCach(asset.getClass(), asset.getId());
        }
    }



    private <A extends BaseIdentity> Object getCaption(A oldObject, Locale it) {
        var ed = SerializableMetaRegistry.get().getEntities().get(oldObject.getClass().getName());
        return ed.getAttributes().get("serializable-caption-expression") != null ? ((Localizable) oldObject).toString(it): oldObject.toString();
    }


    @Override
    public <D extends BaseDocument> void onSave(D doc, OperationContext<D> context) throws Exception {
        if (CacheMetadataProvider.get().isCacheResolve(doc.getClass())) {
            context.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(doc.getClass(), doc.getId()));
        }
        for (var handler : StorageRegistry.get().getProjectionHandlers(doc.getClass())) {
            var findProps = CacheMetadataProvider.get().getFindCacheProperties(handler.getProjectionClass());
            var getAllProps = CacheMetadataProvider.get().getGetAllCacheProperties(handler.getProjectionClass());
            var props = new HashSet<String>();
            props.addAll(findProps);
            props.addAll(getAllProps);
            if (!props.isEmpty()) {
                var newProjections = new ArrayList<>(handler.createProjections(doc, props));
                var oldProjections = context.localContext().getOldObject() == null ?
                        Collections.<BaseProjection<BaseDocument>>emptyList() :
                        handler.createProjections(context.localContext().getOldObject(), props);
                for (var oldProj : oldProjections) {
                    var newProj = oldProj.getNavigationKey() != null ? newProjections.stream()
                            .filter(it -> oldProj.getNavigationKey().equals(it.getNavigationKey())).findFirst().orElse(null) : (
                            newProjections.size() == 1 ? newProjections.get(0) : null
                    );
                    if (newProj != null) {
                        newProjections.remove(newProj);
                    }
                    for (var prop : props) {
                        var oldValue = oldProj.getValue(prop);
                        var newValue = newProj == null ? null : newProj.getValue(prop);
                        if (!Objects.equals(oldValue, newValue)) {
                            context.globalContext().getContext().getPostCommitCallbacks().add(()
                                    -> {
                                if (findProps.contains(prop)) {
                                    cacheStorageAdvice.invalidateFindCache(handler.getProjectionClass(), prop, oldValue);
                                    cacheStorageAdvice.invalidateFindCache(handler.getProjectionClass(), prop, newValue);
                                }
                                if (getAllProps.contains(prop)) {
                                    cacheStorageAdvice.invalidateGetAllCache(handler.getProjectionClass(), prop, oldValue);
                                    cacheStorageAdvice.invalidateGetAllCache(handler.getProjectionClass(), prop, newValue);
                                }
                            });
                        }
                    }
                }
                for (var proj : newProjections) {
                    for (var prop : props) {
                        var newValue = proj.getValue(prop);
                        context.globalContext().getContext().getPostCommitCallbacks().add(()
                                -> {
                            if (findProps.contains(prop)) {
                                cacheStorageAdvice.invalidateFindCache(handler.getProjectionClass(), prop, null);
                                cacheStorageAdvice.invalidateFindCache(handler.getProjectionClass(), prop, newValue);
                            }
                            if (getAllProps.contains(prop)) {
                                cacheStorageAdvice.invalidateGetAllCache(handler.getProjectionClass(), prop, null);
                                cacheStorageAdvice.invalidateGetAllCache(handler.getProjectionClass(), prop, newValue);
                            }
                        });
                    }
                }
            }
        }
        if(CacheMetadataProvider.get().isCacheCaption(doc.getClass())){
            checkCaptionChanged(doc, context.localContext().getOldObject());
        }
    }

}
