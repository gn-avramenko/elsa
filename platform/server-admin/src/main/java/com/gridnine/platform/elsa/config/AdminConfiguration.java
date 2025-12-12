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

package com.gridnine.platform.elsa.config;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.standard.form.FormAclElementHandler;
import com.gridnine.platform.elsa.admin.acl.standard.form.FormRemoteSelectAclElementHandler;
import com.gridnine.platform.elsa.admin.acl.standard.form.FormTextFieldAclElementHandler;
import com.gridnine.platform.elsa.admin.acl.standard.grid.GridAclElementHandler;
import com.gridnine.platform.elsa.admin.common.BooleanValueRenderer;
import com.gridnine.platform.elsa.admin.common.RenderersRegistry;
import com.gridnine.platform.elsa.admin.common.RestrictionsValueRenderer;
import com.gridnine.platform.elsa.admin.web.common.AutocompleteUtils;
import com.gridnine.platform.elsa.admin.web.mainFrame.HomePageRouterPathHandler;
import com.gridnine.platform.elsa.admin.workspace.LinkWorkspaceItemHandler;
import com.gridnine.platform.elsa.admin.workspace.ListWorkspaceItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminConfiguration {

    @Bean
    public ListWorkspaceItemHandler listWorkspaceItemHandler(){
        return new ListWorkspaceItemHandler();
    }

    @Bean
    public LinkWorkspaceItemHandler linkWorkspaceItemHandler(){
        return new LinkWorkspaceItemHandler();
    }

    @Bean
    public HomePageRouterPathHandler  homePageRouterPathHandler(){
        return new HomePageRouterPathHandler();
    }

    @Bean
    public AclEngine aclEngine(){
        return new AclEngine();
    }

    @Bean
    public RenderersRegistry renderersRegistry(){
        return new RenderersRegistry();
    }

    @Bean
    public BooleanValueRenderer   booleanValueRenderer(){
        return new BooleanValueRenderer();
    }

    @Bean
    public RestrictionsValueRenderer  restrictionsValueRenderer(){
        return new RestrictionsValueRenderer();
    }

    @Bean
    public AutocompleteUtils  autocompleteUtils(){
        return new AutocompleteUtils();
    }

    @Bean
    public FormAclElementHandler  formAclElementHandler(){
        return new FormAclElementHandler();
    }

    @Bean
    public GridAclElementHandler gridAclElementHandler(){
        return new GridAclElementHandler();
    }

    @Bean
    public FormTextFieldAclElementHandler  formTextFieldAclElementHandler(){
        return new FormTextFieldAclElementHandler();
    }

    @Bean
    public FormRemoteSelectAclElementHandler  formRemoteSelectAclElementHandler(){
        return new FormRemoteSelectAclElementHandler();
    }
}
