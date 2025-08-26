package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ViewStylesAnnotation;
import net.ooder.esd.annotation.ui.ThemesType;
import net.ooder.esd.tool.ViewStyles;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Set;

@AnnotationType(clazz = ViewStylesAnnotation.class)
public class ModuleStyleBean implements CustomBean {
    String zoom;

    ThemesType theme;
    @JSONField(name = "background-color")
    String backgroundColor;

    @JSONField(name = "background-image")
    String backgroundImage;
    @JSONField(name = "background-repeat")
    String backgroundRepeat;
    @JSONField(name = "background-position")
    String backgroundPosition;
    @JSONField(name = "background-attachment")
    String backgroundAttachment;

    public void update(ViewStyles viewStyles) {
        this.zoom = viewStyles.getZoom();
        this.theme = viewStyles.getTheme();
        this.backgroundAttachment = viewStyles.getBackgroundAttachment();
        this.backgroundColor = viewStyles.getBackgroundColor();
        this.backgroundImage = viewStyles.getBackgroundImage();
        this.backgroundPosition = viewStyles.getBackgroundPosition();
        this.backgroundRepeat = viewStyles.getBackgroundRepeat();
    }

    public ModuleStyleBean(ViewStyles viewStyles) {
        update(viewStyles);
    }

    public ModuleStyleBean() {

    }

    public ModuleStyleBean(ViewStylesAnnotation annotation) {
        this.fillData(annotation);

    }

    public ModuleStyleBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof ViewStylesAnnotation) {
                this.fillData((ViewStylesAnnotation) annotation);
            }
        }


    }


    public ModuleStyleBean fillData(ViewStylesAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public ThemesType getTheme() {
        return theme;
    }

    public void setTheme(ThemesType theme) {
        this.theme = theme;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundRepeat() {
        return backgroundRepeat;
    }

    public void setBackgroundRepeat(String backgroundRepeat) {
        this.backgroundRepeat = backgroundRepeat;
    }

    public String getBackgroundPosition() {
        return backgroundPosition;
    }

    public void setBackgroundPosition(String backgroundPosition) {
        this.backgroundPosition = backgroundPosition;
    }

    public String getBackgroundAttachment() {
        return backgroundAttachment;
    }

    public void setBackgroundAttachment(String backgroundAttachment) {
        this.backgroundAttachment = backgroundAttachment;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
