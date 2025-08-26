package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.ColorPickerFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class ColorPickerProperties extends FieldProperties {


    public Boolean barDisplay;
    public Boolean closeBtn;
    public Boolean advance;

    public ColorPickerProperties(){

    }
    public ColorPickerProperties(ColorPickerFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }



    public Boolean getBarDisplay() {
        return barDisplay;
    }

    public void setBarDisplay(Boolean barDisplay) {
        this.barDisplay = barDisplay;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Boolean getAdvance() {
        return advance;
    }

    public void setAdvance(Boolean advance) {
        this.advance = advance;
    }

}
