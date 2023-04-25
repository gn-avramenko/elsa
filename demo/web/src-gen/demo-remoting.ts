/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */

import {
  EntityReference,
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
