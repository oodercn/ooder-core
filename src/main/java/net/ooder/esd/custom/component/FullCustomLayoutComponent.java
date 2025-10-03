package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.*;
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
import net.ooder.esd.tool.properties.item.LayoutListItem;

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
        LayoutComponent layoutComponent = this.getLayoutComponent(viewBean);


        try {
            if (methodConfig.getViewClass() != null) {
                AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(methodConfig.getViewClass().getClassName(), false);
                List<MethodConfig> methodConfigs = aggEntityConfig.getAllMethods();
                for (MethodConfig moduleConfig : methodConfigs) {
                    if (moduleConfig.getLayoutItem() != null) {
                        CustomLayoutItemBean layoutItemBean = moduleConfig.getLayoutItem();
                        Class ctClass = moduleConfig.getViewClass().getCtClass();
                        if (Component.class.isAssignableFrom(ctClass)) {
                            try {
                                Component component = (Component) ctClass.newInstance();
                                component.setTarget(layoutItemBean.getPos().name());
                                layoutComponent.addChildren(component);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {

                            EUModule mainModule = moduleConfig.getModule(valueMap, module.getPackageName());
                            ModuleComponent moduleComponent = new ModuleComponent();
                            moduleComponent.setClassName(mainModule.getClassName());
                            moduleComponent.setAlias(mainModule.getComponent().getAlias());
                            moduleComponent.setClassName(moduleConfig.getEUClassName());
                            moduleComponent.setAlias(moduleConfig.getName());
                            moduleComponent.setTarget(layoutItemBean.getPos().name());
                            moduleComponent.getModuleVar().putAll(moduleConfig.getTagVar());
                            layoutComponent.addChildren(moduleComponent);
                        }
                    }

                }
            }

            this.addChildLayoutNav(layoutComponent);
            super.fillAction(viewBean);
            this.fillToolBar(viewBean,layoutComponent);
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    @JSONField(serialize = false)
    public LayoutComponent getLayoutComponent(CustomLayoutViewBean layoutViewBean) {
        LayoutComponent layoutComponent = new LayoutComponent(Dock.fill, euModule.getName() + ComponentType.LAYOUT.name());
        LayoutProperties layoutProperties = layoutComponent.getProperties();
        LayoutListItem topItem = new LayoutListItem(PosType.before);
        LayoutListItem mainItem = new LayoutListItem(PosType.main);
        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        for (CustomLayoutItemBean layoutItemBean : itemBeanList) {
            if (layoutItemBean.getPos().equals(PosType.before)) {
                topItem = new LayoutListItem(layoutItemBean);
            } else if (layoutItemBean.getPos().equals(PosType.main)) {
                mainItem = new LayoutListItem(layoutItemBean);
            }
        }

        if (layoutViewBean != null) {
            layoutProperties = new LayoutProperties(layoutViewBean);
            layoutProperties.getItems().clear();
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        } else {
            layoutProperties.setBorderType(BorderType.none);
            layoutProperties.setType(LayoutType.vertical);
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        }
        layoutComponent.setProperties(layoutProperties);
        return layoutComponent;

    }
}
