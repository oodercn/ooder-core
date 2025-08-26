package net.ooder.esd.custom.component.nav;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFoldingTabsFieldBean;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FoldingTabsComponent;
import net.ooder.esd.tool.properties.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NavFoldingTabsComponent extends FoldingTabsComponent {
//    public NavFoldingTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
//        super();
//
//        this.setProperties(new NavFoldingTabsProperties(methodConfig, valueMap));
//        List<FieldModuleConfig> fieldModuleConfigList = methodConfig.getView().getNavItems();
//
//        for (FieldModuleConfig moduleInfo : fieldModuleConfigList) {
//            EUModule childModule = moduleInfo.getModule(valueMap);
//
//            if (childModule.getComponent().getModuleViewType().equals(ModuleViewType.DYNConfig)) {
//                Component component = childModule.getComponent().getTopComponentBox();
//                if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
//                    this.addChildren(component);
//                }
//                List<Component> apiComponents = childModule.getComponent().findComponents(ComponentType.APICaller, null);
//                for (Component apiComponent : apiComponents) {
//                    if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
//                        childModule.getComponent().addChildren(apiComponent);
//                    }
//                }
//
//            } else {
//
//                ModuleComponent moduleComponent = new ModuleComponent();
//                moduleComponent.setClassName(moduleInfo.getMethodConfig().getEUClassName());
//                moduleComponent.setAlias(moduleInfo.getMethodConfig().getName());
//                moduleComponent.setAlias(moduleInfo.getMethodConfig().getMethodName());
//                moduleComponent.getModuleVar().putAll(moduleInfo.getMethodConfig().getTagVar());
//                this.addChildren(moduleComponent);
//            }
//        }
//        this.setAlias(methodConfig.getName() );
//    }

    public NavFoldingTabsComponent(EUModule euModule, FieldFormConfig<CustomFoldingTabsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super();
        try {
            this.init(euModule, field.getMethodConfig(), valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public NavFoldingTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) {
        super();
        try {
            this.init(module, methodConfig, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    public NavFoldingTabsComponent(NavFoldingTabsViewBean tabsViewBean, Map<String, Object> valueMap) {
        super();
        try {
            init(tabsViewBean, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.setAlias(tabsViewBean.getMethodName());
    }

    void init(NavFoldingTabsViewBean tabsViewBean, Map<String, Object> valueMap) throws JDSException {
        this.setProperties(new NavFoldingTabsProperties(tabsViewBean, valueMap));
        if (tabsViewBean.getAutoSave()!=null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE,TabsEventEnum.beforePageClose);
            this.addAction(saveAction,false);
        }

        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            List<MethodConfig> methodConfigList = childTabViewBean.getMethodConfigList();
            for(MethodConfig methodConfig:methodConfigList ){
                ModuleViewType moduleViewType = methodConfig.getView().getModuleViewType();
                if (moduleViewType.equals(ModuleViewType.DYNCONFIG)) {
                    EUModule childModule = methodConfig.getModule(valueMap, getModuleComponent().getProjectName());
                    Component component = childModule.getComponent().getTopComponentBox();
                    if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
                        this.addChildren(component);
                    }
                    List<Component> apiComponents = childModule.getComponent().findComponents(ComponentType.APICALLER, null);
                    for (Component apiComponent : apiComponents) {
                        if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
                            childModule.getComponent().addChildren(apiComponent);
                        }
                    }
                }
            }


        }
    }

    public void init(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
        NavFoldingTabsViewBean tabsViewBean = (NavFoldingTabsViewBean) methodConfig.getView();
        NavFoldingTabsProperties viewsProperties = new NavFoldingTabsProperties(tabsViewBean, valueMap);
        this.setProperties(viewsProperties);
        if (tabsViewBean.getAutoSave()!=null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE,TabsEventEnum.beforePageClose);
            this.addAction(saveAction);
        }

        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            List<MethodConfig> methodConfigList = childTabViewBean.getMethodConfigList();
            for(MethodConfig childMethodConfig:methodConfigList){
                ModuleViewType moduleViewType = childMethodConfig.getView().getModuleViewType();
                if (moduleViewType.equals(ModuleViewType.DYNCONFIG)) {
                    EUModule childModule = childMethodConfig.getModule(valueMap, getModuleComponent().getProjectName());
                    Component component = childModule.getComponent().getTopComponentBox();
                    if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
                        this.addChildren(component);
                    }
                    List<Component> apiComponents = childModule.getComponent().findComponents(ComponentType.APICALLER, null);
                    for (Component apiComponent : apiComponents) {
                        if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
                            childModule.getComponent().addChildren(apiComponent);
                        }
                    }
                }
            }


        }


        Action showAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onItemSelected);
        showAction.updateArgs(this.getAlias(), 4);
        if (tabsViewBean != null && tabsViewBean.getAutoReload()) {
            if (!this.getEvents().containsKey(TabsEventEnum.onItemSelected)) {
                this.addAction(showAction);
            }

        } else {
            if (!this.getEvents().containsKey(TabsEventEnum.onIniPanelView)) {
                showAction.setEventKey(TabsEventEnum.onIniPanelView);
                this.addAction( showAction);
            }
        }


        if (viewsProperties.getItems().size() > 0) {
            Action clickItemAction = new Action(TabsEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(this.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            clickItemAction.setArgs(Arrays.asList(new String[]{viewsProperties.getFristId()}));
            if (!this.getEvents().containsKey(TabsEventEnum.onRender)) {
                this.addAction(clickItemAction);
            }
        }


    }


}
