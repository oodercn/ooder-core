package net.ooder.esd.bean.view;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.event.CustomMFormEvent;
import net.ooder.esd.annotation.MFormAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.MFormLayoutComponent;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = MFormAnnotation.class)
public class CustomMFormViewBean extends CustomFormViewBean {

    ModuleViewType moduleViewType = ModuleViewType.MFORMCONFIG;

    Set<CustomMFormEvent> mevent = new LinkedHashSet<>();

    public CustomMFormViewBean() {
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    public CustomMFormViewBean(ModuleComponent<MFormLayoutComponent> moduleComponent) {
        AnnotationUtil.fillDefaultValue(MFormAnnotation.class, this);
        MFormLayoutComponent blockComponent = moduleComponent.getCurrComponent();
        String realPath=blockComponent.getPath();
        DSMProperties dsmProperties=moduleComponent.getProperties().getDsmProperties();
        if (dsmProperties!=null &&dsmProperties.getRealPath()!=null){
            realPath = dsmProperties.getRealPath();
        }
        this.setXpath(realPath);
        FormLayoutProperties formLayoutProperties = blockComponent.getProperties();
        this.init(formLayoutProperties);
        List<Component> components = blockComponent.getChildren();
        for (Component component : components) {
            FieldFormConfig config = new FieldFormConfig();
            config.update(moduleComponent, component);
            fieldConfigMap.put(config.getFieldname(), config);
            fieldNames.add(config.getFieldname());
        }
    }

    public CustomMFormViewBean(FormLayoutProperties formLayoutProperties) {
        init(formLayoutProperties);
    }

    public Set<CustomMFormEvent> getMevent() {
        return mevent;
    }

    public void setMevent(Set<CustomMFormEvent> mevent) {
        this.mevent = mevent;
    }

    public CustomMFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        MFormAnnotation mformAnnotation = AnnotationUtil.getClassAnnotation(clazz, MFormAnnotation.class);
        if (mformAnnotation == null) {
            AnnotationUtil.fillDefaultValue(MFormAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(mformAnnotation, this);
        }
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
