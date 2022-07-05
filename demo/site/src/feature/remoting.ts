import {
  confirmSubscriptionEvent, internalRemotingConfiguration,
  performServerCall, performSubscription,
  performUnsubscription, PreloaderHandler, remotingConfiguration,
} from './remoting-low-level';
import { restoreObject, serializeToJson } from './serialization';
import { registry, RegistryItem, RegistryItemType } from "./common";

// Types
type ServerCallDescription = {
  requestClassName?: string,
  responseClassName?: string
}

type SubscriptionDescription = {
  parameterClassName: string,
  eventClassName: string
}

type ClientServerCallDescription = {
  requestClassName?: string,
  responseClassName?: string
}

type ClientResponse = {
  errorMessage?: string,
  requestId: string,
  responseStr?: string
}

const serverCallDescriptions = new Map<string, ServerCallDescription>();

const subscriptionDescriptions = new Map<string, SubscriptionDescription>();

const clientCallDescriptions = new Map<string, ClientServerCallDescription>();

interface ClientCallHandler<RQ, RP> {
  // eslint-disable-next-line no-unused-vars
  handle(request:RQ): RP
}

export const clientCallHandlerRegistryItemType = new RegistryItemType<ClientCallHandler<any, any>>('client-call-handlers');

// eslint-disable-next-line max-len
export abstract class BaseClientCallHandler<RQ, RP> implements ClientCallHandler<RQ, RP>, RegistryItem<ClientCallHandler<any, any>> {

  // eslint-disable-next-line no-unused-vars
  abstract handle(request: RQ): RP;

  abstract getId(): string;

  // eslint-disable-next-line class-methods-use-this
  getType(): RegistryItemType<ClientCallHandler<any, any>> {
    return clientCallHandlerRegistryItemType;
  }
}

const fakePreloaderHandler : PreloaderHandler = {
  showPreloader: () => null,
  hidePreloader: () => null,
  delay: 0,
};

internalRemotingConfiguration.clientCallHandler = async (remotingId, data) => {
  const response = {
    requestId: data.callId,
  } as ClientResponse;
  try {
    const fullId = `${remotingId}:${data.groupId}:${data.methodId}`;
    if (!clientCallDescriptions.has(fullId)) {
      const description = await performServerCall(remotingConfiguration.clientId, 'core', 'meta', 'get-client-call-description', {
        remotingId,
        groupId: data.groupId,
        methodId: data.methodId,
      }, fakePreloaderHandler, null) as ClientServerCallDescription;
      clientCallDescriptions.set(fullId, description);
    }
    const clientCallDescription = clientCallDescriptions.get(fullId)!!;
    let request = null;
    if (clientCallDescription.requestClassName) {
      request = JSON.parse(data.data);
      // eslint-disable-next-line max-len
      await restoreObject(clientCallDescription.requestClassName, request, fakePreloaderHandler, null);
    }
    const handler = registry.get(clientCallHandlerRegistryItemType, fullId)!!;
    const result = handler.handle(request);
    if (clientCallDescription.responseClassName) {
      // eslint-disable-next-line max-len
      response.responseStr = await serializeToJson(result, clientCallDescription.responseClassName, fakePreloaderHandler, null);
    }
  } catch (e) {
    response.errorMessage = (e as Error).message;
  }
  const result = JSON.stringify(response);
  fetch(`/remoting/${remotingId}/registerClientResponse`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      requestId: data.callId!!,
      clientId: remotingConfiguration.clientId!!,
    },
    body: result,
  });
};

// eslint-disable-next-line import/prefer-default-export
export async function serverCall<RQ, RP>(
  remotingId: string,
  groupId: string,
  methodId: string,
  request: RQ,
  preloaderHandler: PreloaderHandler| boolean,
  operationId:string|null,
) {
  const fullId = `${remotingId}:${groupId}:${methodId}`;
  // eslint-disable-next-line no-nested-ternary,max-len
  const ph = preloaderHandler === false ? null : (preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : preloaderHandler);
  if (!serverCallDescriptions.has(fullId)) {
    const description = await performServerCall(remotingConfiguration.clientId, 'core', 'meta', 'get-server-call-description', {
      remotingId,
      groupId,
      methodId,
    }, ph, operationId) as ServerCallDescription;
    serverCallDescriptions.set(fullId, description);
  }
  const rd = serverCallDescriptions.get(fullId)!!;
  let requestBody = null;
  if (rd.requestClassName) {
    const rq = JSON.parse(JSON.stringify(request));
    requestBody = await serializeToJson(rq, rd.requestClassName, ph, operationId);
  }
  // eslint-disable-next-line max-len
  const result = await performServerCall(remotingConfiguration.clientId, remotingId, groupId, methodId, requestBody, ph, operationId);
  if (result != null && rd.responseClassName) {
    await restoreObject(rd.responseClassName, result, ph, operationId);
  }
  return result as RP;
}

export async function subscribe<SP, SE>(
  remotingId: string,
  groupId: string,
  subscriptionId: string,
  parameter: SP,
  // eslint-disable-next-line no-unused-vars
  handler: (ev:SE) => boolean,
  preloaderHandler: PreloaderHandler| boolean,
  operationId:string|null,
) {
  const fullId = `${remotingId}:${groupId}:${subscriptionId}`;
  // eslint-disable-next-line no-nested-ternary,max-len
  const ph = preloaderHandler === false ? null : (preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : preloaderHandler);
  if (!subscriptionDescriptions.has(fullId)) {
    const description = (await performServerCall(remotingConfiguration.clientId, 'core', 'meta', 'get-subscription-description', {
      remotingId,
      groupId,
      subscriptionId,
    }, ph, operationId)) as SubscriptionDescription;
    subscriptionDescriptions.set(fullId, description);
  }
  const sd = subscriptionDescriptions.get(fullId)!!;
  const requestBody = await serializeToJson(parameter, sd.parameterClassName, ph, operationId);
  // eslint-disable-next-line max-len
  const subId = await performSubscription(
    remotingId,
    groupId,
    subscriptionId,
    requestBody,
    ph,
    operationId,
    async (data) => {
      const obj = JSON.parse(data);
      await restoreObject(sd.eventClassName, obj, ph, operationId);
      const result = handler(obj);
      // eslint-disable-next-line max-len
      await confirmSubscriptionEvent(remotingConfiguration.clientId, remotingId, subId, result);
    },
  );
  return subId;
}

export async function unsubscribe(
  remotingId: string,
  subscriptionId: string,
  preloaderHandler: PreloaderHandler| boolean,
  operationId:string|null,
) {
  // eslint-disable-next-line no-nested-ternary,max-len
  const ph = preloaderHandler === false ? null : (preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : preloaderHandler);

  return performUnsubscription(remotingId, subscriptionId, ph, operationId);
}
