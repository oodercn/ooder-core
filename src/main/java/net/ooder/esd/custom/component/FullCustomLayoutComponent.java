package net.ooder.esd.custom.component;

import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.LayoutViewAnnotation;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.LayoutProperties;

import java.util.List;
import java.util.Map;

@CustomClass(clazz = LayoutViewAnnotation.class, viewType = CustomViewType.LISTMODULE, moduleType = ModuleViewType.LAYOUTCONFIG)
public class FullCustomLayoutComponent extends CustomModuleComponent<LayoutComponent> {


    public FullCustomLayoutComponent() {
        super();
    }

    public FullCustomLayoutComponent(EUModule module, MethodConfig methodConfig, Map valueMap) {
        super(module, methodConfig, valueMap);
        CustomLayoutViewBean viewBean = (CustomLayoutViewBean) methodConfig.getView();
        LayoutProperties layoutProperties = new LayoutProperties(viewBean);
        LayoutComponent layoutComponent = new LayoutComponent(euModule.getName() + ComponentType.LAYOUT.name(), layoutProperties);
        try {
            if (methodConfig.getViewClass() != null) {
                AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(methodConfig.getViewClass().getClassName(), false);
                List<MethodConfig> methodConfigs = aggEntityConfig.getAllMethods();
                for (MethodConfig itemMethod : methodConfigs) {
                    if (itemMethod.getLayoutItem() != null) {
                        CustomLayoutItemBean layoutItemBean = itemMethod.getLayoutItem();
                        layoutItemBean.setParentAlias(this.getAlias());
                        Class ctClass = itemMethod.getInnerReturnType();
                        if (ModuleComponent.class.isAssignableFrom(ctClass)) {
                            try {
                                ModuleComponent moduleComponent = (ModuleComponent) itemMethod.getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), JDSActionContext.getActionContext().getContext());
                                moduleComponent.setAlias(itemMethod.getName());
                                moduleComponent.setTarget(layoutItemBean.getPos().name());
                                moduleComponent.getModuleVar().putAll(itemMethod.getTagVar());
                                layoutComponent.addChildren(moduleComponent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Component.class.isAssignableFrom(ctClass)) {
                            try {
                                Component component = (Component) itemMethod.getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), JDSActionContext.getActionContext().getContext());
                                component.setTarget(layoutItemBean.getPos().name());
                                layoutComponent.addChildren(component);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ModuleComponent moduleComponent = new ModuleComponent();
                            moduleComponent.setAlias(itemMethod.getName());
                            moduleComponent.setClassName(itemMethod.getEUClassName());
                            moduleComponent.setTarget(layoutItemBean.getPos().name());
                            moduleComponent.getModuleVar().putAll(itemMethod.getTagVar());
                            layoutComponent.addChildren(moduleComponent);
                        }
                    }

                }
            }

            this.addChildLayoutNav(layoutComponent);
            super.fillAction(viewBean);
            this.fillToolBar(viewBean, layoutComponent);
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }

}
