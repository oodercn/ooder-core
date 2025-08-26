package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.LabelPos;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.SliderFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class SliderProperties extends FieldProperties {

    public Integer precision;
    public String numberTpl;
    public Integer steps;
    public String type;
    public Boolean isRange;
    public Boolean showIncreaseHandle;
    public Boolean showDecreaseHandle;
    public LayoutType layoutType;

    public String labelSize;

    public LabelPos labelPos;

    public String labelGap;

    public String labelCaption;

    public HAlignType labelHAlign;

    public VAlignType labelVAlign;
    public SliderProperties(){

    }

    public SliderProperties(SliderFieldBean bean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
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
}
