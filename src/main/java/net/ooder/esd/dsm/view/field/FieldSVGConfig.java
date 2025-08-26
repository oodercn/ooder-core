package net.ooder.esd.dsm.view.field;

import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.bean.WidgetBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.annotation.AnnotationType;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldSVGConfig<M extends WidgetBean, N extends ComboBoxBean> extends FieldFormConfig<M, N> {

    public FieldSVGConfig() {

    }

    public FieldSVGConfig(ModuleComponent parentModuleComponent, Component component) {
        super.update(parentModuleComponent, component);
    }


    public FieldSVGConfig(ESDField info, String sourceClassName, String sourceMethodName) {
        super(info, sourceClassName, sourceMethodName);

    }

    public FieldSVGConfig(FieldAggConfig aggConfig, String sourceClassName, String sourceMethodName) {
        super(aggConfig, sourceClassName, sourceMethodName);
    }


    public String toAnnotationStr() {
        return super.toAnnotationStr();
    }


}
