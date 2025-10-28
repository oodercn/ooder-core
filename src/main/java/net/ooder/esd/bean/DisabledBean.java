package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.Disabled;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = Disabled.class)
public class DisabledBean implements CustomBean {

    Boolean disabled=false;

    Boolean defaultFocus=false;

    Boolean disableClickEffect=false;

    Boolean disableHoverEffect=false;

    Boolean disableTips=false;

    public DisabledBean() {


    }

    public DisabledBean(Component component) {
        update(component);
    }

    public void update(Component component) {
        init(component.getProperties());
    }

    public void init(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }



    public DisabledBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(Disabled.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Disabled) {
                fillData((Disabled) annotation);
            }
        }
    }


    public Boolean getDefaultFocus() {
        return defaultFocus;
    }

    public void setDefaultFocus(Boolean defaultFocus) {
        this.defaultFocus = defaultFocus;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getDisableClickEffect() {
        return disableClickEffect;
    }

    public void setDisableClickEffect(Boolean disableClickEffect) {
        this.disableClickEffect = disableClickEffect;
    }

    public Boolean getDisableHoverEffect() {
        return disableHoverEffect;
    }

    public void setDisableHoverEffect(Boolean disableHoverEffect) {
        this.disableHoverEffect = disableHoverEffect;
    }

    public Boolean getDisableTips() {
        return disableTips;
    }

    public void setDisableTips(Boolean disableTips) {
        this.disableTips = disableTips;
    }

    public DisabledBean(Disabled annotation) {
        fillData(annotation);
    }

    public DisabledBean fillData(Disabled annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
