package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.PopTreeViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.FullPopTreeComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullPopTreeComponent.class,
        viewType = CustomViewType.MODULE,
        moduleType = ModuleViewType.POPTREECONFIG
)
@AnnotationType(clazz = PopTreeViewAnnotation.class)
public class PopTreeDataBean extends TreeDataBaseBean {

    ModuleViewType moduleViewType = ModuleViewType.POPTREECONFIG;

    Class bindClass;

    String caption;

    public PopTreeDataBean() {
    }

    public PopTreeDataBean(MethodConfig methodConfig) {
        super(methodConfig);

    }


    public PopTreeDataBean(PopTreeViewAnnotation annotation) {
        fillData(annotation);
    }

    public PopTreeDataBean fillData(PopTreeViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }
}
