// noinspection JSUnusedGlobalSymbols

import { generateUUID } from './core-text-utils';
import { RemotingMessage } from '../src-gen/core-remoting';

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

type ChannelData = {
    // eslint-disable-next-line no-unused-vars
    awaitingRequests: ({ resolve: () => void, reject: (e: any) => void })[],
    source?: EventSource,
}

let channel: ChannelData | null = null;

const subscriptions = new Map<string, any>();

const asyncCalls = new Map<string, AsyncCallState>();

async function wrapWithLoader(
  preloaderHandler: PreloaderHandler | null,
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
  request: any | null,
  preloaderHandler: PreloaderHandler | null,
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

export async function performUnsubscription(
  remotingId: string,
  groupId: string,
  subscriptionId: string,
  callId: string,
  preloaderHandler: PreloaderHandler | null,
  operationId: string | null,
) {
  if (channel == null) {
    return null;
  }
  return wrapWithLoader(preloaderHandler, operationId, async () => {
    subscriptions.delete(callId);
    if (channel == null) {
      return null;
    }
    const result = await fetch(`/remoting/${remotingId}/${groupId}/${subscriptionId}/unsubscribe/${callId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'plain/text',
        clientId: remotingConfiguration.clientId,
      },
    });
    if (result.status === 200) {
      return null;
    }
    throw new RemotingError(result.status, 'Error occurred', await result.text());
  });
}

// eslint-disable-next-line max-len
async function createChannel(clientId: string, remotingId: string, groupId: string, subscriptionId: string, ph: PreloaderHandler | null, operationId: string | null) {
  channel = { awaitingRequests: [] };
  return new Promise<void>((resolve, reject) => {
    const source = new EventSource(`/remoting/${remotingId}/${groupId}/${subscriptionId}/subscribe?clientId=${clientId}`);
    if (!channel) {
      reject();
      return;
    }
    channel.source = source;
    // eslint-disable-next-line func-names
    source.onopen = function () {
      if (this.readyState === EventSource.OPEN) {
        resolve();
        if (channel) {
          channel.awaitingRequests.forEach((s) => {
            s.resolve();
          });
        }
      }
    };
    // eslint-disable-next-line func-names
    source.onerror = function () {
      // eslint-disable-next-line no-console
      console.error('on error');
      if (source.readyState === EventSource.CLOSED) {
        const error = new Error('unable to subscribe to server events');
        reject(error);
        if (channel) {
          channel!!.awaitingRequests.forEach((s) => {
            s.reject(error);
          });
          channel = null;
        }
      }
    };
    source.onmessage = async (ev) => {
      const message = JSON.parse(ev.data) as RemotingMessage;
      // eslint-disable-next-line default-case
      switch (message.type) {
        case 'PING': {
          // eslint-disable-next-line no-console
          console.debug(`successfully connected to ${remotingId}`);
          break;
        }
        case 'SUBSCRIPTION': {
          // eslint-disable-next-line no-unused-vars,max-len
          const handler = subscriptions.get(message.methodId!!) as ((data: string) => void) | null;
          if (handler === null) {
            // eslint-disable-next-line max-len
            await performUnsubscription(remotingConfiguration.clientId, remotingId, subscriptionId, message.callId, ph, operationId);
            return;
          }
          handler(message.data);
          break;
        }
      }
    };
  });
}

async function awaitInitialization() {
  if (!channel) {
    return null;
  }
  const ch2 = channel;
  return new Promise<void>((resolve, reject) => {
    ch2.awaitingRequests.push({ resolve, reject });
  });
}

async function ensureChannel(
  clientId: string,
  remotingId: string,
  groupId: string,
  subscriptionId: string,
  preloaderHandler: PreloaderHandler | null,
  operationId: string | null,
) {
  if (!channel) {
    // eslint-disable-next-line max-len
    await createChannel(clientId, remotingId, groupId, subscriptionId, preloaderHandler, operationId);
  } else if (!channel.source
        || (channel.source && channel.source.readyState === EventSource.CONNECTING)) {
    await awaitInitialization();
  }
}

export async function performSubscription(
  remotingId: string,
  groupId: string,
  subscriptionId: string,
  request: string,
  preloaderHandler: PreloaderHandler | null,
  operationId: string | null,
  // eslint-disable-next-line no-unused-vars
  handler: (data: string) => void,
) {
  return wrapWithLoader(preloaderHandler, operationId, async () => {
    // eslint-disable-next-line max-len
    await ensureChannel(remotingConfiguration.clientId, remotingId, groupId, subscriptionId, preloaderHandler, operationId);
    const result = await fetch(`/remoting/${remotingId}/subscribe`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        groupId,
        subscriptionId,
        clientId: remotingConfiguration.clientId,
      },
      body: request,
    });
    const subId = await result.text();
    if (result.status !== 200) {
      throw new RemotingError(result.status, 'Error occurred', subId);
    }
    subscriptions.set(subId, handler);
    return subId;
  });
}
