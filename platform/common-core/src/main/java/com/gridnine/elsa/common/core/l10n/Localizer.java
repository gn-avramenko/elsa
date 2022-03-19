/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.l10n;

import com.gridnine.elsa.common.core.model.common.L10nMessage;
import com.gridnine.elsa.common.core.model.domain.CachedCaptionProvider;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.utils.LocaleUtils;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

@Component
public class Localizer {
    @Autowired
    private L10nMetaRegistry l10nRegistry;

    @Autowired
    private CachedCaptionProvider captionProvider;

    @Autowired
    public DomainMetaRegistry domainMetaRegistry;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");

    public String toString(String bundleId, String key, Object... params){
        var result = key;
        if(result == null){
            return null;
        }
        var bundle = l10nRegistry.getBundles().get(bundleId);
        var messageDescription = bundle == null? null : bundle.getMessages().get(result);
        if(messageDescription != null){
            result = LocaleUtils.getLocalizedName(messageDescription.getDisplayNames(), result);
        }
        var size = params.length;
        for(int n = 0; n < size;n++){
            result = result.replace("{%s}".formatted(n), toString(params[n]));
        }
        return result;
    }

    public String toString(L10nMessage message){
        return toString(message.getBundle(), message.getKey(), message.getParameters().toArray());
    }
    private String toString(Object value) {
        if(value instanceof Collection<?> c){
            return StringUtils.join(c.stream().map(this::toStringInternal).toList(), ",");
        }
        return toStringInternal(value);
    }
    private String toStringInternal(Object value) {
        if(value == null){
            return "???";
        }
        if(value instanceof EntityReference<?> er){
            return captionProvider.getCaption(er);
        }
        if(value instanceof Enum<?> en){
            var res = getLocalizedName(domainMetaRegistry.getEnums(), en);
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

    private String getLocalizedName(Map<String, EnumDescription> descriptions, Enum<?> value) {
        var ed = descriptions.get(value.getClass().getName()) ;
        if(ed == null){
            return null;
        }
        var eid = ed.getItems().get(value.name());
        if(eid == null){
            return value.name();
        }
        return LocaleUtils.getLocalizedName(eid.getDisplayNames(), value.name());
    }
}
