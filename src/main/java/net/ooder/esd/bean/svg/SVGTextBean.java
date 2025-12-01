package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGAttrAnnotation;
import net.ooder.esd.annotation.svg.SVGTextAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CursorType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGTextComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGTextComponent;
import net.ooder.esd.tool.properties.svg.text.SVGTextProperties;
import net.ooder.esd.tool.properties.svg.text.TextAttr;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;
import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomSVGTextComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGTEXT)

@AnnotationType(clazz = SVGTextAnnotation.class)
public class SVGTextBean implements FieldComponentBean<SVGTextComponent> {

    SVGAttrBean attr;

    SVGBean svgBean;

    String xpath;

    String path;

    String text;

    String fontSize;

    String fill;

    String fontWeight;

    String fontFamily;

    String fontStyle;

    Integer strokeWidth;

    String stroke;

    String title;

    CursorType cursor;


    public SVGTextBean() {

    }

    public SVGTextBean(SVGTextComponent svgTextComponent) {
        xpath = svgTextComponent.getPath();
        svgBean = new SVGBean(svgTextComponent.getProperties());
        this.init(svgTextComponent.getProperties());

    }


    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    void init(SVGTextProperties properties) {
        TextAttr attr = properties.getAttr();
        if (properties.getAttr() != null) {
            this.fontSize = attr.getFontSize();
            this.fill = attr.getFill();
            this.fontWeight = attr.getFontWeight();
            this.fontFamily = attr.getFontFamily();
            this.fontStyle = attr.getFontStyle();
            this.strokeWidth = attr.getStrokeWidth();
            this.stroke = attr.getStroke();
            this.title = attr.getTitle();
            this.cursor = attr.getCursor();

        }
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, SVGTextComponent component) {
        this.init(component.getProperties());
        return new ArrayList<>();
    }

    public SVGTextBean(SVGTextProperties properties) {
        svgBean = new SVGBean(properties);
        this.init(properties);
    }

    public SVGTextBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGTextAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGTextAnnotation) {
                fillData((SVGTextAnnotation) annotation);
            }
            if (annotation instanceof SVGAttrAnnotation) {
                AnnotationUtil.fillBean(annotation, this);
            }
        }
        attr = new SVGAttrBean(annotations);
        svgBean = new SVGBean(annotations);

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CursorType getCursor() {
        return cursor;
    }

    public void setCursor(CursorType cursor) {
        this.cursor = cursor;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGTEXT;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans = new ArrayList<>();
        if (svgBean != null && !AnnotationUtil.getAnnotationMap(svgBean).isEmpty()) {
            annotationBeans.add(svgBean);
        }
        if (attr != null && !AnnotationUtil.getAnnotationMap(attr).isEmpty()) {
            annotationBeans.add(attr);
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    public SVGTextBean(SVGTextAnnotation annotation) {
        fillData(annotation);
    }

    public SVGTextBean fillData(SVGTextAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public SVGTextBean clone() {
        SVGTextBean textBean = new SVGTextBean();
        textBean.setFontSize(fontSize);
        textBean.setFill(fill);
        textBean.setFontWeight(fontWeight);
        textBean.setFontFamily(fontFamily);
        textBean.setFontStyle(fontStyle);
        textBean.setStrokeWidth(strokeWidth);
        textBean.setStroke(stroke);
        textBean.setText(text);
        textBean.setTitle(title);
        textBean.setCursor(cursor);

        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), textBean, false, false);
        if (svgBean != null) {
            textBean.setSvgBean(svgBean.clone());
        }
        if (attr != null) {
            textBean.setAttr(attr.clone());
        }

        return textBean;
    }

    public SVGAttrBean getAttr() {
        return attr;
    }

    public void setAttr(SVGAttrBean attr) {
        this.attr = attr;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public SVGBean getSvgBean() {
        return svgBean;
    }

    public void setSvgBean(SVGBean svgBean) {
        this.svgBean = svgBean;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    @JSONField(serialize = false)
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
