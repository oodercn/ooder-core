package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ListMenu;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.field.GalleryFieldAnnotation;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomGalleryViewBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomFieldGalleryComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.GalleryComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.GalleryProperties;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomFieldGalleryComponent.class,
        moduleType = ModuleViewType.GALLERYCONFIG,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.GALLERY
)
@AnnotationType(clazz = GalleryFieldAnnotation.class)
public class CustomGalleryFieldBean extends BaseWidgetBean<CustomGalleryViewBean, GalleryComponent> {

    String bgimg;
    String backgroundColor;
    Set<Class> customService;
    BorderType borderType;
    Class<Enum> enumClass;
    ListMenuBean listMenuBean;
    CustomListBean customListBean;

    @JSONField(serialize = false)
    List<GalleryItem> galleryItems;

    public CustomGalleryFieldBean() {

    }

    public CustomGalleryFieldBean(GalleryComponent galleryComponent) {
        update(null, galleryComponent);
    }

    public CustomGalleryFieldBean(ModuleComponent parentModuleComponent, GalleryComponent galleryComponent) {
        update(parentModuleComponent, galleryComponent);
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent parentModuleComponent, GalleryComponent component) {
        this.initWidget(parentModuleComponent,component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        GalleryProperties contentBlockProperties = component.getProperties();
        if (component.getChildren() != null && component.getChildren().size() > 0) {
            javaSrcBeans.addAll(super.update(parentModuleComponent, component));
        } else if (contentBlockProperties.getItems().size() > 0) {
            javaSrcBeans.addAll(super.update(parentModuleComponent, component));
            this.viewBean = genViewBean();
        }
        if (customListBean != null) {
            List<JavaSrcBean> srcBeanList = customListBean.update(parentModuleComponent, component);
            javaSrcBeans.addAll(srcBeanList);
        }
        return javaSrcBeans;

    }


    public CustomGalleryFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomGalleryViewBean) methodConfig.getView();
        init(methodConfig.getCustomMethodInfo(), AnnotationUtil.getAllAnnotations(methodConfig.getMethod(), true));
    }

    public CustomGalleryFieldBean(ESDField esdField, Set<Annotation> annotations) {
        init(esdField, annotations);

    }

    @Override
    public CustomGalleryViewBean createViewBean(ModuleComponent currModuleComponent, GalleryComponent component) {
        this.component = component;
        if (viewBean == null) {
            viewBean = new CustomGalleryViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);


        GalleryProperties galleryProperties = component.getProperties();
        Class<? extends Enum> enumClass = galleryProperties.getEnumClass();
        if (enumClass == null) {
            this.galleryItems = galleryProperties.getItems();
        }

        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }
        this.initProperties(galleryProperties);
        GalleryProperties properties = component.getProperties();
        this.bgimg = properties.getBgimg();
        this.backgroundColor = properties.getBackgroundColor() == null ? properties.getBgimg() : properties.getBackgroundColor();
        this.borderType = properties.getBorderType();

        return viewBean;
    }


    void init(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(GalleryFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof GalleryFieldAnnotation) {
                fillData((GalleryFieldAnnotation) annotation);
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


    void initProperties(GalleryProperties properties) {
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

    public CustomGalleryFieldBean fillData(GalleryFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
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
        return ComponentType.GALLERY;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    public List<GalleryItem> getGalleryItems() {
        return galleryItems;
    }

    public void setGalleryItems(List<GalleryItem> galleryItems) {
        this.galleryItems = galleryItems;
    }


}
