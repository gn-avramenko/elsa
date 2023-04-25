// noinspection JSUnusedGlobalSymbols

import { generateUUID } from './core-text-utils';

export class RemotingError {
  status: number;

  message?: string;

  details?: string;

  constructor(status: number, message?: string, details?: string) {
    this.status = status;
    this.message = message;
    this.details = details;
  }
}

export type PreloaderHandler = {
  // eslint-disable-next-line no-unused-vars
  showPreloader: () => void,
  hidePreloader: () => void,
  delay: number
}

type RemotingConfiguration = {
  globalPreloaderHandler?: PreloaderHandler
  clientId: string,
}

const elsaClientId = generateUUID();

export const remotingConfiguration: RemotingConfiguration = { clientId: elsaClientId };

type AsyncCallState = {
  operationId: string
  firstCallId: string
  loaderShown: boolean
  lastCallId: string
}

const asyncCalls = new Map<string, AsyncCallState>();

async function wrapWithLoader(
  preloaderHandler: PreloaderHandler|null,
  operationId: string | null,
  func: () => Promise<any>,
) {
  const opId = operationId || 'general';
  let state = asyncCalls.get(opId);
  const guid = generateUUID();
  if (state == null) {
    state = {
      operationId: opId,
      loaderShown: false,
      lastCallId: guid,
      firstCallId: guid,
    };
    asyncCalls.set(opId, state);
    if (preloaderHandler) {
      setTimeout(() => {
        const state2 = asyncCalls.get(opId);
        if (state2 != null && state2.firstCallId === guid) {
          preloaderHandler.showPreloader();
          state2.loaderShown = true;
        }
      }, preloaderHandler.delay);
    }
  } else {
    state.lastCallId = guid;
  }
  try {
    return await func();
  } finally {
    const state2 = asyncCalls.get(opId);
    if (guid === state2?.lastCallId) {
      asyncCalls.delete(opId);
      if (preloaderHandler && state2.loaderShown) {
        preloaderHandler.hidePreloader();
      }
    }
  }
}
// eslint-disable-next-line import/prefer-default-export
export async function performServerCall(
  clientId: string,
  remotingId: string,
  groupId: string,
  methodId: string,
  request: any|null,
  preloaderHandler: PreloaderHandler|null,
  operationId: string | null,
) {
  return wrapWithLoader(preloaderHandler, operationId, async () => {
    const result = await fetch(`/remoting/${remotingId}/${groupId}/${methodId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        clientId: remotingConfiguration.clientId,
      },
      body: request && (typeof request === 'string' ? request : JSON.stringify(request)),
    });
    if (result.status === 204) {
      return null;
    }
    const json = await result.json();
    if (result.status !== 200) {
      throw new RemotingError(result.status, json.message, json.details);
    }
    return json;
  });
}
