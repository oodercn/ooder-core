package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.field.DivFieldAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomDivFormViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldDivComponent;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.DivProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CustomClass(clazz = CustomFieldDivComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.DIVCONFIG,
        componentType = ComponentType.DIV
)
@AnnotationType(clazz = DivFieldAnnotation.class)
public class CustomDivFieldBean extends BaseWidgetBean<CustomDivFormViewBean, DivComponent> {

    public String iframeAutoLoad;
    public String ajaxAutoLoad;
    public String width;
    public String height;
    public String html;
    public OverflowType overflow;

    public CustomDivFieldBean() {

    }

    public CustomDivFieldBean(ModuleComponent parentModuleComponent, DivComponent component) {
        AnnotationUtil.fillDefaultValue(DivFieldAnnotation.class, this);
        updateFieldBean(component);
        if (component.getChildren().size() > 0) {
            this.update(parentModuleComponent, component);
        }

    }

    public CustomDivFieldBean(DivComponent component) {
        AnnotationUtil.fillDefaultValue(DivFieldAnnotation.class, this);
        this.update(null, component);
    }


    public CustomDivFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomDivFormViewBean) methodConfig.getView();
        initAnnotations(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(), true).toArray(new Annotation[]{}));
    }


    @Override
    public CustomDivFormViewBean createViewBean(ModuleComponent currModuleComponent, DivComponent component) {
        if (viewBean == null) {
            viewBean = new CustomDivFormViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        updateFieldBean(component);
        return viewBean;
    }

    private void updateFieldBean(DivComponent divComponent) {
        if (divComponent != null) {
            DivProperties divProperties = divComponent.getProperties();
            this.iframeAutoLoad = divProperties.getIframeAutoLoad();
            this.ajaxAutoLoad = divProperties.getAjaxAutoLoad();
            this.width = divProperties.getWidth();
            this.height = divProperties.getHeight();
            this.html = divProperties.getHtml();
            this.overflow = divProperties.getOverflow();
        }


    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }


    void initProperties(DivProperties properties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(properties), Map.class), this, false, false);
        widgetBean = new CustomWidgetBean(properties);
    }

    public CustomDivFieldBean(DivFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomDivFieldBean fillData(DivFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomDivFieldBean(Set<Annotation> annotations) {
        initAnnotations(annotations.toArray(new Annotation[]{}));
    }

    public CustomDivFieldBean(Annotation[] annotations) {
        initAnnotations(annotations);
    }


    void initAnnotations(Annotation[] annotations) {
        AnnotationUtil.fillDefaultValue(DivFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof DivFieldAnnotation) {
                fillData((DivFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.DIV;
    }


    public String getIframeAutoLoad() {
        return iframeAutoLoad;
    }

    public void setIframeAutoLoad(String iframeAutoLoad) {
        this.iframeAutoLoad = iframeAutoLoad;
    }

    public String getAjaxAutoLoad() {
        return ajaxAutoLoad;
    }

    public void setAjaxAutoLoad(String ajaxAutoLoad) {
        this.ajaxAutoLoad = ajaxAutoLoad;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
