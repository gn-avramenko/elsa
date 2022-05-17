/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.activator;

import com.gridnine.elsa.common.core.boot.ElsaActivator;
import com.gridnine.elsa.demo.model.domain.DemoUserAccount;
import com.gridnine.elsa.demo.model.domain.DemoUserAccountProjection;
import com.gridnine.elsa.demo.model.domain.DemoUserAccountProjectionFields;
import com.gridnine.elsa.server.core.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;

public class ElsaDemoActivator implements ElsaActivator {

    @Autowired
    private Storage storage;
    @Override
    public void activate() {
        if(storage.findUniqueDocument(DemoUserAccountProjection.class,
                DemoUserAccountProjectionFields.login, "admin", false) == null){
            {
                var adminProfile = new DemoUserAccount();
                adminProfile.setLogin("admin");
                adminProfile.setName("Admin");
                storage.saveDocument(adminProfile);
            }
            for(var n=1; n <= 10; n++){
                var userProfile = new DemoUserAccount();
                userProfile.setLogin("user"+n);
                userProfile.setName("User" + n);
                storage.saveDocument(userProfile);
            }
        }
    }

    @Override
    public double getPriority() {
        return 10;
    }
}
