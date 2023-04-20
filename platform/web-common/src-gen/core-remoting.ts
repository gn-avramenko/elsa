/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

export type REntityType=
'DOMAIN_ENTITY'
| 'DOMAIN_DATABASE_ENTITY'
| 'CUSTOM'
| 'REMOTING';

export type RGenericDeclaration={
  id: string,
  nestedGenerics: RGenericDeclaration[],
};

export type RSerializableType={
  id: string,
  generics: RGenericDeclaration[],
};

export type RGenericDescription={
  id: string,
  type: string,
  objectIdAttributeName?: string,
  nestedGenerics: RGenericDescription[],
};

export type RTagDescription={
  tagName: string,
  type: string,
  generics: RGenericDescription[],
};

export type RAttribute={
  name: string,
  value?: string,
};

export type RPropertyDescription={
  id: string,
  tagName: string,
  attributes: RAttribute[],
};

export type REntityDescription={
  id: string,
  properties: RPropertyDescription[],
  attributes: RAttribute[],
};

export type GetServerCallDescriptionRequest={
  remotingId: string,
  groupId: string,
  methodId: string,
};

export type GetServerCallDescriptionResponse={
  requestClassName?: string,
  responseClassName?: string,
};

export type TypesMetadata={
  serializableTypes: RSerializableType[],
  domainEntityTags: RTagDescription[],
  domainDatabaseTags: RTagDescription[],
  customEntityTags: RTagDescription[],
  l10nParameterTypeTags: RTagDescription[],
  remotingEntityTags: RTagDescription[],
};

export type GetRemotingEntityDescriptionRequest={
  entityId: string,
};

export type GetRemotingEntityDescriptionResponse={
  type: REntityType,
  description: REntityDescription,
};
