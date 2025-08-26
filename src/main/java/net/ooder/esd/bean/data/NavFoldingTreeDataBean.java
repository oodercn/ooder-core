package net.ooder.esd.bean.data;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.NavFoldingTreeViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.nav.FullNavFoldingTreeComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullNavFoldingTreeComponent.class,
        viewType = CustomViewType.NAV,
        moduleType = ModuleViewType.NAVFOLDINGTREECONFIG
)
@AnnotationType(clazz = NavFoldingTreeViewAnnotation.class)
public class NavFoldingTreeDataBean extends TreeDataBaseBean {
    ModuleViewType moduleViewType = ModuleViewType.NAVFOLDINGTREECONFIG;

    public NavFoldingTreeDataBean() {
    }

    public NavFoldingTreeDataBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);

    }

    public NavFoldingTreeDataBean(NavFoldingTreeViewAnnotation annotation) {
        fillData(annotation);
    }

    public NavFoldingTreeDataBean fillData(NavFoldingTreeViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

}
