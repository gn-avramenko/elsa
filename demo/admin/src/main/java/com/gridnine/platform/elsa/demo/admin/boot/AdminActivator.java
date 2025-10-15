/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.demo.admin.boot;

import com.gridnine.platform.elsa.admin.domain.*;
import com.gridnine.platform.elsa.common.core.boot.ElsaActivator;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.admin.domain.Country;
import com.gridnine.platform.elsa.demo.admin.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminActivator implements ElsaActivator {
    @Autowired
    private Storage storage;

    @Override
    public void activate() throws Exception {
        checkAdminWorkspace();
    }

    private void checkAdminWorkspace() {
        if(storage.findUniqueDocument(WorkspaceProjection.class, WorkspaceProjectionFields.userLogin, "admin", false) != null){
            return;
        }
        var adminWorkspace = new Workspace();
        adminWorkspace.setUserLogin("admin");
        {
            var group = new WorkspaceGroup();
            group.setName("Entities");
            group.setIcon("MenuUnfoldOutlined");
            adminWorkspace.getGroups().add(group);
            {
                var countries = new ListWorkspaceItem();
                countries.setName("Countries");
                countries.setId(Country.class.getName());
                countries.setIcon("CloudOutlined");
                group.getItems().add(countries);
            }
            {
                var organizations = new ListWorkspaceItem();
                organizations.setName("Organizations");
                organizations.setId(Organization.class.getName());
                organizations.setIcon("CameraOutlined");
                group.getItems().add(organizations);
            }
        }
        storage.saveDocument(adminWorkspace, "init");
    }


    private <E extends Enum<E>> E getRandomEnum(Class<E> documentFormatClass) {
        var ordinal = (int) Math.round(Math.random() * (documentFormatClass.getEnumConstants().length - 1));
        return documentFormatClass.getEnumConstants()[ordinal];
    }

    private boolean getRandomBoolean() {
        return Math.random() > 0.5;
    }

    private String getRandomString(List<String> words) {
        return words.get((int) Math.round(Math.random() * (words.size() - 1)));
    }

    @Override
    public double getPriority() {
        return 0;
    }
}
