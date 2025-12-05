package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclActionMetadata;
import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;

public class AllActionsMetadata extends AclActionMetadata<Void> {
    public static final String ACTION_ID = "all-actions";
    public AllActionsMetadata(Localizer localizer) {
        setId(ACTION_ID);
        setName(LocaleUtils.createLocalizable(AdminL10nFactory.All_ActionsMessage(), localizer));
        setRendererId(BooleanValueAclRenderer.RENDERER_ID);
    }
}
