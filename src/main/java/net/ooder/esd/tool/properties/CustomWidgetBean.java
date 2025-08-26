package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.Widget;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AnnotationType(clazz = Widget.class)
public class CustomWidgetBean<T extends Properties> implements CustomBean {

    String width;
    String height;
    Boolean shadow;


    public CustomWidgetBean() {

    }

    public CustomWidgetBean(T properties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(properties), Map.class), this, false, false);
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
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

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public CustomWidgetBean(Widget annotation) {
        fillData(annotation);
    }

    public CustomWidgetBean fillData(Widget annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
