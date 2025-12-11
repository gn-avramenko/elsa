package com.gridnine.platform.elsa.admin.web.common;

import com.gridnine.platform.elsa.admin.web.form.FormRemoteMultiSelectAutocompleteRequest;
import com.gridnine.platform.elsa.admin.web.form.FormRemoteMultiSelectAutocompleteResponse;
import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.webApp.WebAppServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class AutocompleteUtils {
    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Autowired
    private Storage storage;

    public<E extends BaseIdentity> WebAppServiceHandler<FormRemoteMultiSelectAutocompleteRequest, FormRemoteMultiSelectAutocompleteResponse> createStandardAutocomplete(Class<E> cls){
        return (request, context) -> {
            var refs = storage.searchCaptions(cls, request.getPattern(), 10);
            var result = new FormRemoteMultiSelectAutocompleteResponse();
            result.getItems().addAll(refs.stream().map(it ->{
                var item = new Option();
                item.setId(it.getId());
                item.setDisplayName(it.getCaption());
                return item;
            }).toList());
            return result;
        };
    }

}
