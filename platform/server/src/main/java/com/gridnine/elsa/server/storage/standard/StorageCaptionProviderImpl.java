/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.standard;

import com.gridnine.elsa.common.config.Configuration;
import com.gridnine.elsa.common.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.CaptionProvider;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.common.utils.TextUtils;
import com.gridnine.elsa.server.cache.CacheManager;
import com.gridnine.elsa.server.cache.CacheMetadataProvider;
import com.gridnine.elsa.server.cache.CachedValue;
import com.gridnine.elsa.server.cache.KeyValueCache;
import com.gridnine.elsa.server.storage.Storage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class StorageCaptionProviderImpl implements CaptionProvider {

    private final Map<String, KeyValueCache<Long, String>> captionsCache = new ConcurrentHashMap<>();

    private final Map<String, KeyValueCache<Long, Map<Locale, String>>> localizedCaptionsCache = new ConcurrentHashMap<>();

    private final String nullString = TextUtils.generateUUID();

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public String getCaption(EntityReference<?> ref) {
        if(CacheMetadataProvider.get().isCacheCaption(ref.getType())){
            var cache = getOrCreateCaptionCache(captionsCache, String.class);
            var oldValue = cache.get(ref.getId());
            if (oldValue != null && oldValue.value() != null) {
                return oldValue.value().equals(nullString) ? null : oldValue.value();
            }
            var ar = Storage.get().getCaption(ref.getType(), ref.getId(), LocaleUtils.getCurrentLocale());
            var newValue = new CachedValue<>(System.nanoTime(), ar == null ? nullString : ar);
            cache.replace(ref.getId(), oldValue, newValue);
            return Objects.equals(newValue.value(), nullString) ? null : newValue.value();
        }
        if(CacheMetadataProvider.get().isCacheLocalizedCaption(ref.getType())){
            var cache = getOrCreateCaptionCache((Map) localizedCaptionsCache, Map.class);
            var oldValue = (CachedValue<Map<Locale, String>>)cache.get(ref.getId());
            if (oldValue != null && oldValue.value() != null) {
                return oldValue.value().get(LocaleUtils.getCurrentLocale());
            }
            var res = new HashMap<Locale, String>();
            SupportedLocalesProvider.get().getSupportedLocales().forEach(loc ->{
                var ar = Storage.get().getCaption(ref.getType(), ref.getId(), loc);
                res.put(loc, ar);
            });
            var newValue = new CachedValue<>(System.nanoTime(), res);
            cache.replace(ref.getId(), oldValue, newValue);
            return res.get(LocaleUtils.getCurrentLocale());
        }
        return ref.getCaption();
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    <I extends BaseIdentity> void invalidateCaptionsCach(Class<I> cls, long id) {
        if(CacheMetadataProvider.get().isCacheCaption(cls)){
            getOrCreateCaptionCache(captionsCache, String.class).put(id, new CachedValue<>(System.nanoTime(), null));
            return;
        }
        if(CacheMetadataProvider.get().isCacheLocalizedCaption(cls)){
            getOrCreateCaptionCache((Map)localizedCaptionsCache, Map.class).put(id, new CachedValue<>(System.nanoTime(), null));
        }
    }
    private <D> KeyValueCache<Long, D> getOrCreateCaptionCache(Map<String, KeyValueCache<Long, D>> caches, Class<D> cls) {
        var cache = caches.get(cls.getName());
        if (cache == null) {
            var className = cls.getName();
            var capacityStr = Configuration.get().getValue("cache.caption.capacity.%s".formatted(className));
            if (capacityStr == null) {
                capacityStr = Configuration.get().getValue("cache.caption.capacity.default", "10000");
            }
            var capacity = Integer.parseInt(capacityStr);
            var expirationInSecondsStr = Configuration.get().getValue("cache.caption.expiration.%s".formatted(className));
            if (expirationInSecondsStr == null) {
                expirationInSecondsStr = Configuration.get().getValue("cache.caption.expiration.default", "3600");
            }
            var expirationInSeconds = Integer.parseInt(expirationInSecondsStr);
            cache = CacheManager.get().createKeyValueCache(Long.class, cls, "caption_%s".formatted(className), capacity, expirationInSeconds);
            caches.put(className, cache);
        }
        return cache;
    }
}
