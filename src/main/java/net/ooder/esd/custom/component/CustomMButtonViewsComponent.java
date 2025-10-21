package net.ooder.esd.custom.component;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.MButtonViewsViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.MButtonViewsComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonViewsProperties;

import java.util.List;
import java.util.Map;

public class CustomMButtonViewsComponent extends MButtonViewsComponent {
    public CustomMButtonViewsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException {
        super();
        MButtonViewsViewBean tabsViewBean = (MButtonViewsViewBean) methodConfig.getView();
        init(tabsViewBean, valueMap);
        this.setAlias(methodConfig.getName() );
    }

    public CustomMButtonViewsComponent(MButtonViewsViewBean tabsViewBean, Map<String, Object> valueMap) throws JDSException {
        super();
        init(tabsViewBean, valueMap);
    }

    void init(MButtonViewsViewBean tabsViewBean, Map<String, Object> valueMap) throws JDSException {
        this.setProperties(new ButtonViewsProperties(tabsViewBean, valueMap));
        if (tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE,TabsEventEnum.beforePageClose);
            this.addAction(saveAction,false);
        }

        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
           // MethodConfig methodConfig = childTabViewBean.getMethodConfig();

            List<MethodConfig> methodConfigList = childTabViewBean.getMethodConfigList();
            for(MethodConfig methodConfig:methodConfigList){
                ModuleViewType moduleViewType = methodConfig.getView().getModuleViewType();
                if (moduleViewType.equals(ModuleViewType.DYNCONFIG)) {
                    EUModule childModule = methodConfig.getModule(valueMap, this.getModuleComponent().getProjectName());
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

                } else if (methodConfig != null && methodConfig.isModule()) {
                    ModuleComponent moduleComponent = new ModuleComponent();
                    moduleComponent.setClassName(methodConfig.getEUClassName());
                    moduleComponent.setAlias(methodConfig.getName());
                    moduleComponent.setTarget(childTabViewBean.getId());
                    moduleComponent.getProperties().setDock(Dock.fill);
                    moduleComponent.getModuleVar().putAll(methodConfig.getTagVar());
                    this.addChildren(moduleComponent);
                }
            }


        }
    }


}
