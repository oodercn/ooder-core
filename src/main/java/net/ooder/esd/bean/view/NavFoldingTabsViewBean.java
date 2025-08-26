package net.ooder.esd.bean.view;


import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.NavFoldingTabsAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFoldingTabsFieldBean;
import net.ooder.esd.custom.properties.NavFoldingTabsListItem;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.FoldingTabsComponent;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;

import java.util.ArrayList;
import java.util.List;

@AnnotationType(clazz = NavFoldingTabsAnnotation.class)
public class NavFoldingTabsViewBean extends TabsViewBean<NavFoldingTabsListItem> {

    CustomFoldingTabsFieldBean foldingTabsFieldBean;
    ModuleViewType moduleViewType = ModuleViewType.NAVFOLDINGTABSCONFIG;

    public NavFoldingTabsViewBean() {
        super();
    }

    public NavFoldingTabsViewBean(ModuleComponent<FoldingTabsComponent> moduleComponent) {
        super(moduleComponent);
    }

    public NavFoldingTabsViewBean(ModuleComponent<FoldingTabsComponent> moduleComponent, CustomFoldingTabsFieldBean stacksFieldBean) {
        FoldingTabsComponent component = moduleComponent.getCurrComponent();
        String realPath = component.getPath();
        DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
        if (dsmProperties != null && dsmProperties.getRealPath() != null) {
            realPath = dsmProperties.getRealPath();
        }
        this.setXpath(realPath);
        NavFoldingTabsProperties tabsProperties = component.getProperties();
        updateModule(moduleComponent);
        this.foldingTabsFieldBean = stacksFieldBean;
        this.init(tabsProperties);
    }

    @Override
    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateModule(moduleComponent);
        if (moduleComponent.getCurrComponent() instanceof FoldingTabsComponent) {
            FoldingTabsComponent component = (FoldingTabsComponent) moduleComponent.getCurrComponent();
            NavFoldingTabsProperties tabsProperties = component.getProperties();
            this.init(tabsProperties);
        }
        return javaSrcBeans;

    }


    public NavFoldingTabsViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

}