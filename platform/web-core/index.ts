// eslint-disable-next-line import/prefer-default-export
export type {
    EntityReference,
    BaseDocument,
    BaseIdentity,
    VersionInfo,
    BaseAsset,
    BaseProjection,
} from './src/core-model-entities';
export {
    BaseAPI, FetchError, ResponseError
} from './src/core-remoting'
export type {
    ServiceData, ConfigurationParameters, Configuration, Middleware,
    HTTPRequestInit, InitOverrideFunction, HTTPBody
} from './src/core-remoting'
export type {
    HTTPMethod
} from './src/core-metadata-provider'
export {
    BaseL10nApi
} from './src/core-l10n'
export {
    isNull, isNotNull, isNotBlank, isBlank, bytes2Str, str2Bytes, formatDigit, generateUUID
} from './src/core-utils'
export {
    registry
} from './src/core-registry'
export type {
    RegistryItem, RegistryItemType
} from './src/core-registry'
export { WebSocketFacade, SubsequentSubscriptionsManager} from './src/core-websocket'
export type {WebSocketParams, SubscriptionParams } from './src/core-websocket'