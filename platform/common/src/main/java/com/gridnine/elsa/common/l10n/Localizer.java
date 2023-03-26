/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.l10n;


import com.gridnine.elsa.common.model.common.CaptionProvider;
import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.common.utils.TextUtils;
import com.gridnine.elsa.common.model.common.L10nMessage;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;


public class Localizer {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");

    public String toString(String bundleId, String key, Locale locale, Object... params){
        var result = key;
        if(result == null){
            return null;
        }
        var bundle = L10nMetaRegistry.get().getBundles().get(bundleId);
        var messageDescription = bundle == null? null : bundle.getMessages().get(result);
        if(messageDescription != null){
            result = LocaleUtils.getLocalizedName(messageDescription.getDisplayNames(), locale, result);
        }
        var size = params.length;
        for(int n = 0; n < size;n++){
            result = result.replace("{%s}".formatted(n), toString(params[n]));
        }
        return result;
    }

    public String toString(L10nMessage message){
        return toString(message.getBundle(), message.getKey(), null, message.getParameters().toArray());
    }

    public String toString(Object value) {
        return toString(value, null);
    }

    public String toString(Object value, Locale locale) {
        if(value instanceof Collection<?> c){
            return TextUtils.join(c.stream().map(it -> toStringInternal(it, locale)).toList(), ",");
        }
        return toStringInternal(value, locale);
    }
    private String toStringInternal(Object value, Locale loc) {
        if(value == null){
            return "???";
        }
        if(value instanceof EntityReference<?> er){
            return CaptionProvider.get().getCaption(er);
        }
        if(value instanceof Enum<?> en){
            var res = getLocalizedName(SerializableMetaRegistry.get().getEnums(), en, loc);
            return res == null? en.name(): res;
        }
        if(value instanceof LocalDate ld){
            return dateFormatter.format(ld);
        }
        if(value instanceof LocalDateTime ldt){
            return dateTimeFormatter.format(ldt);
        }
        return value.toString();
    }

    private String getLocalizedName(Map<String, EnumDescription> descriptions, Enum<?> value, Locale loc) {
        var ed = descriptions.get(value.getClass().getName()) ;
        if(ed == null){
            return null;
        }
        var eid = ed.getItems().get(value.name());
        if(eid == null){
            return value.name();
        }
        return LocaleUtils.getLocalizedName(eid.getDisplayNames(), loc, value.name());
    }

    public static Localizer get(){
        return Environment.getPublished(Localizer.class);
    }
}
