package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.EnumsClass;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = EnumsClass.class)
public class EnumsClassBean implements CustomBean {
    Class<? extends Enum> clazz;

    public EnumsClassBean() {

    }

    public EnumsClassBean(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Enum> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
