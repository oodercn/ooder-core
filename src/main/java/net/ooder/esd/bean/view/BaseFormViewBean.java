package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComboType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.CustomBlockFieldBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.gen.view.GenFormChildModule;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.OODTypeMapping;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class BaseFormViewBean<M extends Component> extends CustomViewBean<FieldFormConfig, UIItem, M> implements ComponentBean<M> {

    CustomWidgetBean widgetBean;

    RightContextMenuBean contextMenuBean;

    List<CustomFormMenu> toolBarMenu = new ArrayList<>();

    List<CustomFormMenu> customMenu = new ArrayList<>();

    List<CustomFormMenu> bottombarMenu = new ArrayList<>();

    Set<CustomFormEvent> event = new LinkedHashSet<>();

    public BaseFormViewBean() {

    }


    public BaseFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        this.methodName = methodAPIBean.getMethodName();
        this.sourceMethodName = methodAPIBean.getMethodName();
        this.name = StringUtility.formatJavaName(methodName, true);
        try {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
            if (esdClassConfig != null) {
                List<FieldAggConfig> cols = esdClassConfig.getFieldList();
                for (FieldAggConfig esdField : cols) {
                    FieldFormConfig config = new FieldFormConfig(esdField, methodAPIBean.getSourceClassName(), methodAPIBean.getMethodName());
                    fieldConfigMap.put(esdField.getFieldname(), config);
                    fieldNames.add(esdField.getFieldname());
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }


        RightContextMenu contextMenu = AnnotationUtil.getClassAnnotation(clazz, RightContextMenu.class);
        if (contextMenu != null) {
            contextMenuBean = new RightContextMenuBean(this.getName(), contextMenu);
        }
        if (toolBarMenu != null && toolBarMenu.size() > 0) {
            if (this.toolBar == null) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }

        if (customMenu != null && customMenu.size() > 0) {
            if (this.menuBar == null) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }

        if (bottombarMenu != null && bottombarMenu.size() > 0) {
            if (this.bottomBar == null) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }

    }


    @Override
    public abstract ModuleViewType getModuleViewType();


    public abstract void setModuleViewType(ModuleViewType moduleViewType);


    public void initMenuBar() {
        if (getBottomBar() != null && getBottomBar().getEnumItems() != null) {
            bottombarMenu = getBottomBar().getEnumItems();
        }
        if (getMenuBar() != null && getMenuBar().getEnumItems() != null) {
            customMenu = getMenuBar().getEnumItems();
        }
        if (getToolBar() != null && getToolBar().getEnumItems() != null) {
            toolBarMenu = getToolBar().getEnumItems();
        }
    }


    protected List<FieldFormConfig> genChildComponent(ModuleComponent moduleComponent, List<Component> components) {
        List<GenFormChildModule> tasks = new ArrayList<GenFormChildModule>();
        List<FieldFormConfig> fieldFormConfigs = new ArrayList<>();
        int index = 0;
        if (components != null) {
            for (Component component : components) {
                FieldFormConfig config = findFieldByCom(component);
                CustomFieldBean customFieldBean = config.createCustomBean();
                customFieldBean.setIndex(index);
                index++;
                ComponentType componentType = ComponentType.fromType(component.getKey());
                if (componentType.isBar()) {
                    genBar(moduleComponent, component);
                } else if (Arrays.asList(ComponentType.getCustomAPIComponents()).contains(componentType)) {
                    genAPI(moduleComponent, component);
                } else if (!skipType(component)) {
                    GenFormChildModule genChildModule = null;
                    if (component.getChildren() != null && component.getChildren().size() == 1 && componentType.equals(ComponentType.BLOCK)) {
                        component = component.getChildren().get(0);
                        if (component instanceof ModuleComponent) {
                            if (((ModuleComponent) component).getCurrComponent() != null) {
                                component = ((ModuleComponent) component).getCurrComponent().clone();
                                genChildModule = new GenFormChildModule(moduleComponent, component, config);
                            }
                        } else {
                            genChildModule = new GenFormChildModule(moduleComponent, component, config);
                        }
                    }
                    if (genChildModule == null) {
                        genChildModule = new GenFormChildModule(moduleComponent, component, config);
                    }

                    if (genChildModule != null) {
                        tasks.add(genChildModule);
                    } else {
                        config.update(moduleComponent, component);
                        fieldFormConfigs.add(config);
                    }
                }
            }

            try {
                String taskIds = this.getXpath() + "[" + System.currentTimeMillis() + "]";
                ExecutorService service = RemoteConnectionManager.createConntctionService(taskIds);
                List<Future<FieldFormConfig>> futures = service.invokeAll(tasks);
                for (Future<FieldFormConfig> resultFuture : futures) {
                    FieldFormConfig fieldFormConfig = null;
                    try {
                        fieldFormConfig = resultFuture.get();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (fieldFormConfig != null) {
                        fieldFormConfigs.add(fieldFormConfig);
                    }
                    if (fieldFormConfig.getWidgetConfig() != null && fieldFormConfig.getWidgetConfig() instanceof WidgetBean) {
                        AggRootBuild build = ((WidgetBean) fieldFormConfig.getWidgetConfig()).getFieldRootBuild();
                        if (build != null) {
                            this.childClassNameSet.add(build.getEuClassName());
                        }
                    }

                }
                service.shutdownNow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            DSMFactory.getInstance().saveCustomViewBean(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return fieldFormConfigs;
    }

    private void genAPI(ModuleComponent moduleComponent, Component component) {

        //todo
    }

    private void genBar(ModuleComponent moduleComponent, Component component) {
        //todo
    }

    private boolean skipType(Component child) {
        String alias = child.getAlias();
        ComponentType componentType = ComponentType.fromType(child.getKey());
        if (componentType.equals(ComponentType.BLOCK)) {
            BlockComponent component = (BlockComponent) child;
            if (alias.indexOf(CustomBlockFieldBean.skipStr) > -1) {
                return true;
            } else if (alias.endsWith(ModuleComponent.DefaultTopBoxfix)) {
                return true;
            } else if (component.getProperties().getComboType() != null) {
                return true;
            }
        } else if (alias.indexOf("StatusBottom") > -1) {
            return true;
        } else if (alias.equals(ModuleComponent.PAGECTXNAME)) {
            return true;
        }
        return false;
    }

    public FieldFormConfig addPidField(String fieldName) throws JDSException {
        FieldFormConfig fieldFormConfig = this.createField(fieldName, ComponentType.HIDDENINPUT, null, null, null);
        CustomFieldBean customFieldBean = fieldFormConfig.createCustomBean();
        customFieldBean.setPid(true);
        return fieldFormConfig;
    }

    public FieldFormConfig createUidField(String fieldName) throws JDSException {
        FieldFormConfig fieldFormConfig = this.createField(fieldName, ComponentType.HIDDENINPUT, null, null, null);
        CustomFieldBean customFieldBean = fieldFormConfig.createCustomBean();
        customFieldBean.setUid(true);
        this.setUidField(fieldFormConfig);
        return fieldFormConfig;
    }


    public FieldFormConfig<? extends WidgetBean, ? extends ComboBoxBean> createField(String fieldName, ComponentType componentType, ComboInputType inputType, ModuleComponent moduleComponent, String targer) throws JDSException {
        Class clazz = String.class;
        if (inputType == null) {
            inputType = ComboInputType.input;
        }
        clazz = OODTypeMapping.guessResultType(componentType);
        ComboType comboType = inputType.getComboType();
        if (comboType != null) {
            clazz = OODTypeMapping.guessResultComboType(comboType);
        }
        return createField(fieldName, componentType, moduleComponent, inputType, clazz, targer);

    }

    public FieldFormConfig<? extends WidgetBean, ? extends ComboBoxBean> createField(String fieldName, ComponentType componentType, ModuleComponent moduleComponent, ComboInputType inputType, Class clazz, String targer) throws JDSException {
        FieldFormConfig fieldFormConfig = this.getFieldByName(fieldName);
        if (fieldFormConfig == null && targer != null && moduleComponent != null) {
            List<Component> components = moduleComponent.findComponentsByTarget(targer);
            for (Component component : components) {
                if (ComponentType.fromType(component.getKey()).equals(componentType)) {
                    fieldFormConfig = this.getFieldByName(component.getAlias());
                }
            }
        }
        AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
        if (fieldFormConfig == null) {
            fieldFormConfig = new FieldFormConfig();
            fieldFormConfig.setId(fieldName);
            fieldFormConfig.setSourceClassName(sourceClassName);
            fieldFormConfig.setSourceMethodName(methodName);
            fieldFormConfig.setDomainId(domainId);
            fieldFormConfig.setViewClassName(getViewClassName());
            fieldFormConfig.setFieldname(fieldName);

        }
        if (moduleComponent != null) {
            if (fieldFormConfig.getComponentType() == null || !fieldFormConfig.getComponentType().equals(componentType)) {
                fieldFormConfig.setComponentType(componentType);
                FieldComponentBean widgetBean = fieldFormConfig.createDefaultWidget(componentType, moduleComponent, null);
                fieldFormConfig.setWidgetConfig(widgetBean);
            }
        }

        if (inputType != null) {
            if (fieldFormConfig.getComboConfig() == null || !fieldFormConfig.getComboConfig().getInputType().equals(inputType)) {
                componentType = ComponentType.COMBOINPUT;
                fieldFormConfig.setComponentType(componentType);
                ComboBoxBean comboBoxBean = fieldFormConfig.createDefaultCombo(inputType, null);
                fieldFormConfig.setComboConfig(comboBoxBean);
            }
        }


        if (aggEntityConfig != null) {
            FieldAggConfig aggConfig = aggEntityConfig.getFieldByName(fieldName);
            if (clazz != null) {
                aggConfig = aggEntityConfig.createField(fieldName, clazz, componentType);
            } else if (componentType.equals(ComponentType.MODULE)) {
                aggConfig = aggEntityConfig.createField(fieldName, String.class, componentType);
            } else {
                aggConfig = aggEntityConfig.createField(fieldName, ResultModel.class, componentType);
            }
            fieldFormConfig.setAggConfig(aggConfig);

        }
        fieldFormConfig.setComponentType(componentType);

        if (targer != null) {
            CustomFieldBean customFieldBean = fieldFormConfig.createCustomBean();
            customFieldBean.setTarget(targer);
        }
        fieldConfigMap.put(fieldName, fieldFormConfig);
        fieldNames.add(fieldName);
        return fieldFormConfig;
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }

        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }


        if (menuBar != null && !AnnotationUtil.getAnnotationMap(menuBar).isEmpty()) {
            annotationBeans.add(menuBar);
        }

        if (bottomBar != null && !AnnotationUtil.getAnnotationMap(bottomBar).isEmpty()) {
            annotationBeans.add(bottomBar);
        }

        if (toolBar != null && !AnnotationUtil.getAnnotationMap(toolBar).isEmpty()) {
            annotationBeans.add(toolBar);
        }
        if (contextMenuBean != null && !AnnotationUtil.getAnnotationMap(contextMenuBean).isEmpty()) {
            annotationBeans.add(contextMenuBean);
        }
        annotationBeans.add(this);

        return annotationBeans;
    }


    @Override
    public ToolBarMenuBean getToolBar() {
        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
                this.toolBar.setMenus(toolBarMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return toolBar;
    }

    @Override
    public MenuBarBean getMenuBar() {
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
                this.menuBar.setMenus(customMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return menuBar;
    }


    public FieldFormConfig findFieldByCom(Component component) {
        String fieldName = component.getProperties().getName();
        FieldFormConfig config = null;
        if (fieldName != null && !fieldName.equals("")) {
            config = fieldConfigMap.get(fieldName);
        }
        if (config == null) {
            fieldName = OODUtil.formatJavaName(component.getAlias(), false);
            config = fieldConfigMap.get(fieldName);
        }
        if (config == null) {
            config = new FieldFormConfig(component);
        }
        config.setFieldname(fieldName);
        fieldConfigMap.put(fieldName, config);
        fieldNames.add(fieldName);
        return config;
    }

    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());

            }
        }
        return bottomBar;
    }

    @Override
    @JSONField(serialize = false)
    public List<FieldFormConfig> getAllFields() {
        List<FieldFormConfig> fieldFormConfigList = new ArrayList<>();
        List<FieldFormConfig> fieldFormConfigs = super.getAllFields();
        for (FieldFormConfig fieldFormConfig : fieldFormConfigs) {
            if (!fieldFormConfig.getComponentType().equals(ComponentType.APICALLER)) {
                fieldFormConfigList.add(fieldFormConfig);
            }
        }
        return fieldFormConfigList;
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (bindService != null) {
            classSet.add(bindService);
        }
        for (FieldFormConfig fieldFormConfig : this.getAllFields()) {
            classSet.addAll(fieldFormConfig.getOtherClass());
        }
        return classSet;
    }


    @Override
    public abstract ComponentType getComponentType();


    public CustomWidgetBean getWidgetBean() {
        return widgetBean;
    }

    public void setWidgetBean(CustomWidgetBean widgetBean) {
        this.widgetBean = widgetBean;
    }

    @Override
    public ContainerBean getContainerBean() {
        return containerBean;
    }

    @Override
    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public List<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public List<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
    }


    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }


    public List<CustomFormMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<CustomFormMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

}
