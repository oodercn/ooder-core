package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.DialogBtnAnnotation;
import net.ooder.esd.tool.component.DialogComponent;
import net.ooder.esd.tool.properties.DialogProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = DialogBtnAnnotation.class)
public class DialogBtnBean implements CustomBean {


    Boolean minBtn;

    Boolean maxBtn;

    Boolean refreshBtn;

    Boolean infoBtn;

    Boolean pinBtn;

    Boolean landBtn;

    Boolean displayBar;


    public DialogBtnBean(DialogComponent component) {
        AnnotationUtil.fillDefaultValue(DialogBtnAnnotation.class, this);
        this.update(component);
    }

    public void update(DialogComponent component) {
        this.init((DialogProperties) component.getProperties());
    }

    public void init(DialogProperties dialogProperties) {

        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(dialogProperties), Map.class), this, false, false);
        infoBtn = dialogProperties.getInfoBtn();
        maxBtn = dialogProperties.getMaxBtn();
        minBtn = dialogProperties.getMinBtn();
        landBtn = dialogProperties.getLandBtn();
        refreshBtn = dialogProperties.getRefreshBtn();
        pinBtn = dialogProperties.getPinBtn();
        displayBar = dialogProperties.getDisplayBar();
    }


    public DialogBtnBean() {

    }

    public DialogBtnBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(DialogBtnAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof DialogBtnAnnotation) {
                fillData((DialogBtnAnnotation) annotation);
            }
        }

    }

    public Boolean getDisplayBar() {
        return displayBar;
    }

    public void setDisplayBar(Boolean displayBar) {
        this.displayBar = displayBar;
    }

    public Boolean getMinBtn() {
        return minBtn;
    }

    public void setMinBtn(Boolean minBtn) {
        this.minBtn = minBtn;
    }

    public Boolean getMaxBtn() {
        return maxBtn;
    }

    public void setMaxBtn(Boolean maxBtn) {
        this.maxBtn = maxBtn;
    }


    public Boolean getRefreshBtn() {
        return refreshBtn;
    }

    public void setRefreshBtn(Boolean refreshBtn) {
        this.refreshBtn = refreshBtn;
    }

    public Boolean getInfoBtn() {
        return infoBtn;
    }

    public void setInfoBtn(Boolean infoBtn) {
        this.infoBtn = infoBtn;
    }

    public Boolean getPinBtn() {
        return pinBtn;
    }

    public void setPinBtn(Boolean pinBtn) {
        this.pinBtn = pinBtn;
    }

    public Boolean getLandBtn() {
        return landBtn;
    }

    public void setLandBtn(Boolean landBtn) {
        this.landBtn = landBtn;
    }

    public DialogBtnBean(DialogBtnAnnotation annotation) {
        fillData(annotation);
    }

    public DialogBtnBean fillData(DialogBtnAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
