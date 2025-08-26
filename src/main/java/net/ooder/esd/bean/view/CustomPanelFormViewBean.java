package net.ooder.esd.bean.view;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.PanelFormAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.BaseFormViewBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.PanelComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = PanelFormAnnotation.class)
public class CustomPanelFormViewBean extends BaseFormViewBean {


    ModuleViewType moduleViewType = ModuleViewType.PANELCONFIG;


    public CustomPanelFormViewBean() {

    }


    public CustomPanelFormViewBean(ModuleComponent moduleComponent) {
        super();
        this.updateModule(moduleComponent);
    }


    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(PanelFormAnnotation.class, this);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        initMenuBar();
        PanelComponent currComponent = (PanelComponent) moduleComponent.getCurrComponent();
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
        addChildJavaSrc(javaSrcBeans);
        try {
            DSMFactory.getInstance().saveCustomViewEntity(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return javaSrcBeans;


    }

    public CustomPanelFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(methodAPIBean.getMethod(),true);
        PanelFormAnnotation paperAnnotation = AnnotationUtil.getClassAnnotation(clazz, PanelFormAnnotation.class);
        if (paperAnnotation != null) {
            AnnotationUtil.fillBean(paperAnnotation, this);
        }

        try {
            this.initHiddenField(this.getViewClassName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.PANEL;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }


    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
