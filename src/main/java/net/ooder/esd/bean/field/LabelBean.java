package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.Label;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.LabelPos;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.CustomUIBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = Label.class)
public class LabelBean implements CustomBean {

    String labelSize;

    Integer manualWidth;

    String labelGap;

    String labelCaption;

    LabelPos labelPos;

    HAlignType labelHAlign;

    VAlignType labelVAlign;

    CustomUIBean uiBean;

    public LabelBean() {

    }

    public LabelBean(Component component) {
        AnnotationUtil.fillDefaultValue(Label.class, this);
        update(component);
    }


    public LabelBean(Properties properties) {
        init(properties);
    }

    public LabelBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(Label.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Label) {
                fillData((Label) annotation);
            }
            if (annotation instanceof UIAnnotation) {
                uiBean = new CustomUIBean(annotations);
            }
        }
    }

    public void update(Component component) {
        this.init(component.getProperties());
    }

    public void init(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        Object caption = valueMap.get("caption");
        if (caption != null && valueMap.get("labelCaption") == null) {
            valueMap.put("labelCaption", caption);
        }
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public CustomUIBean getUiBean() {
        return uiBean;
    }

    public void setUiBean(CustomUIBean uiBean) {
        this.uiBean = uiBean;
    }

    public Integer getManualWidth() {
        return manualWidth;
    }

    public void setManualWidth(Integer manualWidth) {
        this.manualWidth = manualWidth;
    }

    public String getLabelCaption() {
        return labelCaption;
    }

    public void setLabelCaption(String labelCaption) {
        this.labelCaption = labelCaption;
    }

    public String getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(String labelSize) {
        this.labelSize = labelSize;
    }

    public String getLabelGap() {
        return labelGap;
    }

    public void setLabelGap(String labelGap) {
        this.labelGap = labelGap;
    }

    public LabelPos getLabelPos() {
        return labelPos;
    }

    public void setLabelPos(LabelPos labelPos) {
        this.labelPos = labelPos;
    }

    public HAlignType getLabelHAlign() {
        return labelHAlign;
    }

    public void setLabelHAlign(HAlignType labelHAlign) {
        this.labelHAlign = labelHAlign;
    }

    public VAlignType getLabelVAlign() {
        return labelVAlign;
    }

    public void setLabelVAlign(VAlignType labelVAlign) {
        this.labelVAlign = labelVAlign;
    }

    public LabelBean(Label annotation) {
        fillData(annotation);
    }

    public LabelBean fillData(Label annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
