package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.SpanAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.component.form.field.CustomSpanComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.SpanComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.SpanProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomSpanComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SPAN
)
@AnnotationType(clazz = SpanAnnotation.class)
public class SpanFieldBean extends FieldBaseBean<SpanComponent> {

    String id;

    Boolean selectable;

    String width;

    String height;

    Boolean selectabl;

    String html;

    OverflowType overflow;

    Integer tabindex;

    String xpath;

    public SpanFieldBean(SpanComponent spanComponent) {
        this.xpath = spanComponent.getPath();
        update(spanComponent.getProperties());
    }


    void update(SpanProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public SpanFieldBean(Set<Annotation> annotations) {

        AnnotationUtil.fillDefaultValue(SpanAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SpanAnnotation) {
                fillData((SpanAnnotation) annotation);
            }
        }
    }


    public SpanFieldBean() {

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public Boolean getSelectable() {
        return selectable;
    }


    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, SpanComponent component) {
        return new ArrayList<>();
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
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

    public Boolean getSelectabl() {
        return selectabl;
    }

    public void setSelectabl(Boolean selectabl) {
        this.selectabl = selectabl;
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

    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }


    public SpanFieldBean(SpanAnnotation annotation) {
        fillData(annotation);
    }

    public SpanFieldBean fillData(SpanAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.SPAN;
    }
}
