package net.ooder.esd.custom.properties;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomTabsFieldBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.properties.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NavTabsComponent extends TabsComponent<NavTabsProperties> {


    public NavTabsComponent(EUModule euModule, FieldFormConfig<CustomTabsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getMethodConfig().getFieldName());
        try {
            this.init(euModule, field.getMethodConfig(), valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public NavTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) {
        super(methodConfig.getFieldName());
        try {
            this.init(module, methodConfig, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    public NavTabsComponent(TabsViewBean tabsViewBean, Map<String, Object> valueMap) {
        super(tabsViewBean.getName());
        try {
            init(tabsViewBean, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.setAlias(tabsViewBean.getMethodName());
    }

    void init(TabsViewBean tabsViewBean, Map<String, Object> valueMap) throws JDSException {
        this.setProperties(new NavTabsProperties(tabsViewBean, valueMap));
        if (tabsViewBean.getAutoSave() != null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE,TabsEventEnum.beforePageClose);
            this.addAction(saveAction,false);
        }

        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            MethodConfig methodConfig = childTabViewBean.getMethodConfig();
            if (methodConfig!=null){
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
        TabsViewBean tabsViewBean = (TabsViewBean) methodConfig.getView();
        NavTabsProperties viewsProperties = new NavTabsProperties(tabsViewBean, valueMap);
        this.setProperties(viewsProperties);
        if (tabsViewBean.getAutoSave() != null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE,TabsEventEnum.beforePageClose);
            this.addAction( saveAction,false);
        }

        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            MethodConfig childMethodConfig = childTabViewBean.getMethodConfig();
            if (childMethodConfig != null) {
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
                this.addAction(showAction,false);
            }

        } else {
            if (!this.getEvents().containsKey(TabsEventEnum.onIniPanelView)) {
                this.addAction( showAction,false);
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
                this.addAction( clickItemAction,false);
            }
        }


    }


}
