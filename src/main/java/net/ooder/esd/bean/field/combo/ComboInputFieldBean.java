package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Input;
import net.ooder.esd.annotation.field.ComboInputAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.bean.field.InputBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboInputComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.OODTypeMapping;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomComboInputComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.input},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboInputAnnotation.class)
public class ComboInputFieldBean<M extends ComboInputComponent> implements ComboBoxBean<M>, FieldComponentBean<M> {

    String id;

    String xpath;

    String expression;

    String imageBgSize;

    String imageClass;

    String iconFontCode;

    String unit;

    String units;

    String commandBtn;

    ComboInputType inputType;

    Boolean inputReadonly;

    Boolean readonly;

    String labelCaption;

    InputBean<ComboInputProperties> inputBean;

    List<JavaSrcBean> javaSrcBeans;


    public ComboInputFieldBean() {

    }


    public ComboInputFieldBean(ModuleProperties properties) {
        this.inputType = properties.getType();
        inputBean = new InputBean(properties);
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    protected void update(ComboInputProperties properties) {
        this.inputType = properties.getType();
        inputBean = new InputBean(properties);
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public ComboInputFieldBean(M comboInputComponent) {
        this.xpath = comboInputComponent.getPath();
        update(comboInputComponent.getProperties());
    }


    @Override
    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcBeans() {
        if (javaSrcBeans == null) {
            javaSrcBeans = new ArrayList<>();
        }

        return javaSrcBeans;
    }


    @Override
    public void update(ModuleComponent moduleComponent, M component) {
        this.update(component.getProperties());


    }


    public ComboInputFieldBean(Map valueMap) {

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        if (inputBean != null && !AnnotationUtil.getAnnotationMap(inputBean).isEmpty()) {
            annotationBeans.add(inputBean);
        }
        return annotationBeans;
    }

    @Override
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public ComboInputFieldBean(ComboInputType inputType) {
        AnnotationUtil.fillDefaultValue(ComboInputAnnotation.class, this);
        this.inputType = inputType;
    }

    public ComboInputFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        AnnotationUtil.fillDefaultValue(ComboInputAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboInputAnnotation) {
                fillData((ComboInputAnnotation) annotation);
            }

            if (annotation instanceof Input) {
                inputBean = new InputBean((Input) annotation);
            }
        }
        this.inputType = inputType;
    }

    public ComboInputFieldBean(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ComboInputAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboInputAnnotation) {
                fillData((ComboInputAnnotation) annotation);
            }
            if (annotation instanceof Input) {
                inputBean = new InputBean((Input) annotation);
            }
        }
        if (inputType.equals(AnnotationUtil.getDefaultValue(ComboInputAnnotation.class, "inputType"))) {
            inputType = OODTypeMapping.getType(esdField.getReturnType());
        }
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }


    public String getLabelCaption() {
        return labelCaption;
    }

    public void setLabelCaption(String labelCaption) {
        this.labelCaption = labelCaption;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public String getCommandBtn() {
        return commandBtn;
    }


    public void setCommandBtn(String commandBtn) {
        this.commandBtn = commandBtn;
    }

    public ComboInputType getInputType() {
        return inputType;
    }

    public void setInputType(ComboInputType inputType) {
        this.inputType = inputType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Boolean getInputReadonly() {
        return inputReadonly;
    }

    public void setInputReadonly(Boolean inputReadonly) {
        this.inputReadonly = inputReadonly;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public ComboInputFieldBean(ComboInputAnnotation annotation) {
        fillData(annotation);
    }

    public ComboInputFieldBean fillData(ComboInputAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboInputAnnotation.class);
    }

    public InputBean getInputBean() {
        return inputBean;
    }

    public void setInputBean(InputBean inputBean) {
        this.inputBean = inputBean;
    }

    protected ComboInputType[] getComboInputType(Class<? extends Annotation> annotationClass) {
        ComboInputType[] comboInputTypes = new ComboInputType[]{};
        CustomClass customClass = annotationClass.getAnnotation(CustomClass.class);
        if (customClass != null) {
            comboInputTypes = customClass.inputType();
        }
        return comboInputTypes;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.COMBOINPUT;
    }

}
