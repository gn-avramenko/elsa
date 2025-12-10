package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclActionMetadata;
import com.gridnine.platform.elsa.admin.common.BooleanValueRenderer;
import com.gridnine.platform.elsa.admin.common.RestrictionsValueRenderer;
import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;

public class ListRestrictionsMetadata extends AclActionMetadata<RestrictionsValueRenderer.RestrictionsValueParameters> {
    public static final String ACTION_ID = "list-restrictions";
    public ListRestrictionsMetadata(Localizer localizer, RestrictionsValueRenderer.RestrictionsValueParameters params) {
        setId(ACTION_ID);
        setName(LocaleUtils.createLocalizable(AdminL10nFactory.List_RestrictionsMessage(), localizer));
        setRendererId(RestrictionsValueRenderer.RENDERER_ID);
        setRendererParameters(params);
    }
}
