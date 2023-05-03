import { serverCall } from 'elsa-core';
import { remotingClient } from './src-gen/demo-remoting';
import l10nFactory from './src-gen/demo-l10n';

(window as any).testRest = async () => {
  const result = await remotingClient.elsa_demo_remoting_test_getIndexes({
    document: {
      id: 1,
      type: 'com.gridnine.elsa.demo.model.domain.DemoDomainDocument',
      caption: 'test',
    },
  });
  const sc = serverCall;
  console.log(sc);
  console.log(result);
};
let subId: string| null;
(window as any).testSubscribe = async () => {
  // eslint-disable-next-line max-len
  const result = await remotingClient.elsa_demo_remoting_test_demo_document_changed_subscription_subscribe((ev) => {
    console.log(`recieved ${ev.document.caption}`);
    return Promise.resolve();
  });
  subId = result;
};

(window as any).testUnsubscribe = async () => {
  // eslint-disable-next-line max-len
  await remotingClient.elsa_demo_remoting_test_demo_document_changed_subscription_unsubscribe(subId!!);
};

(window as any).testL10n = async () => {
  await l10nFactory.ensureLoaded();
  console.info(l10nFactory.Message_with_string_param('Hello world'));
};

console.log('Initialized');
