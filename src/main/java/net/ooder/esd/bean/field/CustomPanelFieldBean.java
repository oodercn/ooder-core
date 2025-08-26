package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.PanelFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomPanelFormViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldPanelComponent;
import net.ooder.esd.tool.component.PanelComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@CustomClass(clazz = CustomFieldPanelComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.PANELCONFIG,
        componentType = ComponentType.PANEL
)
@AnnotationType(clazz = PanelFieldAnnotation.class)
public class CustomPanelFieldBean extends BaseWidgetBean<CustomPanelFormViewBean, PanelComponent<PanelProperties>> {

    BorderType borderType;
    Boolean resizer;
    Map<String, Object> resizerProp;
    String sideBarCaption;
    String sideBarType;
    SideBarStatusType sideBarStatus;
    String sideBarSize;
    String background;
    Dock dock;

    public CustomPanelFieldBean() {

    }

    public CustomPanelFieldBean(PanelProperties bean) {
        initProperties(bean);
    }

    public CustomPanelFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomPanelFormViewBean) methodConfig.getView();
        init(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(),true));
    }


    public CustomPanelFieldBean(ModuleComponent parentModuleComponent, PanelComponent component) {
        this.update(parentModuleComponent, component);
    }

    public CustomPanelFieldBean(PanelComponent component) {
        this.update(null, component);
    }

    public CustomPanelFieldBean fillData(PanelFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    void init(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof PanelFieldAnnotation) {
                fillData((PanelFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }

    }

    @Override
    public CustomPanelFormViewBean createViewBean(ModuleComponent currModuleComponent, PanelComponent<PanelProperties> component) {
        if (viewBean == null) {
            viewBean = new CustomPanelFormViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }

    public CustomPanelFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(PanelFieldAnnotation.class, this);
        init(annotations);

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        if (viewBean != null) {
            return viewBean.getOtherClass();
        }
        return new HashSet<>();
    }

    void initProperties(PanelProperties properties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(properties), Map.class), this, false, false);
        widgetBean = new CustomWidgetBean(properties);

    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PANEL;
    }



    public CustomPanelFormViewBean getViewBean() {
        return viewBean;
    }



    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getResizer() {
        return resizer;
    }

    public void setResizer(Boolean resizer) {
        this.resizer = resizer;
    }

    public Map<String, Object> getResizerProp() {
        return resizerProp;
    }

    public void setResizerProp(Map<String, Object> resizerProp) {
        this.resizerProp = resizerProp;
    }

    public String getSideBarCaption() {
        return sideBarCaption;
    }

    public void setSideBarCaption(String sideBarCaption) {
        this.sideBarCaption = sideBarCaption;
    }

    public String getSideBarType() {
        return sideBarType;
    }

    public void setSideBarType(String sideBarType) {
        this.sideBarType = sideBarType;
    }

    public SideBarStatusType getSideBarStatus() {
        return sideBarStatus;
    }

    public void setSideBarStatus(SideBarStatusType sideBarStatus) {
        this.sideBarStatus = sideBarStatus;
    }

    public String getSideBarSize() {
        return sideBarSize;
    }

    public void setSideBarSize(String sideBarSize) {
        this.sideBarSize = sideBarSize;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
