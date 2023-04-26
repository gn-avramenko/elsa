import { remotingClient } from './src-gen/demo-remoting';

(window as any).testRest = async () => {
  const result = await remotingClient.elsa_demo_remoting_test_getIndexes({
    document: {
      id: 1,
      type: 'com.gridnine.elsa.demo.model.domain.DemoDomainDocument',
      caption: 'test',
    },
  });
  console.log(result);
};
console.log('Initialized');
