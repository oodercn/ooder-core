package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ListMenu;
import net.ooder.esd.annotation.field.OpinionFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomOpinionViewBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.tool.component.OpinionComponent;
import net.ooder.esd.custom.component.form.field.CustomOpinionFieldComponent;
import net.ooder.esd.custom.properties.OpinionProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.item.OpinionItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
@CustomClass(clazz = CustomOpinionFieldComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.OPINIONCONFIG,
        componentType = ComponentType.OPINION
)
@AnnotationType(clazz = OpinionFieldAnnotation.class)
public class CustomOpinionFieldBean extends BaseWidgetBean<CustomOpinionViewBean, OpinionComponent> {

    String bgimg;
    String backgroundColor;
    Set<Class> customService;
    BorderType borderType;
    Class<Enum> enumClass;
    ListMenuBean listMenuBean;
    CustomListBean customListBean;

    @JSONField(serialize = false)
    List<OpinionItem> opinionItems;

    public CustomOpinionFieldBean() {

    }

    public CustomOpinionFieldBean(OpinionComponent opinionComponent) {
        update(null, opinionComponent);
    }

    public CustomOpinionFieldBean(ModuleComponent parentModuleComponent, OpinionComponent opinionComponent) {
        update(parentModuleComponent, opinionComponent);
    }


    public CustomOpinionFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomOpinionViewBean) methodConfig.getView();
        init(methodConfig.getCustomMethodInfo(), AnnotationUtil.getAllAnnotations(methodConfig.getMethod(),true));
    }

    public CustomOpinionFieldBean(ESDField esdField, Set<Annotation> annotations) {
        init(esdField, annotations);

    }


    @Override
    public CustomOpinionViewBean createViewBean(ModuleComponent currModuleComponent, OpinionComponent component) {
        if (viewBean == null) {
            viewBean = new CustomOpinionViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        OpinionProperties OpinionProperties = component.getProperties();
        Class<? extends Enum> enumClass = OpinionProperties.getEnumClass();
        if (enumClass == null) {
            this.opinionItems = OpinionProperties.getItems();
        }
        this.initProperties(OpinionProperties);
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }

        return viewBean;
    }

    void init(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(OpinionFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof OpinionFieldAnnotation) {
                fillData((OpinionFieldAnnotation) annotation);
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


    void initProperties(OpinionProperties properties) {
        if (viewBean == null) {
            viewBean = new CustomOpinionViewBean();
        }
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, viewBean, false, false);
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

    public CustomOpinionFieldBean fillData(OpinionFieldAnnotation annotation) {
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
        return ComponentType.OPINION;
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

    public List<OpinionItem> getOpinionItems() {
        return opinionItems;
    }

    public void setOpinionItems(List<OpinionItem> opinionItems) {
        this.opinionItems = opinionItems;
    }
}
