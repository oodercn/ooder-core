package net.ooder.esd.dsm.java;

import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.temp.JavaTemp;

public class JavaGenSource {
    JavaTemp javatemp;
    JavaRoot javaRoot;
    JavaSrcBean srcBean;
    String className;

    public JavaGenSource(){

    }

    public JavaGenSource( String className,JavaRoot javaRoot, JavaTemp javatemp, JavaSrcBean srcBean) {
        this.className=className;
        this.javaRoot = javaRoot;
        this.javatemp = javatemp;
        this.srcBean = srcBean;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public JavaTemp getJavatemp() {
        return javatemp;
    }

    public void setJavatemp(JavaTemp javatemp) {
        this.javatemp = javatemp;
    }

    public JavaRoot getJavaRoot() {
        return javaRoot;
    }

    public void setJavaRoot(JavaRoot javaRoot) {
        this.javaRoot = javaRoot;
    }

    public JavaSrcBean getSrcBean() {
        return srcBean;
    }

    public void setSrcBean(JavaSrcBean srcBean) {
        this.srcBean = srcBean;
    }

}
