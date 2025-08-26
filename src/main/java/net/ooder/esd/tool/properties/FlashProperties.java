package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.DatePickerFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class FlashProperties extends FieldProperties {


    public Boolean selectable;

    public Boolean cover;
    public String src;
    public Map<String, Object> parameters;
    public Map<String, Object> flashvars;


    public FlashProperties(){

    }

    public FlashProperties(DatePickerFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

    }


    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }


    public Boolean isCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getFlashvars() {
        return flashvars;
    }

    public void setFlashvars(Map<String, Object> flashvars) {
        this.flashvars = flashvars;
    }
}
