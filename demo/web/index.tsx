import { remotingClient } from './src-gen/demo-remoting';


(window as any).testRest = async () => {
  // eslint-disable-next-line max-len
  // const result = await serverCall<GetIndexesRequest, GetIndexesResponse>('elsa-demo-remoting', 'test', 'getIndexes', {
  //   document: {
  //     id: 1,
  //     type: 'com.gridnine.elsa.demo.model.domain.DemoDomainDocument',
  //     caption: 'test',
  //   },
  // }, true, null);
  const result = await remotingClient.get_indexes({
    document: {
      id: 1,
      type: 'com.gridnine.elsa.demo.model.domain.DemoDomainDocument',
      caption: 'test',
    },
  });
  console.log(result);
};
console.log('Initialized');
