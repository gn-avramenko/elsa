/* eslint-disable no-unused-vars */
// noinspection JSUnusedLocalSymbols,JSUnusedGlobalSymbols
// noinspection JSUnusedLocalSymbols

import {
  performServerCall, performSubscription, performUnsubscription,
  PreloaderHandler, remotingConfiguration,
} from './core-remoting-low-level';
import { convertResponse, prepareRequest } from './core-serialization';
import { GetSubscriptionDescriptionResponse } from '../src-gen/core-remoting';

// Types
type ServerCallDescription = {
  requestClassName?: string,
  responseClassName?: string
}

type ClientResponse = {
  errorMessage?: string,
  requestId: string,
  responseStr?: string
}

const serverCallDescriptions = new Map<string, ServerCallDescription>();

const subscriptionDescriptions = new Map<string, GetSubscriptionDescriptionResponse>();

export type RemotingCallOptions = {
  preloaderHandler?: PreloaderHandler| boolean,
  operationId?:string|null,
}

export type ServerCallOptions = RemotingCallOptions;

export type SubscriptionOptions = RemotingCallOptions;
export type UnsubscriptionOptions = RemotingCallOptions;

// eslint-disable-next-line import/prefer-default-export
export async function serverCall<RQ, RP>(
  remotingId: string,
  groupId: string,
  methodId: string,
  request: RQ,
  options?: ServerCallOptions,
) {
  const fullId = `${remotingId}:${groupId}:${methodId}`;
  // eslint-disable-next-line no-nested-ternary,max-len
  const ph = options?.preloaderHandler === false ? null : (options?.preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : options?.preloaderHandler) || null;
  const operationId = options?.operationId || null;
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
    const rq = await prepareRequest(rd.requestClassName, request, ph, operationId);
    requestBody = JSON.stringify(rq);
  }
  // eslint-disable-next-line max-len
  const result = await performServerCall(remotingConfiguration.clientId, remotingId, groupId, methodId, requestBody, ph, operationId);
  if (result != null && rd.responseClassName) {
    await convertResponse(rd.responseClassName, result, ph, operationId);
  }
  return result as RP;
}

export async function subscribe<SP, SE>(
  remotingId: string,
  groupId: string,
  subscriptionId: string,
  parameter: SP,
  // eslint-disable-next-line no-unused-vars
  handler: (ev:SE) => Promise<void>,
  options?: SubscriptionOptions,
) {
  const fullId = `${remotingId}:${groupId}:${subscriptionId}`;
  const preloaderHandler = options?.preloaderHandler || false;
  const operationId = options?.operationId || null;
  // eslint-disable-next-line no-nested-ternary,max-len
  const ph = preloaderHandler === false ? null : (preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : preloaderHandler);
  if (!subscriptionDescriptions.has(fullId)) {
    const description = (await performServerCall(remotingConfiguration.clientId, 'core', 'meta', 'get-subscription-description', {
      remotingId,
      groupId,
      subscriptionId,
    }, ph, operationId)) as GetSubscriptionDescriptionResponse;
    subscriptionDescriptions.set(fullId, description);
  }
  const sd = subscriptionDescriptions.get(fullId)!!;
  const requestBody = (sd.parameterClassName && await prepareRequest(sd.parameterClassName, parameter, ph, operationId)) || '{}';
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
      if (sd.eventClassName) {
        await convertResponse(sd.eventClassName, obj, ph, operationId);
      }
      await handler(obj);
    },
  );
  return subId as string;
}

export async function unsubscribe(
  remotingId: string,
  groupId: string,
  subscriptionId: string,
  callId: string,
  options?: UnsubscriptionOptions,
) {
  const preloaderHandler = options?.preloaderHandler || false;
  const operationId = options?.operationId || null;
  // eslint-disable-next-line no-nested-ternary,max-len
  const ph = preloaderHandler === false ? null : (preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : preloaderHandler);

  return performUnsubscription(remotingId, groupId, subscriptionId, callId, ph, operationId);
}
