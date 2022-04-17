/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

public record SerializableMapDescription(String id,
                                  SerializablePropertyType keyType,
                                  String keyClassName, SerializablePropertyType valueType,
                                  String valueClassName, boolean keyIsAbstract, boolean valueIsAbstract) {
}
