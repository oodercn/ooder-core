package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.LayoutAnnotation;
import net.ooder.esd.annotation.LayoutItemAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.gen.view.GenLayoutChildModule;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@AnnotationType(clazz = LayoutAnnotation.class)
public class CustomLayoutViewBean extends CustomViewBean<FieldModuleConfig, LayoutListItem, LayoutComponent> implements ToolsBar {
    ModuleViewType moduleViewType = ModuleViewType.LAYOUTCONFIG;
    String top;
    BorderType borderType;
    String left;
    LayoutType type;
    Boolean dragSortable;
    List<String> listKey;
    Boolean flexSize;
    Boolean transparent;
    Integer conLayoutColumns;
    String sourceClassName;
    String domainId;
    String methodName;

    @JSONField(name = "items")
    List<CustomLayoutItemBean> layoutItems = new ArrayList<>();

    @JSONField(serialize = false)
    List<CustomModuleBean> moduleBeans = new ArrayList<>();

    EnumsClassBean enumsClassBean;

    @JSONField(serialize = false)
    NavComboBaseViewBean comboBaseViewBean;

    public CustomLayoutViewBean() {

    }

    public CustomLayoutViewBean(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(LayoutAnnotation.class, this);
        this.updateModule(moduleComponent);
    }

    @Override
    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        List<CustomModuleBean> navModuleBeans = new ArrayList<>();
        LayoutComponent currComponent = (LayoutComponent) moduleComponent.getCurrComponent();
        List<Component> components = this.cloneComponentList(currComponent.getChildren());
        LayoutProperties layoutProperties = currComponent.getProperties();
        if (tabItems == null || tabItems.isEmpty()) {
            tabItems = layoutProperties.getItems();
        }
        List<GenLayoutChildModule> tasks = new ArrayList<GenLayoutChildModule>();
        if (components != null && components.size() > 0) {
            for (Component childComponent : components) {
                GenLayoutChildModule genChildModule = new GenLayoutChildModule(moduleComponent, childComponent, this);
                tasks.add(genChildModule);
            }
            List<Future<CustomModuleBean>> futures = null;
            try {
                ExecutorService service = RemoteConnectionManager.createConntctionService(this.getXpath());
                futures = service.invokeAll(tasks);
                for (Future<CustomModuleBean> resultFuture : futures) {
                    try {
                        CustomModuleBean cModuleBean = resultFuture.get();
                        if (navModuleBeans != null && !navModuleBeans.contains(cModuleBean)) {
                            navModuleBeans.add(cModuleBean);
                            javaSrcBeans.addAll(cModuleBean.getJavaSrcBeans());
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                service.shutdownNow();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        this.setModuleBeans(navModuleBeans);
        this.setTabItems(tabItems);
        this.init(layoutProperties, moduleComponent.getProjectName());
        this.addChildJavaSrc(javaSrcBeans);
        return javaSrcBeans;
    }

    public ComponentBean findComByPos(PosType posType) {
        CustomLayoutItemBean itemBean = this.getLayoutItem(posType);
        if (itemBean != null) {
            MethodConfig editorMethod = this.findEditorMethod(itemBean.getBindClass());
            if (editorMethod != null) {
                CustomViewBean customViewBean = editorMethod.getView();
                if (customViewBean != null) {
                    return customViewBean;
                }
            }
        }
        return null;
    }

    @Override
    public ComponentBean findComByAlias(String alias) {
        alias = OODUtil.formatJavaName(alias, false);
        if (alias != null) {
            alias = alias.toLowerCase();
        }
        if (OODUtil.formatJavaName(this.getId(), false).equals(alias)) {
            return this;
        } else {
            List<CustomModuleBean> moduleBeans = this.getModuleBeans();
            for (CustomModuleBean moduleBean : moduleBeans) {
                MethodConfig methodConfig = moduleBean.getMethodConfig();
                if (methodConfig != null) {
                    CustomViewBean customViewBean = methodConfig.getView();
                    if (customViewBean != null) {
                        ComponentBean componentBean = customViewBean.findComByAlias(alias);
                        if (componentBean != null) {
                            return componentBean;
                        }
                    }
                }
            }

            for (CustomLayoutItemBean itemBean : layoutItems) {
                MethodConfig editorMethod = this.findEditorMethod(itemBean.getBindClass());
                if (editorMethod != null) {
                    CustomViewBean customViewBean = editorMethod.getView();
                    if (customViewBean != null) {
                        ComponentBean componentBean = customViewBean.findComByAlias(alias);
                        if (componentBean != null) {
                            return componentBean;
                        }
                    }
                }
            }
        }
        return null;
    }

    public CustomLayoutViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        ESDClass esdClass = methodAPIBean.getViewClass();
        if (esdClass != null) {
            LayoutAnnotation moduleAnnotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), LayoutAnnotation.class);
            if (moduleAnnotation == null) {
                AnnotationUtil.fillDefaultValue(LayoutAnnotation.class, this);
            } else {
                init(moduleAnnotation);
            }
            try {

                if (enumClass != null) {
                    this.enumsClassBean = new EnumsClassBean(enumClass);
                    List<LayoutListItem> layoutListItems = ESDEnumsUtil.getEnumItems(enumClass, LayoutListItem.class);
                    for (LayoutListItem layoutListItem : layoutListItems) {
                        CustomLayoutItemBean layoutItem = new CustomLayoutItemBean(layoutListItem);
                        CustomLayoutItemBean layoutItemBean = this.getLayoutItem(layoutItem.getPos());
                        if (layoutItemBean != null) {
                            layoutItems.remove(layoutItemBean);
                        }
                        layoutItems.add(layoutItem);
                    }
                }

                if (!esdClass.getCtClass().isEnum() && !LayoutListItem.class.isAssignableFrom(esdClass.getCtClass())) {
                    AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(esdClass.getCtClass().getName(), false);
                    if (esdClassConfig != null) {
                        List<MethodConfig> allMethods = esdClassConfig.getAllMethods();
                        for (MethodConfig methodConfig : allMethods) {
                            CustomLayoutItemBean layoutItem = methodConfig.getLayoutItem();
                            if (layoutItem != null) {
                                CustomLayoutItemBean layoutItemBean = this.getLayoutItem(layoutItem.getPos());
                                if (layoutItemBean != null) {
                                    layoutItems.remove(layoutItemBean);
                                }
                                layoutItems.add(layoutItem);
                            }
                        }
                    }
                }


            } catch (JDSException e) {
                e.printStackTrace();
            }

        }

        this.sourceClassName = methodAPIBean.getSourceClassName();
        this.domainId = methodAPIBean.getDomainId();
        this.sourceMethodName = methodAPIBean.getMethodName();
        this.methodName = methodAPIBean.getMethodName();
    }

    @Override
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        annotationBeans.add(this);
        return annotationBeans;
    }

    public void init(LayoutProperties layoutProperties, String projectName) {
        this.name = layoutProperties.getName();
        itemConfigMap = new CaselessStringKeyHashMap<>();
        itemNames = new LinkedHashSet<String>();
        tabItems = layoutProperties.getItems();
        moduleBeans = this.getModuleBeans();
        for (LayoutListItem layoutItem : layoutProperties.getItems()) {
            String euClassName = layoutItem.getEuClassName();
            if (euClassName != null) {
                try {
                    EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, projectName);
                    if (euModule == null) {
                        euModule = CustomViewFactory.getInstance().getView(euClassName, projectName);
                    }
                    MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
                    CustomModuleBean customModuleBean = new CustomModuleBean(euModule.getComponent());
                    customModuleBean.reBindMethod(methodConfig);
                    try {
                        Class clazz = ClassUtility.loadClass(euClassName);
                    } catch (ClassNotFoundException e) {
                        CustomViewBean customViewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(euModule.getComponent(), domainId);
                        customViewBean.setDomainId(methodConfig.getDomainId());
                        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, euClassName, projectName);
                        List<JavaSrcBean> serviceList = aggRootBuild.getAggServiceRootBean();
                        if (serviceList == null || serviceList.isEmpty()) {
                            serviceList = aggRootBuild.build();
                        }
                        for (JavaSrcBean javaSrcBean : serviceList) {
                            if (javaSrcBean.getTarget() != null && javaSrcBean.getTarget().equals(layoutItem.getId())) {
                                try {
                                    Class bindService = ClassUtility.loadClass(javaSrcBean.getClassName());
                                    customViewBean.reBindService(bindService);
                                } catch (ClassNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                    moduleBeans.add(customModuleBean);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }


        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(layoutProperties), Map.class), this, false, false);

        tabItems = layoutProperties.getItems();
        for (LayoutListItem layoutItem : tabItems) {
            CustomLayoutItemBean customLayoutItemBean = new CustomLayoutItemBean(layoutItem);
            FieldModuleConfig config = itemConfigMap.get(customLayoutItemBean.getId());
            if (config != null) {
                if (config.getMethodConfig() != null) {
                    customLayoutItemBean.setEuClassName(config.getMethodConfig().getEUClassName());
                    customLayoutItemBean.setBindClass(new Class[]{config.getMethodConfig().getSourceClass().getCtClass()});
                } else {
                    customLayoutItemBean.setEuClassName(config.getViewClassName());
                    try {
                        customLayoutItemBean.setBindClass(new Class[]{ClassUtility.loadClass(config.getSourceClassName())});
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                layoutItems.add(customLayoutItemBean);
            }

        }

    }

    public CustomViewBean getItemViewBean(LayoutListItem currListItem, String projectName) throws JDSException {
        CustomViewBean customViewBean = null;
        if (currListItem.getEuClassName() != null) {
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currListItem.getEuClassName(), projectName);
            if (euModule != null) {
                ModuleComponent cmoduleComponent = euModule.getComponent();
                if (cmoduleComponent.getMethodAPIBean() != null) {
                    customViewBean = cmoduleComponent.getMethodAPIBean().getView();
                }
            }
        }
        return customViewBean;
    }

    public LayoutListItem findTabItem(String target) {
        return this.findTabItem(target, tabItems);
    }

    public LayoutListItem findTabItem(String target, List<LayoutListItem> navTabItems) {
        if (target == null) {
            return navTabItems.get(0);
        } else {
            for (LayoutListItem tabItem : navTabItems) {
                if (target.equals(tabItem.getId())) {
                    return tabItem;
                }
            }
        }
        return null;
    }

    public Set<MethodConfig> bindItem(List<JavaSrcBean> javaSrcBeanList, TabListItem currListItem) {
        List<Class> classList = new ArrayList<>();
        if (currListItem.getBindClass() != null && currListItem.getBindClass().length > 0) {
            for (Class clazz : currListItem.getBindClass()) {
                try {
                    classList.add(ClassUtility.loadClass(clazz.getName()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Set<MethodConfig> methodConfigs = new HashSet<>();
        for (JavaSrcBean srcBean : javaSrcBeanList) {
            try {
                if (srcBean != null) {
                    MethodConfig editorMethod = findEditorMethod(srcBean.getClassName());
                    if (editorMethod != null) {
                        currListItem.setEuClassName(editorMethod.getEUClassName());
                        RequestParamBean[] requestParamBeanArr = (RequestParamBean[]) editorMethod.getParamSet().toArray(new RequestParamBean[]{});
                        currListItem.fillParams(requestParamBeanArr, null);
                        methodConfigs.add(editorMethod);
                    }

                    Class clazz = ClassUtility.loadClass(srcBean.getClassName());
                    if (!classList.contains(clazz)) {
                        classList.add(clazz);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        currListItem.setBindClass(classList.toArray(new Class[]{}));
        return methodConfigs;
    }

    public void init(LayoutAnnotation layoutAnnotation) {
        this.type = layoutAnnotation.type();
        this.left = layoutAnnotation.left();
        this.borderType = layoutAnnotation.borderType();
        this.top = layoutAnnotation.top();
        this.transparent = layoutAnnotation.transparent();
        this.dragSortable = layoutAnnotation.dragSortable();
        this.flexSize = layoutAnnotation.flexSize();

        if (layoutAnnotation.conLayoutColumns() != -1) {
            conLayoutColumns = layoutAnnotation.conLayoutColumns();
        }
        if (layoutAnnotation.listKey().length > 0) {
            listKey = Arrays.asList(layoutAnnotation.listKey());
        }
        for (LayoutItemAnnotation itemAnnotation : layoutAnnotation.layoutItems()) {
            layoutItems.add(new CustomLayoutItemBean(itemAnnotation));
        }

    }

    public CustomLayoutItemBean getLayoutItem(PosType posType) {
        for (CustomLayoutItemBean itemBean : layoutItems) {
            if (itemBean.getPos().equals(posType)) {
                return itemBean;
            }
        }
        for (LayoutListItem itemBean : tabItems) {
            if (itemBean.getPos().equals(posType)) {
                return new CustomLayoutItemBean(itemBean);
            }
        }

        return null;
    }

    @Override
    public ComponentBean findComByPath(String path) {
        path = OODUtil.formatJavaName(path, false);
        if (path != null) {
            path = path.toLowerCase();
        }
        List<CustomModuleBean> moduleBeans = this.getModuleBeans();
        for (CustomModuleBean moduleBean : moduleBeans) {
            MethodConfig methodConfig = moduleBean.getMethodConfig();
            if (methodConfig != null && methodConfig.getView() != null) {
                CustomViewBean customViewBean = methodConfig.getView();
                ComponentBean componentBean = customViewBean.findComByPath(path);
                if (componentBean != null) {
                    return componentBean;
                }

            }
        }
        return null;
    }

    public List<CustomModuleBean> getModuleBeans() {
        if (moduleBeans == null || moduleBeans.size() == 0) {
            this.moduleBeans = new ArrayList<>();
            List<CustomLayoutItemBean> itemBeans = this.getLayoutItems();
            for (CustomLayoutItemBean layoutItemBean : itemBeans) {
                if (layoutItemBean.getBindClass() != null) {
                    MethodConfig methodConfig = this.findEditorMethod(layoutItemBean.getBindClass());
                    if (methodConfig != null) {
                        CustomModuleBean moduleBean = new CustomModuleBean(methodConfig);
                        moduleBeans.add(moduleBean);
                    }
                }
            }
        }
        return moduleBeans;
    }

    public void setModuleBeans(List<CustomModuleBean> moduleBeans) {
        this.moduleBeans = moduleBeans;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public CustomLayoutViewBean(LayoutAnnotation layoutAnnotation) {
        init(layoutAnnotation);
    }

    public CustomLayoutViewBean fillData(LayoutAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public NavComboBaseViewBean getComboBaseViewBean() {
        return comboBaseViewBean;
    }

    public void setComboBaseViewBean(NavComboBaseViewBean comboBaseViewBean) {
        this.comboBaseViewBean = comboBaseViewBean;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        for (CustomLayoutItemBean layoutItemBean : layoutItems) {
            if (layoutItemBean.getBindClass() != null && layoutItemBean.getBindClass().length > 0) {
                classSet.addAll(Arrays.asList(layoutItemBean.getBindClass()));
            }
        }

        for (LayoutListItem layoutListItem : tabItems) {
            if (layoutListItem.getBindClass() != null && layoutListItem.getBindClass().length > 0) {
                classSet.addAll(Arrays.asList(layoutListItem.getBindClass()));
            }
        }
        return ClassUtility.checkBase(classSet);
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public LayoutType getType() {
        return type;
    }

    public void setType(LayoutType type) {
        this.type = type;
    }

    public Boolean getDragSortable() {
        return dragSortable;
    }

    public void setDragSortable(Boolean dragSortable) {
        this.dragSortable = dragSortable;
    }

    public List<CustomLayoutItemBean> getLayoutItems() {
        return layoutItems;
    }

    public void setLayoutItems(List<CustomLayoutItemBean> layoutItems) {
        this.layoutItems = layoutItems;
    }

    public List<String> getListKey() {
        return listKey;
    }

    public void setListKey(List<String> listKey) {
        this.listKey = listKey;
    }

    public EnumsClassBean getEnumsClassBean() {
        return enumsClassBean;
    }

    public void setEnumsClassBean(EnumsClassBean enumsClassBean) {
        this.enumsClassBean = enumsClassBean;
    }

    public Boolean getFlexSize() {
        return flexSize;
    }

    public void setFlexSize(Boolean flexSize) {
        this.flexSize = flexSize;
    }

    public Boolean getTransparent() {
        return transparent;
    }

    public void setTransparent(Boolean transparent) {
        this.transparent = transparent;
    }

    public Integer getConLayoutColumns() {
        return conLayoutColumns;
    }

    public void setConLayoutColumns(Integer conLayoutColumns) {
        this.conLayoutColumns = conLayoutColumns;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.LAYOUT;
    }

}
