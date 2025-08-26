package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.TimePickerFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class TimerProperties extends FieldProperties {
    Boolean autoStart;
    Integer integer;
    Integer Interval;

    public TimerProperties(TimePickerFieldBean bean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

    }


    public Integer getInterval() {
        return Interval;
    }

    public void setInterval(Integer interval) {
        Interval = interval;
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
