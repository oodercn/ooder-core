package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.ComboPopAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboPopComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboPopComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.popbox, ComboInputType.cmd, ComboInputType.cmdbox, ComboInputType.button, ComboInputType.dropbutton},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboPopAnnotation.class)
public class ComboPopFieldBean extends ComboInputFieldBean {

    String parentID;

    Boolean cachePopWnd;

    String width;

    String height;

    String src;

    Boolean dynLoad;

    Class bindClass;


    public ComboPopFieldBean() {

    }
    public ComboPopFieldBean(ComboInputComponent comboInputComponent) {
        super(comboInputComponent);
    }


    public ComboPopFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboPopAnnotation.class, this);
    }

    public ComboPopFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        }

        AnnotationUtil.fillDefaultValue(ComboPopAnnotation.class, this);
        CustomRefBean refBean = esdField.getRefBean();
        if (refBean != null && refBean.getRef() != null) {
            switch (refBean.getRef()) {
                case O2O:
                    this.inputType = ComboInputType.getter;
                    break;
                default:
                    this.inputType = ComboInputType.getter;
                    break;
            }

            if (refBean.getView() != null && refBean.getView().equals(ViewType.DIC)) {
                this.inputType = ComboInputType.getter;
            }
        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboPopAnnotation) {
                fillData((ComboPopAnnotation) annotation);
            }
        }
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();
        if (this.getBindClass()!=null){
            classSet.add(this.getBindClass());
        }
        return ClassUtility.checkBase(classSet);
    }


    public ComboPopFieldBean(ComboPopAnnotation annotation) {
        fillData(annotation);
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public String getParentID() {
        return parentID;
    }


    public void setParentID(String parentID) {
        this.parentID = parentID;
    }


    public Boolean getCachePopWnd() {
        return cachePopWnd;
    }


    public void setCachePopWnd(Boolean cachePopWnd) {
        this.cachePopWnd = cachePopWnd;
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


    public Boolean getDynLoad() {
        return dynLoad;
    }


    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboPopAnnotation.class);
    }

    public ComboPopFieldBean fillData(ComboPopAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
