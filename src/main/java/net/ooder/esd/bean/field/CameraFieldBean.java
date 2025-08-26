package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.CameraAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.PreloadType;
import net.ooder.esd.custom.component.form.field.CustomCameraComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.CameraComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CustomClass(clazz = CustomCameraComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.CAMERA
)
@AnnotationType(clazz = CameraAnnotation.class)
public class CameraFieldBean extends FieldBaseBean<CameraComponent> {



    String width;

    String height;

    String src;
    ;

    Boolean cover;

    Boolean controls;

    PreloadType preload;

    Boolean loop;

    Boolean muted;

    Integer volume;

    Boolean autoplay;

    String poster;

    public CameraFieldBean(CameraComponent component) {
        super(component);

    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, CameraComponent component) {

        return new ArrayList<>();
    }


    public CameraFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(CameraAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof CameraAnnotation) {
                fillData((CameraAnnotation) annotation);
            }
        }
    }

    public CameraFieldBean() {

    }
    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean getCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

    public Boolean getControls() {
        return controls;
    }

    public void setControls(Boolean controls) {
        this.controls = controls;
    }

    public PreloadType getPreload() {
        return preload;
    }

    public void setPreload(PreloadType preload) {
        this.preload = preload;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Boolean getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(Boolean autoplay) {
        this.autoplay = autoplay;
    }

    public CameraFieldBean(CameraAnnotation annotation) {
        fillData(annotation);
    }

    public CameraFieldBean fillData(CameraAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CAMERA;
    }
}
