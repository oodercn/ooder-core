package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.MethodChinaName;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = MethodChinaName.class)
public class MethodChinaBean implements CustomBean {


    public String cname;

    public String imageClass;

    public Integer logLevel;

    public String returnStr;

    public Boolean display;

    public MethodChinaBean() {

    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public String getReturnStr() {
        return returnStr;
    }

    public void setReturnStr(String returnStr) {
        this.returnStr = returnStr;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public MethodChinaBean(MethodChinaName annotation) {
        fillData(annotation);
    }

    public MethodChinaBean fillData(MethodChinaName annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
