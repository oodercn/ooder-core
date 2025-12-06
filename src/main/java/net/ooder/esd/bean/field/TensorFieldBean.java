package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.TensorAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.custom.component.form.field.CustomTensorComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.TensorComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomTensorComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.TENSOR
)
@AnnotationType(clazz =TensorAnnotation.class)
public class TensorFieldBean extends FieldBaseBean<TensorComponent> {

    String width;

    String height;

    String src;

    Class bindClass;

    Boolean prepareFormData;

    String uploadUrl;

    Dock dock;


    @Override
    public void update(ModuleComponent moduleComponent, TensorComponent component) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public TensorFieldBean(TensorComponent component) {
        super(component);
    }



    public TensorFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(TensorAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof TensorAnnotation) {
                fillData((TensorAnnotation) annotation);
            }
        }
    }

    public TensorFieldBean() {

    }
    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }
    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
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

    public Boolean getPrepareFormData() {
        return prepareFormData;
    }

    public void setPrepareFormData(Boolean prepareFormData) {
        this.prepareFormData = prepareFormData;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public TensorFieldBean(TensorAnnotation annotation) {
        fillData(annotation);
    }

    public TensorFieldBean fillData(TensorAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TENSOR;
    }
}
