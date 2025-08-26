package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CallBackPathAnnotation;
import net.ooder.esd.annotation.ui.CallBackTypeEnum;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = CallBackPathAnnotation.class)
public class CallBackPathBean implements CustomBean {

    CallBackTypeEnum type;

    String paramsname;

    String path;

    public CallBackPathBean() {

    }

    public CallBackPathBean(CallBackPathAnnotation event) {
        type = event.type();
        paramsname = event.paramsname();
        path = event.path();
    }

    public CallBackTypeEnum getType() {
        return type;
    }

    public void setType(CallBackTypeEnum type) {
        this.type = type;
    }

    public String getParamsname() {
        return paramsname;
    }

    public void setParamsname(String paramsname) {
        this.paramsname = paramsname;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
