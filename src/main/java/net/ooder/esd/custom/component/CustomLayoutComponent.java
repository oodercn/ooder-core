package net.ooder.esd.custom.component;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.RequestPathAnnotation;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.properties.CustomLayoutProperties;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.List;
import java.util.Map;

public class CustomLayoutComponent extends LayoutComponent {
    public CustomLayoutComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super();
        this.setProperties(new CustomLayoutProperties(methodConfig));
        List<FieldModuleConfig> fieldModuleConfigs = methodConfig.getView().getNavItems();
        for (FieldModuleConfig moduleInfo : fieldModuleConfigs) {
            ModuleComponent moduleComponent = null;
            try {
                moduleComponent = moduleInfo.getModule(valueMap).getComponent();
                Component topComponent = moduleComponent.getLastBoxComponent();
                List<APICallerComponent> components = moduleComponent.findComponents(ComponentType.APICALLER.name(), null);
                for (APICallerComponent apicomponent : components) {
                    apicomponent.setAlias(moduleComponent.getAlias() + "." + moduleComponent.getAlias());
                    module.getComponent().addChildren(apicomponent);
                    apicomponent.getProperties().addRequestData(new UrlPathData((RequestPathAnnotation) RequestPathEnum.CTX));
                }

                if (topComponent == null) {
                    topComponent = moduleComponent.getTopComponentBox();
                }
                topComponent.setTarget(moduleComponent.getTarget());
                this.addChildren(topComponent);
            } catch (JDSException e) {
                e.printStackTrace();
            }

        }
        this.setAlias(methodConfig.getName() );
    }
}
