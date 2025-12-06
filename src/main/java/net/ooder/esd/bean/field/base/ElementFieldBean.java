package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ElementAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.CustomElementComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ElementComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ElementProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomElementComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.ELEMENT
)
@AnnotationType(clazz = ElementAnnotation.class)
public class ElementFieldBean implements FieldComponentBean<ElementComponent> {

    public String nodeName;

    public String html;

    public String className;

    public String attributes;

    public String width;

    String xpath;

    public String height;

    public Boolean selectable;

    public Integer tabindex;

    public ElementFieldBean(ElementComponent elementComponent) {
        this.xpath = elementComponent.getPath();
        this.update(elementComponent.getProperties());

    }

    protected void update(ElementProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, ElementComponent component) {

    }

    public ElementFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ElementAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ElementAnnotation) {
                fillData((ElementAnnotation) annotation);
            }
        }
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
    }

    public ElementFieldBean() {

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
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


    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }


    public Boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }


    public ElementFieldBean(ElementAnnotation annotation) {
        fillData(annotation);
    }

    public ElementFieldBean fillData(ElementAnnotation annotation) {
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
