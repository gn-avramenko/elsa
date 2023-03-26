/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.meta.config.Environment;

public interface CaptionProvider {

    String getCaption(EntityReference<?> ref);

    public static CaptionProvider get(){
        return Environment.getPublished(CaptionProvider.class);
    }
}
