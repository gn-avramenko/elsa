import { serverCall } from 'elsa-core';
import { GetIndexesRequest, GetIndexesResponse } from './src-gen/demo-remoting';

(window as any).testRest = async () => {
  const result = await serverCall<GetIndexesRequest, GetIndexesResponse>('elsa-demo-remoting', 'test', 'getIndexes', {
    document: {
      id: 1,
      type: 'com.gridnine.elsa.demo.model.domain.DemoDomainDocument',
      caption: 'test',
    },
  }, true, null);
  console.log(result);
};
console.log('Initialized');
