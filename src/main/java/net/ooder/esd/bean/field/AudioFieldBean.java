package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.AudioAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.PreloadType;
import net.ooder.esd.custom.component.form.field.CustomAudioComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.AudioComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomAudioComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.AUDIO
)
@AnnotationType(clazz = AudioAnnotation.class)
public class AudioFieldBean extends FieldBaseBean<AudioComponent> {

    String width;

    String height;

    String src;

    Boolean cover;

    Boolean controls;

    PreloadType preload;

    Boolean loop;

    Boolean muted;

    Integer volume;

    Boolean autoplay;


    public AudioFieldBean(AudioComponent component) {
        super(component);
    }

    public AudioFieldBean(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public AudioFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(AudioAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof AudioAnnotation) {
                fillData((AudioAnnotation) annotation);
            }
        }

    }

    public AudioFieldBean() {

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

    public AudioFieldBean(AudioAnnotation annotation) {
        fillData(annotation);
    }

    public AudioFieldBean fillData(AudioAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, AudioComponent component) {
        return new ArrayList<>();
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.AUDIO;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();

        return classSet;
    }
}
