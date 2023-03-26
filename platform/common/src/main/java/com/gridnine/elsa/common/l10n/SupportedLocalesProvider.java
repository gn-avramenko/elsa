/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.l10n;

import com.gridnine.elsa.common.config.Configuration;
import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.meta.config.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class SupportedLocalesProvider {

    private final List<Locale> supportedLocales;

    public SupportedLocalesProvider() {
        var values = Configuration.get().getValues("supported-locales");
        if(values.isEmpty()){
            values = new ArrayList<>();
            values.add("en");
            values.add("ru");
        }
        supportedLocales = values.stream().map(it -> LocaleUtils.getLocale(it, null)).collect(Collectors.toList());
    }

    public List<Locale> getSupportedLocales() {
        return supportedLocales;
    }

    public static SupportedLocalesProvider get(){
        return Environment.getPublished(SupportedLocalesProvider.class);
    }
}
