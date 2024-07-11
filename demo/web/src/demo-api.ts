import { BaseAPI, Configuration } from 'elsa-web-core';

export type TestRequest = {
  testParam: string
}

export type TestResponse = {
  testData: string
}

export default class DemoApi extends BaseAPI {
  constructor(configuration:Configuration) {
    super(configuration, {
      remotingId: 'demo',
      groupId: 'main',
    });
  }

  async testRequest(request: TestRequest) {
    return (await this.request({
      request,
      serviceId: 'test-request',
    })).response as TestResponse;
  }
}
