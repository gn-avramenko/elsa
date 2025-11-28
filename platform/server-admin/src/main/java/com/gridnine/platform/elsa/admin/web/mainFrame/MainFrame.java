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
 *
 *****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.domain.BaseWorkspaceItem;
import com.gridnine.platform.elsa.admin.utils.AdminParameters;
import com.gridnine.platform.elsa.admin.web.common.ContentWrapperConfiguration;
import com.gridnine.platform.elsa.admin.workspace.WorkspaceItemHandler;
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAndArgument;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.IoUtils;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.platform.elsa.webApp.common.ContentWrapper;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.GlobalUiContext;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.ListableBeanFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MainFrame extends MainFrameSkeleton{

    private ListableBeanFactory factory;
    private volatile Map<String, WorkspaceItemHandler<?>> itemHandlers;
    private RunnableWithExceptionAndArgument<OperationUiContext> confirmationCallback;
    private AdminL10nFactory aL10nFactory;
	public MainFrame(String tag, OperationUiContext ctx) throws Exception {
		super(tag, ctx);
        var path = ctx.getParameter(OperationUiContext.PARAMS).get("initPath").getAsString();
        setEmbeddedMode(path.contains("embeddedMode=true"), ctx);
        setTitle(getMainRouter().getTitle(), ctx);
        setAppName(getTitle(), ctx);
        setBackUrl(getMainRouter().getInitBackUrl(), ctx);
        if(Boolean.TRUE.equals(isEmbeddedMode())){
            var locale = path.contains("lang=ru")? LocaleUtils.ruLocale: Locale.ENGLISH;
            GlobalUiContext.setParameter(ctx.getParameter(OperationUiContext.PATH), ctx.getParameter(OperationUiContext.CLIENT_ID), AdminParameters.LOCALE, locale);
            LocaleUtils.setCurrentLocale(locale);
            setThemeToken(path.contains("theme=dark")? getDarkThemeToken(): getLightThemeToken(), ctx);
        } else {
            var ls = ctx.getParameter(OperationUiContext.LOCAL_STORAGE_DATA);
            var darkTheme = ls != null && ls.has("useDarkTheme") &&"true".equals(ls.get("useDarkTheme").getAsString());
            setThemeToken(darkTheme? getDarkThemeToken(): getLightThemeToken(), ctx);
        }
        var headerToolsContainer = new ContentWrapper("header-tools", new ContentWrapperConfiguration(), ctx);
        getHeaderTools(ctx).reversed().forEach(it -> headerToolsContainer.addChild(ctx, it,0));
        addChild(ctx, headerToolsContainer, 0);
        factory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
        aL10nFactory = factory.getBean(AdminL10nFactory.class);
        var workspaceProvider = factory.getBean(WorkspaceProvider.class);
        var workspace = workspaceProvider.getWorkspace();
        for(var group : workspace.getGroups()){
            getWorkspaceGroups().add(toWorkspaceGroup(group));
        }
	}

    protected String getLightThemeToken() throws IOException {
        return getStringContent("admin/mainFrame/light-theme-token.json");
    }

    private String getStringContent(String path) throws IOException {
        var baos = new ByteArrayOutputStream();
        try(var is = getClass().getClassLoader().getResource(path).openStream()){
            IoUtils.copy(is, baos);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

    protected String getDarkThemeToken() throws IOException {
        return getStringContent("admin/mainFrame/dark-theme-token.json");
    }

    protected List<BaseUiElement> getHeaderTools(OperationUiContext context) {
        return List.of(new ThemeTool(context), new LangTool(context));
    }

    @Override
    protected MainFrameConfiguration createConfiguration() {
        var mainFrameConfiguration = new MainFrameConfiguration();
        mainFrameConfiguration.setProcessConfirmationResultListener((act, context) ->{
            try{
                if(confirmationCallback != null && act.isOkPressed()){
                    confirmationCallback.run(context);
                }
            } finally {
                confirmationCallback = null;
            }
        });
        return mainFrameConfiguration;
    }

    public void confirm(String question, RunnableWithExceptionAndArgument<OperationUiContext> callback, OperationUiContext context) {
        this.confirmationCallback = callback;
        var data = new MainFrameShowConfirmationDialogInternalAction();
        data.setQuestion(question);
        data.setTitle(aL10nFactory.Confirmation());
        data.setCancelText(aL10nFactory.Cancel());
        data.setOkText(aL10nFactory.Ok());
        this.showConfirmationDialogInternal(data, context, true);
    }

    public void showError(String errorMessage, OperationUiContext context) {
        var data = new MainFrameShowNotificationInternalAction();
        data.setText(errorMessage);
        data.setTitle(aL10nFactory.Error());
        data.setNotificationType(NotificationType.ERROR);
        this.showNotificationInternal(data, context, true);
    }

    public void showWarning(String waringMessage, OperationUiContext context) {
        var data = new MainFrameShowNotificationInternalAction();
        data.setText(waringMessage);
        data.setTitle(aL10nFactory.Error());
        data.setNotificationType(NotificationType.WARNING);
        this.showNotificationInternal(data, context, true);
    }

    private WorkspaceGroup toWorkspaceGroup(com.gridnine.platform.elsa.admin.domain.WorkspaceGroup group) {
        init();
        var result =  new WorkspaceGroup();
        result.setName(group.getName());
        result.setIcon(group.getIcon());
        for(var item : group.getItems()){
            var workspaceItem = new WorkspaceItem();
            workspaceItem.setName(item.getName());
            workspaceItem.setIcon(item.getIcon());
            workspaceItem.setLink(((WorkspaceItemHandler<BaseWorkspaceItem>) itemHandlers.get(item.getClass().getName())).getLink(item));
            result.getItems().add(workspaceItem);
        }
        return result;
    }

    public static MainFrame lookup(BaseUiElement elm) {
        return lookupInternal(elm);
    }

    private static MainFrame lookupInternal(BaseUiElement elm) {
        if (elm instanceof MainFrame) {
            return (MainFrame) elm;
        }
        return lookupInternal(elm.getParent());
    }

    private void init() {
        if(itemHandlers!=null){
            return;
        }
        synchronized (this) {
            if(itemHandlers==null){
                var handlers = new HashMap<String, WorkspaceItemHandler<?>>();
                factory.getBeansOfType(WorkspaceItemHandler.class).values().forEach(handler -> {
                    handlers.put(handler.getType().getName(), handler);
                });
                itemHandlers = handlers;
            }
        }
    }

    public void saveFile(String fileName, byte[] content, OperationUiContext context) {
        var action  = new MainFrameSaveFileAction();
        action.setName(fileName);
        action.setBase64Content(Base64.getEncoder().encodeToString(content));
        saveFile(action , context, false);
    }

    public<P> void showDialog(DialogHandler<P> handler, P parameters, OperationUiContext context) {
        deleteDialogChildren(context);
        var pack = ExceptionUtils.wrapException(() ->handler.getDialogPack( context, parameters));
        addChild(context, pack.header, 0);
        addChild(context, pack.content, 0);
        addChild(context, pack.footer, 0);
        showDialogInternal(context, true);
    }

    public void closeDialog( OperationUiContext context) {
        closeDialogInternal(context, true);
        deleteDialogChildren(context);
    }

    private void deleteDialogChildren(OperationUiContext context) {
        var toDelete = super.getUnmodifiableListOfChildren().stream().filter(it -> "dialog-footer".equals(it.getTag()) || "dialog-content".equals(it.getTag()) || "dialog-header".equals(it.getTag())).toList();
        toDelete.forEach(it -> {
            removeChild(context, it);
        });
    }

    public record DialogPack(BaseUiElement header, BaseUiElement content, BaseUiElement footer) {}

}