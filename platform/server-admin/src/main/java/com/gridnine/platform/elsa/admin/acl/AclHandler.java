package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface AclHandler<M> {
    String getId();

    double getPriority();

    void updateAclMetadata(AclMetadataElement parent, M elementMetadata, AclEngine aclEngine) throws Exception;

    void fillProperties(AclObjectProxy root, Object aclObject, M metadata, AclEngine aclEngine);

    void applyActions(AclObjectProxy obj, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions);

    void mergeActions(AclObjectProxy root);

    void applyResults(AclObjectProxy root, Object aclObject, M metadata, AclEngine aclEngine, OperationUiContext context);

    default <T extends BaseUiElement> T getElement(Object aclObject, String fieldId, Class<T> cls) {
        return ExceptionUtils.wrapException(() -> {
                    if (aclObject instanceof BaseUiElement) {
                        var field = Arrays.stream(aclObject.getClass().getDeclaredFields()).filter(it -> it.getName().equals(fieldId)).findFirst().orElse(null);
                        if(field == null){
                            return null;
                        }
                        field.setAccessible(true);
                        return (T) field.get(aclObject);
                    }
                    return null;
                }
        );
    }
}
