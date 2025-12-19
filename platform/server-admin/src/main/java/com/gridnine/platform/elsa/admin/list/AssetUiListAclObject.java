package com.gridnine.platform.elsa.admin.list;

import com.gridnine.platform.elsa.admin.web.entityList.EntityListButton;

import java.util.List;

public class AssetUiListAclObject {
    private List<EntityListButton> buttons;
    private BaseAssetUiListHandler<?> list;

    public List<EntityListButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<EntityListButton> buttons) {
        this.buttons = buttons;
    }

    public BaseAssetUiListHandler<?> getList() {
        return list;
    }

    public void setList(BaseAssetUiListHandler<?> list) {
        this.list = list;
    }
}
