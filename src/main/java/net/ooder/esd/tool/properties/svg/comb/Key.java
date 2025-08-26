package net.ooder.esd.tool.properties.svg.comb;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.svg.SVGKeyAnnotation;
import net.ooder.esd.bean.svg.SVGBean;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = SVGKeyAnnotation.class)
public class Key implements CustomBean {
    public Integer x;
    public Integer y;
    public String id;
    public String text;
    public String fill;
    public String stroke;
    public String title;

    public Key() {

    }
    public Key(SVGAttr properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public Key(SVGBean svgBean) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(svgBean), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public Key(SVGProperties properties) {

        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);

        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public Key(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGKeyAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGKeyAnnotation) {
                fillData((SVGKeyAnnotation) annotation);
            }
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Key clone() {
        Key svgBean = new Key();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), svgBean, false, false);
        return svgBean;
    }


    public Key(SVGKeyAnnotation annotation) {
        fillData(annotation);
    }

    public Key fillData(SVGKeyAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
