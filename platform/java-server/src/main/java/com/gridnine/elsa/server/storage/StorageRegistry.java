/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.core.model.common.HasPriority;
import com.gridnine.elsa.core.model.domain.BaseDocument;
import com.gridnine.elsa.core.model.domain.BaseProjection;
import com.gridnine.elsa.core.search.DynamicCriterionHandler;
import com.gridnine.elsa.meta.config.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StorageRegistry {

    private final Set<DynamicCriterionHandler<Object>> dynamicCriterionHandlers = new HashSet<>();

    public Set<DynamicCriterionHandler<Object>> getDynamicCriterionHandlers() {
        return dynamicCriterionHandlers;
    }


    public final Map<String,List<ProjectionHandler<BaseDocument, BaseProjection<BaseDocument>>>> projectionHandlers = new ConcurrentHashMap<>();


    private final List<StorageAdvice> advices = new ArrayList<>();

    private final List<StorageInterceptor> interceptors = new ArrayList<>();

    public synchronized void register(StorageAdvice advice){
        advices.add(advice);
        advices.sort(Comparator.comparingDouble(HasPriority::getPriority));
    }

    public synchronized<D extends BaseDocument, I extends BaseProjection<D>> void register(ProjectionHandler<D,I> projectionHandler){
        String className = projectionHandler.getDocumentClass().getName();
        projectionHandlers.computeIfAbsent(className, (cn) -> new ArrayList<>()).add((ProjectionHandler<BaseDocument, BaseProjection<BaseDocument>>) projectionHandler);
    }

    public List<ProjectionHandler<BaseDocument, BaseProjection<BaseDocument>>> getProjectionHandlers(Class<?> documentClass) {
        List<ProjectionHandler<BaseDocument, BaseProjection<BaseDocument>>> handlers = projectionHandlers.get(documentClass.getName());
        return handlers == null? Collections.emptyList(): handlers;
    }

    public synchronized void register(StorageInterceptor interceptor){
        interceptors.add(interceptor);
        interceptors.sort(Comparator.comparingDouble(HasPriority::getPriority));
    }

    public List<StorageInterceptor> getInterceptors() {
        return interceptors;
    }

    public List<StorageAdvice> getAdvices() {
        return advices;
    }

    public synchronized void register(DynamicCriterionHandler<?> handler){
        dynamicCriterionHandlers.add((DynamicCriterionHandler<Object>) handler);
    }


    public static StorageRegistry get(){
        return Environment.getPublished(StorageRegistry.class);
    }
}
