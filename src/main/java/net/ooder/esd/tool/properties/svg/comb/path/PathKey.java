package net.ooder.esd.tool.properties.svg.comb.path;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.svg.PathKeyAnnotation;
import net.ooder.esd.bean.svg.SVGPathBean;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;


@AnnotationType(clazz = PathKeyAnnotation.class)
public class PathKey extends Key {

    String path;

    public PathKey() {
    }

    public PathKey(SVGPathBean pathBean) {
        super(pathBean.getSvgBean());
        this.path = pathBean.getPath();
    }


    public PathKey(PathProperties properties) {
        super(properties);
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties.getAttr()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public PathKey(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(PathKeyAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof PathKeyAnnotation) {
                fillData((PathKeyAnnotation) annotation);
            }
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PathKey(PathKeyAnnotation annotation) {
        fillData(annotation);
    }

    public PathKey fillData(PathKeyAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public PathKey clone() {
        PathKey text = new PathKey();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), text, false, false);
        return text;
    }

}
