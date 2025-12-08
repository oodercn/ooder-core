package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.ESDEntity;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.annotation.menu.TreeRowMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.gen.view.GenViewDicJava;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.view.field.FieldTreeConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.TreeViewComponent;
import net.ooder.esd.tool.properties.TreeViewProperties;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.DefaultTreeItem;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.util.EnumsUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;
import ognl.OgnlContext;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@AnnotationType(clazz = TreeAnnotation.class)
public class CustomTreeViewBean extends CustomViewBean<FieldTreeConfig, TreeListItem, TreeViewComponent> implements ContextMenuBar, ToolsBar {
    ModuleViewType moduleViewType = ModuleViewType.TREECONFIG;

    Boolean formField;

    Boolean iniFold;

    String patentId;

    String nodeClassName;

    String groupName;

    String enumName;

    String rootClassName;

    String rootMethodName;

    Boolean animCollapse;

    Boolean dynDestory;

    Boolean heplBar = false;

    Boolean lazyLoad = false;

    Boolean noCtrlKey = false;

    Boolean group;

    Boolean autoReload;

    Boolean singleOpen;

    Boolean togglePlaceholder;

    String optBtn;

    String valueSeparator;

    String javaTempId;

    Integer size;

    SelModeType selMode;

    RightContextMenuBean contextMenuBean;

    TreeRowCmdBean rowCmdBean;

    ToolBarMenuBean toolBarMenuBean;

    Set<TreeMenu> rowMenu;

    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;

    List<TreeMenu> toolBarMenu = new ArrayList<>();

    List<TreeMenu> customMenu = new ArrayList<>();

    List<TreeMenu> bottombarMenu = new ArrayList<>();

    List<TreeMenu> contextMenu = new ArrayList<>();

    Set<CustomTreeEvent> event = new LinkedHashSet<>();

    public LinkedHashSet<TreeEventBean> extAPIEvent = new LinkedHashSet<>();

    List<ChildTreeViewBean> childTreeBeans = new ArrayList<>();

    List<CustomTreeViewBean> childBeans = new ArrayList<>();

    @JSONField(serialize = false)
    List<CustomTreeViewBean> childTypeBeans = new ArrayList<>();

    @JSONField(serialize = false)
    List<RequestParamBean> paramList = new ArrayList<>();

    @JSONField(serialize = false)
    Set<String> childTreeViewBeanIdSet = new LinkedHashSet<>();

    @JSONField(serialize = false)
    Set<String> childTreeViewBeanClassName = new LinkedHashSet<>();

    EnumsClassBean enumsClassBean;

    public List<TreeListItem> items = new ArrayList<>();

    public CustomTreeViewBean() {

    }


    public List<JavaGenSource> buildAll() {
        List<Callable<List<JavaGenSource>>> callableList = new ArrayList<>();
        for (Callable childModule : childModules) {
            GenViewDicJava genFormChildModule = (GenViewDicJava) childModule;
            callableList.add(childModule);
        }
        return build(callableList);
    }

    public CustomTreeViewBean(ModuleComponent moduleComponent) {
        super();
        AnnotationUtil.fillDefaultValue(TreeAnnotation.class, this);
        this.updateModule(moduleComponent);
    }


    public CustomTreeViewBean(Class clazz, CustomTreeViewBean parentItem) {
        if (parentItem != null) {
            this.childTreeViewBeanClassName = parentItem.getChildTreeViewBeanClassName();
            this.rootClassName = parentItem.getRootClassName();
            this.rootMethodName = parentItem.getRootMethodName();
            this.sourceClassName = parentItem.getSourceClassName();
            this.methodName = parentItem.getMethodName();

            this.patentId = parentItem.getId();
            this.domainId = parentItem.getDomainId();
            this.id = parentItem.getId() + "_" + clazz.getName();

            this.autoFontColor = parentItem.getAutoFontColor();
            this.autoIconColor = parentItem.getAutoIconColor();
            this.autoItemColor = parentItem.getAutoItemColor();
        }
        init(clazz);

    }

    public CustomTreeViewBean(MethodConfig methodConfig, CustomTreeViewBean parentItem) {
        super(methodConfig);
        if (parentItem != null) {
            this.childTreeViewBeanClassName = parentItem.getChildTreeViewBeanClassName();
            this.rootClassName = parentItem.getRootClassName();
            this.rootMethodName = parentItem.getRootMethodName();
            this.sourceClassName = parentItem.getSourceClassName();
            this.methodName = parentItem.getMethodName();
            this.patentId = parentItem.getId();
            this.domainId = parentItem.getDomainId();
            if (methodConfig.getMethodName() != null) {
                this.id = parentItem.getId() + "_" + methodConfig.getMethodName();
            }

            this.autoFontColor = parentItem.getAutoFontColor();
            this.autoIconColor = parentItem.getAutoIconColor();
            this.autoItemColor = parentItem.getAutoItemColor();
        }
        if (methodConfig.getViewClass() != null) {
            init(methodConfig.getViewClass().getCtClass());
        }
        if (methodConfig.getCaption() != null && !methodConfig.getCaption().equals("")) {
            this.caption = methodConfig.getCaption();
        }
    }

    public CustomTreeViewBean(TreeItem treeItem, Class childClazz, CustomTreeViewBean parentItem) {
        if (parentItem != null) {
            this.childTreeViewBeanClassName = parentItem.getChildTreeViewBeanClassName();
            this.rootClassName = parentItem.getRootClassName();
            this.rootMethodName = parentItem.getRootMethodName();
            this.sourceClassName = parentItem.getSourceClassName();
            this.methodName = parentItem.getMethodName();
            this.patentId = parentItem.getId();
            this.domainId = parentItem.getDomainId();
            this.id = parentItem.getId() + "_" + childClazz.getSimpleName();


            this.autoFontColor = parentItem.getAutoFontColor();
            this.autoIconColor = parentItem.getAutoIconColor();
            this.autoItemColor = parentItem.getAutoItemColor();
        }

        if (treeItem != null) {
            this.enumName = treeItem.getType();
            if (enumName == null && treeItem.getClass().isEnum()) {
                enumName = ((Enum) treeItem).name();
            }
            this.id = parentItem.getId() + "_" + enumName;
            this.caption = treeItem.getName();
            this.imageClass = treeItem.getImageClass();
            this.dynDestory = treeItem.isDynDestory();
            this.lazyLoad = treeItem.isLazyLoad();
            this.iniFold = treeItem.isIniFold();
        }

        init(childClazz);

    }

    public CustomTreeViewBean(TreeViewProperties properties) {
        this.initProperties(properties);
    }

    public CustomTreeViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        this.rootMethodName = methodAPIBean.getMethodName();
        this.rootClassName = methodAPIBean.getSourceClassName();
        this.viewClassName = methodAPIBean.getViewClassName();
        this.patentId = id;


        TreeEvent treeEvent = AnnotationUtil.getMethodAnnotation(methodAPIBean.getMethod(), TreeEvent.class);
        if (treeEvent != null) {
            TreeEventBean treeEventBean = new TreeEventBean(treeEvent);
            this.extAPIEvent.add(treeEventBean);
        }


        if (methodAPIBean.getViewClass() != null) {
            Class clazz = methodAPIBean.getViewClass().getCtClass();
            init(clazz);
        }

        try {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
            if (!methodAPIBean.getSourceClass().isProxy()) {
                ChildTreeViewBean childTreeViewBean = new ChildTreeViewBean(methodAPIBean, this);
                addChildTreeBean(childTreeViewBean);
            }

            List<ESDField> cols = esdClassConfig.getESDClass().getHiddenFieldList();
            for (ESDField esdField : cols) {
                FieldTreeConfig config = new FieldTreeConfig(esdField, sourceClassName, methodName);
                fieldConfigMap.put(esdField.getName(), config);
                if (!fieldNames.contains(esdField.getName())) {
                    fieldNames.add(esdField.getName());
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    void updateBar() {
        if (this.bottomBar != null) {
            bottombarMenu = bottomBar.getEnumItems();
        }
        if (this.toolBar != null) {
            toolBarMenu = toolBar.getEnumItems();
        }
        if (this.menuBar != null) {
            customMenu = menuBar.getEnumItems();
        }
        if (this.contextMenuBean != null) {
            contextMenu = Arrays.asList(contextMenuBean.getContextMenu().toArray(new TreeMenu[]{}));
        }
    }

    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent parentModuleComponent) {
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        this.updateBaseModule(parentModuleComponent);
        updateBar();
        childTreeBeans = new ArrayList<>();
        childTreeViewBeanIdSet.clear();
        childTreeViewBeanClassName.clear();
        childTypeBeans.clear();
        ModuleComponent moduleComponent = parentModuleComponent;
        Component currComponent = parentModuleComponent.getCurrComponent();
        TreeViewComponent treeViewComponent = null;
        if (currComponent instanceof LayoutComponent) {
            LayoutComponent layoutComponent = (LayoutComponent) parentModuleComponent.getCurrComponent();
            for (Component component : layoutComponent.getChildren()) {
                if (component.getTarget() != null && component.getTarget().equals(PosType.before.name())) {
                    if (component instanceof TreeViewComponent) {
                        treeViewComponent = (TreeViewComponent) component;
                    }
                }
            }
            moduleComponent = new ModuleComponent(treeViewComponent);
        } else if (currComponent instanceof TreeViewComponent) {
            treeViewComponent = (TreeViewComponent) currComponent;
        } else {
            for (Component component : currComponent.getChildrenRecursivelyList()) {
                if (component instanceof TreeViewComponent) {
                    treeViewComponent = (TreeViewComponent) component;
                }
            }
            moduleComponent = new ModuleComponent(treeViewComponent);
        }

        this.nodeClassName = parentModuleComponent.getClassName();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(this.getDomainId());
            String simClass = nodeClassName.substring(nodeClassName.lastIndexOf(".") + 1);
            if (getViewClassName() != null) {
                simClass = this.getViewClassName().substring(this.getViewClassName().lastIndexOf(".") + 1);
            }
            this.name = OODUtil.formatJavaName(treeViewComponent.getAlias(), false);

            TreeViewProperties treeViewProperties = treeViewComponent.getProperties();
            this.initProperties(treeViewProperties);
            this.updateContainerBean(treeViewComponent);

            List<TreeListItem> treeListItems = treeViewProperties.getItems();
            if (treeListItems == null) {
                treeListItems = ESDEnumsUtil.getEnumItems(TreeEnums.class, TreeListItem.class);
            }
            if (treeListItems.size() == 1 && treeListItems.get(0).getEnumName().toLowerCase().equals(treeViewComponent.getAlias().toLowerCase())) {
                treeListItems = treeListItems.get(0).getSub();
            }
            Class bindClass = treeViewProperties.getEnumClass();

            if (treeListItems == null || treeListItems.isEmpty()) {
                if (bindClass != null && !bindClass.equals(Enum.class) && bindClass.isEnum()) {
                    treeListItems = ESDEnumsUtil.getTreeItems(bindClass, null);
                } else {
                    treeListItems = ESDEnumsUtil.getTreeItems(TreeEnums.class, null);
                }
            }

            if (treeListItems != null && treeListItems.size() > 0 && treeListItems.get(0) != null) {
                try {
                    for (TreeListItem listItem : treeListItems) {
                        listItem.updateEnumName(listItem.getId());
                        if (listItem.getSub() != null && listItem.getSub().size() > 0) {
                            String packageName = moduleComponent.getClassName().toLowerCase();
                            String module = packageName.substring(0, packageName.lastIndexOf("."));
                            if (domainInst != null && domainInst.getEuPackage() != null) {
                                if (packageName.startsWith(domainInst.getEuPackage())) {
                                    module = packageName.substring(domainInst.getEuPackage().length() + 1);
                                }
                            }
                            String euClassName = packageName + "." + simClass;
                            GenViewDicJava genViewDicJava = new GenViewDicJava(domainInst.getViewInst(), module.toLowerCase(), listItem.getSub(), euClassName, null);
                            tasks.add(genViewDicJava);
//                            GenViewDicJava genViewDicJava = dicTaskMap.get(euClassName);
//                            List<JavaSrcBean> dicBeans = new ArrayList<>();
//                            if (genViewDicJava == null) {
//
//
//                               // dicBeans = BuildFactory.getInstance().syncTasks(euClassName, Arrays.asList(genViewDicJava));
//                                dicTaskMap.put(euClassName, genViewDicJava);
//                            }
//                            if (dicBeans != null && dicBeans.size() > 0) {
//                                try {
//                                    Class dicClass = genViewDicJava.getDicClass();
//                                    if (dicClass != null) {
//                                        listItem.setEntityClass(dicClass);
//                                        for (JavaSrcBean javaSrcBean : dicBeans) {
//                                            this.getViewJavaSrcBean().getViewClassList().add(javaSrcBean.getClassName());
//                                        }
//                                        listItem.setBindClass(new Class[]{dicClass});
//                                    }
//                                } catch (Throwable e) {
//                                    logger.warn(e);
//                                }
//                            }

                        }

                        if ((listItem.getBindClass().length > 0 && listItem.getBindClass()[0] != null && !listItem.getBindClass()[0].equals(Void.class)) || listItem.getEntityClass() != null) {
                            ChildTreeViewBean childTreeViewBean = null;
                            if (listItem.getGroupName() != null && !listItem.getGroupName().equals("")) {
                                childTreeViewBean = this.getChildTreeByGroup(listItem.getGroupName());
                            }

                            if (childTreeViewBean == null) {
                                Class serviceClass = null;
                                if (listItem.getBindClass() != null && listItem.getBindClass().length > 0 && listItem.getBindClass()[0] != null) {
                                    serviceClass = listItem.getBindClass()[0];
                                } else if (listItem.getEntityClass() != null) {
                                    serviceClass = listItem.getEntityClass();
                                }

                                if (serviceClass.isEnum()) {
                                    String packageName = moduleComponent.getClassName().toLowerCase();
                                    String module = packageName.substring(0, packageName.lastIndexOf("."));
                                    List<TabListItem> enumItems = ESDEnumsUtil.getEnumItems(serviceClass, TabListItem.class);
                                    List<TreeListItem> subList = listItem.getSub();
                                    if (subList != null && subList.size() > 0) {
                                        for (TabListItem tabListItem : enumItems) {
                                            tabListItem.setBindClass(subList.get(0).getBindClass());
                                        }
                                    }
                                    if (domainInst != null && domainInst.getEuPackage() != null) {
                                        if (packageName.startsWith(domainInst.getEuPackage())) {
                                            module = packageName.substring(domainInst.getEuPackage().length() + 1);
                                        }
                                    }
                                    String euClassName = packageName + "." + simClass;


                                    GenViewDicJava genViewDicJava = new GenViewDicJava(domainInst.getViewInst(), module.toLowerCase(), listItem.getSub(), euClassName, null);

//                                    tasks.add(genViewDicJava);
//                                    List<JavaSrcBean> dicBeans = new ArrayList<>();
//                                    if (genViewDicJava == null) {
//                                        genViewDicJava = new GenViewDicJava(domainInst.getViewInst(), module.toLowerCase(), listItem.getSub(), euClassName, null);
//                                        tasks.add(genViewDicJava);
//
//                                    }
//                                    if (dicBeans != null && dicBeans.size() > 0) {
//
//                                        try {
//                                            Class dicClass = genViewDicJava.getDicClass();
//                                            if (dicClass != null) {
//                                                listItem.setEntityClass(dicClass);
//                                                for (JavaSrcBean javaSrcBean : dicBeans) {
//                                                    this.getViewJavaSrcBean().getViewClassList().add(javaSrcBean.getClassName());
//                                                }
//                                                listItem.setBindClass(new Class[]{dicClass});
//                                            }
//                                        } catch (Throwable e) {
//                                            logger.warn(e);
//                                        }
//
//
//                                    }
                                }
                                childTreeViewBean = createChildTreeViewBean(listItem, serviceClass, simClass);
                                this.addChildTreeBean(childTreeViewBean);
                            } else {
                                childTreeViewBean.update(listItem, simClass);
                            }
                        }
                        if (listItem.getDynLoad() != null && listItem.getDynLoad()) {
                            listItem.setSub(new ArrayList());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            items = treeListItems;


        } catch (JDSException e) {
            e.printStackTrace();
        }

        return tasks;
    }


    private ChildTreeViewBean createChildTreeViewBean(TreeListItem listItem, Class serviceClass, String simClass) throws JDSException {
        ConstructorBean constructorBean = null;
        ChildTreeViewBean childTreeViewBean = null;
        DefaultTreeItem treeItem = new DefaultTreeItem(listItem);
        if (serviceClass != null) {
            ESDEntity esdEntity = AnnotationUtil.getClassAnnotation(serviceClass, ESDEntity.class);
            if (esdEntity != null) {
                RequestParamBean requestParamBean = new RequestParamBean(StringUtility.formatJavaName(listItem.getEnumName(), false), serviceClass, false);
                constructorBean = new ConstructorBean(simClass, requestParamBean);
                childTreeViewBean = new ChildTreeViewBean(constructorBean, this, treeItem);
                childTreeViewBean.setEntityClassName(serviceClass.getName());

            } else if (TreeItem.class.isAssignableFrom(serviceClass)) {
                RequestParamBean requestParamBean = new RequestParamBean(StringUtility.formatJavaName(listItem.getEnumName(), false), serviceClass, false);
                constructorBean = new ConstructorBean(simClass, requestParamBean);
                treeItem.setBindClass(new Class[]{serviceClass});
                childTreeViewBean = new ChildTreeViewBean(constructorBean, this, treeItem);
                childTreeViewBean.setCustomItems(serviceClass);
            } else if (serviceClass.isEnum()) {
                RequestParamBean requestParamBean = new RequestParamBean(StringUtility.formatJavaName(listItem.getEnumName(), false), serviceClass, false);
                constructorBean = new ConstructorBean(simClass, requestParamBean);
                if (listItem.getBindClass() == null || listItem.getBindClass().length == 0) {
                    treeItem.setBindClass(new Class[]{serviceClass});
                } else {
                    treeItem.setBindClass(listItem.getBindClass());
                }
                childTreeViewBean = new ChildTreeViewBean(constructorBean, this, treeItem);
            } else {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                MethodConfig methodConfig = apiClassConfig.getMethodByEvent(CustomTreeEvent.RELOADCHILD);
                if (methodConfig != null) {
                    RequestParamBean[] requestParamBean = (RequestParamBean[]) methodConfig.getParamSet().toArray(new RequestParamBean[]{});
                    constructorBean = new ConstructorBean(simClass, requestParamBean);
                }
                if (listItem.getBindClass() == null || listItem.getBindClass().length == 0) {
                    treeItem.setBindClass(new Class[]{serviceClass});
                } else {
                    treeItem.setBindClass(listItem.getBindClass());
                }
                childTreeViewBean = new ChildTreeViewBean(constructorBean, this, treeItem);
            }
        }

        if (listItem.getContextMenu() != null && (listItem.getContextMenu().getMenuClass().length > 0 || listItem.getContextMenu().getContextMenu().size() > 0)) {
            childTreeViewBean.setContextMenuBean(listItem.getContextMenu());
        }

        List<TreeListItem> subList = listItem.getSub();
        if (subList != null && subList.size() > 0) {
            for (TreeListItem item : subList) {
                if (item.getBindClass() != null && item.getBindClass().length > 0 && item.getBindClass()[0] != null) {
                    serviceClass = item.getBindClass()[0];
                } else if (item.getEntityClass() != null) {
                    serviceClass = item.getEntityClass();
                }
                this.addChildTreeBean(this.createChildTreeViewBean(item, serviceClass, simClass));
            }
        }

        if (listItem.getTagCmds() != null && listItem.getTagCmds().size() > 0) {
            TreeRowCmdBean treeRowCmdBean = childTreeViewBean.getRowCmdBean();
            if (treeRowCmdBean == null) {
                treeRowCmdBean = new TreeRowCmdBean();
                childTreeViewBean.setRowCmdBean(treeRowCmdBean);
            }
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
        return childTreeViewBean;
    }

    public List<TreeListItem> getTreeItems() {
        List<TreeListItem> treeListItems = new ArrayList<>();
        if (items == null || items.isEmpty()) {
            Class<? extends Enum> enumClass = getEnumClass();
            Class<? extends Enum> viewClass = null;
            String viewClassName = getViewClassName();
            if (viewClassName != null) {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(viewClassName);
                    if (clazz.isEnum()) {
                        viewClass = clazz;
                    }
                } catch (ClassNotFoundException e) {
                    logger.error(e);
                    // e.printStackTrace();
                }
            }

            if (viewClass != null) {
                this.items = ESDEnumsUtil.getEnumItems(viewClass, TreeListItem.class);
            } else if (enumClass != null) {
                this.items = ESDEnumsUtil.getEnumItems(enumClass, TreeListItem.class);
            }
        }

        for (TreeListItem item : items) {
            if (item != null && !treeListItems.contains(item)) {
                treeListItems.add(item);
            }
        }

        return treeListItems;
    }

    public void initProperties(TreeViewProperties properties) {
        this.formField = properties.getFormField();
        this.iniFold = properties.getIniFold();
        this.animCollapse = properties.getAnimCollapse();
        this.group = properties.getGroup();
        this.dynDestory = properties.getDynDestory();
        this.singleOpen = properties.getSingleOpen();
        this.togglePlaceholder = properties.getTogglePlaceholder();
        this.selMode = properties.getSelMode();
        this.rowCmdBean = new TreeRowCmdBean(properties);
        this.valueSeparator = properties.getValueSeparator();
        this.optBtn = properties.getOptBtn();
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (rowCmdBean != null) {
            classSet.addAll(Arrays.asList(rowCmdBean.getMenuClass()));
        }
        if (contextMenuBean != null) {
            classSet.addAll(Arrays.asList(contextMenuBean.getMenuClass()));
        }


        for (ChildTreeViewBean childTreeViewBean : this.getChildTreeViewBeans()) {
            classSet.addAll(childTreeViewBean.getOtherClass());
        }

        for (CustomTreeViewBean treeViewBean : this.getChildBeans()) {
            classSet.addAll(treeViewBean.getOtherClass());
        }
        if (this.getCustomService() != null) {
            classSet.addAll(this.getCustomService());
        }

        if (this.getBindService() != null) {
            classSet.add(this.getBindService());
        }

        if (this.getEnumClass() != null) {
            classSet.add(this.getEnumClass());
        }
        for (FieldTreeConfig fieldTreeConfig : this.getAllFields()) {
            classSet.addAll(fieldTreeConfig.getOtherClass());
        }

        try {
            if (this.getSourceClassName() != null) {
                classSet.add(ClassUtility.loadClass(this.getSourceClassName()));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        classSet.add(this.getClass());
        return ClassUtility.checkBase(classSet);
    }

    public ChildTreeViewBean getChildTreeBean(ConstructorBean constructorBean) {
        Set<ChildTreeViewBean> childTreeViewBeans = this.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            ConstructorBean sourceConstructor = childTreeViewBean.getConstructorBean();
            if (sourceConstructor != null && constructorBean.getConstructorInfo().equals(sourceConstructor.getConstructorInfo())) {
                return childTreeViewBean;
            }
        }
        return null;
    }

    public List<ChildTreeViewBean> getChildTreeBean(Constructor constructor) {
        Set<ChildTreeViewBean> childTreeViewBeans = this.getChildTreeViewBeans();
        List<ChildTreeViewBean> treeViewBeans = new ArrayList<>();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            try {
                Constructor sourceConstructor = childTreeViewBean.getConstructorBean().getSourceConstructor();
                if (sourceConstructor != null && constructor.equals(sourceConstructor)) {
                    treeViewBeans.add(childTreeViewBean);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return treeViewBeans;
    }

    public ChildTreeViewBean getLikeChildTreeBean(Object obj) {
        Class treeClass = obj.getClass();
        if (obj instanceof TreeItem) {
            return getChildTreeBean((TreeItem) obj);
        }
        List<ChildTreeViewBean> cloneChildTreeBeans = new ArrayList<>();
        cloneChildTreeBeans.addAll(this.getChildTreeBeans());
        for (ChildTreeViewBean child : cloneChildTreeBeans) {
            try {
                if (child != null) {
                    ConstructorBean constructorBean = child.getConstructorBean();
                    Constructor constructor = constructorBean.getSourceConstructor();
                    if (constructor != null) {
                        Class[] paramClass = constructor.getParameterTypes();
                        if (paramClass.length > 0 && paramClass[0].isAssignableFrom(treeClass)) {
                            //在多线程访问时会出现忽略性冲突
                            if (child != null) {
                                child.reloadConstructor(constructor);
                            }
                            return child;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<CustomTreeViewBean> cloneTreeBeans = new ArrayList<>();
        cloneTreeBeans.addAll(this.getChildBeans());
        for (CustomTreeViewBean treeViewBean : cloneTreeBeans) {
            ChildTreeViewBean childTreeViewBean = treeViewBean.getLikeChildTreeBean(obj);
            if (childTreeViewBean != null) {
                return childTreeViewBean;
            }
        }
        return null;

    }


    public ChildTreeViewBean getChildTreeBean(Object obj) {
        Class treeClass = obj.getClass();
        if (obj instanceof TreeItem) {
            return getChildTreeBean((TreeItem) obj);
        }

        List<ChildTreeViewBean> cloneChildTreeBeans = new ArrayList<>();
        cloneChildTreeBeans.addAll(this.getChildTreeBeans());
        for (ChildTreeViewBean child : cloneChildTreeBeans) {
            try {
                if (child != null) {
                    ConstructorBean constructorBean = child.getConstructorBean();
                    Constructor constructor = constructorBean.getSourceConstructor();
                    if (constructor != null) {
                        Class[] paramClass = constructor.getParameterTypes();
                        if (paramClass.length > 0 && paramClass[0].getName().equals(treeClass.getName())) {
                            //在多线程访问时会出现忽略性冲突
                            if (child != null) {
                                child.reloadConstructor(constructor);
                            }
                            return child;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<CustomTreeViewBean> childs = this.getChildBeans();
        List<CustomTreeViewBean> cloneChildBeans = new ArrayList<>();
        cloneChildBeans.addAll(childs);

        for (CustomTreeViewBean treeViewBean : cloneChildBeans) {
            ChildTreeViewBean childTreeViewBean = treeViewBean.getChildTreeBean(obj);
            if (childTreeViewBean != null) {
                return childTreeViewBean;
            }
        }
        return null;
    }

    public ChildTreeViewBean getChildTreeByGroup(String groupName) {
        Set<ChildTreeViewBean> childTreeViewBeans = this.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            if (childTreeViewBean.getId() != null && childTreeViewBean.getGroupName() != null && childTreeViewBean.getGroupName().equals(groupName)) {
                return childTreeViewBean;

            }
        }
        return null;
    }

    public ChildTreeViewBean getChildTreeBean(String treeId) {
        Set<ChildTreeViewBean> childTreeViewBeans = this.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            if (childTreeViewBean.getId() != null && childTreeViewBean.getId().equals(treeId)) {
                return childTreeViewBean;

            }
        }
        return null;
    }

    public ChildTreeViewBean getChildTreeBean(TreeItem treeItem) {
        Set<ChildTreeViewBean> childTreeViewBeans = this.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            if (childTreeViewBean.getTreeItem() != null && childTreeViewBean.getTreeItem().equals(treeItem)) {
                return childTreeViewBean;

            }
        }
        return null;
    }


    void addChildTreeBean(ChildTreeViewBean childTreeViewBean) {
        if (childTreeViewBean != null && !childTreeViewBeanIdSet.contains(childTreeViewBean.getId())) {
            childTreeViewBeanIdSet.add(childTreeViewBean.getId());
            childTreeBeans.add(childTreeViewBean);
        }
    }


    void init(Class<? extends TreeListItem> clazz) {
        this.name = clazz.getSimpleName();
        this.viewClassName = clazz.getName();
        EnumsClass enums = AnnotationUtil.getClassAnnotation(clazz, EnumsClass.class);
        if (enums != null && enums.clazz() != null) {
            this.setEnumClass(enums.clazz());
            this.enumsClassBean = new EnumsClassBean(enums.clazz());

        }

        TreeAnnotation treeViewAnnotation = AnnotationUtil.getClassAnnotation(clazz, TreeAnnotation.class);
        if (treeViewAnnotation != null) {
            fillData(treeViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(TreeAnnotation.class, this);
        }


        TreeEvent treeEvent = AnnotationUtil.getClassAnnotation(clazz, TreeEvent.class);
        if (treeEvent != null) {
            TreeEventBean treeEventBean = new TreeEventBean(treeEvent);
            this.extAPIEvent.add(treeEventBean);
        }


        RightContextMenu annotation = AnnotationUtil.getClassAnnotation(clazz, RightContextMenu.class);
        if (annotation != null) {
            if (annotation != null) {
                contextMenuBean = new RightContextMenuBean(this.getEnumName(), annotation);
            }
        }

        if (contextMenu != null && contextMenu.size() > 0) {
            if (this.contextMenuBean == null) {
                this.contextMenuBean = AnnotationUtil.fillDefaultValue(RightContextMenu.class, new RightContextMenuBean(this.getEnumName()));
            }
        }

        TreeRowCmd cmdannotation = AnnotationUtil.getClassAnnotation(clazz, TreeRowCmd.class);
        if (cmdannotation != null) {
            rowCmdBean = new TreeRowCmdBean(cmdannotation);
        }


        if (customMenu != null && customMenu.size() > 0) {
            if (this.menuBar == null) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }

        if (toolBarMenu != null && toolBarMenu.size() > 0) {
            if (this.toolBar == null) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }

        List<InitCustomTreeViewBean> tasks = new ArrayList<>();
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            ChildTreeAnnotation treeItemAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, ChildTreeAnnotation.class);
            if (treeItemAnnotation != null) {
                InitCustomTreeViewBean initCustomTreeViewBean = new InitCustomTreeViewBean(constructor, clazz);
                tasks.add(initCustomTreeViewBean);
            }
        }
        try {

            ExecutorService executorService = RemoteConnectionManager.createConntctionService(this.getId() + viewClassName);
            List<Future<List<CustomTreeViewBean>>> futures = executorService.invokeAll(tasks);
            for (Future<List<CustomTreeViewBean>> resultFuture : futures) {
                try {
                    List<CustomTreeViewBean> viewBeans = resultFuture.get(50, TimeUnit.MILLISECONDS);
                    for (CustomTreeViewBean viewBean : viewBeans) {
                        childTreeViewBeanClassName.add(viewBean.getNodeClassName());
                        childBeans.add(viewBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (bottombarMenu != null && bottombarMenu.size() > 0) {
            if (this.bottomBar == null) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }
        try {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(clazz.getName(), false);
            String classCaption = esdClassConfig.getESDClass().getCaption();
            if (classCaption != null && !classCaption.equals("")) {
                caption = classCaption;
            } else {
                caption = esdClassConfig.getESDClass().getDesc();
            }
            List<ESDField> cols = esdClassConfig.getESDClass().getHiddenFieldList();
            for (ESDField esdField : cols) {
                FieldTreeConfig config = new FieldTreeConfig(esdField, sourceClassName, methodName);
                fieldConfigMap.put(esdField.getName(), config);
                if (fieldNames.contains(esdField.getName())) {
                    fieldNames.add(esdField.getName());
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    class InitCustomTreeViewBean implements Callable<List<CustomTreeViewBean>> {
        private MinServerActionContextImpl autoruncontext;
        private OgnlContext onglContext;
        Constructor constructor;
        Class<? extends TreeListItem> clazz;

        public InitCustomTreeViewBean(Constructor constructor, Class<? extends TreeListItem> clazz) {
            this.constructor = constructor;
            this.clazz = clazz;
            JDSContext context = JDSActionContext.getActionContext();

            this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
            autoruncontext.getParamMap().putAll(context.getPagectx());
            autoruncontext.getParamMap().putAll(context.getContext());

            if (context.getSessionId() != null) {
                autoruncontext.setSessionId(context.getSessionId());
                autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
            }
            autoruncontext.setSessionMap(context.getSession());

            onglContext = context.getOgnlContext();
        }

        @Override
        public List<CustomTreeViewBean> call() throws Exception {
            JDSActionContext.setContext(autoruncontext);
            ChildTreeAnnotation treeItemAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, ChildTreeAnnotation.class);
            List<CustomTreeViewBean> childBeans = new ArrayList<>();
            if (treeItemAnnotation != null) {
                if (!treeItemAnnotation.customItems().equals(TreeItem.class)) {
                    TreeItem[] treeItems = EnumsUtil.getEnums(treeItemAnnotation.customItems());
                    int k = 0;
                    for (TreeItem treeItem : treeItems) {
                        ChildTreeViewBean childTreeViewBean = new ChildTreeViewBean(constructor, CustomTreeViewBean.this, treeItem, k);
                        k = k + 1;
                        addChildTreeBean(childTreeViewBean);
                        try {
                            Class[] bindClassList = treeItem.getBindClass();
                            if (bindClassList != null) {
                                for (Class bindClass : bindClassList) {
                                    CustomTreeViewBean viewBean = null;
                                    ApiClassConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
                                    MethodConfig loadChildMethod = entityConfig.getTreeEvent(CustomTreeEvent.RELOADCHILD);
                                    if (loadChildMethod != null && loadChildMethod.getViewClass() != null) {
                                        String childTreeClassName = loadChildMethod.getViewClass().getClassName();
                                        if (!childTreeClassName.equals(clazz.getName()) && loadChildMethod.getViewClass() != null && !childTreeViewBeanClassName.contains(childTreeClassName)) {
                                            childTreeViewBeanClassName.add(childTreeClassName);
                                            viewBean = new CustomTreeViewBean(treeItem, loadChildMethod.getViewClass().getCtClass(), CustomTreeViewBean.this);
                                            childBeans.add(viewBean);
                                            // childTreeViewBeans.addAll(viewBean.getChildTreeViewBeans());
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (treeItemAnnotation.bindClass() != null && treeItemAnnotation.bindClass().length > 0) {
                    try {
                        Class[] bindServices = treeItemAnnotation.bindClass();
                        for (Class bindClass : bindServices) {
                            if (!bindClass.equals(Void.class)) {
                                ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(bindClass.getName(), false);
                                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
                                ChildTreeViewBean childTreeViewBean = new ChildTreeViewBean(constructor, CustomTreeViewBean.this);
                                if (!esdClass.isProxy() && esdClass.getAggregationBean() != null) {
                                    String sourceClass = DSMFactory.getInstance().getViewManager().changeSourceClass(bindClass.getName(), domainId);
                                    apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClass);
                                }
                                childTreeViewBean.setBindClass(new Class[]{ClassUtility.loadClass(apiClassConfig.getServiceClass())});
                                addChildTreeBean(childTreeViewBean);
                                MethodConfig loadChildMethod = apiClassConfig.getTreeEvent(CustomTreeEvent.RELOADCHILD);
                                if (loadChildMethod != null) {
                                    String childTreeClassName = loadChildMethod.getViewClass().getClassName();
                                    if (!childTreeClassName.equals(clazz.getName()) && !childTreeViewBeanClassName.contains(childTreeClassName)) {
                                        childTreeViewBeanClassName.add(childTreeClassName);
                                        CustomTreeViewBean viewBean = new CustomTreeViewBean(loadChildMethod, CustomTreeViewBean.this);
                                        childBeans.add(viewBean);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ChildTreeViewBean childTreeViewBean = new ChildTreeViewBean(constructor, CustomTreeViewBean.this);
                    childTreeBeans.add(childTreeViewBean);
                }
            } else {
                ChildTreeViewBean childTreeViewBean = new ChildTreeViewBean(constructor, CustomTreeViewBean.this);
                childTreeBeans.add(childTreeViewBean);
            }
            return childBeans;
        }

    }


    @JSONField(serialize = false)
    public List<ChildTreeViewBean> getConstructorTreeBeans() {
        Map<Class<TreeItem>, ChildTreeViewBean> childTreeViewBeanMap = new HashMap<>();
        List<ChildTreeViewBean> childTreeViewBeans = new ArrayList<>();
        for (ChildTreeViewBean childTreeBean : childTreeBeans) {
            Class<TreeItem> treeItemClass = childTreeBean.getCustomItems();
            if (!childTreeViewBeans.contains(childTreeBean)) {
                if (treeItemClass == null) {
                    childTreeViewBeans.add(childTreeBean);
                } else if (!childTreeViewBeanMap.containsKey(treeItemClass)) {
                    childTreeViewBeanMap.put(treeItemClass, childTreeBean);
                    childTreeViewBeans.add(childTreeBean);
                }
            }
        }
        return childTreeViewBeans;

    }

    public CustomTreeViewBean getAllTopViewBean(String treeId) {
        List<CustomTreeViewBean> childTreeViewBeans = this.getAllChild();
        for (CustomTreeViewBean childTreeViewBean : childTreeViewBeans) {
            if (childTreeViewBean.getId() != null && childTreeViewBean.getId().equals(treeId)) {
                return childTreeViewBean;
            }
        }
        return null;
    }


    public CustomTreeViewBean getChildBean(String treeId) {
        List<CustomTreeViewBean> childTreeViewBeans = this.getAllChild();
        for (CustomTreeViewBean childTreeViewBean : childTreeViewBeans) {
            if (childTreeViewBean.getId() != null && childTreeViewBean.getId().equals(treeId)) {
                return childTreeViewBean;
            }
        }
        return null;
    }

    @JSONField(serialize = false)
    public List<CustomTreeViewBean> getAllChild() {
        List<CustomTreeViewBean> beans = new ArrayList<>();
        beans.addAll(childBeans);
        for (CustomTreeViewBean child : childBeans) {
            beans.addAll(child.getAllChild());
        }
        return beans;
    }

    public Set<ChildTreeViewBean> getChildTreeViewBeans() {
        return getChildTreeViewBeans(new LinkedHashSet<>());
    }

    @JSONField(serialize = false)
    public Set<ChildTreeViewBean> getChildTreeViewBeans(Set<String> childIdSet) {
        if (childIdSet == null) {
            childIdSet = new LinkedHashSet<>();
        }
        Set<ChildTreeViewBean> childTreeViewBeanSet = new LinkedHashSet<>();
        for (ChildTreeViewBean child : childTreeBeans) {
            if (child != null && !childIdSet.contains(child.getId())) {
                childIdSet.add(child.getId());
                childTreeViewBeanSet.add(child);
            }
        }

        for (CustomTreeViewBean child : childBeans) {
            if (child != null && !childIdSet.contains(child.getId())) {
                childIdSet.add(child.getId());
                childTreeViewBeanSet.addAll(child.getChildTreeViewBeans(childIdSet));
            }
        }
        return childTreeViewBeanSet;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TREEVIEW;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (contextMenuBean != null && contextMenuBean.getMenuClass().length > 0) {
            annotationBeans.add(contextMenuBean);
        }

        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }
        if (menuBar != null) {
            annotationBeans.add(menuBar);
        }


        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }
        if (bottomBar != null) {
            annotationBeans.add(bottomBar);
        }

        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }
        if (toolBar != null && !AnnotationUtil.getAnnotationMap(toolBar).isEmpty()) {
            annotationBeans.add(toolBar);
        }

        if (rowCmdBean != null && !AnnotationUtil.getAnnotationMap(rowCmdBean).isEmpty()) {
            if (rowCmdBean.getRowMenu().size() > 0 || rowCmdBean.getMenuClass().length > 0) {
                this.rowCmdBean = AnnotationUtil.fillDefaultValue(RowCmdMenu.class, new TreeRowCmdBean());
                annotationBeans.add(rowCmdBean);
            }
        }


        annotationBeans.add(this);
        return annotationBeans;
    }

    public CustomTreeViewBean fillData(TreeAnnotation annotation) {
        if (annotation == null) {
            AnnotationUtil.fillDefaultValue(TreeAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(annotation, this);
        }

        return this;
    }

    @Override
    public ToolBarMenuBean getToolBar() {
        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
                this.toolBar.setMenus(toolBarMenu.toArray(new TreeMenu[]{}));
            }
        }
        return toolBar;
    }

    @Override
    public MenuBarBean getMenuBar() {
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
                this.menuBar.setMenus(customMenu.toArray(new TreeMenu[]{}));
            }
        }
        return menuBar;
    }


    @JSONField(serialize = false)
    public List<RequestParamBean> getOtherFields() {
        Map<String, RequestParamBean> fieldTreeConfigMap = new HashMap<>();
        List<RequestParamBean> otherParamList = new ArrayList<>();
        List<ChildTreeViewBean> childTreeViewBeans = this.getConstructorTreeBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            List<RequestParamBean> requestParamBeans = childTreeViewBean.getConstructorBean().getParamList();
            for (RequestParamBean requestParamBean : requestParamBeans) {
                if (!fieldTreeConfigMap.containsKey(requestParamBean.getParamName()) && !this.getFieldConfigMap().containsKey(requestParamBean.getParamName())) {
                    otherParamList.add(requestParamBean);
                    fieldTreeConfigMap.put(requestParamBean.getParamName(), requestParamBean);
                }
            }
        }
        return otherParamList;
    }

    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                this.bottomBar.setBottombar(bottombarMenu.toArray(new TreeMenu[]{}));
            }
        }
        return bottomBar;
    }

    public List<RequestParamBean> getParamList() {
        if (paramList == null || paramList.isEmpty()) {
            paramList = new ArrayList<>();
            Set<ChildTreeViewBean> childTreeViewBeans = this.getChildTreeViewBeans();
            for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
                paramList.addAll(childTreeViewBean.getConstructorBean().getParamList());
            }
        }
        return paramList;
    }

    public LinkedHashSet<TreeEventBean> getExtAPIEvent() {
        return extAPIEvent;
    }

    public void setExtAPIEvent(LinkedHashSet<TreeEventBean> extAPIEvent) {
        this.extAPIEvent = extAPIEvent;
    }

    public List<TreeListItem> getItems() {
        return items;
    }

    public void setItems(List<TreeListItem> items) {
        this.items = items;
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

    public Set<String> getChildTreeViewBeanClassName() {
        return childTreeViewBeanClassName;
    }

    public void setChildTreeViewBeanClassName(Set<String> childTreeViewBeanClassName) {
        this.childTreeViewBeanClassName = childTreeViewBeanClassName;
    }

    public void setParamList(List<RequestParamBean> paramList) {
        this.paramList = paramList;
    }

    public List<TreeMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<TreeMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }


    public List<ChildTreeViewBean> getChildTreeBeans() {
        return childTreeBeans;
    }

    public void setChildTreeBeans(List<ChildTreeViewBean> childTreeBeans) {
        this.childTreeBeans = childTreeBeans;
    }

    public List<TreeMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<TreeMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    public Boolean getNoCtrlKey() {
        return noCtrlKey;
    }

    public void setNoCtrlKey(Boolean noCtrlKey) {
        this.noCtrlKey = noCtrlKey;
    }


    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getJavaTempId() {
        return javaTempId;
    }

    public void setJavaTempId(String javaTempId) {
        this.javaTempId = javaTempId;
    }

    public Boolean getFormField() {
        return formField;
    }

    public void setFormField(Boolean formField) {
        this.formField = formField;
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

    public Boolean getHeplBar() {
        return heplBar;
    }

    public void setHeplBar(Boolean heplBar) {
        this.heplBar = heplBar;
    }

    public Boolean getTogglePlaceholder() {
        return togglePlaceholder;
    }

    public void setTogglePlaceholder(Boolean togglePlaceholder) {
        this.togglePlaceholder = togglePlaceholder;
    }

    public Set<TreeMenu> getRowMenu() {
        return rowMenu;
    }

    public void setRowMenu(Set<TreeMenu> rowMenu) {
        this.rowMenu = rowMenu;
    }

    public Boolean getGroup() {
        return group;
    }

    public String getGroupName() {
        if (groupName == null || groupName.equals("")) {
            if (this.getCustomService() != null && this.getCustomService().size() > 0) {
                MethodConfig loadChildMethod = this.findMethodByEvent(CustomTreeEvent.RELOADCHILD, this.getCustomService().toArray(new Class[]{}));
                MethodConfig editorMethod = this.findMethodByEvent(CustomTreeEvent.TREENODEEDITOR, this.getCustomService().toArray(new Class[]{}));
                if (loadChildMethod != null) {
                    groupName = loadChildMethod.getEUClassName();
                } else if (editorMethod != null) {
                    groupName = editorMethod.getEUClassName();
                }
            } else if (getNodeClassName() != null) {
                groupName = getNodeClassName();
            }
        }
        if (groupName != null) {
            groupName = groupName.replace(".", "_");
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public String getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(String optBtn) {
        this.optBtn = optBtn;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public Boolean getSingleOpen() {
        return singleOpen;
    }

    public void setSingleOpen(Boolean singleOpen) {
        this.singleOpen = singleOpen;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public String getNodeClassName() {
        return nodeClassName;
    }

    public void setNodeClassName(String nodeClassName) {
        this.nodeClassName = nodeClassName;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public ToolBarMenuBean getToolBarMenuBean() {
        return toolBarMenuBean;
    }

    public void setToolBarMenuBean(ToolBarMenuBean toolBarMenuBean) {
        this.toolBarMenuBean = toolBarMenuBean;
    }

    public List<TreeMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<TreeMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public Set<CustomTreeEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomTreeEvent> event) {
        this.event = event;
    }

    @Override
    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public List<CustomTreeViewBean> getChildBeans() {
        return childBeans;
    }

    public void setChildTypeBeans(List<CustomTreeViewBean> childTypeBeans) {
        this.childTypeBeans = childTypeBeans;
    }


    public void genDic() {

    }

    @JSONField(serialize = false)
    public List<ChildTreeViewBean> getChildTypeBeans() {
        List<ChildTreeViewBean> childTreeViewBeans = new ArrayList<>();
        List<ChildTreeViewBean> allChildBean = new ArrayList<>();
        allChildBean.addAll(this.getChildTreeBeans());
        allChildBean.addAll(this.getChildTreeViewBeans());
        List<ConstructorBean> constructorBeans = new ArrayList<>();
        for (ChildTreeViewBean treeViewBean : allChildBean) {
            ConstructorBean constructorBean = treeViewBean.getConstructorBean();
            if (constructorBean != null && !constructorBeans.contains(constructorBean)) {
                childTreeViewBeans.add(treeViewBean);
                constructorBeans.add(constructorBean);
            }
        }
        return childTreeViewBeans;
    }


    public TreeRowCmdBean getRowCmdBean() {
        return rowCmdBean;
    }

    public void setRowCmdBean(TreeRowCmdBean rowCmdBean) {
        this.rowCmdBean = rowCmdBean;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public String getPatentId() {
        return patentId;
    }

    public void setPatentId(String patentId) {
        this.patentId = patentId;
    }

    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    public String getRootMethodName() {
        return rootMethodName;
    }

    public void setRootMethodName(String rootMethodName) {
        this.rootMethodName = rootMethodName;
    }

    public List<TreeMenu> getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(List<TreeMenu> contextMenu) {
        this.contextMenu = contextMenu;
    }

    public EnumsClassBean getEnumsClassBean() {
        return enumsClassBean;
    }

    public void setEnumsClassBean(EnumsClassBean enumsClassBean) {
        this.enumsClassBean = enumsClassBean;
    }

    public Set<String> getChildTreeViewBeanIdSet() {
        return childTreeViewBeanIdSet;
    }

    public void setChildTreeViewBeanIdSet(Set<String> childTreeViewBeanIdSet) {
        this.childTreeViewBeanIdSet = childTreeViewBeanIdSet;
    }

}
