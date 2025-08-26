package net.ooder.esd.bean.view;

import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.TreeAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.tool.properties.TreeViewProperties;
import net.ooder.annotation.AnnotationType;

@AnnotationType(clazz = TreeAnnotation.class)
public class CustomMTreeViewBean extends CustomTreeViewBean {
    ModuleViewType moduleViewType = ModuleViewType.MTREECONFIG;

    public CustomMTreeViewBean() {

    }

    public CustomMTreeViewBean(Class clazz, CustomMTreeViewBean parentItem) {
        super(clazz, parentItem);
    }

    public CustomMTreeViewBean(MethodConfig methodConfig, CustomMTreeViewBean parentItem) {
        super(methodConfig, parentItem);
    }

    public CustomMTreeViewBean(TreeItem treeItem, Class childClazz, CustomMTreeViewBean parentItem) {
        super(treeItem, childClazz, parentItem);
    }

    public CustomMTreeViewBean(TreeViewProperties properties) {
        super(properties);
    }


    public CustomMTreeViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    @Override
    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }
}
