package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.action.DYNAppendType;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

@AnnotationType(clazz = DynLoadAnnotation.class)
public class CustomDynViewBean extends CustomViewBean<FieldFormConfig, UIItem, Component> {

    ModuleViewType moduleViewType = ModuleViewType.DYNCONFIG;

    String projectName;

    String refClassName;

    String expression;

    String methodName;

    DYNAppendType append;

    String className;

    String domainId;

    Set<CustomFormEvent> event = new LinkedHashSet<>();

    @JSONField(serialize = false)
    Set<ComponentType> bindTypes = new LinkedHashSet<>();


    public CustomDynViewBean() {

    }

    public CustomDynViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        ESDClass esdClass = methodAPIBean.getViewClass();
        if (esdClass != null) {
            DynLoadAnnotation formAnnotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), DynLoadAnnotation.class);
            if (formAnnotation == null) {
                AnnotationUtil.fillDefaultValue(DynLoadAnnotation.class, this);
            } else {
                AnnotationUtil.fillBean(formAnnotation, this);
            }
            if (formAnnotation != null) {
                if (refClassName == null || refClassName.equals("")) {
                    refClassName = formAnnotation.refClassName();
                }
                if (expression == null || expression.equals("")) {
                    expression = formAnnotation.expression();
                }
            }

        }
        this.sourceClassName = methodAPIBean.getSourceClassName();
        this.methodName = methodAPIBean.getMethodName();
        this.domainId = methodAPIBean.getDomainId();


    }

    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent parentModuleComponent) {
        List<Callable<List<JavaGenSource>>> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(parentModuleComponent);
        return javaSrcBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        return classSet;
    }

    @Override
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        annotationBeans.add(this);
        return annotationBeans;
    }

    public CustomDynViewBean fillData(DynLoadAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public List<JavaGenSource> buildAll() {

        return build(childModules);
    }


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }


    public Set<CustomFormEvent> getEvent() {
        return event;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    public String getRefClassName() {
        return refClassName;
    }

    public void setRefClassName(String refClassName) {
        this.refClassName = refClassName;
    }

    public DYNAppendType getAppend() {
        return append;
    }

    public void setAppend(DYNAppendType append) {
        this.append = append;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    @Override
    public List<UIItem> getTabItems() {
        return null;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLOCK;
    }
}
