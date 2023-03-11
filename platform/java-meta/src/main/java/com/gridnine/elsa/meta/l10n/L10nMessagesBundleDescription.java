/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.l10n;

import com.gridnine.elsa.meta.common.BaseElementWithId;

import java.util.LinkedHashMap;
import java.util.Map;

public class L10nMessagesBundleDescription extends BaseElementWithId {
    private final Map<String,L10nMessageDescription> messages = new LinkedHashMap<>();

    public L10nMessagesBundleDescription() {
    }

    public L10nMessagesBundleDescription(String id) {
        super(id);
    }

    public Map<String, L10nMessageDescription> getMessages() {
        return messages;
    }
}
