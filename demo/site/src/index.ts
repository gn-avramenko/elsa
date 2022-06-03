import { Client } from '@stomp/stompjs';

const restWsClient = new Client({
  // brokerURL: 'ws://localhost:8086/restws',
  brokerURL: 'ws://localhost:8086/restws',
  debug(str) {
    console.log(str);
  },
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
  onConnect: (frame) => {
    console.log(`connected ${frame}`);
  },
  onStompError: (frame) => {
    console.log(`Broker reported error: ${frame.headers.message}`);
    console.log(`Additional details: ${frame.body}`);
  },
});
restWsClient.activate();

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

setTimeout(() => {
  // restWsClient.subscribe('/ws/user/123/ws/user/queue/specific-user', (msg) => {
  //   console.log(msg.body);
  // });
  restWsClient.subscribe('/user/queue/specific-user', (msg) => {
    console.log(msg.body);
  });
  // restWsClient.subscribe('/topic', (msg) => {
  //   console.log(msg.body);
  // });
  restWsClient.publish({
    destination: '/app/test',
    headers: { param: 'paramValue' },
    body: 'Hello world',
    skipContentLengthHeader: true,
  });
}, 2000);
