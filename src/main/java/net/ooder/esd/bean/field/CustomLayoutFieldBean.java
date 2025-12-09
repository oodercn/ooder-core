package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.field.FormFieldAnnotation;
import net.ooder.esd.annotation.field.LayoutFieldAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.view.*;
import net.ooder.esd.custom.component.form.field.CustomFieldFormComponent;
import net.ooder.esd.custom.component.form.field.CustomFieldLayoutComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomFieldLayoutComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.LAYOUTCONFIG,
        componentType = ComponentType.LAYOUT
)
@AnnotationType(clazz = LayoutFieldAnnotation.class)
public class CustomLayoutFieldBean extends BaseWidgetBean<CustomViewBean, LayoutComponent> {


    public CustomLayoutFieldBean() {

    }


    public CustomLayoutFieldBean(ModuleComponent parentModuleComponent, LayoutComponent component) {
        update(parentModuleComponent, component);
    }



    @Override
    public void update(ModuleComponent parentModuleComponent, LayoutComponent component) {
        this.initWidget(parentModuleComponent,component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
//        if (component.getChildren() != null && component.getChildren().size() > 0) {
//            javaSrcBeans.addAll(super.update(parentModuleComponent, component));
//        }
//        return javaSrcBeans;

    }

    @Override
    public CustomViewBean createViewBean(ModuleComponent currModuleComponent, LayoutComponent component) {
        List<LayoutListItem> layoutItemBeans = component.getProperties().getItems();
        List<CustomLayoutItemBean> itemBeans = new ArrayList<>();
        for (LayoutListItem tabListItem : layoutItemBeans) {
            CustomLayoutItemBean itemBean = new CustomLayoutItemBean(tabListItem);
            itemBeans.add(itemBean);
        }


        if (viewBean == null) {
            ComponentList components = component.getChildren();
            if (components!=null){
                for (Component childComponent : components) {
                    if (childComponent.getTarget() != null && childComponent.getTarget().endsWith(PosType.before.name())) {
                        ComponentType navType = ComponentType.fromType(childComponent.getKey());
                        switch (navType) {
                            case TREEVIEW:
                                viewBean = new NavTreeComboViewBean(currModuleComponent);
                                break;
                            case MENUBAR:
                                viewBean = new NavMenuBarViewBean(currModuleComponent);
                                break;
                            case GALLERY:
                                viewBean = new NavGalleryComboViewBean(currModuleComponent);
                                break;
                            case BUTTONLAYOUT:
                                viewBean = new NavButtonLayoutComboViewBean(currModuleComponent);
                                break;
                            case FOLDINGTABS:
                                viewBean = new NavFoldingTabsViewBean(currModuleComponent);
                                break;
                            default:
                                viewBean = new CustomLayoutViewBean(currModuleComponent);
                        }
                    }
                }
            }

            if (viewBean == null) {
                viewBean = new CustomLayoutViewBean(currModuleComponent);
            }

            viewBean.setTabItems(layoutItemBeans);
        } else {
            if (viewBean instanceof CustomLayoutViewBean) {
                ((CustomLayoutViewBean) viewBean).setLayoutItems(itemBeans);
            }

            viewBean.setTabItems(layoutItemBeans);
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }


    public ModuleViewType guessComponentType(LayoutComponent currComponent) {
        ModuleViewType moduleViewType = ModuleViewType.LAYOUTCONFIG;
        ComponentList components = currComponent.getChildren();
        for (Component childComponent : components) {
            if (childComponent.getTarget() != null && childComponent.getTarget().endsWith(PosType.before.name())) {
                ComponentType navType = ComponentType.fromType(childComponent.getKey());
                switch (navType) {
                    case TREEVIEW:
                        moduleViewType = ModuleViewType.NAVTREECONFIG;
                        break;
                    case MENUBAR:
                        moduleViewType = ModuleViewType.NAVMENUBARCONFIG;
                        break;
                    case GALLERY:
                        moduleViewType = ModuleViewType.NAVGALLERYCONFIG;
                        break;
                    case BUTTONLAYOUT:
                        moduleViewType = ModuleViewType.NAVBUTTONLAYOUTCONFIG;
                        break;
                    case FOLDINGTABS:
                        moduleViewType = ModuleViewType.NAVFOLDINGTREECONFIG;
                        break;
                }
            }

        }

        return moduleViewType;
    }


    public CustomLayoutFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(LayoutFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof LayoutFieldAnnotation) {
                fillData((LayoutFieldAnnotation) annotation);
            }
        }
    }


    public CustomLayoutFieldBean(LayoutFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomLayoutFieldBean fillData(LayoutFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (viewBean != null) {
            classSet.addAll(viewBean.getOtherClass());
        }
        return classSet;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.LAYOUT;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    @CustomClass(clazz = CustomFieldFormComponent.class,
            viewType = CustomViewType.COMPONENT,
            moduleType = ModuleViewType.FORMCONFIG,
            componentType = ComponentType.FORMLAYOUT
    )
    @AnnotationType(clazz = FormFieldAnnotation.class)
    public static class CustomFormLayoutFieldBean extends BaseWidgetBean<CustomFormViewBean, FormLayoutComponent> {

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
}
