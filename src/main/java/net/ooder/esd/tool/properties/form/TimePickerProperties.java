package net.ooder.esd.tool.properties.form;


import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.TimePickerFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class TimePickerProperties extends FieldProperties {

    public Boolean closeBtn;

    public String dateInputFormat;
    public Integer firstDayOfWeek;
    public Boolean hideWeekLabels;
    public Integer offDays;
    public Boolean timeInput;

    public TimePickerProperties() {

    }

    public TimePickerProperties(TimePickerFieldBean bean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }


    public String getDateInputFormat() {
        return dateInputFormat;
    }

    public void setDateInputFormat(String dateInputFormat) {
        this.dateInputFormat = dateInputFormat;
    }

    public Integer getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(Integer firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public Boolean getHideWeekLabels() {
        return hideWeekLabels;
    }

    public void setHideWeekLabels(Boolean hideWeekLabels) {
        this.hideWeekLabels = hideWeekLabels;
    }

    public Integer getOffDays() {
        return offDays;
    }

    public void setOffDays(Integer offDays) {
        this.offDays = offDays;
    }

    public Boolean getTimeInput() {
        return timeInput;
    }

    public void setTimeInput(Boolean timeInput) {
        this.timeInput = timeInput;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }
}
