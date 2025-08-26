package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.SVGPaperFormAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomSVGPaperFieldBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;
import net.ooder.esd.util.XUIUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = SVGPaperFormAnnotation.class)
public class CustomSVGPaperViewBean extends BaseFormViewBean {
    ModuleViewType moduleViewType = ModuleViewType.SVGPAPERCONFIG;

    CustomSVGPaperFieldBean svgPaperFieldBean;

    public CustomSVGPaperViewBean() {
    }


    public CustomSVGPaperViewBean(ModuleComponent moduleComponent) {
        super();
        this.updateModule(moduleComponent);

    }


    @Override
    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(SVGPaperFormAnnotation.class, this);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        updateBaseModule(moduleComponent);
        initMenuBar();
        SVGPaperComponent currComponent = (SVGPaperComponent) moduleComponent.getCurrComponent();
        this.name = XUIUtil.formatJavaName(currComponent.getAlias(), false);
        if (containerBean == null) {
            containerBean = new ContainerBean(currComponent);
        } else {
            containerBean.update(currComponent);
        }
        this.init(currComponent.getProperties());
        svgPaperFieldBean = new CustomSVGPaperFieldBean(currComponent.getProperties());
        List<Component> components = this.cloneComponentList(currComponent.getChildren());
        List<FieldFormConfig> fieldFormConfigs = genChildComponent(moduleComponent, components);
        for (FieldFormConfig fieldFormConfig : fieldFormConfigs) {
            this.fieldConfigMap.put(fieldFormConfig.getFieldname(), fieldFormConfig);
            javaSrcBeans.addAll(fieldFormConfig.getAllJavaSrcBeans());
        }
        addChildJavaSrc(javaSrcBeans);
        try {
            DSMFactory.getInstance().saveCustomViewEntity(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaSrcBeans;
    }

    public CustomSVGPaperViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        SVGPaperFormAnnotation paperAnnotation = AnnotationUtil.getClassAnnotation(clazz, SVGPaperFormAnnotation.class);
        if (paperAnnotation != null) {
            AnnotationUtil.fillBean(paperAnnotation, this);
        }
        this.methodName = methodAPIBean.getMethodName();
        Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(methodAPIBean.getMethod(),true);
        svgPaperFieldBean = new CustomSVGPaperFieldBean(annotations);

        try {
            this.initHiddenField(this.getViewClassName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public CustomSVGPaperFieldBean getSvgPaperFieldBean() {
        return svgPaperFieldBean;
    }

    public void setSvgPaperFieldBean(CustomSVGPaperFieldBean svgPaperFieldBean) {
        this.svgPaperFieldBean = svgPaperFieldBean;
    }

    public void init(SVGPaperProperties svgPaperProperties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(svgPaperProperties), Map.class), this, false, false);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGPAPER;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }
}
