package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.Input;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = Input.class)
public class InputBean<T extends Properties> implements CustomBean {

    Boolean selectable;

    Boolean selectOnFocus;

    String valueFormat;

    String value;

    HAlignType hAlign;


    public InputBean() {

    }
    public InputBean(Component component) {
         this.init((T) component.getProperties());
    }

    public InputBean(T properties) {
        this.init((T) properties);
    }
    private void init(T properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public InputBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(Input.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Input) {
                fillData((Input) annotation);
            }
        }
    }

    public InputBean(Input annotation) {
        fillData(annotation);
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }

        return annotationBeans;
    }


    public Boolean getSelectOnFocus() {
        return selectOnFocus;
    }

    public void setSelectOnFocus(Boolean selectOnFocus) {
        this.selectOnFocus = selectOnFocus;
    }


    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }


    public String getValueFormat() {
        return valueFormat;
    }

    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }


    public InputBean fillData(Input annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
