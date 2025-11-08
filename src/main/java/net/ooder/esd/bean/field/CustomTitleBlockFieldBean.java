package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ListMenu;
import net.ooder.esd.annotation.field.TitleBlockFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomTitleBlockViewBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomFieldTitleBlockComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.TitleBlockComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.TitleBlockProperties;
import net.ooder.esd.tool.properties.item.TitleBlockItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
@CustomClass(clazz = CustomFieldTitleBlockComponent.class,
        moduleType = ModuleViewType.TITLEBLOCKCONFIG,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.TITLEBLOCK
)
@AnnotationType(clazz = TitleBlockFieldAnnotation.class)
public class CustomTitleBlockFieldBean extends BaseWidgetBean<CustomTitleBlockViewBean, TitleBlockComponent> {

    String bgimg;
    String backgroundColor;
    Set<Class> customService;
    BorderType borderType;
    Class<Enum> enumClass;
    ListMenuBean listMenuBean;
    CustomListBean customListBean;
    @JSONField(serialize = false)
    List<TitleBlockItem> blockItems;


    public CustomTitleBlockFieldBean() {

    }

    public CustomTitleBlockFieldBean(ModuleComponent parentModuleComponent, TitleBlockComponent titleBlockComponent) {
        update(parentModuleComponent, titleBlockComponent);
    }


    public CustomTitleBlockFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomTitleBlockViewBean) methodConfig.getView();
        init(methodConfig.getCustomMethodInfo(), AnnotationUtil.getAllAnnotations(methodConfig.getMethod(),true));
        if (this.customListBean.getBindClass() != null) {
            viewBean.setBindService(this.customListBean.getBindClass());
        }
    }

    public CustomTitleBlockFieldBean(ESDField esdField, Set<Annotation> annotations) {
        init(esdField, annotations);
    }


    @Override
    public CustomTitleBlockViewBean createViewBean(ModuleComponent currModuleComponent, TitleBlockComponent component) {
        if (viewBean == null) {
            viewBean = new CustomTitleBlockViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent parentModuleComponent, TitleBlockComponent component) {
        List<JavaSrcBean> javaSrcBeans = super.update(parentModuleComponent, component);
        if (customListBean == null) {
            customListBean = new CustomListBean();
            customListBean.setBindClass(bindService);
        } else {
            List<JavaSrcBean> srcBeanList = customListBean.update(parentModuleComponent, component);
            javaSrcBeans.addAll(srcBeanList);
        }
        TitleBlockProperties titleBlockProperties = component.getProperties();
        Class<? extends Enum> enumClass = titleBlockProperties.getEnumClass();
        if (enumClass == null) {
            this.blockItems = titleBlockProperties.getItems();
        }
        this.initProperties(component.getProperties());
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }
        return javaSrcBeans;
    }

    void init(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(TitleBlockFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof TitleBlockFieldAnnotation) {
                fillData((TitleBlockFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                this.widgetBean = new CustomWidgetBean((Widget) annotation);
            }
            if (annotation instanceof ListMenu) {
                this.listMenuBean = new ListMenuBean((ListMenu) annotation);
            }
        }
        this.customListBean = new CustomListBean(esdField, annotations);
        this.containerBean = new ContainerBean(annotations);
    }


    void initProperties(TitleBlockProperties properties) {
        if (viewBean == null) {
            viewBean = new CustomTitleBlockViewBean();
        }
        viewBean.init(properties);
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
        widgetBean = new CustomWidgetBean(properties);
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        annotationBeans.addAll(this.getFieldAnnotationBeans());
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (customListBean != null) {
            classSet.addAll(customListBean.getOtherClass());
        }
        if (listMenuBean != null) {
            classSet.addAll(listMenuBean.getOtherClass());
        }
        return ClassUtility.checkBase(classSet);
    }

    public CustomTitleBlockFieldBean fillData(TitleBlockFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Set<Class> getCustomService() {
        return customService;
    }

    public void setCustomService(Set<Class> customService) {
        this.customService = customService;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public CustomListBean getCustomListBean() {
        return customListBean;
    }

    public void setCustomListBean(CustomListBean customListBean) {
        this.customListBean = customListBean;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public ListMenuBean getListMenuBean() {
        return listMenuBean;
    }

    public void setListMenuBean(ListMenuBean listMenuBean) {
        this.listMenuBean = listMenuBean;
    }


    public Class<Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLEBLOCK;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public List<CustomBean> getFieldAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getFieldAnnotationBeans();

        if (customListBean != null) {
            annotationBeans.addAll(customListBean.getAnnotationBeans());
        }
        if (listMenuBean != null) {
            annotationBeans.add(listMenuBean);
        }

        return annotationBeans;
    }


    public List<TitleBlockItem> getBlockItems() {
        return blockItems;
    }

    public void setBlockItems(List<TitleBlockItem> blockItems) {
        this.blockItems = blockItems;
    }

}
