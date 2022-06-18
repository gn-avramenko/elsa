/* eslint-disable no-unused-vars */
// noinspection JSUnusedLocalSymbols

import { Client, StompSubscription } from '@stomp/stompjs';
import { generateUUID } from './common';

type RemotingConfiguration = {
  baseConnectionUrl: string,
  showDebugMessages: boolean,
  clientId: string,
}

export const remotingConfiguration:RemotingConfiguration = {
  baseConnectionUrl: `ws://${window.location.host}`,
  clientId: generateUUID(),
  showDebugMessages: false,
};

type SubscriptionStatus = 'NEW' | 'INITIALIZING' | 'ACTIVE' | 'DEACTIVATING'

type ChannelData = {
    client: Client,
    status: SubscriptionStatus,
    replySubscription: StompSubscription|null,
  // eslint-disable-next-line no-unused-vars
    pendingInitializations: ({resolve: ()=>void, reject: (e:any) =>void})[]
  // eslint-disable-next-line no-unused-vars
  pendingRequests: ({requestId: number, resolve: ()=>void, reject: (e:any) =>void})[]
}

const channels = new Map<string, ChannelData>();

const cancelRequests = (channel:ChannelData) => {
  channel.pendingRequests.forEach((it) => it.reject(new Error('Connection is closed')));
};

const awaitRestInitialization = async (restId:string) => {
  const channel = channels.get(restId);
  if (channel && channel.status === 'DEACTIVATING') {
    throw new Error('channel is deactivating');
  }
  if (channel && channel.status === 'INITIALIZING') {
    return new Promise<void>((resolve, reject) => {
      channel?.pendingInitializations?.push({ resolve, reject });
    });
  }
  const brokerURL = `ws://${remotingConfiguration.baseConnectionUrl}/${restId}?clientId=${remotingConfiguration.clientId}`;
  return new Promise<void>((resolve, reject) => {
    let channelInt:ChannelData;
    const client = new Client({
      brokerURL,
      debug(str) {
        if (remotingConfiguration.showDebugMessages) {
          console.log(str);
        }
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onWebSocketClose: async () => {
        cancelRequests(channel!);
        switch (channel!.status) {
          case 'INITIALIZING': {
            reject(new Error(`unable to establish connection to ${brokerURL}`));
            break;
          }
          case 'ACTIVE': {
            channelInt!.status = 'DEACTIVATING';
            channelInt!.replySubscription!.unsubscribe();
            try {
              await client.deactivate();
            } catch (e2:any) {
              // noops
            }
            channels.delete(restId);
            break;
          }
          default: {
            // noops
          }
        }
      },
      // onStompError: (frame) => {
      //   console.log(`Broker reported error: ${frame.headers.message}`);
      //   console.log(`Additional details: ${frame.body}`);
      // },
      onConnect: () => {
        channelInt!.replySubscription = client.subscribe(`/user/${remotingConfiguration.clientId}/${restId}/reply`, (msg) => {
          // const { requestId } = msg.headers;
          // const payload = msg.body

        });
        resolve();
      },
    });
    channelInt = {
      client,
      status: 'NEW',
      replySubscription: null,
      pendingInitializations: [],
      pendingRequests: [],
    };
    channels.set(restId, channelInt);
    client.activate();
  });
};

// const callRaw = async (restId:string, methodId: string, payload:string) => {
//   if (channels.get(restId)?.status !== 'ACTIVE') {
//     await awaitRestInitialization(restId);
//   }
// };

// eslint-disable-next-line no-unused-vars
let resolve:((b: boolean) => void)| null = null;
let subscription: StompSubscription | null = null;
const clientId = generateUUID();
const restWsClient = new Client({
  brokerURL: `ws://localhost:8086/restws?clientId=${clientId}`,
  debug(str) {
    console.log(str);
  },
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
  onWebSocketClose: () => {
    console.log('onClose');
  },
  onStompError: (frame) => {
    console.log(`Broker reported error: ${frame.headers.message}`);
    console.log(`Additional details: ${frame.body}`);
  },
  onConnect: () => {
    subscription = restWsClient.subscribe('/user/123/response', (msg) => {
      console.log(msg.body);
    });
    resolve!!(true);
  },
});

export const connect = () => new Promise<boolean>(((resolve1) => {
  resolve = resolve1;
  restWsClient.activate();
}));

export const sendMessage = () => {
  restWsClient.publish({
    destination: '/app/request',
    body: 'Hello world',
    skipContentLengthHeader: true,
  });
};

export const deactivate = async () => {
  resolve = null;
  if (subscription) {
    subscription?.unsubscribe();
  }
  await restWsClient.deactivate();
};
