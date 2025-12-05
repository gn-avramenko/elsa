package com.gridnine.platform.elsa.admin.utils;

import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.core.model.common.L10nMessage;
import com.gridnine.platform.elsa.common.core.model.common.Localizable;

public class LocaleUtils {
    public static Localizable createLocalizable(L10nMessage name, Localizer localizer) {
        return  locale -> localizer.toString(name.getBundle(), name.getKey(), locale, name.getParameters().toArray());
    }
}
