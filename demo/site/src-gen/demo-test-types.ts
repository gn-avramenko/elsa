/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

export type DemoTestEnum=
'ITEM1'
| 'ITEM2';

export type DemoTestEntity={
  stringProperty?: string,
};

export type DemoTestServerCallRequest={
  param?: string,
};

export type DemoTestServerCallResponse={
  stringProperty?: string,
  dateProperty?: Date,
  enumProperty?: DemoTestEnum,
  stringCollection: string[],
  dateCollection: Date[],
  entityCollection: DemoTestEntity[],
  stringMap: Map<string,string>,
  dateMap: Map<Date,Date>,
  eneityMap: Map<DemoTestEntity,DemoTestEntity>,
};

export type DemoTestSubscriptionParameters={
  param?: string,
};

export type DemoTestSubscriptionEvent={
  stringProperty?: string,
};
