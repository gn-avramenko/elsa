/* ****************************************************************
 * This is generated code, don't modify it manually
 **************************************************************** */
import {
  L10nCallOptions,
  ensureBundleLoaded,
  getMessage,
} from 'elsa-core';

const l10nFactory = {

  ensureLoaded: (options?: L10nCallOptions) => ensureBundleLoaded('demo', options),

  Message_with_string_param: (param: string) => getMessage('demo', 'Message_with_string_param', param),
};

export default l10nFactory;
