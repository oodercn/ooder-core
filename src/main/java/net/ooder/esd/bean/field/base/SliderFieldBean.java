package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.SliderAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.CustomSliderComponent;
import net.ooder.esd.custom.component.form.field.CustomTimePickerComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SliderComponent;
import net.ooder.esd.tool.properties.form.SliderProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.Callable;

@CustomClass(clazz = CustomSliderComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SLIDER
)
@AnnotationType(clazz = SliderAnnotation.class)
public class SliderFieldBean    extends FieldBaseBean<CustomSliderComponent> {

    String id;

    String width;

    String height;

    Integer precision;

    String numberTpl;

    Integer steps;

    String xpath;
    String value;

    LayoutType layoutType;

    Boolean isRange;

    Boolean showIncreaseHandle;

    Boolean showDecreaseHandle;

    String labelSize;

    LabelPos labelPos;

    String labelGap;

    String labelCaption;

    HAlignType labelHAlign;

    VAlignType labelVAlign;

    public SliderFieldBean(SliderComponent sliderComponent) {
        this.xpath=sliderComponent.getPath();
        this.update((SliderProperties) sliderComponent.getProperties());

    }
    private void update(SliderProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public SliderFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SliderAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SliderAnnotation) {
                fillData((SliderAnnotation) annotation);
            }
        }
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
    }
    public SliderFieldBean() {

    }
    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public String getNumberTpl() {
        return numberTpl;
    }

    public void setNumberTpl(String numberTpl) {
        this.numberTpl = numberTpl;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public Boolean getRange() {
        return isRange;
    }

    public void setRange(Boolean range) {
        isRange = range;
    }

    public Boolean getShowIncreaseHandle() {
        return showIncreaseHandle;
    }

    public void setShowIncreaseHandle(Boolean showIncreaseHandle) {
        this.showIncreaseHandle = showIncreaseHandle;
    }

    public Boolean getShowDecreaseHandle() {
        return showDecreaseHandle;
    }

    public void setShowDecreaseHandle(Boolean showDecreaseHandle) {
        this.showDecreaseHandle = showDecreaseHandle;
    }

    public String getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(String labelSize) {
        this.labelSize = labelSize;
    }

    public LabelPos getLabelPos() {
        return labelPos;
    }

    public void setLabelPos(LabelPos labelPos) {
        this.labelPos = labelPos;
    }

    public String getLabelGap() {
        return labelGap;
    }

    public void setLabelGap(String labelGap) {
        this.labelGap = labelGap;
    }

    public String getLabelCaption() {
        return labelCaption;
    }

    public void setLabelCaption(String labelCaption) {
        this.labelCaption = labelCaption;
    }

    public HAlignType getLabelHAlign() {
        return labelHAlign;
    }

    public void setLabelHAlign(HAlignType labelHAlign) {
        this.labelHAlign = labelHAlign;
    }

    public VAlignType getLabelVAlign() {
        return labelVAlign;
    }

    public void setLabelVAlign(VAlignType labelVAlign) {
        this.labelVAlign = labelVAlign;
    }

    public SliderFieldBean(SliderAnnotation annotation) {
        fillData(annotation);
    }

    public SliderFieldBean fillData(SliderAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.SLIDER;
    }

    @Override
    public void update(ModuleComponent moduleComponent, CustomSliderComponent component) {

    }
}
