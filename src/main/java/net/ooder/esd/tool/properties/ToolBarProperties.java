package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.properties.BarProperties;
import net.ooder.esd.engine.enums.MenuBarBean;

import java.util.List;

public class ToolBarProperties extends BarProperties<TreeListItem> {


    @JSONField(serialize = false)
    TreeListItem group;

    String fristGroupId;

    public String groupId;

    public String handle;

    public Boolean formField;

    public Boolean disabled;

    public String iconFontSize;


    public ToolBarProperties() {
        super();
    }

    public ToolBarProperties(String id) {
        super();
        this.id = id;
    }


    public ToolBarProperties(ToolBarMenuBean toolBarBean) {
        super();
        String groupId = toolBarBean.getGroupId() == null ? toolBarBean.getAlias() : toolBarBean.getGroupId();
        this.id = groupId + "tool";
        group = new TreeListItem(groupId, groupId);
        this.fristGroupId = group.getId();
        this.handler = toolBarBean.getHandler();
        this.formField = toolBarBean.getFormField();
        this.disabled = toolBarBean.getDisabled();
        this.lazy = toolBarBean.getLazy();
        this.dynLoad = toolBarBean.getDynLoad();
        this.iconFontSize = toolBarBean.getIconFontSize();
        this.sethAlign(toolBarBean.gethAlign());
        this.setvAlign(toolBarBean.getvAlign());
        this.addItem(group);
        if (toolBarBean.getCustomListBean() != null) {
            List<TreeListItem> itemList = toolBarBean.getCustomListBean().getItems();
            for (TreeListItem item : itemList) {
                group.addChild(item);
            }
        }

    }

    public ToolBarProperties(MenuBarBean menuBarBean) {
        super();
        this.id = menuBarBean.getId();
        group = new TreeListItem(menuBarBean.getId() + "Root", menuBarBean.getCaption());
        this.addItem(group);
        this.fristGroupId = group.getId();
        this.sethAlign(menuBarBean.gethAlign());
        this.setvAlign(menuBarBean.getvAlign());
    }

    public ToolBarProperties addChild(TreeListItem item) {
        this.getGroup().addChild(item);
        return this;
    }

    public void setGroup(TreeListItem group) {
        this.group = group;
    }

    public String getFristGroupId() {
        return fristGroupId;
    }

    public void setFristGroupId(String fristGroupId) {
        this.fristGroupId = fristGroupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @Override
    public Boolean getFormField() {
        return formField;
    }

    @Override
    public void setFormField(Boolean formField) {
        this.formField = formField;
    }

    @Override
    public Boolean getDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public TreeListItem getGroup() {
        List<TreeListItem> items = this.getItems();
        if ((group == null || group.getSub() == null) && items != null) {
            for (TreeListItem item : items) {
                if (item != null && item.getId() != null && this.fristGroupId != null && item.getId().equals(this.fristGroupId)) {
                    group = item;
                    continue;
                }
            }
            for (TreeListItem item : items) {
                if (item != null && item.getSub() != null) {
                    group = item;
                    continue;
                }
            }
        }

        if (group == null) {
            String groupId = "Root";
            if (id != null) {
                groupId = id + "Root";
            }
            group = new TreeListItem(groupId, groupId);
        }

        return group;
    }


}
