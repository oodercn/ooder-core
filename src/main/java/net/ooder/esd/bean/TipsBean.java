package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Tips;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = Tips.class)
public class TipsBean implements CustomBean {

    String mask;

    String tipsErr;

    String tipsBinder;

    String tips;

    String placeholder;

    String tipsOK;

    public TipsBean() {

    }

    public TipsBean(Component component) {
        AnnotationUtil.fillDefaultValue(Tips.class, this);
        this.update(component);
    }

    public void update(Component component) {
        this.init(component.getProperties());
    }

    private void init(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public TipsBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(Tips.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Tips) {
                fillData((Tips) annotation);
            }
        }

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }


    public String getTipsErr() {
        return tipsErr;
    }

    public void setTipsErr(String tipsErr) {
        this.tipsErr = tipsErr;
    }

    public String getTipsBinder() {
        return tipsBinder;
    }

    public void setTipsBinder(String tipsBinder) {
        this.tipsBinder = tipsBinder;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getTipsOK() {
        return tipsOK;
    }

    public void setTipsOK(String tipsOK) {
        this.tipsOK = tipsOK;
    }

    public TipsBean(Tips annotation) {
        fillData(annotation);
    }

    public TipsBean fillData(Tips annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
