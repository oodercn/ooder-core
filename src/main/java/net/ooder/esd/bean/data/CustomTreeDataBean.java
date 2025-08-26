package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.TreeViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.FullTreeComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullTreeComponent.class,
        viewType = CustomViewType.LISTMODULE,
        moduleType = ModuleViewType.TREECONFIG
)
@AnnotationType(clazz = TreeViewAnnotation.class)
public class CustomTreeDataBean extends TreeDataBaseBean {

    ModuleViewType moduleViewType = ModuleViewType.TREECONFIG;

    public CustomTreeDataBean() {
    }

    public CustomTreeDataBean(MethodConfig methodConfig) {
        super(methodConfig);

    }


    public CustomTreeDataBean(TreeViewAnnotation annotation) {
        fillData(annotation);
    }

    public CustomTreeDataBean fillData(TreeViewAnnotation annotation) {
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
