/* eslint-disable no-unused-vars */
// noinspection JSUnusedLocalSymbols,JSUnusedGlobalSymbols
// noinspection JSUnusedLocalSymbols

import {
  GetL10nBundleDescriptionRequest,
  GetL10nBundleDescriptionResponse,
  RL10nMessageDescription,
} from '../src-gen/core-remoting';
import { performServerCall, PreloaderHandler, remotingConfiguration } from './core-remoting-low-level';
import { getTagDescription, initializeTypesMetadata } from './core-serialization';
import { RemotingCallOptions } from './core-remoting';

export type L10nCallOptions = RemotingCallOptions;

const bundles = new Map<string, Map<string, RL10nMessageDescription>>();

export const ensureBundleLoaded = async (
  bundleId: string,
  options?: L10nCallOptions,
) => {
  if (bundles.has(bundleId)) {
    return;
  }
  const ph = options?.preloaderHandler === false ? null : (options?.preloaderHandler === true
    ? (remotingConfiguration.globalPreloaderHandler || null) : options?.preloaderHandler) || null;
  const operationId = options?.operationId || null;
  await initializeTypesMetadata(ph, operationId);
  const result = (await performServerCall(
    remotingConfiguration.clientId,
    'core',
    'meta',
    'get-l10n-bundle-description',
       { bundleId } as GetL10nBundleDescriptionRequest,
       ph,
       operationId,
  )) as GetL10nBundleDescriptionResponse;
  const values = new Map<string, RL10nMessageDescription>();
  result.messages.forEach((msg) => {
    values.set(msg.id, msg);
  });
  bundles.set(bundleId, values);
};

export type TypeLocalizer<T> = {
  localize: (param:T) => string
}

const typesLocalizers = new Map<string, TypeLocalizer<any>>();

typesLocalizers.set('STRING', {
  localize: (str:string) => str,
});

const defaultLocalizer = {
  localize: (param) => param.toString(),
} as TypeLocalizer<any>;

const replace = (str: string, index: number, type: string, param: any|null) => {
  const localizer = typesLocalizers.get(type) ?? defaultLocalizer;
  const strValue = (param === null || param === undefined) ? '???' : localizer.localize(param);
  return str.replace(`{${index}}`, strValue);
};

export const getMessage = (bundleId: string, messageId: string, ...params: any|null) => {
  const messageDescription = bundles.get(bundleId)?.get(messageId);
  if (!messageDescription) {
    return messageId;
  }
  let result = messageDescription.displayName || '???';
  for (let n = 0; n < params.length; n += 1) {
    const param = messageDescription.parameters[n];
    const tagDescription = getTagDescription('L10N', param.tagName);
    result = replace(result, n, tagDescription.type, params[n]);
  }
  return result;
};
