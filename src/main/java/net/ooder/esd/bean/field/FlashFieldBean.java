package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.FlashAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.component.form.field.CustomFlashComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.FlashComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.FlashProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomFlashComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.FLASH
)
@AnnotationType(clazz = FlashAnnotation.class)
public class FlashFieldBean extends FieldBaseBean<FlashComponent> {

    String width;

    String height;

    String src;

    Boolean cover;


    public FlashFieldBean(FlashProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, FlashComponent component) {

        return new ArrayList<>();
    }

    public FlashFieldBean(Set<Annotation> annotations) {
        super( annotations);
        AnnotationUtil.fillDefaultValue(FlashAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof FlashAnnotation) {
                fillData((FlashAnnotation) annotation);
            }
        }

    }

    public FlashFieldBean() {

    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
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


    public FlashFieldBean(FlashAnnotation annotation) {
        fillData(annotation);
    }

    public FlashFieldBean fillData(FlashAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.FLASH;
    }
}
