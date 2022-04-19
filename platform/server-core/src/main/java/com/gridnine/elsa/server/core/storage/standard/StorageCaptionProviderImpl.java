/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.standard;

import com.gridnine.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.domain.CaptionProvider;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.utils.LocaleUtils;
import com.gridnine.elsa.common.core.utils.TextUtils;
import com.gridnine.elsa.server.core.cache.CacheManager;
import com.gridnine.elsa.server.core.cache.CacheMetadataProvider;
import com.gridnine.elsa.server.core.cache.CachedValue;
import com.gridnine.elsa.server.core.cache.KeyValueCache;
import com.gridnine.elsa.server.core.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class StorageCaptionProviderImpl implements CaptionProvider {

    private final Map<String, KeyValueCache<Long, String>> captionsCache = new ConcurrentHashMap<>();

    private final Map<String, KeyValueCache<Long, Map<Locale, String>>> localizedCaptionsCache = new ConcurrentHashMap<>();

    @Autowired
    private Environment env;

    @Autowired
    private CacheMetadataProvider cacheMetadataProvider;

    @Autowired
    private Storage storage;

    @Autowired
    private CacheManager cacheManager;

    private final String nullString = TextUtils.generateUUID();

    @Autowired
    private SupportedLocalesProvider supportedLocalesProvider;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public String getCaption(EntityReference<?> ref) {
        if(cacheMetadataProvider.isCacheCaption(ref.getType())){
            var cache = getOrCreateCaptionCache(captionsCache, String.class);
            var oldValue = cache.get(ref.getId());
            if (oldValue != null && oldValue.value() != null) {
                return oldValue.value().equals(nullString) ? null : oldValue.value();
            }
            var ar = storage.getCaption(ref.getType(), ref.getId(), LocaleUtils.getCurrentLocale());
            var newValue = new CachedValue<>(System.nanoTime(), ar == null ? nullString : ar);
            cache.replace(ref.getId(), oldValue, newValue);
            return Objects.equals(newValue.value(), nullString) ? null : newValue.value();
        }
        if(cacheMetadataProvider.isCacheLocalizedCaption(ref.getType())){
            var cache = getOrCreateCaptionCache((Map) localizedCaptionsCache, Map.class);
            var oldValue = (CachedValue<Map<Locale, String>>)cache.get(ref.getId());
            if (oldValue != null && oldValue.value() != null) {
                return oldValue.value().get(LocaleUtils.getCurrentLocale());
            }
            var res = new HashMap<Locale, String>();
            supportedLocalesProvider.getSupportedLocales().forEach(loc ->{
                var ar = storage.getCaption(ref.getType(), ref.getId(), loc);
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
        if(cacheMetadataProvider.isCacheCaption(cls)){
            getOrCreateCaptionCache(captionsCache, String.class).put(id, new CachedValue<>(System.nanoTime(), null));
            return;
        }
        if(cacheMetadataProvider.isCacheLocalizedCaption(cls)){
            getOrCreateCaptionCache((Map)localizedCaptionsCache, Map.class).put(id, new CachedValue<>(System.nanoTime(), null));
        }
    }
    private <D> KeyValueCache<Long, D> getOrCreateCaptionCache(Map<String, KeyValueCache<Long, D>> caches, Class<D> cls) {
        var cache = caches.get(cls.getName());
        if (cache == null) {
            var className = cls.getName();
            var capacityStr = env.getProperty("cache.caption.capacity.%s".formatted(className));
            if (capacityStr == null) {
                capacityStr = env.getProperty("cache.caption.capacity.default", "10000");
            }
            var capacity = Integer.parseInt(capacityStr);
            var expirationInSecondsStr = env.getProperty("cache.caption.expiration.%s".formatted(className));
            if (expirationInSecondsStr == null) {
                expirationInSecondsStr = env.getProperty("cache.caption.expiration.default", "3600");
            }
            var expirationInSeconds = Integer.parseInt(expirationInSecondsStr);
            cache = cacheManager.createKeyValueCache(Long.class, cls, "caption_%s".formatted(className), capacity, expirationInSeconds);
            caches.put(className, cache);
        }
        return cache;
    }
}
