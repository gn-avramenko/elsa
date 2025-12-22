package com.gridnine.platform.elsa.admin.utils;

import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;

import java.util.Arrays;

public class ReflectionUtils {
    public static <T extends BaseUiElement> T getChild(Object aclObject, String fieldId, Class<T> cls) {
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
