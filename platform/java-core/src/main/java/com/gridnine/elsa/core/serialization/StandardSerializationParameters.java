/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization;

import java.util.Map;

public final class StandardSerializationParameters {
    public static void setPrettyPrint(boolean value, Map<String,Object> params){
        params.put("pretty-print", value);
    }


    public static boolean isPrettyPrint(Map<String,Object> params){
        return params.containsKey("pretty-print")? (boolean)params.get("pretty-print"): true;
    }

    public static void setClassSerializationStrategy(ClassSerializationStrategy value, Map<String,Object> params){
        params.put("class-serialization-strategy", value);
    }

    public static ClassSerializationStrategy getClassSerializationStrategy( Map<String,Object> params){
        ClassSerializationStrategy value = (ClassSerializationStrategy) params.get("class-serialization-strategy");
        return value == null? ClassSerializationStrategy.NAME: value;
    }

    public static void setEntityReferenceCaptionSerializationStrategy(EntityReferenceCaptionSerializationStrategy value, Map<String,Object> params){
        params.put("entity-reference-caption-serialization-strategy", value);
    }

    public static EntityReferenceCaptionSerializationStrategy getEntityReferenceCaptionSerializationStrategy( Map<String,Object> params){
        EntityReferenceCaptionSerializationStrategy value = (EntityReferenceCaptionSerializationStrategy) params.get("entity-reference-caption-serialization-strategy");
        return value == null? EntityReferenceCaptionSerializationStrategy.ALL: value;
    }


    public static void setEntityReferenceTypeSerializationStrategy(EntityReferenceTypeSerializationStrategy value, Map<String,Object> params){
        params.put("entity-reference-type-serialization-strategy", value);
    }

    public static EntityReferenceTypeSerializationStrategy getEntityReferenceTypeSerializationStrategy( Map<String,Object> params){
        EntityReferenceTypeSerializationStrategy value = (EntityReferenceTypeSerializationStrategy) params.get("entity-reference-type-serialization-strategy");
        return value == null? EntityReferenceTypeSerializationStrategy.ALL_CLASS_NAME: value;
    }

    public static void setEnumSerializationStrategy(EnumSerializationStrategy value, Map<String,Object> params){
        params.put("enum-serialization-strategy", value);
    }

    public static EnumSerializationStrategy getEnumSerializationStrategy( Map<String,Object> params){
        EnumSerializationStrategy value = (EnumSerializationStrategy) params.get("enum-serialization-strategy");
        return value == null? EnumSerializationStrategy.NAME: value;
    }

    public enum EntityReferenceCaptionSerializationStrategy{
        ALL,
        ONLY_NOT_CACHED
    }
    public enum EntityReferenceTypeSerializationStrategy{
        ALL_CLASS_NAME,
        ABSTRACT_CLASS_ID
    }
    public enum EnumSerializationStrategy{
        ID,
        NAME
    }
    public enum ClassSerializationStrategy{
        ID,
        NAME
    }

}
