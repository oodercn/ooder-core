package net.ooder.esd.custom;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Caption;
import net.ooder.annotation.Pid;
import net.ooder.annotation.Uid;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.util.DSMAnnotationUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;
import net.ooder.web.util.MethodUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomFieldInfo<M extends ComponentBean, N extends ComboBoxBean> extends BaseFieldInfo<M, N> implements Comparable<CustomFieldInfo> {

    Object value;

    Class returnType;
    @JSONField(serialize = false)
    Type genericType;
    @JSONField(serialize = false)
    Field field;
    @JSONField(serialize = false)
    Method method;


    private boolean isDefault = true;

    private boolean isUid = false;

    boolean captionField = false;


    public CustomFieldInfo(Field field, Integer index, ESDClass esdClass) {
        this.field = field;
        this.esdClass = esdClass;
        this.id = field.getName();
        this.genericType = field.getGenericType();
        this.returnType = field.getType();
        this.name = field.getName();
        this.fieldName = field.getName();
        if (returnType.isArray() || Collection.class.isAssignableFrom(returnType)) {
            Class innerClass = JSONGenUtil.getInnerReturnType(field);
            if (innerClass != null) {
                returnType = innerClass;
            }
        }


        CustomAnnotation methodmapping = field.getAnnotation(CustomAnnotation.class);
        Uid uid = field.getAnnotation(Uid.class);
        Pid pid = field.getAnnotation(Pid.class);
        Caption caption = field.getAnnotation(Caption.class);
        if (methodmapping != null || uid != null || pid != null || caption != null) {
            isDefault = false;
        }
        if (methodmapping != null) {
            if (methodmapping.captionField()) {
                captionField = true;
            }
            if (methodmapping.uid()) {
                this.uid = true;
            }
        }


        for (Method method : this.esdClass.getAllCtMethods()) {
            String fieldName = MethodUtil.getFieldName(method);
            if (fieldName.equals(this.name)) {
                this.method = method;
                continue;
            }
        }

        this.init(index, esdClass);


    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    Annotation getWidgetAnnotation(ComboInputType inputType) {
        if (inputType != null) {
            return DSMAnnotationUtil.getComboxAnnotation(field, inputType);
        }
        return CustomViewConfigFactory.getInstance().getWidgetAnnotation(field);
    }

    @Override
    <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        A annotation = field.getAnnotation(annotationClass);
        if (annotation == null && method != null) {
            annotation = (A) AnnotationUtil.getMethodAnnotation(method, annotationClass);
        }
        return annotation;
    }

    @Override
    Set<Annotation> getAllAnnotation() {
        Set<Annotation> annotationSet = new HashSet<>();
        Annotation[] allAnnotation = field.getAnnotations();
        annotationSet.addAll(Arrays.asList(allAnnotation));
        if (method != null) {
            Set<Annotation> allMethodAnnotation = AnnotationUtil.getAllAnnotations(method, false);
            if (allMethodAnnotation.size() > 0) {
                annotationSet.addAll(allMethodAnnotation);
            }

        }
        return annotationSet;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public boolean isUid() {
        return isUid;
    }

    @Override
    public void setUid(boolean uid) {
        isUid = uid;
    }

    public boolean isCaptionField() {
        return captionField;
    }

    public void setCaptionField(boolean captionField) {
        this.captionField = captionField;
    }

    @Override
    public Class getReturnType() {
        return returnType;
    }


    @Override
    public Type getGenericType() {
        return genericType;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public Object getValue() {
        return value;
    }


    public void setValue(Object value) {
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    @Override
    public int compareTo(CustomFieldInfo o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }
}
