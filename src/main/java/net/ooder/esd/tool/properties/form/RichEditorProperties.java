package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.LabelPos;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.RichEditorFieldBean;
import net.ooder.esd.bean.field.base.TextEditorFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class RichEditorProperties extends FieldProperties {

    Boolean selectable;
    Boolean enableBar;
    String frameTemplate;
    String frameStyle;
    String cmdList;
    String textType;
    String cmdFilter;
    String labelGap;
    Integer steps;

    String labelSize;
    LabelPos labelPos;
    String labelCaption;
    HAlignType labelHAlign;
    VAlignType labelVAlign;


    public RichEditorProperties() {

    }


  public RichEditorProperties(RichEditorFieldBean bean, ContainerBean containerBean) {
      OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }



    public RichEditorProperties(TextEditorFieldBean bean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }


//
//    public RichEditorProperties(TextEditorFieldBean textEditorFieldBean) {
//        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(textEditorFieldBean), Map.class), this, false, false);
//    }
//
//    public RichEditorProperties(RichEditorFieldBean editorFieldBean) {
//        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(editorFieldBean), Map.class), this, false, false);
//    }

    public Boolean getEnableBar() {
        return enableBar;
    }

    public void setEnableBar(Boolean enableBar) {
        this.enableBar = enableBar;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
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

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }


    public String getFrameTemplate() {
        return frameTemplate;
    }

    public void setFrameTemplate(String frameTemplate) {
        this.frameTemplate = frameTemplate;
    }

    public String getFrameStyle() {
        return frameStyle;
    }

    public void setFrameStyle(String frameStyle) {
        this.frameStyle = frameStyle;
    }

    public String getCmdList() {
        return cmdList;
    }

    public void setCmdList(String cmdList) {
        this.cmdList = cmdList;
    }

    public String getCmdFilter() {
        return cmdFilter;
    }

    public void setCmdFilter(String cmdFilter) {
        this.cmdFilter = cmdFilter;
    }

    public String getLabelGap() {
        return labelGap;
    }

    public void setLabelGap(String labelGap) {
        this.labelGap = labelGap;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }


}
