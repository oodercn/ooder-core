package net.ooder.esd.bean.field;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.StacksFieldAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.view.StacksViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldStacksComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.StacksComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.tool.properties.list.AbsUIListProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomFieldStacksComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.NAVSTACKSCONFIG,
        componentType = ComponentType.STACKS
)
@AnnotationType(clazz = StacksFieldAnnotation.class)
public class CustomStacksFieldBean extends BaseWidgetBean<StacksViewBean, StacksComponent> {

    public CustomStacksFieldBean() {

    }

    public CustomStacksFieldBean(StacksComponent component) {
        update(null, component);
    }

    public CustomStacksFieldBean(ModuleComponent parentModuleComponent, StacksComponent component) {
        update(parentModuleComponent, component);
    }


    @Override
    public void update(ModuleComponent parentModuleComponent, StacksComponent component) {
        this.initWidget(parentModuleComponent,component);
        this.initProperties(component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        AbsListProperties listProperties= component.getProperties();
        if (component.getChildren() != null && component.getChildren().size() > 0) {
           super.update(parentModuleComponent, component);
        } else if (listProperties.getItems().size()>0){
            this.viewBean=genViewBean();
        }


    }

    @Override
    public StacksViewBean createViewBean(ModuleComponent currModuleComponent, StacksComponent component) {
        if (viewBean == null) {
            viewBean = new StacksViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public CustomStacksFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(StacksFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof StacksFieldAnnotation) {
                fillData((StacksFieldAnnotation) annotation);
            }
        }
    }


    public CustomStacksFieldBean(StacksFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomStacksFieldBean fillData(StacksFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.STACKS;
    }

}
