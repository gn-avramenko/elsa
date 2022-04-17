/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

public record SerializableCollectionDescription(String id,
                                         SerializablePropertyType elementType,
                                         String elementClassName, boolean isAbstract) {
}
