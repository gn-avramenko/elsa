import {
  BaseClientCallHandler,
  serverCall, subscribe, unsubscribe,
} from './feature/remoting';
import { registry } from './feature/common';
import { DemoTestClientCallRequest, DemoTestClientCallResponse } from '../src-gen/demo-test-types';

const w = window as any;

// eslint-disable-next-line max-len
class DemoClientCallHandler extends BaseClientCallHandler<DemoTestClientCallRequest, DemoTestClientCallResponse> {
  handle(request: DemoTestClientCallRequest): DemoTestClientCallResponse {
    return {
      stringProperty: request.param,
    };
  }

  getId(): string {
    return 'demo:test:client-call';
  }
}
registry.register(new DemoClientCallHandler());
w.processPublic = async () => {
  // const result = await serverCall('demo', 'test', 'server-call', {
  //   param: 'test',
  // }, false, null);
  // console.log(result);
  const subId = await subscribe('demo', 'test', 'subscription', {
    param: 'test',
  }, (ev) => {
    console.log('subscription event', ev);
    return true;
  }, false, null);

  await serverCall('demo', 'test', 'initiate-subscription', null, false, null);

  console.log(subId);

  setTimeout(() => {
    unsubscribe('demo', subId, false, null);
  }, 3000);
};
w.processPublic2 = async () => {
  const result = await serverCall('demo', 'test', 'server-call', {
    param: 'test',
  }, false, null);
  console.log(result);
};
