/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */
import {
  EntityReference,
  ServerCallOptions,
  SubscriptionOptions,
  UnsubscriptionOptions,
  serverCall,
  subscribe,
  unsubscribe,
} from 'elsa-core';
import {
  DemoDomainDocumentProjection,
} from './demo-domain';

export type GetIndexesRequest = {
  document?: EntityReference,
};

export type GetIndexesResponse = {
  indexes: DemoDomainDocumentProjection[],
};

export type DownloadIndexRequest = {
  fileId?: string,
};

export type UploadFileRequest = {
  fileId?: string,
};

export type DemoDocumentChangedEvent = {
  document: EntityReference,
};

export const remotingClient = {

  elsa_demo_remoting_test_getIndexes: (request:GetIndexesRequest, options?:ServerCallOptions) => serverCall<GetIndexesRequest, GetIndexesRequest>('elsa-demo-remoting', 'test', 'getIndexes', request, options),

  // eslint-disable-next-line no-unused-vars
  elsa_demo_remoting_test_demo_document_changed_subscription_subscribe: (handler: (ev:DemoDocumentChangedEvent) => Promise<void>, options?:SubscriptionOptions) => subscribe<undefined, DemoDocumentChangedEvent>('elsa-demo-remoting', 'test', 'demo-document-changed-subscription', undefined, handler, options),

  elsa_demo_remoting_test_demo_document_changed_subscription_unsubscribe: (callId:string, options?:UnsubscriptionOptions) => unsubscribe('elsa-demo-remoting', 'test', 'demo-document-changed-subscription', callId, options),
};
