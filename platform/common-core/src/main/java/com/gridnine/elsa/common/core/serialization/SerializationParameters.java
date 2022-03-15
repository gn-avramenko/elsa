/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization;

public class SerializationParameters {

    private boolean prettyPrint = true;

    private EntityReferenceCaptionSerializationStrategy entityReferenceCaptionSerializationStrategy = EntityReferenceCaptionSerializationStrategy.ALL;

    private EntityReferenceTypeSerializationStrategy entityReferenceTypeSerializationStrategy = EntityReferenceTypeSerializationStrategy.ALL_CLASS_NAME;

    private EnumSerializationStrategy enumSerializationStrategy = EnumSerializationStrategy.NAME;

    private ClassSerializationStrategy classSerializationStrategy = ClassSerializationStrategy.NAME;

    public ClassSerializationStrategy getClassSerializationStrategy() {
        return classSerializationStrategy;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public SerializationParameters setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    public EntityReferenceCaptionSerializationStrategy getEntityReferenceCaptionSerializationStrategy() {
        return entityReferenceCaptionSerializationStrategy;
    }

    public SerializationParameters setClassSerializationStrategy(ClassSerializationStrategy classSerializationStrategy) {
        this.classSerializationStrategy = classSerializationStrategy;
        return this;
    }

    public SerializationParameters setEntityReferenceCaptionSerializationStrategy(EntityReferenceCaptionSerializationStrategy entityReferenceCaptionSerializationStrategy) {
        this.entityReferenceCaptionSerializationStrategy = entityReferenceCaptionSerializationStrategy;
        return this;
    }

    public EntityReferenceTypeSerializationStrategy getEntityReferenceTypeSerializationStrategy() {
        return entityReferenceTypeSerializationStrategy;
    }

    public SerializationParameters setEntityReferenceTypeSerializationStrategy(EntityReferenceTypeSerializationStrategy entityReferenceTypeSerializationStrategy) {
        this.entityReferenceTypeSerializationStrategy = entityReferenceTypeSerializationStrategy;
        return this;
    }

    public SerializationParameters setEnumSerializationStrategy(EnumSerializationStrategy enumSerializationStrategy) {
        this.enumSerializationStrategy = enumSerializationStrategy;
        return this;
    }

    public EnumSerializationStrategy getEnumSerializationStrategy() {
        return enumSerializationStrategy;
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
