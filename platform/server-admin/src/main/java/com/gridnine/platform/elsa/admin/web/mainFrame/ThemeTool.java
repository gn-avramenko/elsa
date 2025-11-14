package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.web.common.DropDownIcon;
import com.gridnine.platform.elsa.admin.web.common.DropDownIconConfiguration;
import com.gridnine.platform.elsa.admin.web.common.IconMenuItem;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class ThemeTool extends DropDownIcon {

    public ThemeTool(OperationUiContext ctx) {
        super("theme", new DropDownIconConfiguration(), ctx);
        var factory = ctx.getParameter(StandardParameters.BEAN_FACTORY).getBean(AdminL10nFactory.class);
        {
            var darkTheme = new IconMenuItem();
            darkTheme.setIcon("MoonOutlined");
            darkTheme.setId("dark");
            darkTheme.setDisplayName(factory.Dark());
            getItems().add(darkTheme);
        }
        {
            var lightTheme = new IconMenuItem();
            lightTheme.setIcon("SunOutlined");
            lightTheme.setId("light");
            lightTheme.setDisplayName(factory.Light());
            getItems().add(lightTheme);
        }
        var ls = ctx.getParameter(OperationUiContext.LOCAL_STORAGE_DATA);
        var light = !ls.has("useDarkTheme") || !"true".equals(ls.get("useDarkTheme").getAsString());
        setSelectedItem(createSelectedItem(light), ctx);
        setClickListener((act, context) ->{
            setSelectedItem(createSelectedItem(act.getId().equals("light")), context);
            var param = new MainFrameSetWebPeerParamAction();
            param.setKey("useDarkTheme");
            param.setValue(Boolean.toString(act.getId().equals("dark")));
            MainFrame.lookup(ThemeTool.this).setWebPeerParam(param, context, false);
        });
    }

    private IconMenuItem createSelectedItem(boolean light) {
        var selectedTheme = new IconMenuItem();
        selectedTheme.setId(light ? "light" : "dark");
        selectedTheme.setIcon(light ? "SunOutlined" : "MoonOutlined");
        return selectedTheme;
    }
}
