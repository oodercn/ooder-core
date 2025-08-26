package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.XPath;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = XPath.class)
public class XPathBean implements CustomBean {

    String path;

    public void update(XPathBean oodBean) {
        this.path = oodBean.getPath();

    }

    public XPathBean(XPathBean viewConf) {
        update(viewConf);
    }

    public XPathBean(String path) {
        this.path = path;
    }

    public XPathBean() {
        AnnotationUtil.fillDefaultValue(XPath.class, this);
    }

    public XPathBean(XPath annotation) {
        this.fillData(annotation);

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public XPathBean fillData(XPath annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
