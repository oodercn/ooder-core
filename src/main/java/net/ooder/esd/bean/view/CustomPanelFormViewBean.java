package net.ooder.esd.bean.view;

import net.ooder.annotation.AnnotationType;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.PanelFormAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.PanelComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

@AnnotationType(clazz = PanelFormAnnotation.class)
public class CustomPanelFormViewBean extends BaseFormViewBean {


    ModuleViewType moduleViewType = ModuleViewType.PANELCONFIG;


    public CustomPanelFormViewBean() {

    }


    public CustomPanelFormViewBean(ModuleComponent moduleComponent) {
        super();
        this.updateModule(moduleComponent);
    }


    @Override
    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(PanelFormAnnotation.class, this);
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        initMenuBar();
        PanelComponent currComponent = null;
        if (moduleComponent.getCurrComponent() instanceof PanelComponent) {
            currComponent = (PanelComponent) moduleComponent.getCurrComponent();
        } else if (moduleComponent.getMainBoxComponent() instanceof PanelComponent) {
            currComponent = (PanelComponent) moduleComponent.getMainBoxComponent();
        }

        if (currComponent != null) {
            this.name = OODUtil.formatJavaName(currComponent.getAlias(), false);
            if (moduleBean != null) {
                moduleBean.updateComponent(currComponent);
            }
            if (containerBean == null) {
                containerBean = new ContainerBean(currComponent);
            } else {
                containerBean.update(currComponent);
            }
            List<Component> components = cloneComponentList(currComponent.getChildren());

            for (Component component : components) {
                FieldFormConfig fieldFormConfig = findFieldByCom(component);
                this.fieldConfigMap.put(fieldFormConfig.getFieldname(), fieldFormConfig);
            }
            tasks = genChildComponent(moduleComponent, components);
            childModules = tasks;
            try {
                DSMFactory.getInstance().saveCustomViewEntity(this);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }

    public CustomPanelFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(methodAPIBean.getMethod(), true);
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
