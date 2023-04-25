/* eslint-disable no-unused-vars */
// noinspection JSUnusedLocalSymbols,JSUnusedGlobalSymbols
// noinspection JSUnusedLocalSymbols

import {
  performServerCall,
  PreloaderHandler, remotingConfiguration,
} from './core-remoting-low-level';
import { convertResponse, prepareRequest } from './core-serialization';

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
