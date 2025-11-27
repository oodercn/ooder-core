package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ListMenu;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.field.ContentBlockFieldAnnotation;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomContentBlockViewBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomFieldContentBlockComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ContentBlockComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ContentBlockProperties;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.item.ContentBlockItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.tool.properties.list.AbsUIListProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomFieldContentBlockComponent.class,
        moduleType = ModuleViewType.CONTENTBLOCKCONFIG,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.CONTENTBLOCK
)
@AnnotationType(clazz = ContentBlockFieldAnnotation.class)
public class CustomContentBlockFieldBean extends BaseWidgetBean<CustomContentBlockViewBean, ContentBlockComponent> {

    String bgimg;
    String backgroundColor;
    BorderType borderType;
    Set<Class> customService;
    Class<Enum> enumClass;
    ListMenuBean listMenuBean;
    CustomListBean customListBean;
    @JSONField(serialize = false)
    List<ContentBlockItem> blockItems;

    public CustomContentBlockFieldBean() {

    }


    public CustomContentBlockFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomContentBlockViewBean) methodConfig.getView();
        init(methodConfig.getCustomMethodInfo(), AnnotationUtil.getAllAnnotations(methodConfig.getMethod(), true));
        if (this.customListBean.getBindClass() != null) {
            viewBean.setBindService(this.customListBean.getBindClass());
        }
    }

    public CustomContentBlockFieldBean(ESDField esdField, Set<Annotation> annotations) {
        init(esdField, annotations);

    }


    public CustomContentBlockFieldBean(ModuleComponent parentModuleComponent, ContentBlockComponent contentBlockComponent) {
        update(parentModuleComponent, contentBlockComponent);
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent parentModuleComponent, ContentBlockComponent component) {
        this.initWidget(parentModuleComponent,component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();

        AbsListProperties listProperties = component.getProperties();

        if (component.getChildren() != null && component.getChildren().size() > 0) {
            javaSrcBeans.addAll(super.update(parentModuleComponent, component));
        } else if (listProperties.getItems().size() > 0) {
            javaSrcBeans.addAll(super.update(parentModuleComponent, component));
            this.viewBean = genViewBean();
        }
        if (customListBean != null) {
            List<JavaSrcBean> srcBeanList = customListBean.update(parentModuleComponent, component);
            javaSrcBeans.addAll(srcBeanList);
        }

        return javaSrcBeans;

    }


    @Override
    public CustomContentBlockViewBean createViewBean(ModuleComponent currModuleComponent, ContentBlockComponent component) {
        if (viewBean == null) {
            viewBean = new CustomContentBlockViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        if (customListBean == null) {
            customListBean = new CustomListBean();
            customListBean.setBindClass(viewBean.getBindService());
        }
        ContentBlockProperties contentBlockProperties = component.getProperties();
        Class<? extends Enum> enumClass = contentBlockProperties.getEnumClass();
        if (enumClass == null) {
            this.blockItems = contentBlockProperties.getItems();
        }
        this.initProperties(component.getProperties());
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }

    void init(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ContentBlockFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ContentBlockFieldAnnotation) {
                fillData((ContentBlockFieldAnnotation) annotation);
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


    void initProperties(ContentBlockProperties properties) {
        if (viewBean == null) {
            viewBean = new CustomContentBlockViewBean();
        }
        viewBean.init(properties);
        this.bgimg = properties.getBgimg();
        this.borderType = properties.getBorderType();
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

    public CustomContentBlockFieldBean fillData(ContentBlockFieldAnnotation annotation) {
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
        return ComponentType.CONTENTBLOCK;
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


    public List<ContentBlockItem> getBlockItems() {
        return blockItems;
    }

    public void setBlockItems(List<ContentBlockItem> blockItems) {
        this.blockItems = blockItems;
    }


}
