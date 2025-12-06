package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.VideoAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.PreloadType;
import net.ooder.esd.custom.component.form.field.CustomVideoComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.VideoComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomVideoComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.VIDEO
)
@AnnotationType(clazz = VideoAnnotation.class)
public class VideoFieldBean extends FieldBaseBean<VideoComponent> {



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

    @Override
    public void update(ModuleComponent moduleComponent, VideoComponent component) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public VideoFieldBean(VideoComponent component) {
        super(component);

    }




    public VideoFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(VideoAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof VideoAnnotation) {
                fillData((VideoAnnotation) annotation);
            }
        }
    }

    public VideoFieldBean() {

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

    public VideoFieldBean(VideoAnnotation annotation) {
        fillData(annotation);
    }

    public VideoFieldBean fillData(VideoAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.VIDEO;
    }
}
