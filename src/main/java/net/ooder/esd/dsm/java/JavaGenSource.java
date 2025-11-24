package net.ooder.esd.dsm.java;

import net.ooder.common.JDSException;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.temp.JavaTemp;

public class JavaGenSource {

    String javaTempId;
    JavaSrcBean srcBean;
    String className;
    JavaRoot javaRoot;

    public JavaGenSource() {

    }

    public JavaGenSource(String className, JavaRoot javaRoot, JavaSrcBean srcBean) {
        this.className = className;
        this.srcBean = srcBean;
        this.javaTempId = srcBean.getJavaTempId();
        this.javaRoot = javaRoot;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public JavaTemp getJavatemp() {
        JavaTemp javaTemp = null;
        try {
            if (javaTempId != null) {
                javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaTemp;
    }


    public String getJavaTempId() {
        return javaTempId;
    }

    public void setJavaTempId(String javaTempId) {
        this.javaTempId = javaTempId;
    }

    public JavaRoot getJavaRoot() {
        return javaRoot;
    }


    public JavaSrcBean getSrcBean() {
        return srcBean;
    }

    public void setSrcBean(JavaSrcBean srcBean) {
        this.srcBean = srcBean;
    }

}
