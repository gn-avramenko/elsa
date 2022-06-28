import { generateUUID, RemotingError } from './common';

type RemotingConfiguration = {
  clientId: string,
}

export const remotingConfiguration:RemotingConfiguration = {
  clientId: generateUUID(),
};

type ChannelData = {
  // eslint-disable-next-line no-unused-vars
  awaitingRequests: ({resolve: ()=>void, reject: (e:any) =>void})[],
  source?: EventSource,
}

const channels = new Map<string, ChannelData>();

async function createChannel(restId:string) {
  const channel:ChannelData = { awaitingRequests: [] };
  channels.set(restId, channel);
  const aPath = `/remoting/${restId}/check`;
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
        s.resolve();
      });
      channels.delete(restId);
      reject(er);
    }).catch((error) => {
      const er = new RemotingError(error.status, 'unable to check');
      channel.awaitingRequests.forEach((s) => {
        s.reject(er);
      });
      channels.delete(restId);
      reject();
    });
  });
  return new Promise<void>((resolve, reject) => {
    const source = new EventSource(`/remoting/${restId}/subscribe`);
    channel.source = source;
    source.onopen = function () {
      console.log('on open');
      if (this.readyState === EventSource.OPEN) {
        resolve();
        channel.awaitingRequests.forEach((s) => {
          s.resolve();
        });
      }
    };
    source.onerror = function (ev) {
      console.log('on error', ev);
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

async function awaitInitialization(restId:string) {
  const channelData = channels.get(restId)!;
  return new Promise<void>((resolve, reject) => {
    channelData.awaitingRequests.push({ resolve, reject });
  });
}
export async function callRawRest(restId:string, methodName:string, request:any) {
  const channelData = channels.get(restId);
  if (!channelData || channelData?.source?.readyState === EventSource.CLOSED) {
    await createChannel(restId);
  } else if (!channelData.source
    || (channelData.source && channelData.source.readyState === EventSource.CONNECTING)) {
    await awaitInitialization(restId);
  }
  return new Promise<any>((resolve, reject) => {
    const aPath = `/remoting/${restId}/${methodName}`;
    fetch(aPath, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    }).then((result) => {
      result.json().then((payload) => {
        if (result.status !== 200) {
          reject(new Error(payload.message));
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
