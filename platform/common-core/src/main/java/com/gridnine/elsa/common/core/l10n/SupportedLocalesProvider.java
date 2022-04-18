/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.l10n;

import com.gridnine.elsa.common.core.utils.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SupportedLocalesProvider {

    private List<Locale> supportedLocales;

    @Autowired
    public void setValues(@Value("#{'${elsa.supportedLocales:en,ru}'.split(',')}") List<String> values) {
        supportedLocales = new ArrayList<>();
        values.forEach(value -> supportedLocales.add(LocaleUtils.getLocale(value, null)));
    }
    public List<Locale> getSupportedLocales() {
        return supportedLocales;
    }
}
