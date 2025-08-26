package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.CodeEditorFieldBean;
import net.ooder.esd.bean.field.base.JavaEditorFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class CodeEditorProperties extends FieldProperties {

    Boolean selectable;
    String frameTemplate;
    String frameStyle;
    String cmdList;
    String cmdFilter;
    String codeType;
    String labelGap;

    public CodeEditorProperties() {

    }

    public CodeEditorProperties(JavaEditorFieldBean editorFieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(editorFieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }

    public CodeEditorProperties(CodeEditorFieldBean editorFieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(editorFieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }


    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
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
}
