/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting.standard;

import com.gridnine.elsa.common.model.remoting.GetL10nBundleDescriptionRequest;
import com.gridnine.elsa.common.model.remoting.GetL10nBundleDescriptionResponse;
import com.gridnine.elsa.common.model.remoting.RL10nMessageDescription;
import com.gridnine.elsa.common.model.remoting.RPropertyDescription;
import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.server.remoting.RemotingCallContext;
import com.gridnine.elsa.server.remoting.RemotingServerCallHandler;

public class L10nBundleDescriptionRemotingHandler implements RemotingServerCallHandler<GetL10nBundleDescriptionRequest, GetL10nBundleDescriptionResponse> {
    @Override
    public GetL10nBundleDescriptionResponse service(GetL10nBundleDescriptionRequest request, RemotingCallContext context) throws Exception {
        L10nMessagesBundleDescription bundle = L10nMetaRegistry.get().getBundles().get(request.getBundleId());
        var result = new GetL10nBundleDescriptionResponse();
        bundle.getMessages().values().forEach(msg ->{
            var item = new RL10nMessageDescription();
            result.getMessages().add(item);
            item.setId(msg.getId());
            var displayName = LocaleUtils.getLocalizedName(msg.getDisplayNames(),
                    LocaleUtils.getCurrentLocale() == null? LocaleUtils.ruLocale: LocaleUtils.getCurrentLocale(), msg.getId());
            item.setDisplayName(displayName);
            msg.getParameters().values().forEach(par ->{
                var item2 = new RPropertyDescription();
                item2.setTagName(par.getTagName());
                item2.setId(par.getId());
                item.getParameters().add(item2);
            });
        });
        return result;
    }
}
