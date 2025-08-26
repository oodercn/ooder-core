package net.ooder.esd.bean.field;

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
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomFieldBlockComponent;
import net.ooder.esd.custom.component.form.field.combo.CustomComboInputComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.XUITypeMapping;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

//@CustomClass(clazz = CustomComboInputComponent.class,
//        viewType = CustomViewType.COMPONENT,
//        inputType = {ComboInputType.input},
//        componentType = ComponentType.COMBOINPUT
//)
//@AnnotationType(clazz = ComboInputAnnotation.class)
public class CustomComboFieldBean<M extends ComboInputComponent> implements ComboBoxBean<M>, FieldComponentBean<M> {

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


    public CustomComboFieldBean() {

    }


    public CustomComboFieldBean(ModuleProperties properties) {
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

    public CustomComboFieldBean(M comboInputComponent) {
        this.xpath = comboInputComponent.getPath();
        update(comboInputComponent.getProperties());
    }


    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, M component) {
        this.update(component.getProperties());
        return new ArrayList<>();

    }


    public CustomComboFieldBean(Map valueMap) {

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

    public CustomComboFieldBean(ComboInputType inputType) {
        AnnotationUtil.fillDefaultValue(ComboInputAnnotation.class, this);
        this.inputType = inputType;
    }

    public CustomComboFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
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

    public CustomComboFieldBean(ESDField esdField, Set<Annotation> annotations) {
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
            inputType = XUITypeMapping.getType(esdField.getReturnType());
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

    public CustomComboFieldBean(ComboInputAnnotation annotation) {
        fillData(annotation);
    }

    public CustomComboFieldBean fillData(ComboInputAnnotation annotation) {
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
