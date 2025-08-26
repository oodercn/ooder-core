package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.Ref;
import net.ooder.annotation.RefType;
import net.ooder.esd.annotation.ViewType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = Ref.class)
public class CustomRefBean implements CustomBean {

    RefType ref;

    String pk = "";

    String fk = "";

    ViewType view;

    public CustomRefBean() {
        ref = RefType.NONE;
    }

    public CustomRefBean(Ref refAnnotation) {
        ref = refAnnotation.ref();
        pk = refAnnotation.pk();
        fk = refAnnotation.pk();
        view = refAnnotation.view();
    }


    public ViewType getView() {
        return view;
    }

    public void setView(ViewType view) {
        this.view = view;
    }

    public RefType getRef() {
        return ref;
    }

    public void setRef(RefType ref) {
        this.ref = ref;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getFk() {
        return fk;
    }

    public void setFk(String fk) {
        this.fk = fk;
    }

    public CustomRefBean fillData(Ref annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
