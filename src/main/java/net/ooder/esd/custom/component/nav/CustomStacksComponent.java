package net.ooder.esd.custom.component.nav;


import net.ooder.common.JDSException;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.StacksViewBean;
import net.ooder.esd.custom.properties.StacksProperties;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.StacksComponent;

import java.util.List;
import java.util.Map;

public class CustomStacksComponent extends StacksComponent {
    public CustomStacksComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException {
        super();

        StacksViewBean tabsViewBean = (StacksViewBean) methodConfig.getView();
        this.setProperties(new StacksProperties(tabsViewBean, valueMap));

        List<FieldModuleConfig> moduleConfigList = methodConfig.getView().getNavItems();
        for (FieldModuleConfig moduleInfo : moduleConfigList) {
            ModuleComponent moduleComponent = new ModuleComponent();
            EUModule childModule = moduleInfo.getModule(valueMap);
            moduleComponent.setClassName(childModule.getClassName());
            moduleComponent.setAlias(childModule.getComponent().getAlias());
            moduleComponent.setTarget(childModule.getComponent().getTarget());
            this.addChildren(moduleComponent);
        }
        this.setAlias(methodConfig.getName() );
    }
}
