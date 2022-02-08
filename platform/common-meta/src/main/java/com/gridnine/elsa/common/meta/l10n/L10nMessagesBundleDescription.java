/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.l10n;

import java.util.LinkedHashMap;
import java.util.Map;

public class L10nMessagesBundleDescription {
    private final Map<String,L10nMessageDescription> messages = new LinkedHashMap<>();

    public Map<String, L10nMessageDescription> getMessages() {
        return messages;
    }
}
