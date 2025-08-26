package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ConDockMarginAnnotation;
import net.ooder.esd.annotation.ConDockSpacingAnnotation;
import net.ooder.esd.annotation.DockAnnotation;
import net.ooder.esd.annotation.DockMarginAnnotation;
import net.ooder.esd.annotation.ui.DockFlexType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Margin;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.Spacing;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = DockAnnotation.class)
public class DockBean implements CustomBean {


    public Boolean conDockRelative;
    public DockFlexType conDockFlexFill;
    public String conDockStretch;
    public Boolean dockIgnore;
    public Boolean dockFloat;
    public Integer dockOrder;
    public Margin dockMargin;
    public Margin conDockPadding;
    public Spacing conDockSpacing;
    public String dockMinW;
    public String dockMinH;
    public String dockMaxW;
    public String dockMaxH;
    public Boolean dockIgnoreFlexFill;
    public String dockStretch;


    public DockBean(DockAnnotation annotation) {
        initDefault();
        fillData(annotation);
    }

    public DockBean() {

    }


    public DockBean(Component component) {
        init(component.getProperties());
    }

    public DockBean fillData(DockAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public void update(Component component) {
        init(component.getProperties());
    }

    private void init(Properties properties) {
        initDefault();
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public void initDefault() {
        AnnotationUtil.fillDefaultValue(DockAnnotation.class, this);
        AnnotationUtil.fillDefaultValue(DockMarginAnnotation.class, this);
        AnnotationUtil.fillDefaultValue(ConDockMarginAnnotation.class, this);
        AnnotationUtil.fillDefaultValue(DockMarginAnnotation.class, this);

    }

    public DockBean(Set<Annotation> annotations) {
        initDefault();
        init(annotations.toArray(new Annotation[]{}));
    }

    void init(Annotation... annotations) {

        for (Annotation annotation : annotations) {
            if (annotation instanceof DockAnnotation) {
                fillData((DockAnnotation) annotation);
            }

            if (annotation instanceof DockMarginAnnotation) {
                dockMargin = AnnotationUtil.fillDefaultValue(DockMarginAnnotation.class, new Margin());
            }

            if (annotation instanceof ConDockMarginAnnotation) {
                conDockPadding = AnnotationUtil.fillDefaultValue(ConDockMarginAnnotation.class, new Margin());
            }
            if (annotation instanceof DockMarginAnnotation) {
                conDockSpacing = AnnotationUtil.fillDefaultValue(ConDockSpacingAnnotation.class, new Spacing());
            }
        }

    }

    public DockBean(Annotation... annotations) {
        initDefault();
        init(annotations);

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List annotationBeans = new ArrayList<>();
        if (dockMargin != null && !AnnotationUtil.getAnnotationMap(dockMargin).isEmpty()) {
            annotationBeans.add(dockMargin);
        }
        if (conDockPadding != null && !AnnotationUtil.getAnnotationMap(conDockPadding).isEmpty()) {
            annotationBeans.add(conDockPadding);
        }
        if (conDockSpacing != null && !AnnotationUtil.getAnnotationMap(conDockSpacing).isEmpty()) {
            annotationBeans.add(conDockSpacing);
        }
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }


    public Spacing getConDockSpacing() {
        return conDockSpacing;
    }

    public void setConDockSpacing(Spacing conDockSpacing) {
        this.conDockSpacing = conDockSpacing;
    }

    public DockFlexType getConDockFlexFill() {
        return conDockFlexFill;
    }

    public void setConDockFlexFill(DockFlexType conDockFlexFill) {
        this.conDockFlexFill = conDockFlexFill;
    }

    public String getConDockStretch() {
        return conDockStretch;
    }

    public void setConDockStretch(String conDockStretch) {
        this.conDockStretch = conDockStretch;
    }


    public String getDockStretch() {
        return dockStretch;
    }

    public void setDockStretch(String dockStretch) {
        this.dockStretch = dockStretch;
    }

    public Boolean getConDockRelative() {
        return conDockRelative;
    }

    public void setConDockRelative(Boolean conDockRelative) {
        this.conDockRelative = conDockRelative;
    }


    public Boolean getDockIgnore() {
        return dockIgnore;
    }

    public void setDockIgnore(Boolean dockIgnore) {
        this.dockIgnore = dockIgnore;
    }

    public Boolean getDockFloat() {
        return dockFloat;
    }

    public void setDockFloat(Boolean dockFloat) {
        this.dockFloat = dockFloat;
    }

    public Integer getDockOrder() {
        return dockOrder;
    }

    public void setDockOrder(Integer dockOrder) {
        this.dockOrder = dockOrder;
    }


    public Boolean getDockIgnoreFlexFill() {
        return dockIgnoreFlexFill;
    }

    public void setDockIgnoreFlexFill(Boolean dockIgnoreFlexFill) {
        this.dockIgnoreFlexFill = dockIgnoreFlexFill;
    }


    public String getDockMinW() {
        return dockMinW;
    }

    public void setDockMinW(String dockMinW) {
        this.dockMinW = dockMinW;
    }

    public String getDockMinH() {
        return dockMinH;
    }

    public void setDockMinH(String dockMinH) {
        this.dockMinH = dockMinH;
    }

    public String getDockMaxW() {
        return dockMaxW;
    }

    public void setDockMaxW(String dockMaxW) {
        this.dockMaxW = dockMaxW;
    }

    public String getDockMaxH() {
        return dockMaxH;
    }

    public void setDockMaxH(String dockMaxH) {
        this.dockMaxH = dockMaxH;
    }


    public Margin getConDockPadding() {
        return conDockPadding;
    }

    public void setConDockPadding(Margin conDockPadding) {
        this.conDockPadding = conDockPadding;
    }

    public Margin getDockMargin() {
        return dockMargin;
    }

    public void setDockMargin(Margin dockMargin) {
        this.dockMargin = dockMargin;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}


