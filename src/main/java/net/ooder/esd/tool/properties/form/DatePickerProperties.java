package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.DatePickerFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class DatePickerProperties extends FieldProperties {
    Boolean timeInput;

    Boolean closeBtn;
    String firstDayOfWee;

    String offDays;
    Boolean hideWeekLabels;
    String dateInputFormat;

    public DatePickerProperties(){

    }
    public DatePickerProperties(DatePickerFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

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

    public String getFirstDayOfWee() {
        return firstDayOfWee;
    }

    public void setFirstDayOfWee(String firstDayOfWee) {
        this.firstDayOfWee = firstDayOfWee;
    }

    public String getOffDays() {
        return offDays;
    }

    public void setOffDays(String offDays) {
        this.offDays = offDays;
    }

    public Boolean getHideWeekLabels() {
        return hideWeekLabels;
    }

    public void setHideWeekLabels(Boolean hideWeekLabels) {
        this.hideWeekLabels = hideWeekLabels;
    }

    public String getDateInputFormat() {
        return dateInputFormat;
    }

    public void setDateInputFormat(String dateInputFormat) {
        this.dateInputFormat = dateInputFormat;
    }


}
