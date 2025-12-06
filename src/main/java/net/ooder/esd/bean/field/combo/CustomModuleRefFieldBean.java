package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ModuleRefFieldAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.field.CustomModuleEmbedFieldBean;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomModuleRefComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CustomClass(clazz = CustomModuleRefComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.MODULE
)
@AnnotationType(clazz = ModuleRefFieldAnnotation.class)
public class CustomModuleRefFieldBean implements FieldComponentBean<Component> {
    String src;

    Boolean dynLoad;

    AppendType append;

    EmbedType embed;

    Dock dock;

    Class bindClass;

    String xpath;

    @JSONField(serialize = false)
    CustomModuleBean moduleBean;

    public CustomModuleRefFieldBean() {

    }

    public CustomModuleRefFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ModuleRefFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ModuleRefFieldAnnotation) {
                fillData((ModuleRefFieldAnnotation) annotation);
            }
        }
    }


    public CustomModuleRefFieldBean(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ModuleRefFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ModuleRefFieldAnnotation) {
                fillData((ModuleRefFieldAnnotation) annotation);
            }
        }
        if (esdField instanceof CustomMethodInfo) {
            moduleBean = new CustomModuleBean((CustomMethodInfo) esdField);
        }
    }


    public CustomModuleRefFieldBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
        this.bindClass = moduleBean.getBindService();
        this.append = AppendType.append;
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, Component component) {

    }

    public CustomModuleRefFieldBean(CustomModuleEmbedFieldBean embedFieldBean) {
        this.src = embedFieldBean.getSrc();
        this.dynLoad = embedFieldBean.getDynLoad();
        this.append = embedFieldBean.getAppend();
        this.embed = embedFieldBean.getEmbed();
        this.bindClass = embedFieldBean.getBindClass();
        this.moduleBean = embedFieldBean.getModuleBean();

    }


    public CustomModuleRefFieldBean(ModuleComponent parentModuleComponent, ModuleComponent moduleComponent) {
        update(moduleComponent);

    }


    public CustomModuleRefFieldBean(ModuleComponent moduleComponent) {
        update(moduleComponent);
    }

    void update(ModuleComponent moduleComponent) {
        ModuleProperties moduleProperties = moduleComponent.getProperties();
        //  Map valueMap = JSON.parseObject(JSON.toJSONString(moduleProperties), Map.class);
        this.xpath = moduleComponent.getPath();
        // OgnlUtil.setProperties(valueMap, this, false, false);
        if (moduleComponent.getMethodAPIBean() != null) {
            this.bindClass = moduleComponent.getMethodAPIBean().getSourceClass().getCtClass();
            ModuleViewType moduleViewType = moduleComponent.getMethodAPIBean().getView().getModuleViewType();
            moduleProperties.setDsmProperties(new DSMProperties(moduleComponent.getMethodAPIBean(), moduleComponent.getProjectName()));
            moduleComponent.setModuleViewType(moduleViewType);
        }
        if (moduleBean != null) {
            moduleBean.update(moduleComponent);
        } else {
            this.moduleBean = new CustomModuleBean(moduleComponent);
            moduleBean.reBindMethod(moduleComponent.getMethodAPIBean());
        }

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (moduleBean != null) {
            if (moduleBean.getModuleComponent() != null && moduleBean.getModuleComponent().getMethodAPIBean() != null
                    && moduleBean.getModuleComponent().getMethodAPIBean().getView() != null) {
                classSet.addAll(moduleBean.getModuleComponent().getMethodAPIBean().getView().getOtherClass());
            }
            if (moduleBean.getBindService() != null && !moduleBean.getBindService().equals(Void.class)) {
                classSet.add(moduleBean.getBindService());
            }

        }
        return ClassUtility.checkBase(classSet);
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public CustomModuleRefFieldBean(ModuleRefFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomModuleBean getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public AppendType getAppend() {
        if (append == null) {
            append = AppendType.ref;
        }
        return append;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public EmbedType getEmbed() {
        return embed;
    }

    public void setEmbed(EmbedType embed) {
        this.embed = embed;
    }

    public void setAppend(AppendType append) {
        this.append = append;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.MODULE;
    }

    public CustomModuleRefFieldBean fillData(ModuleRefFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
