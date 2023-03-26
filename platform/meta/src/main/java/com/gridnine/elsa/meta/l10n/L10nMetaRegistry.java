/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.l10n;

import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashMap;
import java.util.Map;

public class L10nMetaRegistry {
    private final Map<String, L10nMessagesBundleDescription> bundles= new LinkedHashMap<>();

    public Map<String, L10nMessagesBundleDescription> getBundles() {
        return bundles;
    }


    public static L10nMetaRegistry get(){
        return Environment.getPublished(L10nMetaRegistry.class);
    }
}


