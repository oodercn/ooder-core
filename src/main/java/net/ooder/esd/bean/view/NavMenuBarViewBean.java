package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.NavMenuBarAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.NavMenuBarViewAnnotation;
import net.ooder.esd.bean.*;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.MenuBarComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.MenuBarProperties;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = NavMenuBarAnnotation.class)
public class NavMenuBarViewBean extends NavComboBaseViewBean<TreeListItem> {

    ModuleViewType moduleViewType = ModuleViewType.NAVMENUBARCONFIG;

    MenuBarBean menuBarBean;

    @JSONField(serialize = false)
    public List<CustomModuleBean> moduleBeans = new ArrayList<>();


    public NavMenuBarViewBean() {

    }


    public NavMenuBarViewBean(ModuleComponent<MenuBarComponent> moduleComponent) {

        AnnotationUtil.fillDefaultValue(NavMenuBarViewAnnotation.class, this);
        this.updateModule(moduleComponent);
    }


    @Override
    public List<Callable> updateModule(ModuleComponent moduleComponent) {
        List<Callable> tasks = super.updateModule(moduleComponent);
        if (moduleComponent.getCurrComponent() instanceof MenuBarComponent) {
            MenuBarComponent component = (MenuBarComponent) moduleComponent.getCurrComponent();
            MenuBarProperties menuBarProperties = component.getProperties();
            if (moduleBean == null) {
                this.moduleBean = new CustomModuleBean(moduleComponent);
            }
            moduleBean.update(moduleComponent);
            this.init(menuBarProperties, moduleComponent.getProjectName());
        }
        return tasks;
    }


    @Override
    @JSONField(serialize = false)
    public CustomViewBean getCurrViewBean() {
        return this;
    }


    public NavMenuBarViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        MenuBarMenu annotation = AnnotationUtil.getClassAnnotation(clazz, MenuBarMenu.class);
        if (annotation != null) {
            menuBarBean = new MenuBarBean(clazz, annotation, null);
        }
        try {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.viewClassName, false);
            List<ESDField> cols = esdClassConfig.getESDClass().getFieldList();
            for (ESDField esdField : cols) {
                if (esdField != null) {
                    FieldFormConfig config = new FieldFormConfig(esdField, methodAPIBean.getSourceClassName(), methodAPIBean.getMethodName());
                    fieldConfigMap.put(esdField.getName(), config);
                    fieldNames.add(esdField.getName());
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public void init(MenuBarProperties tabsProperties, String projectName) {
        this.name = tabsProperties.getName();
        itemConfigMap = new CaselessStringKeyHashMap<>();
        itemNames = new LinkedHashSet<String>();
        tabItems = tabsProperties.getItems();

        List<TreeListItem> layoutItems = tabsProperties.getItems();
        if (layoutItems != null) {
            for (TabListItem layoutItem : layoutItems) {
                String euClassName = layoutItem.getEuClassName();
                if (euClassName != null) {
                    try {
                        EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, projectName);

                        if (euModule == null) {
                            euModule = CustomViewFactory.getInstance().getView(euClassName, projectName);
                        }
                        CustomViewBean customViewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(euModule.getComponent(), domainId);
                        CustomModuleBean customModuleBean = new CustomModuleBean(euModule.getComponent().getMethodAPIBean());
                        try {
                            Class clazz = ClassUtility.loadClass(euClassName);
                        } catch (ClassNotFoundException e) {
                            AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, euClassName, projectName);
                            List<JavaGenSource> serviceList = aggRootBuild.getAggServiceRootBean();
                            if (serviceList == null || serviceList.isEmpty()) {
                                serviceList = aggRootBuild.build();
                            }

                            for (JavaGenSource genSource : serviceList) {
                                JavaSrcBean javaSrcBean = genSource.getSrcBean();
                                if (javaSrcBean != null && javaSrcBean.getTarget().equals(layoutItem.getId())) {
                                    customModuleBean = new CustomModuleBean(euModule.getComponent());
                                    try {
                                        bindService = ClassUtility.loadClass(javaSrcBean.getClassName());
                                    } catch (ClassNotFoundException e1) {
                                        e1.printStackTrace();
                                    }
                                    customViewBean.reBindService(bindService);
                                    customModuleBean.reBindMethod(customViewBean.getMethodConfig());
                                }
                            }
                        }
                        moduleBeans.add(customModuleBean);
                        FieldModuleConfig config = new FieldModuleConfig(customModuleBean);
                        itemConfigMap.put(config.getId(), config);
                        itemNames.add(config.getId());
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(tabsProperties), Map.class), this, false, false);

    }

    @Override
    public ComponentBean findComByPath(String path) {
        path = OODUtil.formatJavaName(path, false);
        if (path != null) {
            path = path.toLowerCase();
        }
        List<CustomModuleBean> moduleBeans = this.getModuleBeans();
        for (CustomModuleBean moduleBean : moduleBeans) {
            CustomViewBean customViewBean = moduleBean.getMethodConfig().getView();
            if (customViewBean != null) {
                ComponentBean componentBean = customViewBean.findComByPath(path);
                if (componentBean != null) {
                    return componentBean;
                }

            }
        }

        return null;
    }


    public List<CustomModuleBean> getModuleBeans() {
        return moduleBeans;
    }

    public void setModuleBeans(List<CustomModuleBean> moduleBeans) {
        this.moduleBeans = moduleBeans;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.MENUBAR;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (menuBarBean != null) {
            annotationBeans.add(menuBarBean);
        }

        return annotationBeans;
    }

    public MenuBarBean getMenuBarBean() {
        return menuBarBean;
    }

    public void setMenuBarBean(MenuBarBean menuBarBean) {
        this.menuBarBean = menuBarBean;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        return classSet;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
