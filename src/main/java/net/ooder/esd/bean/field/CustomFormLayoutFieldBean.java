package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.field.FormFieldAnnotation;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldFormComponent;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@CustomClass(clazz = CustomFieldFormComponent.class,
            viewType = CustomViewType.COMPONENT,
            moduleType = ModuleViewType.FORMCONFIG,
            componentType = ComponentType.FORMLAYOUT
    )
    @AnnotationType(clazz = FormFieldAnnotation.class)
    public  class CustomFormLayoutFieldBean extends BaseWidgetBean<CustomFormViewBean, FormLayoutComponent> {

        String bgimg;
        String imageClass;
        String backgroundColor;
        BorderType borderType;

        public CustomFormLayoutFieldBean() {

        }

        public CustomFormLayoutFieldBean(ModuleComponent parentModuleComponent, FormLayoutComponent component) {
            if (component.getChildren()!=null && component.getChildren().size() > 0) {
                this.update(parentModuleComponent, component);
            }
        }


        public CustomFormLayoutFieldBean(MethodConfig methodConfig, FormLayoutComponent component) {
            if (methodConfig != null) {
                viewBean = (CustomFormViewBean) methodConfig.getView();
            }
            this.update(null, component);
        }


        public CustomFormLayoutFieldBean(MethodConfig methodConfig) {
            viewBean = (CustomFormViewBean) methodConfig.getView();
            init(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(), true));
        }

        public CustomFormLayoutFieldBean(Set<Annotation> annotations) {
            AnnotationUtil.fillDefaultValue(FormFieldAnnotation.class, this);
            init(annotations);

        }

        public CustomFormLayoutFieldBean(FormLayoutProperties bean) {
            initProperties(bean);
        }


        @Override
        public CustomFormViewBean createViewBean(ModuleComponent currModuleComponent, FormLayoutComponent component) {
            if (viewBean == null) {
                viewBean = new CustomFormViewBean(currModuleComponent);
            } else {
                viewBean.updateModule(currModuleComponent);
            }
            viewBean.updateContainerBean(component);
            return viewBean;
        }

        public void update(ModuleComponent parentModuleComponent, FormLayoutComponent component) {
            this.initWidget(parentModuleComponent,component);
             super.update(parentModuleComponent, component);
            if (containerBean == null) {
                containerBean = new ContainerBean(component);
            } else {
                containerBean.update(component);
            }
            FormLayoutProperties formLayoutProperties = component.getProperties();
            this.bgimg = formLayoutProperties.getPanelBgImg();
            this.imageClass = formLayoutProperties.getImageClass();
            this.backgroundColor = formLayoutProperties.getPanelBgClr();
            this.borderType = formLayoutProperties.getBorderType();
        }


        public CustomFormLayoutFieldBean fillData(FormFieldAnnotation annotation) {
            return AnnotationUtil.fillBean(annotation, this);
        }


        public String getBgimg() {
            return bgimg;
        }

        public void setBgimg(String bgimg) {
            this.bgimg = bgimg;
        }

        public String getImageClass() {
            return imageClass;
        }

        public void setImageClass(String imageClass) {
            this.imageClass = imageClass;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        void init(Set<Annotation> annotations) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof FormFieldAnnotation) {
                    fillData((FormFieldAnnotation) annotation);
                }
                if (annotation instanceof Widget) {
                    widgetBean = new CustomWidgetBean((Widget) annotation);
                }
            }
            containerBean = new ContainerBean(annotations);
        }


        void initProperties(FormLayoutProperties properties) {
            widgetBean = new CustomWidgetBean(properties);
            this.bgimg = properties.getPanelBgImg();
            this.borderType = properties.getBorderType();
            this.imageClass = properties.getImageClass();
            this.backgroundColor = properties.getPanelBgClr();

        }

        @Override
        public ComponentType getComponentType() {
            return ComponentType.FORMLAYOUT;
        }


        @Override
        @JSONField(serialize = false)
        public Set<Class> getOtherClass() {
            Set<Class> classes = new HashSet<>();
            if (viewBean != null) {
                classes.addAll(viewBean.getOtherClass());
            }
            return classes;
        }


        public CustomFormViewBean getViewBean() {
            return viewBean;
        }

        public void setViewBean(CustomFormViewBean viewBean) {
            this.viewBean = viewBean;
        }


        public ContainerBean getContainerBean() {
            return containerBean;
        }

        public void setContainerBean(ContainerBean containerBean) {
            this.containerBean = containerBean;
        }


        public BorderType getBorderType() {
            return borderType;
        }

        public void setBorderType(BorderType borderType) {
            this.borderType = borderType;
        }

        public String toAnnotationStr() {
            return AnnotationUtil.toAnnotationStr(this);
        }

    }