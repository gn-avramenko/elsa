import { performServerCall, PreloaderHandler, remotingConfiguration } from './remoting-low-level';

type RemotingEntityValueType = 'STRING' | 'LOCAL_DATE'| 'LOCAL_DATE_TIME'| 'ENUM'| 'CLASS'
  | 'BOOLEAN'| 'BYTE_ARRAY'| 'ENTITY'| 'ENTITY_REFERENCE'| 'LONG'| 'INT' | 'BIG_DECIMAL'

type RemotingEntityPropertyDescription = {
  type: RemotingEntityValueType,
  id: string,
  className?: string,
  isAbstract: boolean
}

type RemotingEntityCollectionDescription = {
  elementType: RemotingEntityValueType,
  id: string,
  elementClassName?: string,
  elementIsAbstract: boolean
}

type RemotingEntityMapDescription = {
  id: string,
  keyType: RemotingEntityValueType,
  keyClassName?: string,
  keyIsAbstract: boolean,
  valueType: RemotingEntityValueType,
  valueClassName?: string,
  valueIsAbstract: boolean
}

type RemotingEntityDescription={
  properties: RemotingEntityPropertyDescription[],
  collections: RemotingEntityCollectionDescription[],
  maps:RemotingEntityMapDescription[],
}

const entityDescriptions = new Map<string, RemotingEntityDescription>();

function formatDigit(number:number, scale:number) {
  let remaining = Math.round(number);
  let result = '';
  let digit = scale;
  while (digit >= 0) {
    const part = Math.abs(Math.floor(remaining / 10 ** digit));
    result += part;
    digit -= 1;
    remaining -= part * 10 ** digit;
  }
  if (number < 0) {
    return `-${result}`;
  }
  return result;
}
function localDateToString(value: Date) {
  return `${formatDigit(value.getFullYear(), 4)}-${formatDigit(value.getMonth() + 1, 2)}-${formatDigit(value.getDate(), 2)}`;
}

function localDateTimeToString(value: Date) {
  return `${formatDigit(value.getFullYear(), 4)}-${formatDigit(value.getMonth() + 1, 2)}-${formatDigit(value.getDate(), 2)}T${formatDigit(value.getHours(), 2)}-${formatDigit(value.getMinutes(), 2)}-${formatDigit(value.getSeconds(), 2)}-${formatDigit(value.getMilliseconds(), 3)}`;
}

function toInt(str: string) {
  return Number(str);
}

function localDateFromString(value: string) {
  const values = value.replace('_', '-').split('-');
  const result = new Date();

  result.setFullYear(toInt(values[0]), toInt(values[1]) - 1, toInt(values[2]));
  result.setHours(0, 0, 0, 0);
  return result;
}

function localDateTimeFromString(value: string) {
  const values = value.replace('T', '-').split('-');
  const result = new Date();

  result.setFullYear(toInt(values[0]), toInt(values[1]) - 1, toInt(values[2]));
  result.setHours(toInt(values[3]), toInt(values[4]), toInt(values[5]), toInt(values[6]));
  return result;
}

async function convertToSerializable(
  type: RemotingEntityValueType,
  className: string|null,
  value: any,
  ph:PreloaderHandler|null,
  operationId:string|null,
) {
  if (type === 'LOCAL_DATE') {
    return localDateToString(value as Date);
  }
  if (type === 'LOCAL_DATE_TIME') {
    return localDateTimeToString(value as Date);
  }
  if (type === 'ENTITY') {
    // eslint-disable-next-line no-use-before-define
    await modifyObject(value, value.className || className, ph, operationId);
    return value;
  }
  return value;
}

async function restoreFromSerializable(
  type: RemotingEntityValueType,
  className: string|null,
  value: any,
  ph:PreloaderHandler|null,
  operationId:string|null,
) {
  if (type === 'LOCAL_DATE') {
    return localDateFromString(value as string);
  }
  if (type === 'LOCAL_DATE_TIME') {
    return localDateTimeFromString(value as string);
  }
  if (type === 'ENTITY') {
    // eslint-disable-next-line no-use-before-define
    await restoreObject(value, value.className || className, ph, operationId);
    return value;
  }
  return value;
}

async function modifyObject(
  object:any,
  className:string,
  ph:PreloaderHandler|null,
  operationId:string|null,
) {
  if (!entityDescriptions.has(object.className)) {
    const entityDescription = (await performServerCall(remotingConfiguration.clientId, 'core', 'meta', 'get-entity-description', {
      entityId: className,
    }, ph, operationId)).description as RemotingEntityDescription;
    entityDescriptions.set(object.className, entityDescription);
  }
  const entityDescription = entityDescriptions.get(object.className)!!;
  // eslint-disable-next-line no-restricted-syntax
  for (const prop of entityDescription.properties) {
    const obj = object as any;
    const value = obj[prop.id];
    if (value != null) {
      // eslint-disable-next-line max-len,no-await-in-loop
      obj[prop.id] = await convertToSerializable(prop.type, prop.className || null, value, ph, operationId);
    }
  }
  // eslint-disable-next-line no-restricted-syntax
  for (const coll of entityDescription.collections) {
    const obj = object as any;
    const values = obj[coll.id] as any[];
    const correctedValues = [] as any[];
    // eslint-disable-next-line no-restricted-syntax
    for (const value of values) {
      // eslint-disable-next-line max-len,no-await-in-loop
      correctedValues.push(await convertToSerializable(coll.elementType, coll.elementClassName || null, value, ph, operationId));
    }
    obj[coll.id] = correctedValues;
  }
  // eslint-disable-next-line no-restricted-syntax
  for (const map of entityDescription.maps) {
    const obj = object as any;
    const values = obj[map.id] as Map<any, any>;
    const correctedValues = [] as any[];
    // eslint-disable-next-line no-restricted-syntax
    for (const [key, value] of values.values()) {
      // eslint-disable-next-line no-await-in-loop,max-len
      const mk = await convertToSerializable(map.keyType, map.keyClassName || null, key, ph, operationId);
      // eslint-disable-next-line no-await-in-loop,max-len
      const mv = await convertToSerializable(map.valueType, map.valueClassName || null, value, ph, operationId);
      correctedValues.push({
        key: mk,
        value: mv,
      });
    }
    obj[map.id] = correctedValues;
  }
}
// eslint-disable-next-line import/prefer-default-export
export async function serializeToJson(
  object:any,
  className:string,
  ph:PreloaderHandler|null,
  operationId:string|null,
) {
  const modifiedObject = JSON.parse(JSON.stringify(object));
  await modifyObject(modifiedObject, className, ph, operationId);
  return JSON.stringify(modifiedObject);
}

export async function restoreObject(
  className:string,
  object: any,
  ph:PreloaderHandler|null,
  operationId:string|null,
) {
  if (!entityDescriptions.has(className)) {
    const entityDescription = (await performServerCall(remotingConfiguration.clientId, 'core', 'meta', 'get-entity-description', {
      entityId: className,
    }, ph, operationId)).description as RemotingEntityDescription;
    entityDescriptions.set(className, entityDescription);
  }
  const ed = entityDescriptions.get(className)!!;
  // eslint-disable-next-line no-restricted-syntax
  for (const prop of ed.properties) {
    const obj = object as any;
    const value = obj[prop.id];
    if (value != null) {
      // eslint-disable-next-line no-await-in-loop,max-len
      obj[prop.id] = await restoreFromSerializable(prop.type, prop.className || null, value, ph, operationId);
    }
  }
  // eslint-disable-next-line no-restricted-syntax
  for (const coll of ed.collections) {
    const obj = object as any;
    const values = obj[coll.id] as any[];
    const modified = [] as any[];
    // eslint-disable-next-line no-restricted-syntax
    for (const value of values) {
      // eslint-disable-next-line max-len,no-await-in-loop
      modified.push(await restoreFromSerializable(coll.elementType, coll.elementClassName || null, value, ph, operationId));
    }
    obj[coll.id] = modified;
  }

  // eslint-disable-next-line no-restricted-syntax
  for (const map of ed.maps) {
    const obj = object as any;
    const values = obj[map.id] as any[];
    const modified = new Map<any, any>();
    // eslint-disable-next-line no-restricted-syntax
    for (const value of values) {
      // eslint-disable-next-line max-len,no-await-in-loop
      modified.set(
        // eslint-disable-next-line no-await-in-loop,max-len
        await restoreFromSerializable(map.keyType, map.keyClassName || null, value.key, ph, operationId),
        // eslint-disable-next-line max-len,no-await-in-loop
        await restoreFromSerializable(map.valueType, map.valueClassName || null, value.key, ph, operationId),
      );
    }
    obj[map.id] = modified;
  }
}
