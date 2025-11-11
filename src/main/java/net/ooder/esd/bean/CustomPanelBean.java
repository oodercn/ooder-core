package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.BtnAnnotation;
import net.ooder.esd.annotation.PanelAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.nav.BtnBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.PanelComponent;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@CustomClass(viewType = CustomViewType.COMPONENT,clazz =PanelComponent.class, componentType = ComponentType.PANEL)
@AnnotationType(clazz = PanelAnnotation.class)
public class CustomPanelBean implements CustomBean {

    String xpath;

    Dock dock;

    String caption;

    String html;

    Boolean toggle;

    String image;

    ImagePos imagePos;

    String imageBgSize;

    String imageClass;

    String iconFontCode;

    BorderType borderType;

    Boolean noFrame;

    HAlignType hAlign;

    ToggleIconType toggleIcon;

    CustomDivBean divBean;

    BtnBean btnBean;


    public CustomPanelBean() {

    }

    public CustomPanelBean(Component component) {
        AnnotationUtil.fillDefaultValue(PanelAnnotation.class, this);
        update(component);
    }


    public void update(Component component) {
        if (component instanceof PanelComponent) {
            this.init((PanelProperties) component.getProperties());
        } else {
            Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
            OgnlUtil.setProperties(valueMap, this, false, false);
        }

        this.xpath = component.getPath();
        if (divBean == null) {
            divBean = new CustomDivBean(component);
        } else {
            divBean.update(component);
        }

        if (btnBean == null) {
            btnBean = new BtnBean(component);
        }
    }

    public void init(PanelProperties bean) {

        if (bean.getCaption() != null && !bean.getCaption().equals("")) {
            this.caption = bean.getCaption();
        }
        if (bean.getHtml() != null && !bean.getHtml().equals("")) {
            this.setHtml(bean.getHtml());
        }
        if (bean.getToggle() != null && bean.getToggle()) {
            toggle = bean.getToggle();
        }

        if (bean.getImage() != null && !bean.getImage().equals("")) {
            this.image = bean.getImage();
        }
        if (bean.getDock() != null && !bean.getDock().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "dock"))) {
            this.dock = bean.getDock();
        }

        if (bean.getImagePos() != null && !bean.getImagePos().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "imagePos"))) {
            this.imagePos = bean.getImagePos();
        }

        if (bean.getImageBgSize() != null && !bean.getImageBgSize().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "imageBgSize"))) {
            this.imageBgSize = bean.getImageBgSize();
        }
        if (bean.getImageClass() != null && !bean.getImageClass().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "imageClass"))) {
            this.imageClass = bean.getImageClass();
        }

        if (bean.getIconFontCode() != null && !bean.getIconFontCode().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "iconFontCode"))) {
            this.iconFontCode = bean.getIconFontCode();
        }

        if (bean.getBorderType() != null && !bean.getBorderType().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "borderType"))) {
            this.borderType = bean.getBorderType();
        }

        if (bean.getNoFrame() != null && !bean.getNoFrame().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "noFrame"))) {
            this.noFrame = bean.getNoFrame();
        }

        if (bean.gethAlign() != null && !bean.gethAlign().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "hAlign"))) {
            this.hAlign = bean.gethAlign();
        }


        if (bean.getToggleIcon() != null && !bean.getToggleIcon().equals(AnnotationUtil.getDefaultValue(PanelAnnotation.class, "toggleIcon"))) {
            this.toggleIcon = bean.getToggleIcon();
        }

    }

    public CustomPanelBean(PanelAnnotation annotation) {
        fillData(annotation);
    }

    public CustomPanelBean fillData(PanelAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomPanelBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(PanelAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof PanelAnnotation) {
                fillData((PanelAnnotation) annotation);
            }
            if (annotation instanceof BtnAnnotation) {
                btnBean = new BtnBean((BtnAnnotation) annotation);
            }
        }
        divBean = new CustomDivBean(annotations);
    }

    public CustomPanelBean(Annotation[] annotations) {
        AnnotationUtil.fillDefaultValue(PanelAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof PanelAnnotation) {
                fillData((PanelAnnotation) annotation);
            }
            if (annotation instanceof BtnAnnotation) {
                btnBean = new BtnBean((BtnAnnotation) annotation);
            }
        }
        divBean = new CustomDivBean(annotations);
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        if (btnBean != null && !AnnotationUtil.getAnnotationMap(btnBean).isEmpty()) {
            annotationBeans.add(btnBean);
        }
        if (divBean != null) {
            annotationBeans.addAll(divBean.getAnnotationBeans());
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAllAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (divBean != null) {
            annotationBeans.addAll(divBean.getAnnotationBeans());
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }


    public CustomDivBean getDivBean() {
        return divBean;
    }

    public void setDivBean(CustomDivBean divBean) {
        this.divBean = divBean;
    }

    public BtnBean getBtnBean() {
        return btnBean;
    }

    public void setBtnBean(BtnBean btnBean) {
        this.btnBean = btnBean;
    }


    public Boolean getToggle() {
        return toggle;
    }

    public void setToggle(Boolean toggle) {
        this.toggle = toggle;
    }

    public Boolean getNoFrame() {
        return noFrame;
    }

    public void setNoFrame(Boolean noFrame) {
        this.noFrame = noFrame;
    }


    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ImagePos getImagePos() {
        return imagePos;
    }

    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }


    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public ToggleIconType getToggleIcon() {
        return toggleIcon;
    }

    public void setToggleIcon(ToggleIconType toggleIcon) {
        this.toggleIcon = toggleIcon;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
