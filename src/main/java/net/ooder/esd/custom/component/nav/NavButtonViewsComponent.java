package net.ooder.esd.custom.component.nav;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomButtonViewsFieldBean;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonViewsProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NavButtonViewsComponent extends ButtonViewsComponent {


    public NavButtonViewsComponent(EUModule euModule, FieldFormConfig<CustomButtonViewsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super();
        try {
            this.init(euModule, field.getMethodConfig(), valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void init(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
        List<FieldModuleConfig> moduleConfigList = methodConfig.getView().getNavItems();
        CustomButtonViewsViewBean viewBean = (CustomButtonViewsViewBean) methodConfig.getView();
        ButtonViewsProperties viewsProperties = new ButtonViewsProperties(methodConfig, valueMap);
        this.setProperties(viewsProperties);
        this.setAlias(methodConfig.getName());
        for (FieldModuleConfig moduleInfo : moduleConfigList) {
            MethodConfig method = moduleInfo.getMethodConfig();
            EUModule childModule = method.getModule(valueMap, module.getProjectVersion().getProjectName());
            if (method != null) {
                ModuleViewType moduleViewType = method.getView().getModuleViewType();
                if (moduleViewType.equals(ModuleViewType.DYNCONFIG)) {
                  //  EUModule childModule = method.getModule(valueMap, module.getProjectVersion().getProjectName());
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


//            else if (method != null && method.isModule()) {
//                ModuleComponent moduleComponent = new ModuleComponent();
//                moduleComponent.setClassName(method.getEUClassName());
//                moduleComponent.setAlias(method.getName());
//                moduleComponent.setTarget(moduleInfo.getFieldname());
//                moduleComponent.getProperties().setDock(Dock.fill);
//                moduleComponent.getModuleVar().putAll(method.getTagVar());
//                //this.addChildren(moduleComponent);
//            }
        }

        Action showAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onItemSelected);
        showAction.updateArgs(this.getAlias(), 4);
        if (viewBean != null && viewBean.getAutoReload() != null && viewBean.getAutoReload()) {
            this.addAction(showAction);
        } else {
            showAction.setEventKey(TabsEventEnum.onIniPanelView);
            this.addAction( showAction);
        }


        if (viewsProperties.getItems().size() > 0) {
            Action clickItemAction = new Action(TabsEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(this.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            clickItemAction.setArgs(Arrays.asList(new String[]{viewsProperties.getFristId()}));
            this.addAction(clickItemAction);
        }

    }

    public NavButtonViewsComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) {
        super();
        try {
            this.init(module, methodConfig, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }
}
