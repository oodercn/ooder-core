package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.util.CnToSpell;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.annotation.ui.FontColorEnum;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.ItemColorEnum;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.CustomImageType;
import net.ooder.esd.annotation.ui.StatusItemType;
import net.ooder.esd.bean.view.ChildTreeViewBean;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.custom.properties.CustomCmdBar;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeListItem<T extends TreeListItem> extends TabListItem implements CustomBean {
    @JSONField(serialize = false)
    public static final String ESDSearchPattern = "esdsearchpattern";
    public List<T> sub;
    public String valueSeparator;
    @JSONField(serialize = false)
    public boolean patterned;
    public Boolean group;
    public List<CmdItem> tagCmds;
    public RightContextMenuBean contextMenu;
    public String groupName;
    public Boolean dynDestory;
    public Boolean iniFold;
    public StatusItemType itemType;
    @JSONField(serialize = false)
    public String parentClassName;
    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;


    public TreeListItem() {
    }


    public TreeListItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
        this.tips = tips;
        this.tagVar = params;
    }

    public TreeListItem(ChildTreeViewBean childTreeViewBean) {
        this.id = childTreeViewBean.getId();
        this.enumName = childTreeViewBean.getEnumName();
        if (childTreeViewBean.getContextMenuBean() != null) {
            this.contextMenu = childTreeViewBean.getContextMenuBean();
        }

        if (childTreeViewBean.getEntityClass() != null) {
            this.entityClass = childTreeViewBean.getEntityClass().getCtClass();
        }
        addTagVar("name", enumName);
        if (id.indexOf("_") > -1 && !id.endsWith("_")) {
            id = id.substring(id.indexOf("_") + 1);
        }
        this.initChildTree(childTreeViewBean);
    }


    public TreeListItem(CustomTreeViewBean treeViewBean) {
        this.id = treeViewBean.getId();
        if (id.indexOf("_") > -1 && !id.endsWith("_")) {
            id = id.substring(id.indexOf("_") + 1);
        }

        this.caption = treeViewBean.getCaption();
        this.groupName = treeViewBean.getGroupName();
        this.imageClass = treeViewBean.getImageClass();
        if (treeViewBean.getBindService() != null) {
            this.bindClass = new Class[]{treeViewBean.getBindService()};
        }
        this.iniFold = false;
        List<ChildTreeViewBean> childTreeViewBeans = treeViewBean.getChildTreeBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            if (childTreeViewBean != null) {
                this.addChild((T) new TreeListItem<T>(childTreeViewBean));
            }
        }
        List<CustomTreeViewBean> childTreeBeans = treeViewBean.getChildBeans();
        for (CustomTreeViewBean childTreeBean : childTreeBeans) {
            if (childTreeBean != null) {
                this.addChild((T) new TreeListItem<T>(childTreeBean));
            }
        }
    }

    public TreeListItem(FieldModuleConfig itemConfig) {
        this.caption = itemConfig.getCaption();
        this.imageClass = itemConfig.getImageClass();
        String url = itemConfig.getUrl();
        if (url != null) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            euClassName = StringUtility.replace(url, "/", ".");
        } else if (itemConfig.getSourceMethodConfig() != null) {
            euClassName = itemConfig.getSourceMethodConfig().getEUClassName();
        }
        this.id = itemConfig.getId();
        if (tagVar == null) {
            this.tagVar = new HashMap<>();
        }

    }

    @JSONField(serialize = false)
    public Class getFristBindClass() {
        if (this.getBindClass() == null || this.getBindClass().length == 0 || getBindClass()[0] == null) {
            return Enum.class;
        } else {
            return getBindClass()[0];
        }
    }


    public TreeListItem(Enum enumType) {
        super(enumType);
        this.id = enumType.name();
        if (enumType instanceof TreeItem) {
            TreeItem enumItem = (TreeItem) enumType;
            Class[] bindClasses = enumItem.getBindClass();
            if (bindClasses != null && bindClasses.length > 0) {
                for (Class bindClass : bindClasses) {
                    if (bindClass.isEnum() && !bindClass.equals(enumType.getClass())) {
                        this.setSub(ESDEnumsUtil.getEnumItems(bindClass, TreeListItem.class));
                    }
                }
            }
        }
    }

    public List<T> getSub() {
        List<T> subList = null;

        if (sub != null) {
            List<String> keyList = new ArrayList<>();
            subList = new ArrayList<>();
            for (T item : sub) {
                if (this.getPattern() != null && !this.getPattern().equals("") && !patterned) {
                    Pattern p = Pattern.compile(this.getPattern(), Pattern.CASE_INSENSITIVE);
                    Matcher namematcher = p.matcher(item.getName() == null ? "" : item.getName());
                    Matcher cnnamematcher = p.matcher(item.getEuClassName() == null ? "" : item.getEuClassName());
                    Matcher captionmatcher = p.matcher(item.getCaption() == null ? "" : item.getCaption());
                    Matcher cnmatcher = p.matcher(CnToSpell.getFullSpell(item.getCaption() == null ? "" : item.getCaption()));
                    if (namematcher.find()
                            || cnnamematcher.find()
                            || captionmatcher.find()
                            || cnmatcher.find()
                            || item.getChildrenRecursivelyList().size() > 0
                            ) {
                        if (!keyList.contains(item.getId())) {
                            subList.add(item);
                            keyList.add(item.getId());
                        }
                    }
                } else {
                    subList.add(item);
                }
            }
        }
        return subList;
    }


    @JSONField(serialize = false)
    private TreeListItem getFristId(TreeListItem itemInfo) {
        if (itemInfo.getEuClassName() != null && !itemInfo.getEuClassName().equals("")) {
            return itemInfo;
        } else {
            List<TreeListItem> sub = itemInfo.getSub();
            if (sub != null && sub.size() > 0) {
                for (TreeListItem item : sub) {
                    TreeListItem fristItem = getFristId(item);
                    if (fristItem != null) {
                        return fristItem;
                    }
                }
            }
        }
        return null;
    }


    public void initChildTree(ChildTreeViewBean childTreeViewBean) {
        setDynDestory(childTreeViewBean.getDynDestory());
        setDynLoad(childTreeViewBean.getLazyLoad());
        setIniFold(childTreeViewBean.getIniFold());
        setCloseBtn(childTreeViewBean.getCloseBtn());
        setGroupName(childTreeViewBean.getGroupName());

        if (childTreeViewBean.getBindClass() != null && childTreeViewBean.getBindClass().length > 0) {
            bindClass = childTreeViewBean.getBindClass();
        }


        if (childTreeViewBean.getRowCmdBean() != null) {
            CustomCmdBar cmdBar = new CustomCmdBar(childTreeViewBean.getRowCmdBean(), this, null);
            this.tagCmds = cmdBar.getCmdItems();
            Class[] menuClasses = childTreeViewBean.getRowCmdBean().getMenuClass();
            if (menuClasses != null) {
                if (this.getBindClass() == null || this.getBindClass().length == 0) {
                    this.setBindClass(menuClasses);
                } else {
                    List<Class> classes = Arrays.asList(this.getBindClass());
                    for (Class menuClass : menuClasses) {
                        if (!classes.contains(menuClass)) {
                            classes.add(menuClass);
                        }
                    }
                    this.setBindClass(classes.toArray(new Class[]{}));
                }
            }
        }


        if (getAutoIconColor() == null) {
            setAutoIconColor(childTreeViewBean.getAutoIconColor());
        }
        if (getAutoFontColor() == null) {
            setAutoFontColor(childTreeViewBean.getAutoFontColor());
        }
        if (getAutoItemColor() == null) {
            setAutoItemColor(childTreeViewBean.getAutoItemColor());
        }

        if (iconColor == null) {
            iconColor = childTreeViewBean.getIconColor();
        }

        if (fontColor == null) {
            fontColor = childTreeViewBean.getFontColor();
        }

        if (itemColor == null) {
            this.itemColor = childTreeViewBean.getItemColor();
        }

        if (getCaption() == null || getCaption().equals("")) {
            this.caption = childTreeViewBean.getCaption();
        }

        if (getImageClass() == null || getImageClass().equals("")) {
            String imageClass = childTreeViewBean.getImageClass();
            if (imageClass == null || imageClass.equals("")) {
                imageClass = CustomImageType.treebar.getImageClass();
            }
            this.imageClass = imageClass;
        }

    }

    public T addChild(T item) {
        if (sub == null) {
            sub = new ArrayList<T>();
        }
        if (item != null && !sub.contains(item)) {
            sub.add(item);
        }
        return item;
    }


    @JSONField(serialize = false)
    public List<T> getChildrenRecursivelyList() {
        List<T> allChildList = new ArrayList<T>();
        allChildList = addChildList(allChildList);
        return allChildList;
    }

    protected List<T> addChildList(List<T> allChildList) {
        List<T> childs = this.getSub();
        if (childs != null) {
            for (T childitem : childs) {
                allChildList.add(childitem);
                List<T> childList = childitem.getSub();
                if (childList != null && childList.size() > 0) {
                    allChildList.addAll(childitem.getChildrenRecursivelyList());
                }
            }
        }
        return allChildList;
    }


    @JSONField(serialize = false)
    public TreeListItem getFristClassItem(TreeListItem item) {
        TreeListItem fristItem = getFristId(item);
        if (fristItem == null) {
            fristItem = item;
        }
        return fristItem;
    }

    public String toEnumsStr() {
        StringBuffer enumBuffer = new StringBuffer();

        if (this.getBindClass() != null && this.getBindClass().length > 0) {
            for (Class clazz : this.getBindClass()) {
                enumBuffer.append(clazz.getName() + ".class,");
            }
//        } else if (this.getBindClassName() != null && !this.getBindClassName().equals("")&& !this.getBindClassName().equals(Void.class.getName()) && !getBindClassName().equals(Enum.class.getName())) {
//            enumBuffer.append(this.getBindClassName() + ".class");
        } else {
            enumBuffer.append("null");
        }
        String enumStr = enumBuffer.toString();
        if (enumStr.endsWith(",")) {
            enumStr = enumStr.substring(0, enumStr.length() - 1);
        }

        return enumStr;
    }


    public void setEnumName(String enumName) {
        this.addTagVar("name", enumName);
        this.enumName = enumName;
    }


    public TreeListItem(ComboInputType inputType, String id) {
        this.id = id;
        this.type = inputType;
    }

    public TreeListItem(String id, String caption) {
        this.id = id;
        this.caption = caption;

    }

    public TreeListItem(String id, String caption, String imageClass, String tips, ComboInputType type, IconColorEnum iconColor, ItemColorEnum itemColor, FontColorEnum fontColor) {
        super(id, caption, imageClass, tips, type);
        this.iconColor = iconColor;
        this.itemColor = itemColor;
        this.fontColor = fontColor;
    }

    public TreeListItem(String id, String caption, String imageClass, String tips, ComboInputType type) {
        super(id, caption, imageClass, tips, type);
    }

    public TreeListItem(String id, String caption, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }


    public StatusItemType getItemType() {
        return itemType;
    }

    public void setItemType(StatusItemType itemType) {
        this.itemType = itemType;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }


    public Boolean getAutoIconColor() {
        return autoIconColor;
    }

    public void setAutoIconColor(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
    }

    public Boolean getAutoItemColor() {
        return autoItemColor;
    }

    public void setAutoItemColor(Boolean autoItemColor) {
        this.autoItemColor = autoItemColor;
    }

    public Boolean getAutoFontColor() {
        return autoFontColor;
    }

    public void setAutoFontColor(Boolean autoFontColor) {
        this.autoFontColor = autoFontColor;
    }

    public void setSub(List<T> sub) {
        this.sub = sub;
    }

    public boolean isPatterned() {
        return patterned;
    }

    public void setPatterned(boolean patterned) {
        this.patterned = patterned;
    }


    public String getParentClassName() {
        return parentClassName;
    }

    public void setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public RightContextMenuBean getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(RightContextMenuBean contextMenu) {
        this.contextMenu = contextMenu;
    }

    public String getPattern() {
        String pattern = null;
        if (JDSActionContext.getActionContext().getParams(TreeListItem.ESDSearchPattern) != null) {
            pattern = JDSActionContext.getActionContext().getParams(TreeListItem.ESDSearchPattern).toString();
        }
        return pattern;
    }

    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TreeListItem) {
            return ((TreeListItem) obj).getId().equals(this.id);
        }
        return super.equals(obj);
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
