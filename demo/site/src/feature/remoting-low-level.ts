import { generateUUID, RemotingError } from './common';

type RemotingMessageType = 'PING' | 'SUBSCRIPTION' | 'CLIENT_CALL';

export type RemotingMessage = {
  type: RemotingMessageType,
  subscriptionId?: string,
  callId?: string,
  groupId?:string,
  methodId?:string,
  data: string
}

type ChannelData = {
  // eslint-disable-next-line no-unused-vars
  awaitingRequests: ({resolve: ()=>void, reject: (e:any) =>void})[],
  source?: EventSource,
}

export type PreloaderHandler = {
  // eslint-disable-next-line no-unused-vars
  showPreloader: () => void,
  hidePreloader: () => void,
  delay: number
}

export type InternalRemotingConfiguration = {
  // eslint-disable-next-line no-unused-vars
  clientCallHandler: (remotingId: string, data:RemotingMessage) => void
}

export const internalRemotingConfiguration: InternalRemotingConfiguration = {
  clientCallHandler: () => {},
};

type RemotingConfiguration = {
  globalPreloaderHandler?: PreloaderHandler
  clientId: string,
}

let elsaClientId = window.localStorage.getItem('elsa-client-id');
if (!elsaClientId) {
  elsaClientId = generateUUID();
  window.localStorage.setItem('elsa-client-id', elsaClientId);
}
export const remotingConfiguration: RemotingConfiguration = { clientId: elsaClientId };

const channels = new Map<string, ChannelData>();

const subscriptions = new Map<string, any>();

export async function confirmSubscriptionEvent(
  clientId:string,
  remotingId:string,
  subscriptionId:string,
  received:boolean,
) {
  const result = await fetch(`/remoting/${remotingId}/confirmSubscriptionEvent?received=${received}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'plain/text',
      subscriptionId,
      clientId,
    },
  });
  if (result.status === 200) {
    return null;
  }
  throw new RemotingError(result.status, 'Error occurred', await result.text());
}

// eslint-disable-next-line no-unused-vars,max-len
async function createChannel(clientId:string, remotingId:string) {
  const channel:ChannelData = { awaitingRequests: [] };
  channels.set(remotingId, channel);
  const checkResult = await fetch(`/remoting/${remotingId}/check`, {
    method: 'GET',
  });
  if (checkResult.status !== 200) {
    const er = checkResult.status === 403 ? new RemotingError(403, 'no access')
      : new RemotingError(checkResult.status, 'unable to check');
    channel.awaitingRequests.forEach((s) => {
      s.reject(er);
    });
    channels.delete(remotingId);
    throw er;
  }
  return new Promise<void>((resolve, reject) => {
    const source = new EventSource(`/remoting/${remotingId}/createChannel?clientId=${clientId}`);
    channel.source = source;
    // eslint-disable-next-line func-names
    source.onopen = function () {
      if (this.readyState === EventSource.OPEN) {
        resolve();
        channel.awaitingRequests.forEach((s) => {
          s.resolve();
        });
      }
    };
    // eslint-disable-next-line func-names
    source.onerror = function () {
      console.log('on error');
      if (source.readyState === EventSource.CLOSED) {
        const error = new Error('unable to subscribe to server events');
        reject(error);
        channel.awaitingRequests.forEach((s) => {
          s.reject(error);
        });
      }
    };
    source.onmessage = async (ev) => {
      const message = JSON.parse(ev.data) as RemotingMessage;
      // eslint-disable-next-line default-case
      switch (message.type) {
        case 'PING': {
          console.debug(`successfully connected to ${remotingId}`);
          break;
        }
        case 'SUBSCRIPTION': {
          // eslint-disable-next-line no-unused-vars,max-len
          const handler = subscriptions.get(message.subscriptionId!!) as ((data: string) => void) | null;
          if (handler === null) {
            // eslint-disable-next-line max-len
            confirmSubscriptionEvent(remotingConfiguration.clientId, remotingId, message.subscriptionId!!, false);
            return;
          }
          handler(message.data);
          break;
        }
        case 'CLIENT_CALL': {
          internalRemotingConfiguration.clientCallHandler(remotingId, message);
          break;
        }
      }
    };
  });
}

async function awaitInitialization(remotingId:string) {
  const channelData = channels.get(remotingId)!;
  return new Promise<void>((resolve, reject) => {
    channelData.awaitingRequests.push({ resolve, reject });
  });
}

async function ensureChannel(clientId:string, remotingId:string) {
  const channelData = channels.get(remotingId);
  if (!channelData || channelData?.source?.readyState === EventSource.CLOSED) {
    await createChannel(clientId, remotingId);
  } else if (!channelData.source
    || (channelData.source && channelData.source.readyState === EventSource.CONNECTING)) {
    await awaitInitialization(remotingId);
  }
}

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
    await ensureChannel(clientId, remotingId);
    const result = await fetch(`/remoting/${remotingId}/request`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        groupId,
        methodId,
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

export async function performSubscription(
  remotingId:string,
  groupId:string,
  subscriptionId:string,
  request:string,
  preloaderHandler: PreloaderHandler|null,
  operationId: string | null,
  // eslint-disable-next-line no-unused-vars
  handler: (data: string) => void,
) {
  return wrapWithLoader(preloaderHandler, operationId, async () => {
    await ensureChannel(remotingConfiguration.clientId, remotingId);
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

export async function performUnsubscription(
  remotingId:string,
  subscriptionId:string,
  preloaderHandler: PreloaderHandler|null,
  operationId: string | null,
) {
  return wrapWithLoader(preloaderHandler, operationId, async () => {
    subscriptions.delete(subscriptionId);
    await ensureChannel(remotingConfiguration.clientId, remotingId);
    const result = await fetch(`/remoting/${remotingId}/unsubscribe`, {
      method: 'POST',
      headers: {
        'Content-Type': 'plain/text',
        subscriptionId,
        clientId: remotingConfiguration.clientId,
      },
    });
    if (result.status === 200) {
      return null;
    }
    throw new RemotingError(result.status, 'Error occurred', await result.text());
  });
}
