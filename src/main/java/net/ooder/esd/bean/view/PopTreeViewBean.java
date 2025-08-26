package net.ooder.esd.bean.view;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.PopTreeAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Set;

@AnnotationType(clazz = PopTreeAnnotation.class)
public class PopTreeViewBean extends CustomTreeViewBean {
    ModuleViewType moduleViewType = ModuleViewType.POPTREECONFIG;

    public PopTreeViewBean() {
        super();
    }


    public PopTreeViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TREEVIEW;
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        return classSet;
    }

    public PopTreeViewBean fillData(PopTreeAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

}