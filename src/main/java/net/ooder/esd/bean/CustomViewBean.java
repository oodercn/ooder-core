package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esb.config.formula.ModuleFormulaInst;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.PopMenuEventEnum;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.view.NavComboBaseViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.java.ViewJavaSrcBean;
import net.ooder.esd.dsm.view.field.*;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.PopMenuProperties;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.ComponentDeserializer;
import net.ooder.esd.util.json.ComponentsDeserializer;
import net.ooder.esd.util.json.EnumSetDeserializer;
import net.ooder.esd.util.json.EsdFieldMapDeserializer;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public abstract class CustomViewBean<T extends ESDFieldConfig, U extends UIItem, M extends Component> implements CustomView<T, U>, FieldComponentBean<M> {

    protected static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomViewBean.class);

    public String expression;

    public String methodName;

    public String sourceMethodName;

    public String viewClassName;

    public String sourceClassName;

    public String entityClassName;

    public String name;

    public String value;

    public Class bindService;

    public String domainId;

    public Integer index = -1;

    public String id;

    public String caption;

    public T captionField;

    public T uidField;

    @JSONField(serialize = false)
    protected Class<? extends Enum> enumClass;

    public String imageClass;

    public ViewJavaSrcBean viewJavaSrcBean;

    @JSONField(deserializeUsing = ComponentDeserializer.class)
    public Component component;

    @JSONField(deserializeUsing = EnumSetDeserializer.class)
    public Set<ComponentType> bindTypes = new LinkedHashSet<ComponentType>();

    @JSONField(serialize = false)
    public Set<CustomMenu> menus = new HashSet<>();

    @JSONField(serialize = false)
    public Set<Class> customService;

    public Set<String> customServiceClass = new LinkedHashSet<>();

    public LinkedHashSet<String> fieldNames = new LinkedHashSet<>();

    public LinkedHashSet<String> itemNames;

    public CaselessStringKeyHashMap<String, FieldModuleConfig> itemConfigMap;

    public List<U> tabItems = new ArrayList<>();

    @JSONField(deserializeUsing = EsdFieldMapDeserializer.class)
    public CaselessStringKeyHashMap<String, T> fieldConfigMap = new CaselessStringKeyHashMap<>();

    public ToolBarMenuBean toolBar;

    public MenuBarBean menuBar;

    public BottomBarMenuBean bottomBar;

    public ContainerBean containerBean;

    public Map tagVar = new HashMap<>();

    @JSONField(serialize = false)
    public MethodConfig methodConfig;

    //仅供临时存储更新
    public CustomModuleBean moduleBean;

    @JSONField(deserializeUsing = ComponentsDeserializer.class)
    public ComponentList customComponentBeans;

    @JSONField(serialize = false)
    public List<Callable<List<JavaGenSource>>> childModules;


    public XPathBean xpathBean;

    public List<ModuleFormulaInst> formulas = new ArrayList<ModuleFormulaInst>();

    public CustomViewBean() {

    }

    public CustomViewBean(MethodConfig methodAPIBean) {
        this.methodConfig = methodAPIBean;
        this.moduleBean = new CustomModuleBean(methodAPIBean.getCustomMethodInfo());
        if (methodAPIBean.getViewClass() != null) {
            this.viewClassName = methodAPIBean.getViewClass().getClassName();
            Class clazz = methodAPIBean.getViewClass().getCtClass();
            this.initViewClass(clazz);
        }

        if (methodAPIBean.getMethod() != null) {
            if (containerBean == null) {
                containerBean = new ContainerBean(AnnotationUtil.getAllAnnotations(methodAPIBean.getMethod(), true));
            } else {
                ContainerBean fieldContainerBean = new ContainerBean(AnnotationUtil.getAllAnnotations(methodAPIBean.getMethod(), true));
                OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldContainerBean), Map.class), containerBean, false, false);
            }
        }
        this.id = methodAPIBean.getId();
        this.caption = methodAPIBean.getCaption();
        this.imageClass = methodAPIBean.getImageClass();
        this.tagVar = methodAPIBean.getTagVar();
        this.index = methodAPIBean.getIndex();
        this.name = methodAPIBean.getName();
        this.domainId = methodAPIBean.getDomainId();
        this.methodName = methodAPIBean.getMethodName();
        this.sourceMethodName = methodName;
        this.sourceClassName = methodAPIBean.getSourceClassName();
        if (menuBar != null && (menuBar.getId() == null || menuBar.getId().equals(""))) {
            menuBar.setId(this.methodName + ComponentType.MENUBAR.getType());
        }
        String serviceClass = methodAPIBean.getMethod().getDeclaringClass().getName();
        if (customServiceClass.isEmpty() || !customServiceClass.contains(serviceClass)) {
            customServiceClass.add(serviceClass);
        }

        if (moduleBean != null && moduleBean.getName() != null && !moduleBean.getName().equals("")) {
            name = moduleBean.getName();
        }

    }


    public void addChildJavaSrc(List<JavaSrcBean> javaSrcBeans) {
        List<String> childs = this.getViewJavaSrcBean().getChildClassList();
        for (JavaSrcBean javaSrcBean : javaSrcBeans) {
            if (javaSrcBean != null && !childs.contains(javaSrcBean.getClassName())) {
                childs.add(javaSrcBean.getClassName());
            }
        }
    }

    public abstract List<JavaGenSource> buildAll() ;

    public abstract List<? extends Callable> updateModule(ModuleComponent moduleComponent);

    @Override
    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcBeans() {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        ViewJavaSrcBean viewJavaSrcBean = this.getViewJavaSrcBean();
        if (viewJavaSrcBean != null) {
            try {
                DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(this.getDomainId());
                for (String className : viewJavaSrcBean.getAllClassNames()) {
                    JavaSrcBean javaSrcBean = domainInst.getJavaSrcByClassName(className);
                    if (javaSrcBean != null && !javaSrcBeans.contains(javaSrcBean)) {
                        javaSrcBeans.add(javaSrcBean);
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return javaSrcBeans;
    }

    @Override
    public void update(ModuleComponent moduleComponent, M component) {
        if (!(component instanceof ModuleComponent)) {
            moduleComponent.setCurrComponent(component);
        }
        updateModule(moduleComponent);
    }

    public void updateContainerBean(Component component) {
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }
    }


    public List<JavaGenSource> build(List<Callable<List<JavaGenSource>>> tasks) {
        List<JavaGenSource> javaSrcBeans = new ArrayList<>();
        List<Future<List<JavaGenSource>>> futures = null;
        try {
            ExecutorService service = RemoteConnectionManager.createConntctionService(this.getXpath());
            futures = service.invokeAll(tasks);
            for (Future<List<JavaGenSource>> resultFuture : futures) {
                try {
                    javaSrcBeans.addAll(resultFuture.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            service.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return javaSrcBeans;
    }

    protected void updateBaseModule(ModuleComponent moduleComponent) {
        customComponentBeans = moduleComponent.findComponents(ComponentType.getCustomAPIComponents());
        if (moduleComponent.getCurrComponent() != null) {
            this.initRealPath(moduleComponent, moduleComponent.getCurrComponent());
            this.updateContainerBean(moduleComponent.getCurrComponent());
        }

        if (moduleBean == null) {
            moduleBean = new CustomModuleBean(moduleComponent);
        } else {
            moduleBean.update(moduleComponent);
        }
        this.sourceClassName = moduleBean.getSourceClassName();
        this.sourceMethodName = moduleBean.getSourceMethodName();
        this.domainId = moduleBean.getDomainId();
        MethodConfig parentMethodConfig = moduleComponent.getMethodAPIBean();
        if (parentMethodConfig != null && moduleComponent.getMethodAPIBean().getView() != null) {
            domainId = moduleComponent.getMethodAPIBean().getView().getDomainId();
        }
        if (domainId == null) {
            try {
                DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(moduleComponent.getProjectName(), UserSpace.VIEW);
                domainId = domainInst.getDomainId();
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        List<Component> allComponent = moduleComponent.getChildrenRecursivelyList();
        for (Component component : allComponent) {
            ComponentType componentType = ComponentType.fromType(component.getKey());
            switch (componentType) {
                case STATUSBUTTONS:
                    if (bottomBar == null) {
                        bottomBar = new BottomBarMenuBean((StatusButtonsComponent) component);
                    } else {
                        bottomBar.update((StatusButtonsComponent) component);
                    }

                    break;
                case MENUBAR:
                    if (menuBar == null) {
                        this.menuBar = new MenuBarBean((MenuBarComponent) component);
                    } else {
                        menuBar.update((MenuBarComponent) component);
                    }

                    break;
                case TOOLBAR:
                    if (toolBar == null) {
                        this.toolBar = new ToolBarMenuBean((ToolBarComponent) component);
                    } else {
                        toolBar.update((ToolBarComponent) component);
                    }
                    break;
            }
        }
    }

    @JSONField(serialize = false)
    public List<CustomBean> getMethodAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (containerBean != null) {
            annotationBeans.addAll(containerBean.getAnnotationBeans());
        }
        return annotationBeans;
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (this.xpathBean != null) {
            annotationBeans.add(xpathBean);
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public List<JavaSrcBean> getAllJavaSrc() {
        List<JavaSrcBean> allJavaSrc = new ArrayList<>();
        allJavaSrc.addAll(this.getJavaSrcBeans());
        List<T> esdfields = this.getAllFields();
        for (T esdField : esdfields) {
            if (esdField instanceof FieldFormConfig) {
                FieldFormConfig fieldFormConfig = (FieldFormConfig) esdField;
                if (fieldFormConfig != null && fieldFormConfig.getAllJavaSrcBeans() != null) {
                    List<JavaSrcBean> allWidgetJavaSrc = fieldFormConfig.getAllJavaSrcBeans();
                    for (JavaSrcBean widgetSrc : allWidgetJavaSrc) {
                        if (widgetSrc != null && !allJavaSrc.contains(widgetSrc)) {
                            allJavaSrc.add(widgetSrc);
                        }
                    }
                }
            }
        }
        return allJavaSrc;
    }

    public ComponentBean findComByAlias(String alias) {
        String path = OODUtil.formatJavaName(alias, false).toLowerCase();
        List<T> esdfields = this.getAllFields();
        if (this.getId() != null && OODUtil.formatJavaName(this.getId(), false).toLowerCase().equals(path)) {
            return this;
        } else if (this.getName() != null && OODUtil.formatJavaName(this.getName(), false).toLowerCase().equals(path)) {
            return this;
        } else {
            for (T esdField : esdfields) {
                if (esdField instanceof FieldFormConfig) {
                    FieldFormConfig fieldFormConfig = (FieldFormConfig) esdField;
                    if (fieldFormConfig.getFieldname().toLowerCase().equals(path)) {
                        ComponentBean componentBean = fieldFormConfig.getWidgetConfig();
                        return componentBean;
                    } else if (fieldFormConfig.getMethodConfig() != null && fieldFormConfig.getMethodConfig().getView() != null) {
                        CustomViewBean customViewBean = fieldFormConfig.getMethodConfig().getView();
                        if (customViewBean != null) {
                            ComponentBean componentBean = customViewBean.findComByAlias(path);
                            if (componentBean != null) {
                                return componentBean;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public ComponentBean findComByPath(String path) {
        path = OODUtil.formatJavaName(path, false);
        if (path != null) {
            path = path.toLowerCase();
        }
        List<T> esdfields = this.getAllFields();
        if (OODUtil.formatJavaName(this.getXpath(), false).equals(path)) {
            return this;
        } else {
            for (T esdField : esdfields) {
                if (esdField instanceof FieldFormConfig) {
                    FieldFormConfig fieldFormConfig = (FieldFormConfig) esdField;
                    String filePath = this.getXpath();
                    if (!this.getXpath().toLowerCase().endsWith("." + fieldFormConfig.getFieldname().toLowerCase())) {
                        filePath = OODUtil.formatJavaName(filePath + "." + fieldFormConfig.getFieldname(), false);
                    }
                    if (filePath.equals(path)) {
                        ComponentBean componentBean = fieldFormConfig.getWidgetConfig();
                        return componentBean;
                    } else if (fieldFormConfig.getMethodConfig() != null && fieldFormConfig.getMethodConfig().getView() != null) {
                        CustomViewBean customViewBean = fieldFormConfig.getMethodConfig().getView();
                        if (customViewBean != null) {
                            customViewBean.setXpath(filePath);
                            ComponentBean componentBean = customViewBean.findComByPath(path);
                            if (componentBean != null) {
                                return componentBean;
                            }

                        }
                    }
                }
            }
        }
        return null;
    }

    public void initViewClass(Class clazz) {

        ToolBarMenu toolBarAnnotation = AnnotationUtil.getClassAnnotation(clazz, ToolBarMenu.class);
        if (toolBarAnnotation != null) {
            toolBar = new ToolBarMenuBean(clazz, toolBarAnnotation);
        }
        ContainerAnnotation containeAnnotation = AnnotationUtil.getClassAnnotation(clazz, ContainerAnnotation.class);
        if (containeAnnotation != null) {
            containerBean = new ContainerBean(clazz.getAnnotations());
        }


        BottomBarMenu annotation = AnnotationUtil.getClassAnnotation(clazz, BottomBarMenu.class);
        if (annotation != null) {
            bottomBar = new BottomBarMenuBean(annotation);
        }

        EnumsClass enums = AnnotationUtil.getClassAnnotation(clazz, EnumsClass.class);
        if (enums != null && enums.clazz() != null && !enums.equals(Enum.class)) {
            this.enumClass = enums.clazz();
        }


        XPath oodAnn = AnnotationUtil.getClassAnnotation(clazz, XPath.class);
        if (oodAnn != null) {
            xpathBean = new XPathBean(oodAnn);
        }

        MenuBarMenu customMenuBar = AnnotationUtil.getClassAnnotation(clazz, MenuBarMenu.class);
        if (customMenuBar != null) {
            menuBar = new MenuBarBean(customMenuBar);
        } else {
            menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
        }

    }


    public <M extends CustomMenu> List<M> parBottomBar(StatusButtonsComponent customBottomBar) {
        List<CmdItem> buttomItems = customBottomBar.getProperties().getItems();
        List<M> customMenus = new ArrayList();
        for (CmdItem cmdItem : buttomItems) {
            if (cmdItem.getId().indexOf("_") > -1) {
                String menuId = cmdItem.getId().split("_")[0];
                Class<? extends CustomMenu>[] menus = (Class<? extends CustomMenu>[]) this.getModuleViewType().getMenuClasses();
                try {
                    for (Class<? extends CustomMenu> menuClass : menus) {
                        M enumstype = (M) menuClass.getMethod("valueOf", String.class).invoke(menuClass.getEnumConstants(), menuId);
                        if (enumstype != null && !customMenus.contains(enumstype)) {
                            customMenus.add(enumstype);
                        }
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                    //e.printStackTrace();
                }
            }
        }

        return customMenus;
    }

    public <M extends CustomMenu> List<M> parMenuBar(MenuBarComponent menuBarComponent) {
        List<TreeListItem> menuItems = menuBarComponent.getProperties().getItems();

        List<M> customMenus = new ArrayList();
        for (TreeListItem cmdItem : menuItems) {
            if (cmdItem.getId().indexOf("_") > -1) {
                String menuId = cmdItem.getId().split("_")[0];
                Class<? extends CustomMenu>[] menus = (Class<? extends CustomMenu>[]) this.getModuleViewType().getMenuClasses();
                try {
                    for (Class<? extends CustomMenu> menuClass : menus) {
                        M enumstype = (M) menuClass.getMethod("valueOf", String.class).invoke(menuClass.getEnumConstants(), menuId);
                        if (enumstype != null && !customMenus.contains(enumstype)) {
                            customMenus.add(enumstype);
                        }
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                    // e.printStackTrace();
                }
            }
        }

        return customMenus;
    }

    public <M extends CustomMenu> List<M> parToolBar(ToolBarComponent toolBarComponent) {
        List<TreeListItem> toolBarItems = toolBarComponent.getProperties().getItems();
        List<M> customMenus = new ArrayList();
        for (TreeListItem item : toolBarItems) {
            if (item.getSub() != null) {
                List<TreeListItem> items = item.getSub();
                for (TreeListItem cmdItem : items) {
                    if (cmdItem.getId().indexOf("_") > -1) {
                        String menuId = cmdItem.getId().split("_")[0];
                        Class<? extends CustomMenu>[] menus = (Class<? extends CustomMenu>[]) this.getModuleViewType().getMenuClasses();
                        try {
                            for (Class<? extends CustomMenu> menuClass : menus) {
                                M enumstype = (M) menuClass.getMethod("valueOf", String.class).invoke(menuClass.getEnumConstants(), menuId);
                                if (enumstype != null && !customMenus.contains(enumstype)) {
                                    customMenus.add(enumstype);
                                }
                            }
                        } catch (Exception e) {
                            logger.warn(e.getMessage());
                            //e.printStackTrace();
                        }
                    }
                }

            }

        }
        return customMenus;
    }

    public <M extends CustomMenu> List<M> initPopBar(PopMenuComponent<PopMenuProperties, PopMenuEventEnum> popMenuComponent) {
        List<TreeListItem> buttomItems = popMenuComponent.getProperties().getItems();
        List<M> customMenus = new ArrayList();
        for (TreeListItem cmdItem : buttomItems) {
            if (cmdItem.getId().indexOf("_") > -1) {
                String menuId = cmdItem.getId().split("_")[0];
                Class<? extends CustomMenu>[] menus = (Class<? extends CustomMenu>[]) this.getModuleViewType().getMenuClasses();
                try {
                    for (Class<? extends CustomMenu> menuClass : menus) {
                        M enumstype = (M) menuClass.getMethod("valueOf", String.class).invoke(menuClass.getEnumConstants(), menuId);
                        if (enumstype != null && !customMenus.contains(enumstype)) {
                            customMenus.add(enumstype);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return customMenus;
    }


    @JSONField(serialize = false)
    public T getCaptionField() {
        if (captionField == null) {
            for (T field : getAllFields()) {
                if (field.getEsdField() != null && field.getEsdField().isCaption()) {
                    captionField = field;
                }
            }
        }
        return captionField;
    }

    @JSONField(serialize = false)
    public T getUidField() {
        if (uidField == null) {
            for (T field : getAllFields()) {
                if (field.getEsdField() != null && field.getEsdField().isUid()) {
                    uidField = field;
                }
            }
        }
        return uidField;
    }


    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        if (methodConfig == null && this.getSourceClassName() != null) {
            try {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getSourceClassName());
                if (apiClassConfig != null) {
                    if (methodName != null) {
                        methodConfig = apiClassConfig.getMethodByName(methodName);
                    } else if (sourceMethodName != null) {
                        methodConfig = apiClassConfig.getMethodByName(sourceMethodName);
                    }
                }

                if (methodConfig == null) {
                    AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getSourceClassName(), false);
                    if (aggEntityConfig != null) {
                        if (methodName != null) {
                            methodConfig = aggEntityConfig.getMethodByName(methodName);
                        } else if (sourceMethodName != null) {
                            methodConfig = aggEntityConfig.getMethodByName(sourceMethodName);
                        }
                    }
                }

                if (methodConfig == null && this.getModuleBean() != null) {
                    methodConfig = this.getModuleBean().getMethodConfig();
                }

                if (methodConfig != null && !(methodConfig.getView() instanceof NavComboBaseViewBean)) {
                    methodConfig.setView(this);
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

    public void moveBottom(String fieldname) {
        List<String> fieldNames = new ArrayList<>();
        List<String> displayFieldNames = this.getDisplayFieldNames();
        fieldNames.addAll(displayFieldNames);
        fieldNames.addAll(this.getHiddenFieldNames());
        int k = fieldNames.indexOf(fieldname);
        fieldNames.add(fieldNames.size() - 1, fieldNames.remove(k));
        LinkedHashSet<String> setNames = new LinkedHashSet<>();
        for (String displayName : fieldNames) {
            setNames.add(displayName);
            updateIndex(displayName, fieldNames);
        }
        updateFileIndex(fieldname);
        this.setFieldNames(setNames);
    }


    private ESDFieldConfig updateFileIndex(String fieldname) {
        ESDFieldConfig fieldConfig = getFieldByName(fieldname);
        if (fieldConfig != null && fieldConfig instanceof FieldFormConfig) {
            FieldFormConfig fieldFormConfig = (FieldFormConfig) fieldConfig;
            FieldBean fieldBean = fieldFormConfig.getFieldBean();
            if (fieldBean == null) {
                fieldBean = fieldFormConfig.createFieldBean();
            }
            fieldBean.setColSpan(-1);
        }
        return fieldConfig;
    }

    public void moveTop(String fieldname) {
        List<String> fieldNames = new ArrayList<>();
        List<String> displayFieldNames = this.getDisplayFieldNames();
        fieldNames.addAll(displayFieldNames);
        fieldNames.addAll(this.getHiddenFieldNames());
        if (fieldNames.contains(fieldname)) {
            int k = fieldNames.indexOf(fieldname);
            fieldNames.add(0, fieldNames.remove(k));
            LinkedHashSet<String> setNames = new LinkedHashSet<>();
            for (String displayName : fieldNames) {
                setNames.add(displayName);
                updateIndex(displayName, fieldNames);
            }
            this.setFieldNames(setNames);
        }
        updateFileIndex(fieldname);
    }

    private void updateIndex(String displayName, List<String> fieldNames) {
        ESDFieldConfig esdFieldConfig = this.getFieldByName(displayName);
        if (esdFieldConfig instanceof FieldFormConfig) {
            FieldFormConfig fieldFormConfig = (FieldFormConfig) esdFieldConfig;
            if (fieldFormConfig.getCustomBean() != null) {
                fieldFormConfig.getCustomBean().setIndex(fieldNames.indexOf(displayName));
            } else if (fieldFormConfig.getAggConfig() != null) {
                fieldFormConfig.getAggConfig().setIndex(fieldNames.indexOf(displayName));
            }
        } else if (esdFieldConfig instanceof FieldGridConfig) {
            FieldGridConfig fieldGridConfig = (FieldGridConfig) esdFieldConfig;
            if (fieldGridConfig.getAggConfig() != null) {
                fieldGridConfig.getAggConfig().setIndex(fieldNames.indexOf(displayName));
            }

        } else if (esdFieldConfig instanceof FieldGalleryConfig) {
            FieldGalleryConfig fieldGalleryConfig = (FieldGalleryConfig) esdFieldConfig;
            if (fieldGalleryConfig.getAggConfig() != null) {
                fieldGalleryConfig.getAggConfig().setIndex(fieldNames.indexOf(displayName));
            }
        } else if (esdFieldConfig instanceof FieldItemConfig) {
            FieldItemConfig fieldItemConfig = (FieldItemConfig) esdFieldConfig;
            if (fieldItemConfig.getMethodConfig() != null) {
                fieldItemConfig.getMethodConfig().setIndex(fieldNames.indexOf(displayName));
            }
        } else if (esdFieldConfig instanceof FieldModuleConfig) {
            FieldModuleConfig fieldModuleConfig = (FieldModuleConfig) esdFieldConfig;
            if (fieldModuleConfig.getMethodConfig() != null) {
                fieldModuleConfig.getMethodConfig().setIndex(fieldNames.indexOf(displayName));
            }

        } else if (esdFieldConfig instanceof FieldTreeConfig) {
            FieldTreeConfig fieldTreeConfig = (FieldTreeConfig) esdFieldConfig;
            if (fieldTreeConfig.getAggConfig() != null) {
                fieldTreeConfig.getAggConfig().setIndex(fieldNames.indexOf(displayName));
            }
        } else if (esdFieldConfig instanceof FieldComponentConfig) {
            FieldComponentConfig fieldComponentConfig = (FieldComponentConfig) esdFieldConfig;
            if (fieldComponentConfig.getMethodConfig() != null) {
                fieldComponentConfig.getMethodConfig().setIndex(fieldNames.indexOf(displayName));
            }

        }
    }


    public void moveDown(String fieldname) {
        List<String> fieldNames = new ArrayList<>();
        List<String> displayFieldNames = this.getDisplayFieldNames();
        fieldNames.addAll(displayFieldNames);
        fieldNames.addAll(this.getHiddenFieldNames());
        if (fieldNames.contains(fieldname)) {
            int k = fieldNames.indexOf(fieldname);
            if (k < fieldNames.size()) {
                fieldNames.add(k + 1, fieldNames.remove(k));
            }
            LinkedHashSet<String> setNames = new LinkedHashSet<>();
            for (String displayName : fieldNames) {
                setNames.add(displayName);
                updateIndex(displayName, fieldNames);
            }
            this.setFieldNames(setNames);
            updateFileIndex(fieldname);
        }
    }

    public void moveUP(String fieldname) {
        List<String> fieldNames = new ArrayList<>();
        List<String> displayFieldNames = this.getDisplayFieldNames();
        int k = displayFieldNames.indexOf(fieldname);
        if (k < displayFieldNames.size()) {
            displayFieldNames.add(k - 1, displayFieldNames.remove(k));
        }
        fieldNames.addAll(displayFieldNames);
        fieldNames.addAll(this.getHiddenFieldNames());
        LinkedHashSet<String> setNames = new LinkedHashSet<>();
        for (String displayName : fieldNames) {
            setNames.add(displayName);
            updateIndex(displayName, fieldNames);
        }
        this.setFieldNames(setNames);
        updateFileIndex(fieldname);
    }


    @Override
    @JSONField(serialize = false)
    public List<T> getAllFields() {
        HashSet<String> fieldNames = this.getFieldNames();
        List<T> fields = new ArrayList<>();
        int k = 0;
        for (String fieldName : fieldNames) {
            T fieldFormConfig = this.getFieldConfigMap().get(fieldName);
            if (fieldFormConfig != null) {
                fieldFormConfig.setDomainId(domainId);

                fieldFormConfig.setFieldname(fieldName);
                CustomFieldBean customFieldBean = fieldFormConfig.getCustomBean();
                if (customFieldBean == null) {
                    customFieldBean = new CustomFieldBean();
                    customFieldBean.setIndex(k);
                    fieldFormConfig.setCustomBean(customFieldBean);
                } else {
                    customFieldBean.setIndex(k);
                }

                if (!fields.contains(fieldFormConfig)) {
                    fields.add(fieldFormConfig);
                }
            }
            k++;
        }
        return fields;
    }

    @Override
    @JSONField(serialize = false)
    public List<FieldModuleConfig> getNavItems() {
        LinkedHashSet<String> fieldNames = this.getItemNames();
        List<FieldModuleConfig> fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldModuleConfig fieldFormConfig = this.getItemConfigMap().get(fieldName);
            if (fieldFormConfig != null) {
                if (fieldFormConfig.getSourceClassName() == null || fieldFormConfig.getSourceClassName().equals("")) {
                    fieldFormConfig.setSourceClassName(sourceClassName);
                }

                if (fieldFormConfig.getViewClassName() == null || fieldFormConfig.getViewClassName().equals("")) {
                    fieldFormConfig.setViewClassName(viewClassName);
                }

                fieldFormConfig.setDomainId(domainId);
                fieldFormConfig.setIndex(fields.size());
                if (!fields.contains(fieldFormConfig)) {
                    fields.add(fieldFormConfig);
                }

            }
        }
        return fields;
    }


    public List<U> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<U> tabItems) {
        this.tabItems = tabItems;
    }

    public Set<Class> getCustomService() {
        customService = new HashSet<>();
        for (String className : this.getCustomServiceClass()) {
            try {
                if (className.endsWith(".class")) {
                    className.substring(0, className.length() - ".class".length());
                }
                customService.add(ClassUtility.loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return customService;
    }

    public void setCustomService(Set<Class> customService) {
        if (customService == null || customService.isEmpty()) {
            this.getCustomServiceClass().clear();
        } else {
            for (Class clazz : customService) {
                if (clazz != null) {
                    this.getCustomServiceClass().add(clazz.getName());
                }
            }
        }
    }

    private LinkedHashSet<String> getFieldNames() {
        return fieldNames;
    }

    @JSONField(serialize = false)
    public List<String> getDisplayFieldNames() {
        List<String> displayFieldNames = new ArrayList<>();
        for (T field : this.getAllFields()) {
            if ((field.getColHidden() == null || !field.getColHidden()) && !displayFieldNames.contains(field.getFieldname())) {
                displayFieldNames.add(field.getFieldname());
            }
        }
        return displayFieldNames;
    }

    @JSONField(serialize = false)
    public List<String> getHiddenFieldNames() {
        List<String> hiddenFieldNames = new ArrayList<>();
        for (T field : this.getAllFields()) {
            String fieldName = field.getFieldname();
            if (!hiddenFieldNames.contains(fieldName)) {
                boolean isPid = false;
                if (field.getPid() != null && field.getPid()) {
                    isPid = true;
                }
                boolean isUid = false;
                if (field.getUid() != null && field.getUid()) {
                    isUid = true;
                }

                if (isUid || isPid) {
                    hiddenFieldNames.add(fieldName);
                } else if (field.getColHidden() != null && field.getColHidden()) {
                    hiddenFieldNames.add(fieldName);
                }
            }
        }
        return hiddenFieldNames;
    }

    @JSONField(serialize = false)
    public List<String> getModuleFieldNames() {
        List<String> moduleFields = new ArrayList<>();
        for (T field : this.getAllFields()) {
            String fieldName = field.getFieldname();
            if (field.getEsdField() != null && field.getEsdField().getComponentType().equals(ComponentType.MODULE) && !moduleFields.contains(fieldName)) {
                moduleFields.add(field.getFieldname());
            }
        }
        return moduleFields;
    }


    @JSONField(serialize = false)
    public List<T> getCustomFields() {
        List<T> customFields = new ArrayList<>();
        for (T field : this.getAllFields()) {
            String fieldName = field.getFieldname();
            if (field.getEsdField() != null && !field.getEsdField().getComponentType().equals(ComponentType.MODULE)) {
                if (!customFields.contains(field)) {
                    customFields.add(field);
                }
            }
        }
        return customFields;
    }


    @JSONField(serialize = false)
    public List<T> getModuleFields() {
        List<T> moduleFields = new ArrayList<>();
        for (T field : this.getAllFields()) {
            String fieldName = field.getFieldname();
            if (field.getEsdField() != null && field.getEsdField().getComponentType().equals(ComponentType.MODULE)) {
                if (!moduleFields.contains(field)) {
                    moduleFields.add(field);
                }
            }
        }
        return moduleFields;
    }

    protected LinkedHashSet<String> getItemNames() {
        if (itemNames == null) {
            itemNames = new LinkedHashSet<>();
            List<FieldModuleConfig> fields = new ArrayList<>();
            Map<String, FieldModuleConfig> itemsMap = getItemConfigMap();
            itemsMap.forEach((k, v) -> {
                if (!fields.contains(v)) {
                    fields.add(v);
                }
            });
            //排序
            Collections.sort(fields);
            for (FieldModuleConfig field : fields) {
                itemNames.add(field.getFieldname());
            }
        }
        return itemNames;
    }

    protected Map<String, FieldModuleConfig> getItemConfigMap() {
        if (itemConfigMap == null) {
            itemConfigMap = new CaselessStringKeyHashMap<>();
            try {
                ApiClassConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.viewClassName);
                if (esdClassConfig != null) {
                    List<MethodConfig> methodConfigs = esdClassConfig.getAllViewMethods();
                    for (MethodConfig esdField : methodConfigs) {
                        FieldModuleConfig config = new FieldModuleConfig(esdField);
                        if (config.getSourceClassName() == null || config.getSourceClassName().equals("")) {
                            config.setSourceClassName(sourceClassName);
                        }

                        if (config.getViewClassName() == null || config.getViewClassName().equals("")) {
                            config.setViewClassName(viewClassName);
                        }

                        config.setDomainId(domainId);
                        itemConfigMap.put(esdField.getName(), config);
                    }
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return itemConfigMap;
    }

    public ToolBarMenuBean getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBarMenuBean toolBar) {
        this.toolBar = toolBar;
    }

    public MenuBarBean getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(MenuBarBean menuBar) {
        this.menuBar = menuBar;
    }

    public BottomBarMenuBean getBottomBar() {
        return bottomBar;
    }

    public void setBottomBar(BottomBarMenuBean bottomBar) {
        this.bottomBar = bottomBar;
    }

    protected CaselessStringKeyHashMap<String, T> getFieldConfigMap() {
        return fieldConfigMap;
    }

    protected void setFieldConfigMap(CaselessStringKeyHashMap<String, T> fieldConfigMap) {
        this.fieldConfigMap = fieldConfigMap;
    }

    public void removeField(String name) {
        fieldConfigMap.remove(name);
        fieldNames.remove(name);
    }


    @Override
    public T getFieldByName(String name) {
        return fieldConfigMap.get(name);
    }

    public T findFieldByName(String name) {
        for (T fieldFormConfig : this.getAllFields()) {
            if (fieldFormConfig.getFieldname().toUpperCase().equals(name.toUpperCase())) {
                return fieldFormConfig;
            }
        }
        return null;
    }

    @Override
    public FieldModuleConfig getItemByName(String name) {
        return this.getItemConfigMap().get(name);
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public void setCaptionField(T captionField) {
        this.captionField = captionField;
    }

    public void setUidField(T uidField) {
        this.uidField = uidField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class getBindService() {
        return bindService;
    }

    public void reBindService(Class bindService) {
        if (bindService != null) {
            this.bindService = bindService;
            MethodConfig editorMethod = this.findEditorMethod(bindService.getName());
            if (editorMethod != null) {
                this.setSourceClassName(bindService.getName());
                this.setSourceMethodName(editorMethod.getMethodName());
                this.setViewClassName(editorMethod.getViewClassName());
            }
        }
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public String getId() {
        if (id == null) {
            if (this.component != null && component.getAlias() != null) {
                id = component.getAlias();
            } else if (this.getMethodName() != null) {
                id = ComponentType.TREEVIEW.getType() + "_" + this.getMethodName();
            } else if (this.getName() != null) {
                id = ComponentType.TREEVIEW.getType() + "_" + this.getName();
            } else {
                id = ComponentType.TREEVIEW.getType();
            }
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        if (caption == null && this.getName() != null) {
            caption = this.getName();
        }
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getViewClassName() {
        if (viewClassName == null && entityClassName != null) {
            this.viewClassName = entityClassName;
        }
        if (viewClassName == null && methodConfig != null) {
            viewClassName = methodConfig.getViewClassName();
        }
        return viewClassName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public String getName() {
        if (name == null && moduleBean != null && moduleBean.getName() != null && !moduleBean.getName().equals("")) {
            this.name = moduleBean.getName();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getCustomServiceClass() {
        return customServiceClass;
    }

    public void setCustomServiceClass(Set<String> customServiceClass) {
        this.customServiceClass = customServiceClass;
    }

    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public Set<CustomMenu> getMenus() {
        return menus;
    }

    public void setMenus(Set<CustomMenu> menus) {
        this.menus = menus;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    @Override
    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setFieldNames(LinkedHashSet<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public void setItemNames(LinkedHashSet<String> itemNames) {
        this.itemNames = itemNames;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setItemConfigMap(CaselessStringKeyHashMap<String, FieldModuleConfig> itemConfigMap) {
        this.itemConfigMap = itemConfigMap;
    }

    public String getImageClass() {
        if (imageClass == null || imageClass.equals(MethodConfig.DefaultImageClass)) {
            imageClass = this.getModuleViewType().getImageClass();
        }
        return imageClass;
    }

    public void setModuleBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    public void updateModule(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    @JSONField(serialize = false)
    public Set<RequestParamBean> getParamSet() {
        return this.getMethodConfig().getParamSet();
    }

    public Map getTagVar() {
        return tagVar;
    }

    public void setTagVar(Map tagVar) {
        this.tagVar = tagVar;
    }

    @JSONField(serialize = false)
    public FieldModuleConfig getFieldModuleConfig(String methodName) {
        return getItemConfigMap().get(methodName);
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (this.getViewClassName() != null) {
            try {
                Class viewClass = ClassUtility.loadClass(this.getViewClassName());
                if (viewClass != null) {
                    classSet.add(viewClass);
                }

            } catch (ClassNotFoundException e) {
                logger.error("error sourceClass: " + this.getViewClassName() + "  path: " + this.getXpath());
                logger.error("error path:" + this.getXpathBean().getPath());
                e.printStackTrace();
            }
        }
        List<CustomBean> customBeans = this.getAnnotationBeans();
        for (CustomBean customBean : customBeans) {
            if (customBean != null && customBean.getClass() != null) {
                classSet.add(customBean.getClass());
            }

        }

        if (this.getCustomService() != null) {
            classSet.addAll(this.getCustomService());
        }
        if (this.getBindService() != null) {
            classSet.add(this.getBindService());
        }

        if (this.getBottomBar() != null && this.getBottomBar().getMenuClasses() != null) {
            classSet.addAll(Arrays.asList(this.getBottomBar().getMenuClasses()));
        }
        if (this.getToolBar() != null && this.getToolBar().getMenuClasses() != null) {
            classSet.addAll(Arrays.asList(this.getToolBar().getMenuClasses()));
        }
        if (this.getMenuBar() != null && this.getMenuBar().getMenuClasses() != null) {
            classSet.addAll(Arrays.asList(this.getMenuBar().getMenuClasses()));
        }

        return ClassUtility.checkBase(classSet);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @JSONField(serialize = false)
    private Object getService(RequestMethodBean methodBean, Map<String, Object> allParamsMap, OgnlContext onglContext) throws ClassNotFoundException, OgnlException {
        Class clazz = ClassUtility.loadClass(methodBean.getClassName());
        Object service = getRealService(clazz);
        if (service != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (allParamsMap.get(field.getName()) != null) {
                    try {
                        OgnlRuntime.setProperty(onglContext, service, field.getName(), TypeUtils.cast(allParamsMap.get(field.getName()), field.getType(), null));
                    } catch (OgnlException e) {
                    }
                }
            }
        }
        return service;
    }

    Object getRealService(Class clazz) throws OgnlException {
        Object service = null;
        if (clazz.getInterfaces().length > 0) {
            service = EsbUtil.parExpression(clazz.getInterfaces()[0]);
        } else {
            service = EsbUtil.parExpression(clazz);
        }

        if (service == null) {
            if (clazz.isInterface()) {
                Aggregation aggregation = (Aggregation) clazz.getAnnotation(Aggregation.class);
                if (aggregation != null && !aggregation.rootClass().equals(Void.class) && !aggregation.rootClass().equals(clazz)) {
                    clazz = aggregation.rootClass();
                    service = getRealService(clazz);
                }
            } else {
                service = OgnlRuntime.callConstructor(JDSActionContext.getActionContext().getOgnlContext(), clazz.getName(), new Object[]{});
            }

        }

        return service;
    }

    public Object invokMethod(RequestMethodBean methodBean, Map<String, Object> allParamsMap) throws ClassNotFoundException, OgnlException {
        Object object = null;

        OgnlContext onglContext = JDSActionContext.getActionContext().getOgnlContext();
        if (allParamsMap == null) {
            allParamsMap = new HashMap<>();
        }

        Object service = getService(methodBean, allParamsMap, onglContext);
        if (service != null) {
            Map<String, String> paramsMap = methodBean.getParamsMap();
            Set<RequestParamBean> keySet = methodBean.getParamSet();
            Object[] objects = new Object[paramsMap.size()];
            Class[] objectTyps = new Class[paramsMap.size()];
            int k = 0;
            for (RequestParamBean paramBean : keySet) {
                String key = paramBean.getParamName();
                Class ctClass = ClassUtility.loadClass(paramsMap.get(paramBean.getParamName()));
                String iClassName = ctClass.getName();
                Class iClass = ClassUtility.loadClass(iClassName);
                Object value = null;
                if (allParamsMap.get(key) != null) {
                    value = TypeUtils.cast(allParamsMap.get(key), iClass, null);
                } else if (onglContext.get(key) != null) {
                    value = TypeUtils.cast(onglContext.get(key), iClass, null);
                }
                objectTyps[k] = iClass;
                objects[k] = value;
                k = k + 1;
            }
            object = OgnlRuntime.callMethod(onglContext, service, methodBean.getMethodName(), objects);
        }
        return object;
    }

    public CustomModuleBean getModuleBean() {
        return moduleBean;
    }


    public String getSourceMethodName() {
        if (sourceMethodName == null) {
            sourceMethodName = methodName;
        }
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public Class<? extends Enum> getEnumClass() {
        if (enumClass == null) {
            String viewClassName = this.getViewClassName();
            try {
                if (viewClassName != null && ClassUtility.loadClass(viewClassName) != null) {
                    Class clazz = ClassUtility.loadClass(viewClassName);
                    EnumsClass enums = AnnotationUtil.getClassAnnotation(clazz, EnumsClass.class);
                    if (enums != null && enums.clazz() != null && !enums.equals(Enum.class)) {
                        this.enumClass = enums.clazz();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return enumClass;
    }


    public AggRootBuild getAggRootBuild(ModuleComponent moduleComponent, Component component) throws JDSException {
        String parentClassName = moduleComponent.getClassName();
        String simClass = OODUtil.formatJavaName(moduleComponent.getCurrComponent().getAlias(), true);
        if (!parentClassName.endsWith("." + simClass)) {
            parentClassName = parentClassName.toLowerCase() + "." + simClass;
        }
        String projectName = moduleComponent.getProjectName();
        String cSimClass = OODUtil.formatJavaName(component.getAlias(), true);
        String cPackageName = parentClassName.toLowerCase();
        String cEuClassName = cPackageName + "." + cSimClass;
        DSMProperties dsmProperties = new DSMProperties();
        dsmProperties.setSourceClassName(moduleComponent.getClassName());
        ModuleProperties moduleProperties = new ModuleProperties();
        moduleProperties.setMethodName(OODUtil.getGetMethodName(component.getAlias()));
        moduleProperties.setDsmProperties(dsmProperties);

        ModuleComponent childComponent = new ModuleComponent();
        childComponent.setProperties(moduleProperties);
        childComponent.setCurrComponent(component);
        childComponent.addChildren(component);
        childComponent.setClassName(cEuClassName);

        CustomViewBean viewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(childComponent, domainId);
        String childRealPath = getXpath();
        if (!childRealPath.toLowerCase().endsWith("." + cSimClass.toLowerCase())) {
            childRealPath = childRealPath.toLowerCase() + "." + cSimClass;
        }
        viewBean.setXpath(childRealPath);
        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, cEuClassName, projectName);
        return aggRootBuild;
    }


    @Override
    @JSONField(serialize = false)
    public String getXpath() {
        String path = null;
        if (xpathBean != null && xpathBean.getPath() != null) {
            path = xpathBean.getPath();
        } else {
            MethodConfig methodConfig = getMethodConfig();
            CustomModuleBean moduleBean = this.getModuleBean();
            if (moduleBean != null && moduleBean.getEuClassName() != null) {
                CustomView view = methodConfig.getView();
                if (view != null && view.equals(this) || (moduleBean.getEuClassName().equals(methodConfig.getEUClassName()))) {
                    path = moduleBean.getEuClassName();
                } else {
                    path = ((CustomViewBean) view).getXpath();
                }

            } else if (methodConfig != null && methodConfig.getView() != null) {
                CustomView view = methodConfig.getView();
                if (view.equals(this)) {
                    path = methodConfig.getEUClassName();
                } else {
                    path = ((CustomViewBean) view).getXpath();
                }
            }

        }
        if (path != null) {
            path = path.toLowerCase();
        }

        return path;
    }

    public void setXpath(String xpath) {
        if (xpathBean == null) {
            xpathBean = new XPathBean(xpath);
        } else {
            xpathBean.setPath(xpath);
        }
    }

    private void initRealPath(ModuleComponent moduleComponent, Component component) {
        String realPath = component.getPath();
        if (moduleComponent == null) {
            moduleComponent = component.getModuleComponent();
        }

        if (moduleComponent != null && moduleComponent.getProperties() != null) {
            DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
            if (dsmProperties != null && dsmProperties.getRealPath() != null) {
                realPath = dsmProperties.getRealPath();
                String fieldName = OODUtil.formatJavaName(component.getAlias(), false).toLowerCase();
                if (!OODUtil.formatJavaName(realPath, false).endsWith("." + fieldName)) {
                    realPath = realPath + "." + component.getAlias();
                }
            }
        }
        this.setXpath(realPath);
    }

    protected void initHiddenField(String viewClassName) throws JDSException {
        if (viewClassName != null) {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(viewClassName, false);
            if (esdClassConfig != null) {
                List<FieldAggConfig> cols = esdClassConfig.getHiddenFieldList();
                for (FieldAggConfig esdField : cols) {
                    try {
                        FieldFormConfig config = new FieldFormConfig(esdField, this.getSourceClassName(), this.getMethodName());
                        fieldConfigMap.put(esdField.getFieldname(), (T) config);
                        fieldNames.add(esdField.getFieldname());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    public MethodConfig findEditorMethod(String... bindClassNames) {
        MethodConfig editorMethod = this.findMethodByEvent(CustomTabsEvent.TABEDITOR, bindClassNames);
        if (editorMethod == null) {
            editorMethod = this.findMethodByEvent(CustomTreeEvent.TREENODEEDITOR, bindClassNames);
        }
        if (editorMethod == null) {
            editorMethod = this.findMethodByMenuItem(CustomMenuItem.INDEX, bindClassNames);
        }
        return editorMethod;
    }

    public MethodConfig findEditorMethod(Class... bindClassList) {
        MethodConfig editorMethod = this.findMethodByEvent(CustomTabsEvent.TABEDITOR, bindClassList);
        if (editorMethod == null) {
            editorMethod = this.findMethodByEvent(CustomTreeEvent.TREENODEEDITOR, bindClassList);
        }
        if (editorMethod == null) {
            editorMethod = this.findMethodByMenuItem(CustomMenuItem.INDEX, bindClassList);
        }
        return editorMethod;
    }

    public MethodConfig findMethodByEvent(CustomEvent eventEnum, Class... bindClasses) {
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

    public MethodConfig findMethodByMenuItem(CustomMenuItem menuItem, Class... bindClassList) {
        MethodConfig methodConfig = null;
        if (bindClassList != null) {
            for (Class bindClass : bindClassList) {
                if (bindClass != null && !bindClass.equals(Void.class)) {
                    String bindClassName = bindClass.getName();
                    try {
                        ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                        if (config != null) {
                            methodConfig = config.getMethodByItem(menuItem);
                        } else {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bindClassName, false);
                            if (entityConfig != null) {
                                methodConfig = entityConfig.getMethodByItem(menuItem);
                            }
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

    public MethodConfig findMethodByMenuItem(CustomMenuItem menuItem, String... bindClassNames) {
        MethodConfig methodConfig = null;
        if (bindClassNames != null) {
            for (String bindClassName : bindClassNames) {
                if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName()) && !bindClassName.equals(Enum.class.getName())) {
                    try {
                        ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                        if (config != null) {
                            methodConfig = config.getMethodByItem(menuItem);
                        } else {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bindClassName, false);
                            if (entityConfig != null) {
                                methodConfig = entityConfig.getMethodByItem(menuItem);
                            }
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


    public MethodConfig findMethodByEvent(CustomEvent eventEnum, String... bindClassNames) {
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
                            if (entityConfig != null) {
                                methodConfig = entityConfig.getMethodByEvent(eventEnum);
                            }
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


    protected List<Component> cloneComponentList(List<Component> childs) {
        List<Component> components = new ArrayList<>();
        if (childs != null) {
            for (Component component : childs) {
                if (component instanceof ModuleComponent) {
                    ModuleComponent moduleComponent = (ModuleComponent) component;
                    try {
                        EUModule module = ESDFacrory.getAdminESDClient().getModule(moduleComponent.getClassName(), moduleComponent.getProjectName());
                        if (module != null && module.getComponent() != null) {
                            Component currComponent = module.getComponent().clone();
                            currComponent.setTarget(component.getTarget());
                            currComponent.setAlias(component.getAlias());
                            components.add(currComponent);
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }

                } else {
                    components.add(component.clone());
                }

            }
        }
        return components;
    }

    public ComponentList getCustomComponentBeans() {
        return customComponentBeans;
    }

    public void setCustomComponentBeans(ComponentList customComponentBeans) {
        this.customComponentBeans = customComponentBeans;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public ViewJavaSrcBean getViewJavaSrcBean() {
        if (viewJavaSrcBean == null) {
            viewJavaSrcBean = new ViewJavaSrcBean();
        }
        return viewJavaSrcBean;
    }

    public List<Callable<List<JavaGenSource>>> getChildModules() {
        return childModules;
    }

    public void setChildModules(List<Callable<List<JavaGenSource>>> childModules) {
        this.childModules = childModules;
    }

    public void setViewJavaSrcBean(ViewJavaSrcBean viewJavaSrcBean) {
        this.viewJavaSrcBean = viewJavaSrcBean;
    }

    public XPathBean getXpathBean() {
        return xpathBean;
    }

    public void setXpathBean(XPathBean xpathBean) {
        this.xpathBean = xpathBean;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public List<ModuleFormulaInst> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<ModuleFormulaInst> formulas) {
        this.formulas = formulas;
    }

}
