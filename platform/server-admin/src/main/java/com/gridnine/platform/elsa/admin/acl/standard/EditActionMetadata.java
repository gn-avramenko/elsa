package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclActionMetadata;
import com.gridnine.platform.elsa.admin.common.BooleanValueRenderer;
import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;

public class EditActionMetadata extends AclActionMetadata<Void> {
    public static final String ACTION_ID = "edit";
    public EditActionMetadata(Localizer localizer) {
        setId(ACTION_ID);
        setName(LocaleUtils.createLocalizable(AdminL10nFactory.EditMessage(), localizer));
        setRendererId(BooleanValueRenderer.RENDERER_ID);
    }
}
