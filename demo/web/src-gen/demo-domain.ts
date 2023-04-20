/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

export type DemoEnum=
'ITEM1'
| 'ITEM2'
| 'ITEM3';

export type DemoDomainAsset={
  stringProperty?: string,
  dateTimeProperty?: Date,
};

export type DemoDomainDocumentProjection={
  stringProperty?: string,
  getAllProperty?: string,
  stringCollection: string[],
  dateTimeProperty?: Date,
  longProperty?: bigint,
  bigDecimalProprerty?: number,
  booleanProperty?: boolean,
  dateProperty?: Date,
  enumProperty?: DemoEnum,
  entityReferenceProperty?: EntityReference,
  enumCollection: DemoEnum[],
  entityRefCollection: EntityReference[],
};
