package net.ooder.esd.bean.data;

import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.NavTreeViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.nav.FullNavTreeComponent;
import net.ooder.web.util.AnnotationUtil;

@CustomClass(
        clazz = FullNavTreeComponent.class,
        viewType = CustomViewType.NAV,
        moduleType = ModuleViewType.NAVTREECONFIG
)
@AnnotationType(clazz = NavTreeViewAnnotation.class)
public class NavTreeDataBean extends TreeDataBaseBean {

    ModuleViewType moduleViewType = ModuleViewType.NAVTREECONFIG;


    public NavTreeDataBean() {

    }

    public NavTreeDataBean(MethodConfig methodConfig) {
        super(methodConfig);
    }

    public NavTreeDataBean(NavTreeViewAnnotation annotation) {
        fillData(annotation);
    }

    public NavTreeDataBean fillData(NavTreeViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }
}
