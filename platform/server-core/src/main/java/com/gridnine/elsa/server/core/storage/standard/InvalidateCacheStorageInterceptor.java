/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.standard;

import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.model.domain.BaseDocument;
import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.server.core.cache.CacheManager;
import com.gridnine.elsa.server.core.cache.CacheMetadataProvider;
import com.gridnine.elsa.server.core.storage.OperationContext;
import com.gridnine.elsa.server.core.storage.SearchableProjectionHandler;
import com.gridnine.elsa.server.core.storage.StorageInterceptor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InvalidateCacheStorageInterceptor implements StorageInterceptor {

    @Autowired
    private CacheMetadataProvider cacheMetadataProvider;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ListableBeanFactory factory;

    private Map<String, List<SearchableProjectionHandler<BaseDocument, BaseSearchableProjection<BaseDocument>>>> projectionHandlers;

    @Autowired
    private CacheStorageAdvice cacheStorageAdvice;

    @Override
    public double getPriority() {
        return 100;
    }


    @Override
    public <A extends BaseAsset> void onDelete(A oldAssset, OperationContext<A> operationContext) {
        init();
        if (cacheMetadataProvider.isCacheResolve(oldAssset.getClass())) {
            operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(oldAssset.getClass(), oldAssset.getId()));
        }
        {
            var props = cacheMetadataProvider.getFindCacheProperties(oldAssset.getClass());
            if (!props.isEmpty()) {
                for (var prop : props) {
                    var value = oldAssset.getValue(prop);
                    operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                            -> cacheStorageAdvice.invalidateFindCache(oldAssset.getClass(), prop, value));
                }
            }
        }
        {
            var props = cacheMetadataProvider.getGetAllCacheProperties(oldAssset.getClass());
            if (!props.isEmpty()) {
                for (var prop : props) {
                    var value = oldAssset.getValue(prop);
                    operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                            -> cacheStorageAdvice.invalidateGetAllCache(oldAssset.getClass(), prop, value));
                }
            }
        }
    }

    @Override
    public <D extends BaseDocument> void onDelete(D document, OperationContext<D> operationContext) throws Exception {
        init();
        if (cacheMetadataProvider.isCacheResolve(document.getClass())) {
            operationContext.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(document.getClass(), document.getId()));
        }
        for (var handler : projectionHandlers.get(document.getClass().getName())) {
            {
                var props = cacheMetadataProvider.getFindCacheProperties(handler.getProjectionClass());
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
                var props = cacheMetadataProvider.getGetAllCacheProperties(handler.getProjectionClass());
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
    }

    @Override
    public <A extends BaseAsset> void onSave(A asset, OperationContext<A> context) {
        init();
        if (cacheMetadataProvider.isCacheResolve(asset.getClass())) {
            context.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(asset.getClass(), asset.getId()));
        }
        {
            var props = cacheMetadataProvider.getFindCacheProperties(asset.getClass());
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
            var props = cacheMetadataProvider.getGetAllCacheProperties(asset.getClass());
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
    }

    @Override
    public <D extends BaseDocument> void onSave(D doc, OperationContext<D> context) throws Exception {
        init();
        if (cacheMetadataProvider.isCacheResolve(doc.getClass())) {
            context.globalContext().getContext().getPostCommitCallbacks().add(()
                    -> cacheStorageAdvice.invalidateResolveCache(doc.getClass(), doc.getId()));
        }
        for (var handler : projectionHandlers.get(doc.getClass().getName())) {
            var findProps = cacheMetadataProvider.getFindCacheProperties(handler.getProjectionClass());
            var getAllProps = cacheMetadataProvider.getGetAllCacheProperties(handler.getProjectionClass());
            var props = new HashSet<String>();
            props.addAll(findProps);
            props.addAll(getAllProps);
            if (!props.isEmpty()) {
                var newProjections = new ArrayList<>(handler.createProjections(doc, props));
                var oldProjections = context.localContext().getOldObject() == null ?
                        Collections.<BaseSearchableProjection<BaseDocument>>emptyList() :
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
    }

    private void init() {
        if (projectionHandlers == null) {
            projectionHandlers = new ConcurrentHashMap<>();
            factory.getBeansOfType(SearchableProjectionHandler.class).values().forEach(h -> {
                var lst = projectionHandlers.computeIfAbsent(h.getDocumentClass().getName(), k -> new ArrayList<>());
                lst.add(h);
            });
        }
    }
}
