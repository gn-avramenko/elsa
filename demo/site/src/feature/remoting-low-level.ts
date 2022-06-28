import { generateUUID, RemotingError } from './common';

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

const channels = new Map<string, ChannelData>();

async function createChannel(remotingId:string) {
  const channel:ChannelData = { awaitingRequests: [] };
  channels.set(remotingId, channel);
  const aPath = `/remoting/${remotingId}/check`;
  await new Promise<void>((resolve, reject) => {
    fetch(aPath, {
      method: 'GET',
    }).then((result) => {
      if (result.status === 200) {
        resolve();
        return;
      }
      const er = result.status === 403 ? new RemotingError(403, 'no access')
        : new RemotingError(result.status, 'unable to check');
      channel.awaitingRequests.forEach((s) => {
        s.reject(er);
      });
      channels.delete(remotingId);
      reject(er);
    }).catch((error) => {
      const er = new RemotingError(error.status, 'unable to check');
      channel.awaitingRequests.forEach((s) => {
        s.reject(er);
      });
      channels.delete(remotingId);
      reject(er);
    });
  });
  return new Promise<void>((resolve, reject) => {
    const source = new EventSource(`/remoting/${remotingId}/subscribe`);
    channel.source = source;
    source.onopen = function () {
      if (this.readyState === EventSource.OPEN) {
        resolve();
        channel.awaitingRequests.forEach((s) => {
          s.resolve();
        });
      }
    };
    source.onerror = function () {
      if (source.readyState === EventSource.CLOSED) {
        const error = new Error('unable to subscribe to server events');
        reject(error);
        channel.awaitingRequests.forEach((s) => {
          s.reject(error);
        });
      }
    };
    source.onmessage = (ev) => {
      console.log(ev.data);
    };
  });
}

async function awaitInitialization(remotingId:string) {
  const channelData = channels.get(remotingId)!;
  return new Promise<void>((resolve, reject) => {
    channelData.awaitingRequests.push({ resolve, reject });
  });
}

async function performServerCallInternal(
  remotingId:string,
  groupId:string,
  methodName:string,
  request:any|null,
) {
  const channelData = channels.get(remotingId);
  if (!channelData || channelData?.source?.readyState === EventSource.CLOSED) {
    await createChannel(remotingId);
  } else if (!channelData.source
    || (channelData.source && channelData.source.readyState === EventSource.CONNECTING)) {
    await awaitInitialization(remotingId);
  }
  return new Promise<any>((resolve, reject) => {
    const aPath = `/remoting/${remotingId}/request`;
    fetch(aPath, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        group: groupId,
        method: methodName,
      },
      body: request && JSON.stringify(request),
    }).then((result) => {
      if (result.status === 204) {
        resolve(null);
        return;
      }
      result.json().then((payload) => {
        if (result.status !== 200) {
          reject(new RemotingError(result.status, payload.message, payload.details));
        } else {
          resolve(payload);
        }
      }).catch((error) => {
        reject(error);
      });
    }).catch((error) => {
      reject(error);
    });
  });
}

type AsyncCallState = {
  operationId: string
  firstCallId: string
  loaderShown: boolean
  lastCallId: string
}

const asyncCalls = new Map<string, AsyncCallState>();

// eslint-disable-next-line import/prefer-default-export
export async function performServerCall(
  remotingId: string,
  groupId: string,
  methodId: string,
  request: any|null,
  preloaderHandler: PreloaderHandler|null,
  operationId: string | null,
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
    return performServerCallInternal(remotingId, groupId, methodId, request);
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
