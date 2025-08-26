package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.Label;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.InputType;
import net.ooder.esd.annotation.ui.LabelPos;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.TipsBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.base.InputFieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

public class InputProperties extends ListFieldProperties {

    public InputType texttype;
    public String mask;
    public Integer maxlength;
    public Boolean multiLines;
    public Boolean shadow;
    public Boolean hiddenBorder;
    public Boolean resizer;
    public Map<String, Object> resizerProp;
    public Boolean selectOnFocus;
    public String placeholder;
    public String valueFormat;

    public LabelPos labelPos;

    public String labelGap;

    public String labelSize;

    public HAlignType labelHAlign;

    public VAlignType labelVAlign;

    public String tipsErr;

    public String tipsOK;

    public HAlignType hAlign;

    public Integer autoexpand;

    public String tipsBinder;

    public String labelCaption;

    public InputProperties() {

    }

    public InputProperties(FieldFormConfig<InputFieldBean, ?> field) {
        FieldAggConfig fieldAggConfig = field.getAggConfig();
        FieldBean fieldBean = field.getFieldBean();

        this.init(field.getContainerBean());



        TipsBean tipsBean = field.getTipsBean();
        if (tipsBean != null) {
            OgnlUtil.setProperties((Map) JSON.toJSON(tipsBean), this, false, false);
        }

        LabelBean labelBean = field.getLabelBean();
        if (labelBean==null){
            labelBean =   AnnotationUtil.fillDefaultValue(Label.class, new LabelBean());
        }



        if (labelBean != null) {
            OgnlUtil.setProperties((Map) JSON.toJSON(labelBean), this, false, false);
        }


        ComponentBean widgetBean = field.getWidgetConfig();
        if (widgetBean != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(widgetBean), Map.class), this, false, false);
        }
        ComboBoxBean comboBoxBean = field.getComboConfig();
        if (comboBoxBean != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(comboBoxBean), Map.class), this, false, false);
        }

        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(field), Map.class), this, false, false);
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldAggConfig), Map.class), this, false, false);
        this.setId(field.getId());

        this.setName(field.getFieldname());
        this.setDesc(fieldAggConfig.getCaption());
        this.setLabelCaption(fieldAggConfig.getCaption());
        if (value != null) {
            this.setValue(value);
        }

        this.setReadonly(fieldAggConfig.getReadonly());
        this.setDisabled(fieldAggConfig.getDisabled());
        this.setRequired(fieldBean.getRequired());
        this.setDynCheck(fieldBean.getDynCheck());

    }


    public Boolean getHiddenBorder() {
        return hiddenBorder;
    }

    public void setHiddenBorder(Boolean hiddenBorder) {
        this.hiddenBorder = hiddenBorder;
    }

    public InputType getTexttype() {
        return texttype;
    }

    public void setTexttype(InputType texttype) {
        this.texttype = texttype;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Integer getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    public Boolean getMultiLines() {
        return multiLines;
    }

    public void setMultiLines(Boolean multiLines) {
        this.multiLines = multiLines;
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public Boolean getResizer() {
        return resizer;
    }

    public void setResizer(Boolean resizer) {
        this.resizer = resizer;
    }

    public Map<String, Object> getResizerProp() {
        return resizerProp;
    }

    public void setResizerProp(Map<String, Object> resizerProp) {
        this.resizerProp = resizerProp;
    }

    public Boolean getSelectOnFocus() {
        return selectOnFocus;
    }

    public void setSelectOnFocus(Boolean selectOnFocus) {
        this.selectOnFocus = selectOnFocus;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getValueFormat() {
        return valueFormat;
    }

    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
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


    public String getTipsErr() {
        return tipsErr;
    }

    public void setTipsErr(String tipsErr) {
        this.tipsErr = tipsErr;
    }

    public String getTipsOK() {
        return tipsOK;
    }

    public void setTipsOK(String tipsOK) {
        this.tipsOK = tipsOK;
    }


    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public Integer getAutoexpand() {
        return autoexpand;
    }

    public void setAutoexpand(Integer autoexpand) {
        this.autoexpand = autoexpand;
    }

    public String getTipsBinder() {
        return tipsBinder;
    }

    public void setTipsBinder(String tipsBinder) {
        this.tipsBinder = tipsBinder;
    }
}
