/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.userAccount;

import com.gridnine.elsa.demo.model.domain.DemoUserAccount;
import com.gridnine.elsa.demo.model.domain.DemoUserAccountProjection;
import com.gridnine.elsa.server.core.storage.SearchableProjectionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DemoUserAccountProjectionHandler implements SearchableProjectionHandler<DemoUserAccount, DemoUserAccountProjection> {
    @Override
    public Class<DemoUserAccount> getDocumentClass() {
        return DemoUserAccount.class;
    }

    @Override
    public Class<DemoUserAccountProjection> getProjectionClass() {
        return DemoUserAccountProjection.class;
    }

    @Override
    public List<DemoUserAccountProjection> createProjections(DemoUserAccount doc, Set<String> properties) throws Exception {
        var proj = new DemoUserAccountProjection();
        proj.setName(doc.getName());
        proj.setLogin(doc.getLogin());
        return Collections.singletonList(proj);
    }
}
