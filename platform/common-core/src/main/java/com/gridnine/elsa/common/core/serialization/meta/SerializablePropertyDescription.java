/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

public record SerializablePropertyDescription(String id,
                                       SerializablePropertyType type,
                                       String className, boolean isAbstract) {
}
