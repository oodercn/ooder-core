package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboNumberAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboNumberComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboNumberComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.number, ComboInputType.currency, ComboInputType.counter, ComboInputType.spin},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboNumberAnnotation.class)
public class ComboNumberFieldBean extends ComboInputFieldBean {
    Integer precision;

    String increment;

    String min;

    String max;

    String numberTpl;

    Boolean forceFillZero = true;

    Boolean trimTailZero;

    String decimalSeparator = ".";

    String groupingSeparator;

    String currencyTpl = "$ *";

    public ComboNumberFieldBean(ComboInputComponent comboInputComponent) {
        super(comboInputComponent);
        this.xpath=comboInputComponent.getPath();


    }

    public ComboNumberFieldBean() {
        super(ComboInputType.number);
    }

    public ComboNumberFieldBean(ComboInputType inputType) {

    }

    public ComboNumberFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        } else {
            inputType = ComboInputType.number;
        }

        AnnotationUtil.fillDefaultValue(ComboNumberAnnotation.class, this);

        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboNumberAnnotation) {
                fillData((ComboNumberAnnotation) annotation);
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
        return classSet;
    }

    public String getCurrencyTpl() {
        return currencyTpl;
    }

    public void setCurrencyTpl(String currencyTpl) {
        this.currencyTpl = currencyTpl;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public String getGroupingSeparator() {
        return groupingSeparator;
    }

    public void setGroupingSeparator(String groupingSeparator) {
        this.groupingSeparator = groupingSeparator;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getNumberTpl() {
        return numberTpl;
    }

    public void setNumberTpl(String numberTpl) {
        this.numberTpl = numberTpl;
    }


    public Boolean getForceFillZero() {
        return forceFillZero;
    }

    public void setForceFillZero(Boolean forceFillZero) {
        this.forceFillZero = forceFillZero;
    }

    public Boolean getTrimTailZero() {
        return trimTailZero;
    }

    public void setTrimTailZero(Boolean trimTailZero) {
        this.trimTailZero = trimTailZero;
    }


    public ComboNumberFieldBean(ComboNumberAnnotation annotation) {
        fillData(annotation);
    }


    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboNumberAnnotation.class);
    }

    public ComboNumberFieldBean fillData(ComboNumberAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
