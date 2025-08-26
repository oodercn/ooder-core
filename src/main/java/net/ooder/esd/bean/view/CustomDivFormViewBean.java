package net.ooder.esd.bean.view;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.DivFormAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.ArrayList;
import java.util.List;

@AnnotationType(clazz = DivFormAnnotation.class)
public class CustomDivFormViewBean extends BaseFormViewBean {
    ModuleViewType moduleViewType = ModuleViewType.DIVCONFIG;


    public CustomDivFormViewBean() {
    }

    public CustomDivFormViewBean(ModuleComponent<DivComponent> moduleComponent) {
        this.updateModule(moduleComponent);
    }


    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(DivFormAnnotation.class, this);
        super.updateBaseModule(moduleComponent);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        initMenuBar();
        DivComponent currComponent = (DivComponent) moduleComponent.getCurrComponent();
        this.name = OODUtil.formatJavaName(currComponent.getAlias(), false);
        if (moduleBean != null) {
            moduleBean.updateComponent(currComponent);
        }
        if (containerBean == null) {
            containerBean = new ContainerBean(currComponent);
        } else {
            containerBean.update(currComponent);
        }
        List<Component> components = this.cloneComponentList(currComponent.getChildren());
        List<FieldFormConfig> fieldFormConfigs = genChildComponent(moduleComponent, components);
        for (FieldFormConfig fieldFormConfig : fieldFormConfigs) {
            this.fieldConfigMap.put(fieldFormConfig.getFieldname(), fieldFormConfig);
            javaSrcBeans.addAll(fieldFormConfig.getAllJavaSrcBeans());
        }
        this.addChildJavaSrc(javaSrcBeans);
        try {
            DSMFactory.getInstance().saveCustomViewEntity(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaSrcBeans;
    }


    public CustomDivFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        if (methodAPIBean.getMethod() != null) {
            Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
            DivFormAnnotation paperAnnotation = AnnotationUtil.getClassAnnotation(clazz, DivFormAnnotation.class);
            if (paperAnnotation != null) {
                AnnotationUtil.fillBean(paperAnnotation, this);
            }
        }
        try {
            this.initHiddenField(this.getViewClassName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.DIV;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }


}
