package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.DesignViewAnnotation;
import net.ooder.esd.annotation.ViewStylesAnnotation;
import net.ooder.esd.tool.ModuleViewConfig;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModuleViewBean {
    public DesignViewBean designViewBean;
    public ModuleStyleBean viewStyles;

    public ModuleViewBean() {

    }


    public void update(ModuleViewConfig viewConfig) {
        if (viewConfig != null) {
            if (viewStyles == null) {
                this.viewStyles = new ModuleStyleBean(viewConfig.getViewStyles());
            } else {
                viewStyles.update(viewConfig.getViewStyles());
            }
            if (this.designViewBean == null) {
                this.designViewBean = new DesignViewBean(viewConfig.getDesignViewConf());
            } else {
                designViewBean.update(viewConfig.getDesignViewConf());
            }

        } else {
            viewStyles = new ModuleStyleBean();
            AnnotationUtil.fillDefaultValue(ViewStylesAnnotation.class, viewStyles);

            designViewBean = new DesignViewBean();
            AnnotationUtil.fillDefaultValue(DesignViewAnnotation.class, viewStyles);

        }
    }

    public ModuleViewBean(ModuleViewConfig viewConfig) {
        update(viewConfig);
    }

    public ModuleViewBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof ViewStylesAnnotation) {
                viewStyles = new ModuleStyleBean((ViewStylesAnnotation) annotation);
            }
            if (annotation instanceof DesignViewAnnotation) {
                designViewBean = new DesignViewBean((DesignViewAnnotation) annotation);
            }
        }
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (viewStyles != null && !AnnotationUtil.getAnnotationMap(viewStyles).isEmpty()) {
            annotationBeans.add(viewStyles);
        }
        if (designViewBean != null && !AnnotationUtil.getAnnotationMap(designViewBean).isEmpty()) {
            annotationBeans.add(designViewBean);
        }
        return annotationBeans;
    }

    public DesignViewBean getDesignViewBean() {
        return designViewBean;
    }

    public void setDesignViewBean(DesignViewBean designViewBean) {
        this.designViewBean = designViewBean;
    }

    public ModuleStyleBean getViewStyles() {
        return viewStyles;
    }

    public void setViewStyles(ModuleStyleBean viewStyles) {
        this.viewStyles = viewStyles;
    }
}
