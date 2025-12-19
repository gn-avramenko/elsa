package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.admin.common.RenderersRegistry;
import com.gridnine.platform.elsa.admin.domain.AclCondition;
import com.gridnine.platform.elsa.admin.domain.AclEntry;
import com.gridnine.platform.elsa.admin.domain.Restriction;
import com.gridnine.platform.elsa.admin.web.common.RestrictionsEditor;
import com.gridnine.platform.elsa.common.core.search.JunctionCriterion;
import com.gridnine.platform.elsa.common.core.search.NotCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AclEngine {

    private final Map<String, AclMetadataElement> inverseMap = new HashMap<>();

    @Autowired(required = false)
    private List<AclHandler<?>> handlers;

    private final Map<String, AclHandler<?>> handlersMap = new HashMap<>();

    @Autowired
    private RenderersRegistry renderersRegistry;

    public AclMetadataElement getNode(String nodeId) {
        return inverseMap.get(nodeId);
    }

    public void addNode(String parentId, AclMetadataElement element) {
        if(parentId == null){
            inverseMap.put(element.getId(), element);
            return;
        }
        element.setParentId(parentId);
        var elm = inverseMap.get(parentId);
        elm.getChildren().add(element);
        if(element.getId() != null){
            inverseMap.put(element.getId(), element);
        }
    }
    @PostConstruct
    public void init() throws Exception {
        if (handlers != null) {
            for (AclHandler<?> handler : handlers.stream().sorted(Comparator.comparing(AclHandler::getPriority)).toList()) {
                handler.updateAclMetadata(null, null, this);
                handlersMap.put(handler.getId(), handler);
            }
        }
    }

    public void applyAcl(String nodeId, Object aclObject, List<List<AclEntry>> acls, OperationUiContext context) {
        var node = inverseMap.get(nodeId);
        var proxy = new AclObjectProxy();
        proxy.setAclElement(node);
        proxy.setId(nodeId);
        node.getChildren().forEach(child -> {
            addChild(proxy, child);
        });
        var root = createParent(proxy, node);
        var handler = handlersMap.get(inverseMap.get(root.getId()).getHandlerId());
        handler.fillProperties(root, aclObject, null, this);
        acls.forEach(acl ->{
            var entries = new HashMap<String, AclEntry>();
            acl.forEach(it -> entries.put(it.getId(), it));
            applyRules(root, new HashMap<>(), entries);
            mergeActions(root);
        });
        handlersMap.get(inverseMap.get(nodeId).getHandlerId()).applyResults(proxy, aclObject, null, this, context);
    }

    private void mergeActions(AclObjectProxy root) {
        var handler =  handlersMap.get(inverseMap.get(root.getId()).getHandlerId());
        handler.mergeActions(root, null);
        root.getCurrentActions().clear();
        root.getChildren().forEach(this::mergeActions);
    }

    private void applyRules(AclObjectProxy root, Map<String, Object> parentActions, Map<String, AclEntry> entries) {
        var handler =  handlersMap.get(inverseMap.get(root.getId()).getHandlerId());
        var entry = entries.get(root.getId());
        boolean rulesApplied = false;
        if(entry != null){
            for(var rule : entry.getRules()) {
                if(rule.getConditions().isEmpty() || rule.getConditions().stream().allMatch(it-> match(it, root))){
                   handler.applyActions(root, null, rule.getActions(),  this, parentActions);
                   rulesApplied = true;
                }
            }
        }
        if(!rulesApplied){
            handler.applyActions(root, null, List.of(),  this, parentActions);
        }
        root.getChildren().forEach(child -> {
            applyRules(child, root.getCurrentActions(), entries);;
        });
    }

    private boolean match(AclCondition condition, AclObjectProxy root) {
        switch (condition.getConditionType()){
            case SIMPLE -> {
                var propValue = root.getProperties().get(condition.getPropertyId());
                var restrValue = condition.getValue();
                var restrictionRenderer = renderersRegistry.getRestrictionRenderer(root.getAclElement().getProperties().stream().filter(
                        it -> it.getId().equals(condition.getPropertyId())).findFirst().get().getRestrictionRendererId());
                return restrictionRenderer.match(propValue, restrValue, condition.getConditionId());
            }
            case OR -> {
                return condition.getNestedConditions().isEmpty() || condition.getNestedConditions().stream().anyMatch(it -> match(it, root));
            }
            case NOT -> {
                return condition.getNestedConditions().isEmpty() || condition.getNestedConditions().stream().noneMatch(it -> match(it, root));
            }
            case AND -> {
                return condition.getNestedConditions().isEmpty() || condition.getNestedConditions().stream().allMatch(it -> match(it, root));
            }
        };
        return false;
    }

    private AclObjectProxy createParent(AclObjectProxy proxy, AclMetadataElement node) {
        if(node.getParentId() == null){
            return proxy;
        }
        var parent = inverseMap.get(node.getParentId());
        var result = new AclObjectProxy();
        result.setAclElement(parent);
        result.setId(parent.getId());
        result.getChildren().add(proxy);
        proxy.setParent(result);
        return createParent(result, parent);
    }

    private void addChild(AclObjectProxy proxy, AclMetadataElement child) {
        var item = new AclObjectProxy();
        item.setAclElement(child);
        item.setId(child.getId());
        proxy.getChildren().add(item);
        item.setParent(proxy);
        child.getChildren().forEach(child2 -> {
            addChild(item, child2);
        });
    }

    public AclHandler getHandler(String handlerId) {
        return handlersMap.get(handlerId);
    }

    public SearchCriterion toSearchCriterion(List<Restriction> restrictions, List<RestrictionsEditor.RestrictionPropertyMetadata> properties) {
        if(restrictions == null || restrictions.isEmpty()){
            return null;
        }
        if(restrictions.size() == 1){
            return toSearchCriterion(restrictions.get(0), properties);
        }
        return new JunctionCriterion(false, restrictions.stream().map(it -> toSearchCriterion(it, properties)).toList());

    }

    private SearchCriterion toSearchCriterion(Restriction restriction, List<RestrictionsEditor.RestrictionPropertyMetadata> properties) {
        return switch (restriction.getRestrictionType()) {
            case SIMPLE -> {
                var mtd = properties.stream().filter(it -> it.propertyId().equals(restriction.getPropertyId())).findFirst().get();
                var rr = renderersRegistry.getRestrictionRenderer(mtd.rendererId());
                yield  rr.getSearchCriterion(restriction.getPropertyId(), restriction.getConditionId(), restriction.getValue());
            }
            case OR ->
                    new JunctionCriterion(true, restriction.getNestedRestrictions().stream().map(it -> toSearchCriterion(it, properties)).toList());
            case NOT -> new NotCriterion(toSearchCriterion(restriction.getNestedRestrictions().get(0), properties));
            case AND -> new JunctionCriterion(false, restriction.getNestedRestrictions().stream().map(it -> toSearchCriterion(it, properties)).toList());
        };
    }
}
