/* eslint-disable no-unused-vars,max-len,max-classes-per-file,no-await-in-loop,no-restricted-syntax */
import {
  RSerializableType,
  RTagDescription,
  RTypesMetadata,
  GetRemotingEntityDescriptionRequest,
  GetRemotingEntityDescriptionResponse,
  REntityType,
  RPropertyDescription, RGenericDescription,
} from '../src-gen/core-remoting';
import { performServerCall, PreloaderHandler, remotingConfiguration } from './core-remoting-low-level';
import { registry, RegistryItem, RegistryItemType } from './registry';
import { bytes2Str, formatDigit, str2Bytes } from './core-text-utils';
import { EntityReference } from './core-model-entities';

type TypesMetadata = {
    serializableTypes: Map<string, RSerializableType>
    domainEntityTags: Map<String, RTagDescription>
    domainDatabaseTags: Map<String, RTagDescription>
    customEntityTags: Map<String, RTagDescription>
    l10nParameterTypeTags: Map<String, RTagDescription>
    remotingEntityTags: Map<String, RTagDescription>
}

let typesMetadata: TypesMetadata;

const entitiesMetadata = new Map<string, GetRemotingEntityDescriptionResponse>();

const getEntityMetadata = async (
  className: string,
  ph: PreloaderHandler | null,
  operationId: string | null,
) => {
  const result = await performServerCall(
    remotingConfiguration.clientId,
    'core',
    'meta',
    'get-entity-description',
        {
          entityId: className,
        } as GetRemotingEntityDescriptionRequest,
        ph,
        operationId,
  ) as GetRemotingEntityDescriptionResponse;
  entitiesMetadata.set(className, result);
  return result;
};

export const getTagDescription = (entityType:REntityType, tagName:string) : RTagDescription => {
  switch (entityType) {
    case 'DOMAIN_ENTITY':
      return typesMetadata!!.domainEntityTags.get(tagName)!!;
    case 'DOMAIN_DATABASE_ENTITY':
      return typesMetadata!!.domainDatabaseTags.get(tagName)!!;
    case 'CUSTOM':
      return typesMetadata!!.customEntityTags.get(tagName)!!;
    case 'REMOTING':
      return typesMetadata!!.remotingEntityTags.get(tagName)!!;
    case 'L10N':
      return typesMetadata!!.l10nParameterTypeTags.get(tagName)!!;
    default:
      throw new Error(`unsupported ${entityType} ${tagName}`);
  }
};

export interface SerializationHandler<T> {
    // eslint-disable-next-line no-unused-vars,max-len
    prepareRequestValue(value: T | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any|null>;
    convertResponseValue(value: any | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<T|null>;
}

export const serializationHandlerRegistryItemType = new RegistryItemType<SerializationHandler<unknown>>('serialization-handler');

// eslint-disable-next-line max-len
export abstract class BaseSerializationHandler<T> implements RegistryItem<SerializationHandler<T>>, SerializationHandler<T> {
    abstract getId(): string;

    // eslint-disable-next-line max-len
    prepareRequestValue(value: T | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any|null> {
      return Promise.resolve(value);
    }

    convertResponseValue(value: any | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<T|null> {
      return Promise.resolve(value);
    }

    getType(): RegistryItemType<SerializationHandler<T>> {
      return serializationHandlerRegistryItemType;
    }
}

type GenericHanlderData = {
  handler: SerializationHandler<any>,
  tagDescription: RTagDescription,
}

const calculateGenericHandlerData = (genericId:string, generics: RGenericDescription[]) => {
  const generic = generics.find((gen) => gen.id === genericId)!!;
  return {
    tagDescription: {
      generics: generic.nestedGenerics,
      tagName: 'tag',
      type: generic.type,
      objectIdAttributeName: generic.objectIdAttributeName,
    },
    handler: registry.get(serializationHandlerRegistryItemType, generic.type),
  } as GenericHanlderData;
};

class EntitySerializationHandler extends BaseSerializationHandler<any> {
  getId(): string {
    return 'ENTITY';
  }

  // eslint-disable-next-line max-len
  prepareRequestValue(value: any | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any | null> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any | null>(async (resolve) => {
      if (!value) {
        resolve(null);
        return;
      }
      const result = {} as any;
      // eslint-disable-next-line no-underscore-dangle
      result._cn = value._cn;
      const className = prop.attributes.find((it) => it.name === tag.objectIdAttributeName)!!.value!!;
      // eslint-disable-next-line max-len
      const ed = entitiesMetadata.get(className) ?? (await getEntityMetadata(className, ph, operationId));
      // eslint-disable-next-line no-restricted-syntax
      for (const prop2 of ed.description.properties) {
        if (value[prop2.id] !== null && value[prop2.id] !== undefined) {
          const tag2 = getTagDescription(ed.type, prop2.tagName);
          const handler = registry.get(serializationHandlerRegistryItemType, tag2.type)!!;
          // eslint-disable-next-line no-await-in-loop
          result[prop2.id] = await handler.prepareRequestValue(value[prop2.id], tag2, prop2, ph, operationId);
        }
      }
      resolve(result);
    });
  }

  convertResponseValue(value: any, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any | null>(async (resolve) => {
      if (!value) {
        resolve(null);
        return;
      }
      const result = {} as any;
      // eslint-disable-next-line no-underscore-dangle
      result._cn = value._cn;
      const className = prop.attributes.find((it) => it.name === tag.objectIdAttributeName)!!.value!!;
      // eslint-disable-next-line max-len
      const ed = entitiesMetadata.get(className) ?? (await getEntityMetadata(className, ph, operationId));
      // eslint-disable-next-line no-restricted-syntax
      for (const prop2 of ed.description.properties) {
        if (value[prop2.id] !== null && value[prop2.id] !== undefined) {
          const tag2 = getTagDescription(ed.type, prop2.tagName);
          const handler = registry.get(serializationHandlerRegistryItemType, tag2.type)!!;
          // eslint-disable-next-line no-await-in-loop
          result[prop2.id] = await handler.convertResponseValue(value[prop2.id], tag2, prop2, ph, operationId);
        }
      }
      resolve(result);
    });
  }
}

class ArrayListSerializationHandler extends BaseSerializationHandler<any[]> {
  getId(): string {
    return 'ARRAY-LIST';
  }

  // eslint-disable-next-line max-len
  prepareRequestValue(value: any[], tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any|null> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any|null>(async (resolve) => {
      const result = [] as any[];
      const data = calculateGenericHandlerData('element-class-name', tag.generics);
      for (const item of value) {
        result.push(await data.handler.prepareRequestValue(item, data.tagDescription, prop, ph, operationId));
      }
      resolve(result);
    });
  }

  convertResponseValue(value: any[], tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any|null>(async (resolve) => {
      const result = [] as any[];
      const data = calculateGenericHandlerData('element-class-name', tag.generics);
      for (const item of value) {
        result.push(await data.handler.convertResponseValue(item, data.tagDescription, prop, ph, operationId));
      }
      resolve(result);
    });
  }
}
registry.register(new ArrayListSerializationHandler());

class LinkedHashSetSerializationHandler extends BaseSerializationHandler<any[]> {
  getId(): string {
    return 'LINKED-HASH-SET';
  }

  // eslint-disable-next-line max-len
  prepareRequestValue(value: any[], tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any|null> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any|null>(async (resolve) => {
      const result = [] as any[];
      const data = calculateGenericHandlerData('element-class-name', tag.generics);
      for (const item of value) {
        result.push(await data.handler.prepareRequestValue(item, data.tagDescription, prop, ph, operationId));
      }
      resolve(result);
    });
  }

  convertResponseValue(value: any[], tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any|null>(async (resolve) => {
      const result = [] as any[];
      const data = calculateGenericHandlerData('element-class-name', tag.generics);
      for (const item of value) {
        result.push(await data.handler.convertResponseValue(item, data.tagDescription, prop, ph, operationId));
      }
      resolve(result);
    });
  }
}
registry.register(new LinkedHashSetSerializationHandler());

class LinkedHashMapSerializationHandler extends BaseSerializationHandler<Map<any, any>> {
  getId(): string {
    return 'LINKED-HASH-MAP';
  }

  // eslint-disable-next-line max-len
  prepareRequestValue(value: Map<any, any>, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any|null> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any|null>(async (resolve) => {
      const result = new Map<any, any>();
      const keyData = calculateGenericHandlerData('key-class-name', tag.generics);
      const valueData = calculateGenericHandlerData('key-class-name', tag.generics);
      for (const [key, aValue] of value) {
        result.set(
          await keyData.handler.prepareRequestValue(key, keyData.tagDescription, prop, ph, operationId),
          await valueData.handler.prepareRequestValue(aValue, valueData.tagDescription, prop, ph, operationId),
        );
      }
      resolve(result);
    });
  }

  convertResponseValue(value: Map<any, any>, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise<any|null>(async (resolve) => {
      const result = new Map<any, any>();
      const keyData = calculateGenericHandlerData('key-class-name', tag.generics);
      const valueData = calculateGenericHandlerData('key-class-name', tag.generics);
      for (const [key, aValue] of value) {
        result.set(
          await keyData.handler.convertResponseValue(key, keyData.tagDescription, prop, ph, operationId),
          await valueData.handler.convertResponseValue(aValue, valueData.tagDescription, prop, ph, operationId),
        );
      }
      resolve(result);
    });
  }
}
registry.register(new LinkedHashMapSerializationHandler());

class StringSerializationHandler extends BaseSerializationHandler<string> {
  getId(): string {
    return 'STRING';
  }
}
registry.register(new StringSerializationHandler());

class LongSerializationHandler extends BaseSerializationHandler<number> {
  getId(): string {
    return 'LONG';
  }

  prepareRequestValue(value: number | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    return Promise.resolve(value && BigInt(value.toString()));
  }

  convertResponseValue(value: number | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    return Promise.resolve(value && value.toString());
  }
}
registry.register(new LongSerializationHandler());

class IntSerializationHandler extends BaseSerializationHandler<number> {
  getId(): string {
    return 'INT';
  }
}
registry.register(new IntSerializationHandler());

class BigDecimalSerializationHandler extends BaseSerializationHandler<number> {
  getId(): string {
    return 'BIG-DECIMAL';
  }
}
registry.register(new BigDecimalSerializationHandler());

class LocalDateTimeSerializationHandler extends BaseSerializationHandler<Date> {
  getId(): string {
    return 'LOCAL-DATE-TIME';
  }

  localDateTimeFromString(value: string|null) {
    if (!value) {
      return null;
    }
    const values = value.replace('T', '-').split('-');
    const result = new Date();

    result.setFullYear(Number(values[0]), Number(values[1]) - 1, Number(values[2]));
    result.setHours(Number(values[3]), Number(values[4]), Number(values[5]), Number(values[6]));
    return result;
  }

  prepareRequestValue(value: Date | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    return Promise.resolve(value && `${formatDigit(value.getFullYear(), 4)}-${formatDigit(value.getMonth() + 1, 2)}-${formatDigit(value.getDate(), 2)}T${formatDigit(value.getHours(), 2)}-${formatDigit(value.getMinutes(), 2)}-${formatDigit(value.getSeconds(), 2)}-${formatDigit(value.getMilliseconds(), 3)}`);
  }

  convertResponseValue(value: string | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<Date|null> {
    return Promise.resolve(this.localDateTimeFromString(value));
  }
}
registry.register(new LocalDateTimeSerializationHandler());

class LocalDateSerializationHandler extends BaseSerializationHandler<Date> {
  getId(): string {
    return 'LOCAL-DATE';
  }

  localDateFromString(value: string | null) {
    if (!value) {
      return null;
    }
    const values = value.replace('_', '-').split('-');
    const result = new Date();

    result.setFullYear(Number(values[0]), Number(values[1]) - 1, Number(values[2]));
    result.setHours(0, 0, 0, 0);
    return result;
  }

  prepareRequestValue(value: Date | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    return Promise.resolve(value && `${formatDigit(value.getFullYear(), 4)}-${formatDigit(value.getMonth() + 1, 2)}-${formatDigit(value.getDate(), 2)}`);
  }

  convertResponseValue(value: string | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<Date| any> {
    return Promise.resolve(this.localDateFromString(value));
  }
}
registry.register(new LocalDateSerializationHandler());

class BooleanSerializationHandler extends BaseSerializationHandler<boolean> {
  getId(): string {
    return 'BOOLEAN';
  }
}
registry.register(new BooleanSerializationHandler());

class ByteArraySerializationHandler extends BaseSerializationHandler<Uint8Array> {
  getId(): string {
    return 'BYTE-ARRAY';
  }

  prepareRequestValue(value: Uint8Array | null, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<any> {
    return Promise.resolve(value && bytes2Str(value));
  }

  convertResponseValue(value: any, tag: RTagDescription, prop: RPropertyDescription, ph: PreloaderHandler | null, operationId: string | null): Promise<Uint8Array | null> {
    return Promise.resolve(value && str2Bytes(value));
  }
}
registry.register(new ByteArraySerializationHandler());

class EntityReferenceSerializationHandler extends BaseSerializationHandler<EntityReference> {
  getId(): string {
    return 'ENTITY-REFERENCE';
  }
}
registry.register(new EntityReferenceSerializationHandler());

const entitySerializationHandler = new EntitySerializationHandler();
registry.register(entitySerializationHandler);

class EnumSerializationHandler extends BaseSerializationHandler<String> {
  getId(): string {
    return 'ENUM';
  }
}
const enumSerializationHandler = new EnumSerializationHandler();
registry.register(enumSerializationHandler);

const initializeTypesMetadata = async (
  ph: PreloaderHandler | null,
  operationId: string | null,
) => {
  if (typesMetadata) {
    return typesMetadata;
  }
  const data = await performServerCall(remotingConfiguration.clientId, 'core', 'meta', 'get-types-metadata', {}, ph, operationId) as RTypesMetadata;
  typesMetadata = {
    serializableTypes: new Map<string, RSerializableType>(),
    domainEntityTags: new Map<String, RTagDescription>(),
    domainDatabaseTags: new Map<String, RTagDescription>(),
    customEntityTags: new Map<String, RTagDescription>(),
    l10nParameterTypeTags: new Map<String, RTagDescription>(),
    remotingEntityTags: new Map<String, RTagDescription>(),
  };
  data.serializableTypes.forEach((it) => typesMetadata.serializableTypes.set(it.id, it));
  data.domainEntityTags.forEach((it) => typesMetadata.domainEntityTags.set(it.tagName, it));
  data.domainDatabaseTags.forEach((it) => typesMetadata.domainDatabaseTags.set(it.tagName, it));
  data.customEntityTags.forEach((it) => typesMetadata.customEntityTags.set(it.tagName, it));
  // eslint-disable-next-line max-len
  data.l10nParameterTypeTags.forEach((it) => typesMetadata.l10nParameterTypeTags.set(it.tagName, it));
  data.remotingEntityTags.forEach((it) => typesMetadata.remotingEntityTags.set(it.tagName, it));
  return typesMetadata;
};

// eslint-disable-next-line import/prefer-default-export,max-len
export const prepareRequest = async (className: string, originalRequest:any, ph: PreloaderHandler | null, operationId: string | null) => {
  if (!typesMetadata) {
    await initializeTypesMetadata(ph, operationId);
  }
  return entitySerializationHandler.prepareRequestValue(originalRequest, {
    tagName: 'prop',
    objectIdAttributeName: 'class-name',
    type: 'ENTITY',
    generics: [],
  }, {
    tagName: 'prop',
    id: 'prop',
    attributes: [{
      name: 'class-name',
      value: className,
    }],
  }, ph, operationId);
};

export const convertResponse = async (className: string, originalResponse:any, ph: PreloaderHandler | null, operationId: string | null) => {
  if (!typesMetadata) {
    await initializeTypesMetadata(ph, operationId);
  }
  return entitySerializationHandler.convertResponseValue(originalResponse, {
    tagName: 'prop',
    objectIdAttributeName: 'class-name',
    type: 'ENTITY',
    generics: [],
  }, {
    tagName: 'prop',
    id: 'prop',
    attributes: [{
      name: 'class-name',
      value: className,
    }],
  }, ph, operationId);
};
