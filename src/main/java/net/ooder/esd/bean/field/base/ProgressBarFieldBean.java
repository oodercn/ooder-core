package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ProgressBarAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.component.form.field.CustomProgressBarComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.HTMLButtonComponent;
import net.ooder.esd.tool.component.ProgressBarComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.tool.properties.form.HTMLButtonProperties;
import net.ooder.esd.tool.properties.form.ProgressBarProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomProgressBarComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.PROGRESSBAR
)
@AnnotationType(clazz = ProgressBarAnnotation.class)
public class ProgressBarFieldBean extends FieldBaseBean<ProgressBarComponent> {


    Integer value;

    String width;

    String height;

    String captionTpl;

    String fillBG;

    LayoutType layoutType;


    public ProgressBarFieldBean(ModuleComponent moduleComponent, ProgressBarComponent component) {
        super(moduleComponent, component);
    }

    public void updateProperties(ProgressBarProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    @Override
    public void update(ModuleComponent moduleComponent, ProgressBarComponent component) {
        updateProperties(component.getProperties());
        super.update(moduleComponent, component);
    }

    public ProgressBarFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(ProgressBarAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ProgressBarAnnotation) {
                fillData((ProgressBarAnnotation) annotation);
            }
        }

    }

    public ProgressBarFieldBean() {

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();

        annotationBeans.add(this);
        return annotationBeans;
    }
    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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

    public String getCaptionTpl() {
        return captionTpl;
    }

    public void setCaptionTpl(String captionTpl) {
        this.captionTpl = captionTpl;
    }

    public String getFillBG() {
        return fillBG;
    }

    public void setFillBG(String fillBG) {
        this.fillBG = fillBG;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }


    public ProgressBarFieldBean(ProgressBarAnnotation annotation) {
        fillData(annotation);
    }

    public ProgressBarFieldBean fillData(ProgressBarAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PROGRESSBAR;
    }
}
