package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormBooleanFieldDescription;
import com.gridnine.webpeer.core.ui.BaseUiElement;

import java.util.Arrays;

public interface AclElementHandler<E> extends AclHandler {
    void updateAclMetadata(AclMetadataElement parent, E element, AclEngine aclEngine) throws Exception;

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
