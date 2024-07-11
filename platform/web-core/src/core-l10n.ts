/* eslint-disable no-unused-vars */
// noinspection JSUnusedLocalSymbols,JSUnusedGlobalSymbols
// noinspection JSUnusedLocalSymbols

import {L10nMessageDescription, L10nMessagesBundleDescription, StandardValueType} from "./core-metadata-provider";
import {BaseAPI, Configuration} from "./core-remoting";


export class BaseL10nApi extends BaseAPI{
  private messages = new Map<string, L10nMessageDescription>();

  private loaded = false

  constructor(protected configuration: Configuration, protected bundleId: string) {
    super(configuration, {
      remotingId: 'core',
      groupId: 'meta',
    })
  }

  public async ensureBundleLoaded(){
    if (this.loaded) {
      return;
    }
    const response = (await this.request({
      request: {bundleId: this.bundleId},
      serviceId: 'get-l10n-bundle-description'
    })).response as L10nMessagesBundleDescription
    response.messages.forEach((msg) => {
      this.messages.set(msg.id, msg);
    });
    this.loaded = true
  }

  protected getMessage(messageId: string, ...params: any|null) {
    const messageDescription = this.messages.get(messageId);
    if (!messageDescription) {
      return messageId;
    }
    let result = messageDescription.displayName || '???';
    for (let n = 0; n < params.length; n += 1) {
      const param = messageDescription.parameters[n];
      result = BaseL10nApi.replace(result, n, param.type, params[n]);
    }
    return result;
  };


  private static replace(template: string, index: number, type: StandardValueType, value: any|null){
    let strValue = '???'
    if(value !== null && value !== undefined){
      switch (type) {
        default:
          strValue = value.toString()
      }
    }
    return template.replace(`{${index}}`, strValue);
  };
}
