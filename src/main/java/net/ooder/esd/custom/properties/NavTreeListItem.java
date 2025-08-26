package net.ooder.esd.custom.properties;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NavTreeListItem extends TreeListItem<NavTreeListItem> {
    public NavTreeListItem() {
        super();
    }


    public NavTreeListItem(Enum enumType) {
        super(enumType);
    }


    public NavTreeListItem(CustomTreeViewBean viewBean) {
    this.id = viewBean.getId();
    if (viewBean.getIndex() != null && viewBean.getIndex() != -1) {
        this.tabindex = viewBean.getIndex();
    }
    this.caption = viewBean.getCaption();
    this.setBorderType(BorderType.none);
    this.imageClass = viewBean.getImageClass();
    this.tagVar = viewBean.getTagVar();
    if (tagVar == null) {
        this.tagVar = new HashMap<>();
    }
    if (viewBean.getNavItems().size() > 0) {
        sub = new ArrayList<>();
        this.setIniFold(false);
        List<FieldModuleConfig> fieldConfigs = viewBean.getNavItems();
        for (ESDFieldConfig childItemInfo : fieldConfigs) {
            FieldModuleConfig child = (FieldModuleConfig) childItemInfo;
            sub.add( new NavTreeListItem(child));
        }
    }

}

public NavTreeListItem(FieldModuleConfig itemConfig) {
    this.caption = itemConfig.getCaption();
    this.imageClass = itemConfig.getImageClass();
    this.euClassName = itemConfig.getEuClassName();
    this.tabindex = itemConfig.getIndex();
    this.index = itemConfig.getIndex();
    if (euClassName == null) {
        String url = itemConfig.getUrl();
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        euClassName = StringUtility.replace(url, "/", ".");
    }
    this.id = itemConfig.getId();
    this.setIniFold(false);
    if (tagVar == null) {
        this.tagVar = new HashMap<>();
    }

}
}
