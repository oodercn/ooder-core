package net.ooder.esd.tool;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.ThemesType;
import net.ooder.esd.bean.ModuleStyleBean;
import net.ooder.web.util.AnnotationUtil;

public class ViewStyles {
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

    public ViewStyles() {

    }

    public ViewStyles(ModuleStyleBean styleBean) {
        this.zoom = styleBean.getZoom();
        this.theme = styleBean.getTheme();
        this.backgroundAttachment = styleBean.getBackgroundAttachment();
        this.backgroundColor = styleBean.getBackgroundColor();
        this.backgroundImage = styleBean.getBackgroundImage();
        this.backgroundPosition = styleBean.getBackgroundPosition();
        this.backgroundRepeat = styleBean.getBackgroundRepeat();

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
