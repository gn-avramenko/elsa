/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.l10n;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class L10nMetaRegistry {
    private final Map<String, L10nMessagesBundleDescription> bundles= new LinkedHashMap<>();

    @Autowired
    public L10nMetaRegistry(List<L10nMetaRegistryConfigurator> configurators) {
        configurators.forEach(it -> it.updateMetaRegistry(this));
    }

    public Map<String, L10nMessagesBundleDescription> getBundles() {
        return bundles;
    }

}


