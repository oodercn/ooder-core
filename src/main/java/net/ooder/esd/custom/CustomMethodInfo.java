package net.ooder.esd.custom;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Caption;
import net.ooder.annotation.Pid;
import net.ooder.annotation.Uid;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ModuleRefFieldAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.util.DSMAnnotationUtil;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;
import net.ooder.web.util.MethodUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class CustomMethodInfo<M extends ComponentBean, N extends ComboBoxBean> extends BaseFieldInfo<M, N> implements Comparable<CustomMethodInfo> {

    String methodInfo;

    String methodName;

    boolean isModule = false;

    boolean isDefault = false;

    boolean captionField = false;

    ViewType viewType;

    @JSONField(serialize = false)
    Method innerMethod;

    @JSONField(serialize = false)
    Class returnType;

    @JSONField(serialize = false)
    Field field;


    public CustomMethodInfo() {

    }


    public CustomMethodInfo(Method method, ESDClass esdClass) {
        this.innerMethod = method;
        this.returnType = method.getReturnType();
        this.domainId = esdClass.getDomainId();
        this.esdClass = esdClass;
        this.methodName = method.getName();
        this.fieldName = MethodUtil.getFieldName(method);
        this.innerMethod = method;
        this.name = fieldName;
        this.id = fieldName;
        for (Field fieldInfo : this.esdClass.getAllCtFields()) {
            if (fieldInfo.getName().equals(fieldName)) {
                this.field = fieldInfo;
                continue;
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

        if (methodmapping != null && methodmapping.uid()) {
            this.uid = true;
        }
        if (uid != null) {
            this.uid = true;
        }




        try {
            if (returnType.isArray() || Collection.class.isAssignableFrom(returnType)) {
                Class innerClass = JSONGenUtil.getInnerReturnType(method);
                if (innerClass != null) {
                    returnType = innerClass;
                }
            }
            init(index, esdClass);
            if (this.getRefBean() != null) {
                CustomRefBean ref = this.getRefBean();
                if (ref.getView() != null && ref.getView().equals(ViewType.DIC)) {
                    componentType = ComponentType.COMBOINPUT;
                }
                this.refType = ref.getRef();
                this.viewType = ref.getView();
            }

            if (componentType == null) {
                componentType = ComponentType.COMBOINPUT;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        ModuleAnnotation annotation = AnnotationUtil.getMethodAnnotation(method, ModuleAnnotation.class);
        if (annotation != null) {
            isModule = true;
        }

        ModuleRefFieldAnnotation comboModuleAnnotation = AnnotationUtil.getMethodAnnotation(method, ModuleRefFieldAnnotation.class);
        if (comboModuleAnnotation != null) {
            isModule = true;
        }

        APIEventAnnotation apiEventAnnotation = AnnotationUtil.getMethodAnnotation(method, APIEventAnnotation.class);
        if (apiEventAnnotation != null) {
            for (CustomMenuItem menuItem : apiEventAnnotation.bindMenu()) {
                if (!menuItem.getReturnView().equals(ModuleViewType.NONE) && menuItem.getDefaultView()) {
                    isModule = true;
                }
            }

        }
    }


    @Override
    Annotation getWidgetAnnotation(ComboInputType inputType) {
        if (inputType != null) {
            return DSMAnnotationUtil.getComboxAnnotation(innerMethod, inputType);
        }

        return CustomViewConfigFactory.getInstance().getWidgetAnnotation(innerMethod);
    }

    @Override
    <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        A annotation = null;
        Map<Class<Annotation>, Annotation> annotationMap = AnnotationUtil.getMethodAnnotationMap(innerMethod);
        if (annotationMap.get(annotationClass) != null) {
            annotation = (A) annotationMap.get(annotationClass);
        } else {
            annotation = AnnotationUtil.getMethodAnnotation(innerMethod, annotationClass);
        }

        if (annotation == null && field != null) {
            annotation = (A) field.getAnnotation(annotationClass);
        }
        return annotation;
    }

    @Override
    Set<Annotation> getAllAnnotation() {
        Set<Annotation> annotationSet = new HashSet<>();
        if (field != null) {
            Annotation[] allAnnotation = field.getAnnotations();
            annotationSet.addAll(Arrays.asList(allAnnotation));
        }
        annotationSet.addAll(AnnotationUtil.getAllAnnotations(innerMethod, false));
        return annotationSet;

    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isCaptionField() {
        return captionField;
    }

    public void setCaptionField(boolean captionField) {
        this.captionField = captionField;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public Class getReturnType() {
        return returnType;
    }

    @Override
    public Type getGenericType() {
        Type type = null;
        try {
            type = innerMethod.getGenericReturnType();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        return type;
    }


    @Override
    public CustomFieldBean getCustomBean() {
        return null;
    }


    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }


    public String getMethodName() {
        return methodName;
    }


    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public Method getInnerMethod() {
        return innerMethod;
    }

    public void setInnerMethod(Method innerMethod) {
        this.innerMethod = innerMethod;
    }

    @JSONField(serialize = false)
    public String getMethodInfo() {
        if (methodInfo != null) {
            boolean isModule = AnnotationUtil.getAllAnnotations(innerMethod, ModuleAnnotation.class, true) == null ? false : true;

            if (isModule) {
                this.methodInfo = MethodUtil.toMethodStr(innerMethod, this.getViewType(), OODUtil.formatJavaName(methodName, true), false, null).toString();
            } else {
                this.methodInfo = MethodUtil.toMethodStr(innerMethod, this.getViewType(), "", false, null).toString();
            }
        }
        return methodInfo;
    }

    public void set(String methodInfo) {
        this.methodInfo = methodInfo;
    }

    public void setMethodInfo(String methodInfo) {
        this.methodInfo = methodInfo;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }


    @Override
    public String toString() {
        return methodName;
    }

    public boolean isModule() {
        return isModule;
    }

    public void setModule(boolean module) {
        isModule = module;
    }

    @Override
    public int compareTo(CustomMethodInfo o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 0;
    }

}
