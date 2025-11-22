package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.SVGPaperFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.view.CustomSVGPaperViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.form.field.CustomFieldSVGPaperComponent;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@CustomClass(clazz = CustomFieldSVGPaperComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.SVGPAPERCONFIG,
        componentType = ComponentType.SVGPAPER
)
@AnnotationType(clazz = SVGPaperFieldAnnotation.class)
public class CustomSVGPaperFieldBean extends BaseWidgetBean<CustomSVGPaperViewBean, SVGPaperComponent> {


    String iframeAutoLoad;

    Integer graphicZIndex;

    Boolean selectable;

    String html;


    String width;

    String height;

    OverflowType overflow;

    Boolean scaleChildren;



    public CustomSVGPaperFieldBean(SVGPaperComponent component) {
        update(null, component);
    }


    public CustomSVGPaperFieldBean(ModuleComponent parentModuleComponent, SVGPaperComponent component) {
        update(parentModuleComponent, component);
    }


    @Override
    public CustomSVGPaperViewBean createViewBean(ModuleComponent currModuleComponent, SVGPaperComponent component) {
        if (viewBean == null) {
            viewBean = new CustomSVGPaperViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }

    public CustomSVGPaperFieldBean() {

    }

    public CustomSVGPaperFieldBean(SVGPaperProperties properties) {
        initProperties(properties);
    }


    public CustomSVGPaperFieldBean(Set<Annotation> annotations) {
        init(annotations);
    }

    public CustomSVGPaperFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomSVGPaperViewBean) methodConfig.getView();
        init(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(),true));
    }

    void init(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGPaperFieldAnnotation) {
                fillData((SVGPaperFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }

    }


    void initProperties(SVGPaperProperties properties) {
        widgetBean = new CustomWidgetBean(properties);
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(properties), Map.class), this, false, false);
    }

    public CustomSVGPaperFieldBean(SVGPaperFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomSVGPaperFieldBean fillData(SVGPaperFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGPAPER;
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


    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public Boolean getScaleChildren() {
        return scaleChildren;
    }

    public void setScaleChildren(Boolean scaleChildren) {
        this.scaleChildren = scaleChildren;
    }

    public String getIframeAutoLoad() {
        return iframeAutoLoad;
    }

    public void setIframeAutoLoad(String iframeAutoLoad) {
        this.iframeAutoLoad = iframeAutoLoad;
    }

    public Integer getGraphicZIndex() {
        return graphicZIndex;
    }

    public void setGraphicZIndex(Integer graphicZIndex) {
        this.graphicZIndex = graphicZIndex;
    }


}
