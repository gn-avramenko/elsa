/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

import {
  BaseAsset,
  BaseProjection,
  EntityReference,
} from 'elsa-core';

export type DemoEnum=
'ITEM1'
| 'ITEM2'
| 'ITEM3';

export type DemoDomainAsset = BaseAsset & {
  stringProperty?: string,
  dateTimeProperty?: Date,
};

export type DemoDomainDocumentProjection = BaseProjection & {
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
