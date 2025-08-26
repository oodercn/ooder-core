package net.ooder.esd.bean.view;


import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.MButtonViewsViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;

@AnnotationType(clazz = MButtonViewsViewAnnotation.class)
public class MButtonViewsViewBean extends CustomButtonViewsViewBean {

    ModuleViewType moduleViewType = ModuleViewType.MBUTTONVIEWSCONFIG;

    public MButtonViewsViewBean() {
        super();
    }


    public MButtonViewsViewBean(MethodConfig methodAPIBean) {
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
