import { Client, StompSubscription } from '@stomp/stompjs';
import exp = require('constants');

// eslint-disable-next-line no-unused-vars
let resolve:((b: boolean) => void)| null = null;
let subscription: StompSubscription | null = null;
const restWsClient = new Client({
  brokerURL: 'ws://localhost:8086/restws?clientId=123',
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
