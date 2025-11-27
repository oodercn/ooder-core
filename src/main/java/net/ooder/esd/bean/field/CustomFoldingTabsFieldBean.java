package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.NavFoldingTabsFieldAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldFoldingTabsComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.esd.tool.component.FoldingTabsComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CustomClass(clazz = CustomFieldFoldingTabsComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.NAVFOLDINGTABSCONFIG,
        componentType = ComponentType.FOLDINGTABS
)
@AnnotationType(clazz = NavFoldingTabsFieldAnnotation.class)
public class CustomFoldingTabsFieldBean extends BaseWidgetBean<NavFoldingTabsViewBean, FoldingTabsComponent> {

    public CustomFoldingTabsFieldBean() {

    }

    public CustomFoldingTabsFieldBean(FoldingTabsComponent tabsComponent) {
        update(null, tabsComponent);
    }


    public CustomFoldingTabsFieldBean(ModuleComponent parentModuleComponent, FoldingTabsComponent tabsComponent) {
        update(parentModuleComponent, tabsComponent);
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent parentModuleComponent, FoldingTabsComponent component) {
        this.initWidget(parentModuleComponent,component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (component.getChildren() != null && component.getChildren().size() > 0) {
            javaSrcBeans.addAll(super.update(parentModuleComponent, component));
        }else if (component.getProperties().getItems().size()>0){
            this.viewBean=genViewBean();
        }
        return javaSrcBeans;

    }

    @Override
    public NavFoldingTabsViewBean createViewBean(ModuleComponent currModuleComponent, FoldingTabsComponent component) {
        if (viewBean == null) {
            viewBean = new NavFoldingTabsViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }


    void initProperties(TabsComponent component) {
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }

        widgetBean = new CustomWidgetBean(component.getProperties());

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }


    public CustomFoldingTabsFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(NavFoldingTabsFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof NavFoldingTabsFieldAnnotation) {
                fillData((NavFoldingTabsFieldAnnotation) annotation);
            }
        }
    }


    public CustomFoldingTabsFieldBean(NavFoldingTabsFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomFoldingTabsFieldBean fillData(NavFoldingTabsFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.FOLDINGTABS;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }
}
