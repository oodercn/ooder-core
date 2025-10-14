package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.ESDEntity;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ChildTreeAnnotation;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.annotation.TreeRowCmd;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.annotation.menu.TreeRowMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDClassManager;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.view.field.FieldTreeConfig;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.*;
import net.ooder.web.ConstructorBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.*;

@AnnotationType(clazz = ChildTreeAnnotation.class)
public class ChildTreeViewBean<T extends FieldTreeConfig> implements ContextMenuBar, CustomBean, Comparable<ChildTreeViewBean> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, ChildTreeViewBean.class);

    Boolean iniFold;

    String className;

    String id;

    @JSONField(serialize = false)
    String groupName;

    String imageClass;

    String caption;

    Integer index = 1;

    String methodName;

    Boolean animCollapse;

    Boolean dynDestory;

    Boolean lazyLoad;

    String enumName;


    Boolean noCtrlKey = false;

    Boolean autoHidden = true;

    Boolean canEditor;

    Boolean group;

    Boolean singleOpen;

    Boolean closeBtn = false;

    Boolean deepSearch;

    Boolean togglePlaceholder;

    List<TreeMenu> contextMenu = new ArrayList<>();

    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    Class[] bindClass;

    SelModeType selMode;

    RightContextMenuBean contextMenuBean;

    TreeRowCmdBean rowCmdBean;

    ToolBarMenuBean toolBarMenuBean;

    Class<? extends TreeItem> customItems;

    LinkedHashSet<TreeEventBean> extAPIEvent = new LinkedHashSet<>();


    String treeItemClassName;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    IconColorEnum iconColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    ItemColorEnum itemColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    FontColorEnum fontColor;

    Boolean autoIconColor;

    Boolean autoItemColor;

    Boolean autoFontColor;

    Set<TreeMenu> toolBarMenu = new LinkedHashSet<>();

    Set<TreeMenu> customMenu = new LinkedHashSet<>();

    Set<TreeMenu> bottombarMenu = new LinkedHashSet<>();

    Set<CustomTreeEvent> event = new LinkedHashSet<>();

    ConstructorBean constructorBean;

    Set<ComponentType> bindTypes = new LinkedHashSet<>();

    @JSONField(serialize = false)
    ESDClass entityClass;

    String entityClassName;

    public String domainId;

    public String parentId;


    @JSONField(deserializeUsing = TreeItemDeserializer.class, serializeUsing = TreeItemSerializer.class)
    public TreeItem treeItem;

    @JSONField(serialize = false)
    MethodConfig methodConfig;


    public ChildTreeViewBean() {

    }


    public ChildTreeViewBean(Constructor constructor, CustomTreeViewBean viewBean, TreeItem treeItem, Integer index) {
        constructorBean = new ConstructorBean(constructor);
        this.domainId = viewBean.getDomainId();
        this.index = index;
        this.treeItemClassName = treeItem.getClass().getName();
        this.parentId = viewBean.getId();
        this.reloadConstructor(constructor);
        initTreeItem(treeItem);


    }


    public ChildTreeViewBean(MethodConfig methodConfig, CustomTreeViewBean viewBean, TreeItem treeItem, Integer index) {
        this.methodConfig = methodConfig;
        this.initMethod(methodConfig);
        this.domainId = viewBean.getDomainId();
        initTreeItem(treeItem);
        this.parentId = viewBean.getId();
        this.index = index;


    }

    void initTreeItem(TreeItem treeItem) {
        if (treeItem != null) {
            this.enumName = treeItem.getType();
            if (enumName == null && treeItem.getClass().isEnum()) {
                enumName = ((Enum) treeItem).name();
            }
            this.caption = treeItem.getName();
            if (caption == null) {
                caption = enumName;
            }

            this.imageClass = treeItem.getImageClass();
            this.bindClass = treeItem.getBindClass();
            this.dynDestory = treeItem.isDynDestory();
            this.lazyLoad = treeItem.isLazyLoad();
            this.iniFold = treeItem.isIniFold();
            this.treeItem = treeItem;
        }

    }


    public ChildTreeViewBean(ConstructorBean constructorBean, CustomTreeViewBean viewBean, TreeItem treeItem) {
        this.constructorBean = constructorBean;
        this.domainId = viewBean.getDomainId();
        this.parentId = viewBean.getId();
        this.treeItem = treeItem;
        caption = viewBean.getCaption() + "（" + constructorBean.getName() + ")";
        initTreeItem(treeItem);

    }

    public ChildTreeViewBean(Constructor constructor, CustomTreeViewBean viewBean) {
        constructorBean = new ConstructorBean(constructor);
        this.domainId = viewBean.getDomainId();
        this.parentId = viewBean.getId();

        this.reloadConstructor(constructor);
        String childCaption = null;
        Class[] bindClasses = this.getBindClass();
        if (bindClasses != null && bindClasses.length > 0) {
            Class bindClass = bindClasses[bindClasses.length - 1];
            if (bindClass != null && bindClass.equals(Void.class) && !this.getBindClass().equals(Enum.class)) {
                childCaption = bindClass.getSimpleName();
            }
        } else if (entityClass != null) {
            childCaption = entityClass.getName();
        } else {
            childCaption = this.getGroupName();
        }
        if (caption == null) {
            if (viewBean.getCaption() != null) {
                caption = viewBean.getCaption() + "（" + childCaption + ")";
            } else {
                caption = viewBean.getName() + "（" + childCaption + ")";
            }
        }

    }


    private void initMethod(MethodConfig methodConfig) {

        if (methodConfig.getViewClass() != null && !TreeListItem.class.isAssignableFrom(methodConfig.getViewClass().getCtClass())) {
            RequestParamBean requestParamBean = new RequestParamBean();
            String paramName = OODUtil.formatJavaName(methodConfig.getViewClass().getName(), false);
            requestParamBean.setParamName(paramName);
            requestParamBean.setParamClassName(methodConfig.getViewClassName());
            requestParamBean.setMethodName(methodConfig.getMethodName());
            requestParamBean.setSourceClassName(methodConfig.getSourceClassName());

            if (constructorBean != null) {
                constructorBean.setFristParam(requestParamBean);
            }

            this.className = methodConfig.getViewClass().getClassName();
        }

        this.bindClass = new Class[]{methodConfig.getMethod().getDeclaringClass()};
        this.imageClass = methodConfig.getImageClass();
        this.caption = methodConfig.getCaption();

        ChildTreeAnnotation treeViewAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), ChildTreeAnnotation.class);
        if (treeViewAnnotation != null) {
            fillData(treeViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(ChildTreeAnnotation.class, this);
        }


        TreeEvent treeEvent = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TreeEvent.class);
        if (treeEvent != null) {
            TreeEventBean treeEventBean = new TreeEventBean(treeEvent);
            this.extAPIEvent.add(treeEventBean);
        }


        RightContextMenu annotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getEnumName(), annotation);
        }

        TreeRowCmd cmdannotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TreeRowCmd.class);
        if (cmdannotation != null) {
            rowCmdBean = new TreeRowCmdBean(cmdannotation);
        }
        this.entityClass = methodConfig.getViewClass();
        if (entityClass != null) {
            this.caption = entityClass.getName();
            this.entityClassName = entityClass.getClassName();
        } else if (this.getBindClass() != null && this.getBindClass().length > 0) {
            this.caption = this.getBindClass()[0].getSimpleName();
        } else {
            caption = this.getGroupName();
        }
    }

    public ChildTreeViewBean(MethodConfig methodConfig, CustomTreeViewBean viewBean) {
        this.methodConfig = methodConfig;
        this.initMethod(methodConfig);
        this.domainId = viewBean.getDomainId();
        this.parentId = viewBean.getId();
        this.enumName = methodConfig.getFieldName();
        LinkedHashSet<RequestParamBean> paramBeans = methodConfig.getParamSet();
        RequestParamBean[] paramBeansArr = Arrays.copyOf(paramBeans.toArray(new RequestParamBean[]{}), paramBeans.size());
        try {
            Class clazz = ClassUtility.loadClass(viewBean.getNodeClassName());
            constructorBean = new ConstructorBean(clazz.getSimpleName(), paramBeansArr);
        } catch (Exception e) {
            constructorBean = new ConstructorBean(methodConfig.getJavaSimpleName(), paramBeansArr);
        }
    }


    public void update(TreeListItem listItem, String simClass) {
        Class serviceClass = null;
        if (listItem.getBindClass() != null && listItem.getBindClass().length > 0 && listItem.getBindClass()[0] != null) {
            serviceClass = listItem.getBindClass()[0];
        } else if (listItem.getEntityClass() != null) {
            serviceClass = listItem.getEntityClass();
        }
        if (serviceClass != null) {
            ESDEntity esdEntity = AnnotationUtil.getClassAnnotation(serviceClass, ESDEntity.class);
            if (esdEntity != null) {
                RequestParamBean requestParamBean = new RequestParamBean(StringUtility.formatJavaName(listItem.getEnumName(), false), serviceClass, false);
                constructorBean = new ConstructorBean(simClass, requestParamBean);
                this.entityClassName = serviceClass.getName();
            } else if (TreeItem.class.isAssignableFrom(serviceClass)) {
                RequestParamBean requestParamBean = new RequestParamBean(StringUtility.formatJavaName(listItem.getEnumName(), false), serviceClass, false);
                this.customItems = serviceClass;
                constructorBean = new ConstructorBean(simClass, requestParamBean);
            } else if (serviceClass.isEnum()) {
                RequestParamBean requestParamBean = new RequestParamBean(StringUtility.formatJavaName(listItem.getEnumName(), false), serviceClass, false);
                constructorBean = new ConstructorBean(simClass, requestParamBean);

            } else {
                ApiClassConfig apiClassConfig = null;
                try {
                    apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                    MethodConfig methodConfig = apiClassConfig.getMethodByEvent(CustomTreeEvent.RELOADCHILD);
                    if (methodConfig != null) {
                        RequestParamBean[] requestParamBean = (RequestParamBean[]) methodConfig.getParamSet().toArray(new RequestParamBean[]{});
                        constructorBean = new ConstructorBean(simClass, requestParamBean);
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
            treeItem = new DefaultTreeItem(listItem);
        }

        if (listItem.getContextMenu() != null && (listItem.getContextMenu().getMenuClass().length > 0 || listItem.getContextMenu().getContextMenu().size() > 0)) {
            setContextMenuBean(listItem.getContextMenu());
        }


        if (listItem.getTagCmds() != null && listItem.getTagCmds().size() > 0) {
            TreeRowCmdBean treeRowCmdBean = genRowCmdBean();
            treeRowCmdBean.setMenuClass(listItem.getBindClass());
            List<CmdItem> cmdItems = listItem.getTagCmds();
            Set<TreeRowMenu> customMenus = new HashSet<>();
            for (CmdItem<TreeRowMenu> cmdItem : cmdItems) {
                TreeRowMenu enumstype = cmdItem.getEnumItem();
                if (enumstype == null && cmdItem.getId().indexOf("_") > -1) {
                    String menuId = cmdItem.getId().split("_")[0];
                    enumstype = TreeRowMenu.valueOf(menuId);
                }
                if (enumstype != null && !customMenus.contains(enumstype)) {
                    customMenus.add(enumstype);
                }
            }

            treeRowCmdBean.setRowMenu(customMenus);
        }


    }


    public void reloadConstructor(Constructor constructor) {
        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            try {
                this.entityClassName = paramClass[0].getName();
                this.entityClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(entityClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        ChildTreeAnnotation treeViewAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, ChildTreeAnnotation.class);
        if (treeViewAnnotation != null) {
            fillData(treeViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(ChildTreeAnnotation.class, this);
        }

        MethodConfig methodConfig = this.findMethod(CustomTreeEvent.TREENODEEDITOR);
        if (methodConfig != null) {
            TreeEvent treeEvent = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TreeEvent.class);
            if (treeEvent != null) {
                TreeEventBean treeEventBean = new TreeEventBean(treeEvent);
                this.extAPIEvent.add(treeEventBean);
            }
        } else {
            TreeEvent treeEvent = AnnotationUtil.getConstructorAnnotation(constructor, TreeEvent.class);
            if (treeEvent != null) {
                TreeEventBean treeEventBean = new TreeEventBean(treeEvent);
                this.extAPIEvent.add(treeEventBean);
            }
        }


        RightContextMenu annotation = AnnotationUtil.getConstructorAnnotation(constructor, RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getEnumName(), annotation);
        }


        TreeRowCmd cmdannotation = AnnotationUtil.getConstructorAnnotation(constructor, TreeRowCmd.class);
        if (cmdannotation != null) {
            rowCmdBean = new TreeRowCmdBean(cmdannotation);
        }


    }

    public String getId() {
        if (id == null || id.equals("")) {
            TreeItem treeItem = this.getTreeItem();
            if (treeItem != null && treeItem.getType() != null) {
                id = treeItem.getType();
                if (!(treeItem instanceof DefaultTreeItem)) {
                    id = treeItem.getClass().getName() + "_" + id;
                }
            } else {
                id = this.getGroupName();
            }
            if (parentId != null) {
                id = parentId + "_" + id;
            }
            if (id != null) {
                id = id.replace(".", "_");
            }
        }

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (contextMenuBean != null) {
            if (contextMenuBean.getContextMenu().size() > 0 || contextMenuBean.getMenuClass().length > 0) {
                annotationBeans.add(contextMenuBean);
            }
        }

        if (rowCmdBean != null) {
            if (rowCmdBean.getRowMenu().size() > 0 || rowCmdBean.getMenuClass().length > 0) {
                annotationBeans.add(rowCmdBean);
            }
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    public Set<TreeMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(Set<TreeMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    public ChildTreeViewBean fillData(ChildTreeAnnotation annotation) {
        if (annotation == null) {
            AnnotationUtil.fillDefaultValue(ChildTreeAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(annotation, this);
        }

        return this;
    }

    public String getGroupName() {
        if (groupName == null || groupName.equals("")) {
            if (bindClass != null && bindClass.length > 0) {
                MethodConfig loadChildMethod = findMethodByEvent(CustomTreeEvent.RELOADCHILD, bindClass);
                MethodConfig editorMethod = findMethodByEvent(CustomTreeEvent.TREENODEEDITOR, bindClass);
                if (loadChildMethod != null) {
                    groupName = loadChildMethod.getEUClassName();
                } else if (editorMethod != null) {
                    groupName = editorMethod.getEUClassName();
                } else {
                    if (this.getEntityClass() != null) {
                        groupName = this.getEntityClass().getName();
                    } else {
                        groupName = getClassName();
                    }
                }
            }

            if (groupName != null) {
                if (treeItem != null && !groupName.startsWith(treeItem.getClass().getSimpleName())) {
                    String itemName = treeItem.getClass().getSimpleName();
                    if (treeItem.getType() != null) {
                        itemName = itemName + "_" + treeItem.getType();
                    }
                    groupName = itemName + "_" + groupName;
                }

//                else if (this.getParentId() != null && !groupName.startsWith(this.getParentId())) {
//                    groupName = this.getParentId() + "_" + groupName;
//                }
            }

            if (groupName != null) {
                groupName = groupName.replace(".", "_");
            }
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getDynDestory() {
        if (dynDestory == null) {
            if (dynDestory == null && this.getTreeItem() != null) {
                dynDestory = this.getTreeItem().isDynDestory();
            } else if (this.getLazyLoad() != null && this.getLazyLoad()) {
                dynDestory = true;
            } else {
                dynDestory = false;
            }
        }

        return dynDestory;
    }


    @JSONField(serialize = false)
    public ESDClass getEntityClass() {
        if (entityClass == null) {
            try {
                if (this.getEntityClassName() != null) {
                    this.entityClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(this.getEntityClassName(), false);
                } else if (this.getMethodConfig() != null) {
                    this.entityClass = this.getMethodConfig().getViewClass();
                } else if (this.getBindClass() != null && this.getBindClass().length > 0) {
                    this.entityClass = ESDClassManager.getInstance().loadEntity(this.getBindClass()[0].getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entityClass;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (rowCmdBean != null) {
            classSet.addAll(Arrays.asList(rowCmdBean.getMenuClass()));
        }
        if (this.getBindClass() != null) {
            classSet.addAll(Arrays.asList(this.getBindClass()));
        }

        try {
            if (this.constructorBean != null && constructorBean.getSourceConstructor() != null) {
                classSet.add(constructorBean.getSourceConstructor().getDeclaringClass());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (getEntityClass() != null) {
            classSet.add(getEntityClass().getCtClass());
        }


        if (methodConfig != null && methodConfig.getViewClass() != null) {
            classSet.add(methodConfig.getViewClass().getCtClass());
        }


        if (this.getCustomItems() != null) {
            classSet.add(this.getCustomItems());
        }
        classSet.add(this.getClass());
        return classSet;
    }


    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public IconColorEnum getIconColor() {
        return iconColor;
    }

    public void setIconColor(IconColorEnum iconColor) {
        this.iconColor = iconColor;
    }

    public ItemColorEnum getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColorEnum itemColor) {
        this.itemColor = itemColor;
    }

    public FontColorEnum getFontColor() {
        return fontColor;
    }

    public void setFontColor(FontColorEnum fontColor) {
        this.fontColor = fontColor;
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

    public Boolean getAutoHidden() {
        return autoHidden;
    }

    public void setAutoHidden(Boolean autoHidden) {
        this.autoHidden = autoHidden;
    }

    public Boolean getCanEditor() {
        return canEditor;
    }

    public void setCanEditor(Boolean canEditor) {
        this.canEditor = canEditor;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public Boolean getNoCtrlKey() {
        return noCtrlKey;
    }

    public void setNoCtrlKey(Boolean noCtrlKey) {
        this.noCtrlKey = noCtrlKey;
    }

    public ToolBarMenuBean getToolBarMenuBean() {
        return toolBarMenuBean;
    }

    public void setToolBarMenuBean(ToolBarMenuBean toolBarMenuBean) {
        this.toolBarMenuBean = toolBarMenuBean;
    }

    public Set<TreeMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(Set<TreeMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public LinkedHashSet<TreeEventBean> getExtAPIEvent() {
        return extAPIEvent;
    }

    public void setExtAPIEvent(LinkedHashSet<TreeEventBean> extAPIEvent) {
        this.extAPIEvent = extAPIEvent;
    }

    public Boolean getLazyLoad() {
        if (lazyLoad == null && this.getTreeItem() != null) {
            lazyLoad = this.getTreeItem().isLazyLoad();
        }
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getIniFold() {
        if (iniFold == null && this.getTreeItem() != null) {
            iniFold = this.getTreeItem().isIniFold();
        }
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

    public Boolean getTogglePlaceholder() {
        return togglePlaceholder;
    }

    public void setTogglePlaceholder(Boolean togglePlaceholder) {
        this.togglePlaceholder = togglePlaceholder;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public Boolean getDeepSearch() {
        return deepSearch;
    }

    public void setDeepSearch(Boolean deepSearch) {
        this.deepSearch = deepSearch;
    }

    public Boolean getSingleOpen() {
        return singleOpen;
    }

    public void setSingleOpen(Boolean singleOpen) {
        this.singleOpen = singleOpen;
    }


    public Class[] getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class[] bindClass) {
        this.bindClass = bindClass;
    }


    public Set<CustomTreeEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomTreeEvent> event) {
        this.event = event;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public RightContextMenuBean genContextMenuBean() {
        if (contextMenuBean == null) {
            contextMenuBean = new RightContextMenuBean(this.getEnumName());
            AnnotationUtil.fillDefaultValue(RightContextMenu.class, contextMenuBean);
        }
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }


    public TreeRowCmdBean genRowCmdBean() {
        if (rowCmdBean == null) {
            rowCmdBean = new TreeRowCmdBean();
            AnnotationUtil.fillDefaultValue(TreeRowCmd.class, rowCmdBean);
        }
        return rowCmdBean;
    }

    public TreeRowCmdBean getRowCmdBean() {
        return rowCmdBean;
    }

    public void setRowCmdBean(TreeRowCmdBean rowCmdBean) {
        this.rowCmdBean = rowCmdBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public ConstructorBean getConstructorBean() {
        return constructorBean;
    }

    public void setConstructorBean(ConstructorBean constructorBean) {
        this.constructorBean = constructorBean;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public TreeItem getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItem treeItem) {
        this.treeItem = treeItem;
    }


    public Class<? extends TreeItem> getCustomItems() {
        return customItems;
    }

    public void setCustomItems(Class<? extends TreeItem> customItems) {
        this.customItems = customItems;
    }

    public MethodConfig getMethodConfig() {
        if (methodConfig == null && this.getBindClass() != null && this.getBindClass()[0] != null) {
            try {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(getBindClass()[0].getName());
                if (apiClassConfig != null) {
                    methodConfig = apiClassConfig.getMethodByEvent(CustomTreeEvent.RELOADCHILD);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return methodConfig;
    }

    public void setMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
    }

    public String getParentId() {
        return parentId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTreeItemClassName() {
        return treeItemClassName;
    }

    public void setTreeItemClassName(String treeItemClassName) {
        this.treeItemClassName = treeItemClassName;
    }

    public Set<TreeMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(Set<TreeMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    @Override
    public int compareTo(ChildTreeViewBean o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }

    @Override
    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }


    private MethodConfig findMethodByEvent(CustomEvent eventEnum, Class... bindClasses) {
        if (bindClasses != null) {
            Set<String> bindClassSet = new HashSet<>();
            for (Class bindClass : bindClasses) {
                if (bindClass != null && !bindClass.equals(Void.class)) {
                    bindClassSet.add(bindClass.getName());
                }
            }
            MethodConfig methodConfig = findMethodByEvent(eventEnum, bindClassSet.toArray(new String[]{}));
            if (methodConfig != null) {
                return methodConfig;
            }
        }
        return null;
    }

    private MethodConfig findMethodByEvent(CustomEvent eventEnum, String... bindClassNames) {
        MethodConfig methodConfig = null;
        if (bindClassNames != null) {
            for (String bindClassName : bindClassNames) {
                if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName()) && !bindClassName.equals(Enum.class.getName())) {
                    try {
                        ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                        if (config != null) {
                            methodConfig = config.getMethodByEvent(eventEnum);
                        } else {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bindClassName, false);
                            methodConfig = entityConfig.getMethodByEvent(eventEnum);
                        }
                        if (methodConfig != null) {
                            return methodConfig;
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return methodConfig;
    }


    public String getEnumName() {
        if (enumName == null) {
            enumName = this.getId();
        }
        if (enumName != null && enumName.indexOf("_") > -1) {
            enumName = StringUtility.formatJavaName(enumName.substring(enumName.lastIndexOf("_") + 1), false);
        }
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public void setEntityClass(ESDClass entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ChildTreeViewBean) {
            return ((ChildTreeViewBean) obj).getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    public List<TreeMenu> getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(List<TreeMenu> contextMenu) {
        this.contextMenu = contextMenu;
    }

    @JSONField(serialize = false)
    public MethodConfig findMethod(CustomTreeEvent event) {
        Set<Class> bindClassList = new HashSet<>();
        if (getBindClass() != null && getBindClass().length > 0) {
            bindClassList.addAll(Arrays.asList(getBindClass()));
        }
        if (getTreeItem() != null && getTreeItem().getBindClass().length > 0) {
            bindClassList.addAll(Arrays.asList(getTreeItem().getBindClass()));
        }
        for (Class bindClass : bindClassList) {
            if (!bindClass.equals(Void.class)) {
                try {
                    ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
                    if (config != null) {
                        MethodConfig methodConfig = config.getTreeEvent(event);
                        if (methodConfig != null) {
                            return methodConfig;
                        }
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
