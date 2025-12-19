package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.util.ClassUtility;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.annotation.field.ModuleEmbedFieldAnnotation;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModulePlaceHolder;
import net.ooder.esd.custom.component.form.field.CustomFieldModuleComponent;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.TimerComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomFieldModuleComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.MODLUEPLACEHOLDER
)
@AnnotationType(clazz = ModuleEmbedFieldAnnotation.class)
public class CustomModuleEmbedFieldBean  implements FieldComponentBean<ModuleComponent> {
    String src;

    String xpath;

    Boolean dynLoad;

    AppendType append;

    EmbedType embed;

    Class bindClass;

    CustomModuleBean moduleBean;

    public CustomModuleEmbedFieldBean() {

    }


    public CustomModuleEmbedFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ModuleEmbedFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ModuleEmbedFieldAnnotation) {
                fillData((ModuleEmbedFieldAnnotation) annotation);
            }
        }

    }

    public CustomModuleEmbedFieldBean(ModuleComponent parentModuleComponent, ModuleComponent moduleComponent) {
        update(moduleComponent);

    }

    public CustomModuleEmbedFieldBean(ModuleComponent parentModuleComponent, ModulePlaceHolder moduleComponent) {
        //  update(moduleComponent);
        this.
    }

    void update(ModuleComponent moduleComponent) {
        ModuleProperties moduleProperties = moduleComponent.getProperties();
        Map valueMap = JSON.parseObject(JSON.toJSONString(moduleProperties), Map.class);
        this.xpath = moduleComponent.getPath();
        OgnlUtil.setProperties(valueMap, this, false, false);
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

    public CustomModuleEmbedFieldBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
        this.bindClass = moduleBean.getBindService();
        this.append = AppendType.ref;

    }


    public CustomModuleEmbedFieldBean(CustomModuleEmbedFieldBean embedFieldBean) {
        this.src = embedFieldBean.getSrc();
        this.dynLoad = embedFieldBean.getDynLoad();
        this.append = embedFieldBean.getAppend();
        this.embed = embedFieldBean.getEmbed();
        this.bindClass = embedFieldBean.getBindClass();
        this.moduleBean = embedFieldBean.getModuleBean();

    }


    public CustomModuleEmbedFieldBean(ModuleComponent moduleComponent) {
        ModuleProperties moduleProperties = moduleComponent.getProperties();
        Map valueMap = JSON.parseObject(JSON.toJSONString(moduleProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
        MethodConfig methodConfig = moduleComponent.getMethodAPIBean();
        this.moduleBean = new CustomModuleBean(moduleComponent);
        this.xpath = moduleComponent.getPath();
        moduleBean.reBindMethod(methodConfig);
    }

    public CustomModuleEmbedFieldBean(ModulePlaceHolder modulePlaceHolder) {
        ModuleProperties moduleProperties = modulePlaceHolder.getProperties();
        Map valueMap = JSON.parseObject(JSON.toJSONString(moduleProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

        this.moduleBean = new CustomModuleBean(moduleProperties);
    }

    public CustomModuleEmbedFieldBean(ModuleProperties moduleProperties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(moduleProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
        DSMProperties dsmProperties = moduleProperties.getDsmProperties();
        String dsmClassName = dsmProperties.getSourceClassName();
        try {
            bindClass = ClassUtility.loadClass(dsmClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.moduleBean = new CustomModuleBean(moduleProperties);
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (moduleBean != null) {
            annotationBeans.addAll(moduleBean.getAnnotationBeans());
        }
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    @Override
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (moduleBean != null) {
            if (moduleBean.getModuleComponent() != null && moduleBean.getModuleComponent().getMethodAPIBean() != null
                    && moduleBean.getModuleComponent().getMethodAPIBean().getView() != null) {
                classSet.addAll(moduleBean.getModuleComponent().getMethodAPIBean().getView().getOtherClass());
            }
            if (moduleBean.getBindService() != null) {
                classSet.add(moduleBean.getBindService());
            }

        }
        return ClassUtility.checkBase(classSet);
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public CustomModuleEmbedFieldBean(ModuleEmbedFieldAnnotation annotation) {
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
        return ComponentType.MODLUEPLACEHOLDER;
    }

    public CustomModuleEmbedFieldBean fillData(ModuleEmbedFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, ModuleComponent component) {

    }
}
