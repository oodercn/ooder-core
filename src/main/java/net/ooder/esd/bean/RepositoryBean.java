package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.RepositoryAnnotation;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = RepositoryAnnotation.class)
public class RepositoryBean implements CustomBean {


    String imageClass;

    Class entityClass;

    Class sourceClass;

    public RepositoryBean() {

    }


    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public Class getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(Class sourceClass) {
        this.sourceClass = sourceClass;
    }

    public RepositoryBean(RepositoryAnnotation annotation) {
        fillData(annotation);
    }

    public RepositoryBean fillData(RepositoryAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
