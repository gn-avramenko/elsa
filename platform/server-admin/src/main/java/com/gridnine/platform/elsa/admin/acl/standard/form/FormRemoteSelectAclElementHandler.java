package com.gridnine.platform.elsa.admin.acl.standard.form;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclPropertyMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.*;
import com.gridnine.platform.elsa.admin.common.RestrictionsValueRenderer;
import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.admin.web.common.RestrictionsEditor;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormComponentType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormRemoteSelectDescription;
import com.gridnine.platform.elsa.common.meta.common.StandardValueType;
import com.gridnine.platform.elsa.common.meta.domain.DatabasePropertyType;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FormRemoteSelectAclElementHandler implements AclElementHandler<FormRemoteSelectDescription> {
    @Autowired
    private Localizer localizer;

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Override
    public String getElementId() {
        return "admin-ui-form-%s".formatted(FormComponentType.REMOTE_SELECT.name());
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, FormRemoteSelectDescription element, AclEngine aclEngine) throws Exception {
        var fieldMetadata = new AclMetadataElement();
        fieldMetadata.setId("%s.%s".formatted(parent.getId(), element.getId()));
        fieldMetadata.setName(LocaleUtils.createLocalizable(element.getTitle()));
        fieldMetadata.getActions().add(new AllActionsMetadata(localizer));
        fieldMetadata.getActions().add(new EditActionMetadata(localizer));
        fieldMetadata.getActions().add(new ViewActionMetadata(localizer));
        List<RestrictionsEditor.RestrictionPropertyMetadata> props = new ArrayList<>();
        var ad = domainMetaRegistry.getAssets().get(element.getClassName());
        {
            var prop = new RestrictionsEditor.RestrictionPropertyMetadata("_id", "%s-%s".formatted(StandardValueType.ENTITY_REFERENCE, ad.getId()), null, LocaleUtils.createLocalizable(AdminL10nFactory.List_ObjectMessage(), localizer));
            props.add(prop);
        }
        ad.getProperties().values().forEach(it ->{
            if(it.getType() != DatabasePropertyType.ENTITY_REFERENCE) {
                //TODO process all types
                return;
            }
            var prop = new RestrictionsEditor.RestrictionPropertyMetadata(it.getId(), "%s-%s".formatted(it.getType().name(), it.getClassName()), null, LocaleUtils.createLocalizable(it.getDisplayNames()));
            props.add(prop);
        });
        fieldMetadata.getActions().add(new ListRestrictionsMetadata(localizer, new RestrictionsValueRenderer.RestrictionsValueParameters(Collections.unmodifiableList(props))));
        {
            var prop = new AclPropertyMetadata<Void>();
            prop.setName(LocaleUtils.createLocalizable(element.getTitle()));
            prop.setId(element.getId());
            prop.setRestrictionRendererId( "%s-%s".formatted(StandardValueType.ENTITY_REFERENCE.name(), element.getClassName()));
            parent.getProperties().add(prop);
        }
        aclEngine.addNode(parent.getId(), fieldMetadata);
    }
}
