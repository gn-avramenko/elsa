import { performServerCall, PreloaderHandler } from './remoting-low-level';

type ServerCallDescription = {
  requestClassName?: string,
  responseClassName?: string
}

type RemotingConfiguration = {
  globalPreloaderHandler?: PreloaderHandler
}

export const remotingConfiguration: RemotingConfiguration = {};

const serverCallDescriptions = new Map<string, ServerCallDescription>();

// eslint-disable-next-line import/prefer-default-export
export async function serverCall<RQ, RP>(
  remotingId: string,
  groupId: string,
  methodId: string,
  request: RQ,
  preloaderHandler: PreloaderHandler| boolean,
  operationId:string|null,
) {
  const fullId = `${remotingId}:${groupId}:${methodId}`;
  // eslint-disable-next-line no-nested-ternary,max-len
  const ph = preloaderHandler === false ? null : (preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : preloaderHandler);
  if (!serverCallDescriptions.has(fullId)) {
    const description = await performServerCall('core', 'meta', 'get-server-call-description', {
      remotingId,
      groupId,
      methodId,
    }, ph, operationId) as ServerCallDescription;
    serverCallDescriptions.set(fullId, description);
  }
  const rd = serverCallDescriptions.get(fullId);
  return rd as RP;
}
