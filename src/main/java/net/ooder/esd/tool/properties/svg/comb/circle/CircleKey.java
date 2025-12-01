package net.ooder.esd.tool.properties.svg.comb.circle;


import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.svg.CircleKeyAnnotation;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = CircleKeyAnnotation.class)
public class CircleKey extends Key {
    public String cx = "0";
    public String cy = "0";
    public String r = "25";


    public CircleKey(SVGProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public CircleKey() {
    }

    public CircleKey(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(CircleKeyAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof CircleKeyAnnotation) {
                fillData((CircleKeyAnnotation) annotation);
            }
        }

    }

    public CircleKey(CircleKeyAnnotation annotation) {
        fillData(annotation);
    }

    public CircleKey fillData(CircleKeyAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getCy() {
        return cy;
    }

    public void setCy(String cy) {
        this.cy = cy;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public CircleKey clone() {
        CircleKey text = new CircleKey();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), text, false, false);
        return text;
    }

}
