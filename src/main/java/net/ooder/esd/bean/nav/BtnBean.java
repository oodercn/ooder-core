package net.ooder.esd.bean.nav;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.BtnAnnotation;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Set;

@AnnotationType(clazz = BtnAnnotation.class)
public class BtnBean implements CustomBean {


    Boolean infoBtn;

    Boolean optBtn;

    Boolean toggleBtn;

    Boolean refreshBtn;

    Boolean closeBtn;

    Boolean popBtn;


    public BtnBean(Component component) {
        AnnotationUtil.fillDefaultValue(BtnAnnotation.class, this);
        this.update(component);
    }

    public void update(Component component) {
        PanelProperties panelProperties = (PanelProperties) component.getProperties();
        this.init(panelProperties);
    }

    void init(PanelProperties panelProperties) {
        infoBtn = panelProperties.getInfoBtn();
        optBtn = panelProperties.getOptBtn();
        toggleBtn = panelProperties.getToggleBtn();
        closeBtn = panelProperties.getCloseBtn();
        refreshBtn = panelProperties.getRefreshBtn();
        popBtn = panelProperties.getPopBtn();
    }

    public BtnBean() {

    }

    public BtnBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(BtnAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof BtnAnnotation) {
                fillData((BtnAnnotation) annotation);
            }
        }

    }


    public Boolean getInfoBtn() {
        return infoBtn;
    }

    public void setInfoBtn(Boolean infoBtn) {
        this.infoBtn = infoBtn;
    }

    public Boolean getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(Boolean optBtn) {
        this.optBtn = optBtn;
    }

    public Boolean getToggleBtn() {
        return toggleBtn;
    }

    public void setToggleBtn(Boolean toggleBtn) {
        this.toggleBtn = toggleBtn;
    }

    public Boolean getRefreshBtn() {
        return refreshBtn;
    }

    public void setRefreshBtn(Boolean refreshBtn) {
        this.refreshBtn = refreshBtn;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Boolean getPopBtn() {
        return popBtn;
    }

    public void setPopBtn(Boolean popBtn) {
        this.popBtn = popBtn;
    }

    public BtnBean(BtnAnnotation annotation) {
        fillData(annotation);
    }

    public BtnBean fillData(BtnAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
