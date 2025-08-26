package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.TagCmdsAlign;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.TreeRowCmdBean;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.list.TreeListProperties;
import net.ooder.esd.util.ESDEnumsUtil;

import java.util.ArrayList;
import java.util.List;

public class TreeBarProperties<T extends TreeListItem> extends TreeListProperties<T> {

    public Boolean iniFold;
    public Boolean animCollapse;
    public Boolean dynDestory;
    public Boolean togglePlaceholder;
    public List<CmdItem> tagCmds;
    public Boolean group;
    public TagCmdsAlign tagCmdsAlign;


    public TreeBarProperties() {
    }


    public TreeBarProperties(CustomTreeViewBean customTreeViewBean) {
        init(customTreeViewBean);
    }


    protected void init(CustomTreeViewBean customTreeViewBean) {
        this.init(customTreeViewBean.getContainerBean());
        if (customTreeViewBean != null) {
            this.formField = customTreeViewBean.getFormField();
            this.iniFold = customTreeViewBean.getIniFold();
            this.animCollapse = customTreeViewBean.getAnimCollapse();
            this.group = customTreeViewBean.getGroup();
            this.dynDestory = customTreeViewBean.getDynDestory();
            this.singleOpen = customTreeViewBean.getSingleOpen();
            this.caption = customTreeViewBean.getCaption();
            this.togglePlaceholder = customTreeViewBean.getTogglePlaceholder();
            this.selMode = customTreeViewBean.getSelMode();
            TreeRowCmdBean rowCmdBean = customTreeViewBean.getRowCmdBean();
            if (rowCmdBean != null) {
                this.tagCmdsAlign = rowCmdBean.getTagCmdsAlign();
            }

            this.valueSeparator = customTreeViewBean.getValueSeparator();
            this.optBtn = customTreeViewBean.getOptBtn();

            this.items = new ArrayList<>();

            TreeListItem treeBean = new TreeListItem(customTreeViewBean);
            items.add((T) treeBean);

            if (this.getItems() == null || this.getItems().isEmpty()) {
                Class<? extends Enum> enumClass = customTreeViewBean.getEnumClass();
                Class<? extends Enum> viewClass = null;
                String viewClassName = customTreeViewBean.getViewClassName();
                if (viewClassName != null) {
                    Class clazz = null;
                    try {
                        clazz = ClassUtility.loadClass(viewClassName);
                        if (clazz.isEnum()) {
                            viewClass = clazz;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if (viewClass != null) {
                    this.items = (List<T>) ESDEnumsUtil.getEnumItems(viewClass, TreeListItem.class);
                } else if (enumClass != null) {
                    this.items = (List<T>) ESDEnumsUtil.getEnumItems(enumClass, TreeListItem.class);
                }
            }

        }
    }


    @JSONField(serialize = false)
    public List<T> getChildrenRecursivelyList() {
        List<T> allChildList = new ArrayList<>();
        for (T item : items) {
            allChildList.add(item);
            allChildList.addAll(item.getChildrenRecursivelyList());
        }
        return allChildList;
    }


    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }


    public Boolean getAnimCollapse() {
        return animCollapse;
    }

    public void setAnimCollapse(Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }


    public Boolean getTogglePlaceholder() {
        return togglePlaceholder;
    }

    public void setTogglePlaceholder(Boolean togglePlaceholder) {
        this.togglePlaceholder = togglePlaceholder;
    }

    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }


    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

}
