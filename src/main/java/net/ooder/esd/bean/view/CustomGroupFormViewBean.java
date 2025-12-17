package net.ooder.esd.bean.view;

import net.ooder.annotation.AnnotationType;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.GroupFormAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.GroupComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@AnnotationType(clazz = GroupFormAnnotation.class)
public class CustomGroupFormViewBean extends BaseFormViewBean {


    ModuleViewType moduleViewType = ModuleViewType.GROUPCONFIG;


    public CustomGroupFormViewBean() {

    }


    public CustomGroupFormViewBean(ModuleComponent parentModuleComponent) {
        updateModule(parentModuleComponent);
    }


    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(GroupFormAnnotation.class, this);
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        GroupComponent currComponent = (GroupComponent) moduleComponent.getCurrComponent();
        super.updateBaseModule(moduleComponent);
        initMenuBar();

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

        return tasks;
    }


    public CustomGroupFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        GroupFormAnnotation paperAnnotation = AnnotationUtil.getClassAnnotation(clazz, GroupFormAnnotation.class);
        if (paperAnnotation != null) {
            AnnotationUtil.fillBean(paperAnnotation, this);
        }

    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLOCK;
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }


    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


}
