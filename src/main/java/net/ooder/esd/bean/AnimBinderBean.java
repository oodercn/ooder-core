package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.AnimBinder;
import net.ooder.esd.annotation.AnimFrameAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomAnimType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.AnimBinderComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGRectCombComponent;
import net.ooder.esd.tool.properties.AnimBinderProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(viewType = CustomViewType.COMPONENT,clazz =AnimBinderComponent.class, componentType = ComponentType.ANIMBINDER)
@AnnotationType(clazz = AnimBinder.class)
public class AnimBinderBean extends FieldBaseBean<AnimBinderComponent> {

    CustomAnimType customAnim;

    String dataBinder;

    String dataField;

    String name;

    String xpath;

    List<AnimFramesPropBean> frames;

    public AnimBinderBean(AnimBinderComponent component) {
        AnimBinderProperties properties = component.getProperties();
        this.xpath = component.getPath();
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public AnimBinderBean(AnimBinderProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public AnimBinderBean() {

    }

    public AnimBinderBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof AnimBinder) {
                fillData((AnimBinder) annotation);
            }
            if (annotation instanceof AnimFrameAnnotation) {
                if (frames == null) {
                    frames = new ArrayList<>();
                }
                frames.add(new AnimFramesPropBean((AnimFrameAnnotation) annotation));
            }

        }
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public CustomAnimType getCustomAnim() {
        return customAnim;
    }

    public void setCustomAnim(CustomAnimType customAnim) {
        this.customAnim = customAnim;
    }

    public String getDataBinder() {
        return dataBinder;
    }

    public void setDataBinder(String dataBinder) {
        this.dataBinder = dataBinder;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AnimFramesPropBean> getFrames() {
        return frames;
    }

    public void setFrames(List<AnimFramesPropBean> frames) {
        this.frames = frames;
    }

    public AnimBinderBean(AnimBinder annotation) {
        fillData(annotation);
    }

    public AnimBinderBean fillData(AnimBinder annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.ANIMBINDER;
    }

    @Override
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        annotationBeans.addAll(frames);
        return annotationBeans;
    }


    @Override
    public void update(ModuleComponent moduleComponent, AnimBinderComponent component) {

    }
}
