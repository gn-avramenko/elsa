import { Client } from '@stomp/stompjs';

const restWsClient = new Client({
  brokerURL: window.location.host === 'localhost:3000' ? 'ws://localhost:8086/restws?clientId=123' : `ws://${window.location.host}/restws`,
  // brokerURL: `ws://${window.location.host}/restws`,
  connectHeaders: {
    clientId: '123',
  },
  debug(str) {
    console.log(str);
  },
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
  onConnect: (frame) => {
    console.log(`connected ${frame}`);
    restWsClient.subscribe('/user/123/response', (msg) => {
      console.log(msg.body);
    });
  },
  onStompError: (frame) => {
    console.log(`Broker reported error: ${frame.headers.message}`);
    console.log(`Additional details: ${frame.body}`);
  },
});
restWsClient.activate();

setTimeout(() => {
  restWsClient.publish({
    destination: '/app/request',
    headers: { param: 'paramValue' },
    body: 'Hello world',
    skipContentLengthHeader: true,
  });
}, 1000);

// setTimeout(() => {
//   // restWsClient.subscribe('/ws/user/123/ws/user/queue/specific-user', (msg) => {
//   //   console.log(msg.body);
//   // });
//   restWsClient.subscribe('/topic', (msg) => {
//     console.log(msg.body);
//   });
//   restWsClient.publish({
//     destination: '/app/restws',
//     headers: { param: 'paramValue' },
//     body: 'Hello world',
//     skipContentLengthHeader: true,
//   });
// }, 2000);

// setTimeout(() => {
//   // restWsClient.subscribe('/ws/user/123/ws/user/queue/specific-user', (msg) => {
//   //   console.log(msg.body);
//   // });
//   restWsClient.subscribe('/user/123/response', (msg) => {
//     console.log(msg.body);
//   });
//   // restWsClient.subscribe('/topic', (msg) => {
//   //   console.log(msg.body);
//   // });
//   restWsClient.publish({
//     destination: '/app/request',
//     headers: { param: 'paramValue' },
//     body: 'Hello world',
//     skipContentLengthHeader: true,
//   });
// }, 2000);
