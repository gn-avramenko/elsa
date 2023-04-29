/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

import {
  EntityReference,
  ServerCallOptions,
  serverCall,
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
  document?: EntityReference,
};

export const remotingClient = {

  elsa_demo_remoting_test_getIndexes: (request:GetIndexesRequest, options?:ServerCallOptions) => serverCall<GetIndexesRequest, GetIndexesRequest>('elsa-demo-remoting', 'test', 'getIndexes', request, options),
};
