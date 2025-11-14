package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.platform.elsa.admin.web.common.DropDownImage;
import com.gridnine.platform.elsa.admin.web.common.DropDownImageConfiguration;
import com.gridnine.platform.elsa.admin.web.common.ImageMenuItem;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class LangTool extends DropDownImage {

    public LangTool(OperationUiContext ctx) {
        super("lang", new DropDownImageConfiguration(), ctx);
        {
            var en = new ImageMenuItem();
            en.setId("en");
            en.setSrc("/_resources/classpath/admin/mainFrame/en-flag.png");
            en.setImageHeight(14);
            en.setDisplayName("English");
            getItems().add(en);
        }
        {
            var en = new ImageMenuItem();
            en.setId("ru");
            en.setSrc("/_resources/classpath/admin/mainFrame/ru-flag.png");
            en.setImageHeight(14);
            en.setDisplayName("Русский");
            getItems().add(en);
        }
        var ls = ctx.getParameter(OperationUiContext.LOCAL_STORAGE_DATA);
        var enLang = ls.has("lang") && "en".equals(ls.get("lang").getAsString());
        setSelectedItem(createSelectedItem(enLang), ctx);
        setClickListener((act, context) ->{
            setSelectedItem(createSelectedItem(act.getId().equals("en")), context);
            var param = new MainFrameSetWebPeerParamAction();
            param.setKey("lang");
            param.setValue(act.getId());
            MainFrame.lookup(LangTool.this).setWebPeerParam(param, context, false);
        });
    }

    private ImageMenuItem createSelectedItem(boolean en) {
        var selectedTheme = new ImageMenuItem();
        selectedTheme.setId(en ? "en" : "ru");
        selectedTheme.setSrc(en ? "/_resources/classpath/admin/mainFrame/en-flag.png" : "/_resources/classpath/admin/mainFrame/ru-flag.png");
        selectedTheme.setImageHeight(14);
        return selectedTheme;
    }
}
