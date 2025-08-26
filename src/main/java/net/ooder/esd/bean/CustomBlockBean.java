package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.BlockAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.annotation.ui.SideBarStatusType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@AnnotationType(clazz = BlockAnnotation.class)
public class CustomBlockBean implements ComponentBean {

    BorderType borderType;
    Boolean resizer;
    Map<String, Object> resizerProp;
    String sideBarCaption;
    String sideBarType;
    SideBarStatusType sideBarStatus;
    String sideBarSize;
    String background;
    Dock dock;
    String xpath;
    CustomWidgetBean widgetBean;
    ContainerBean containerBean;


    public CustomBlockBean(Component component) {
        AnnotationUtil.fillDefaultValue(BlockAnnotation.class, this);
        this.update(component);

    }

    public void update(Component component) {
        if (component instanceof BlockComponent) {
            BlockProperties properties = (BlockProperties) component.getProperties();
            initProperties(properties);
        } else {
            Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
            OgnlUtil.setProperties(valueMap, this, false, false);
        }
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }
        this.xpath = component.getPath();


    }


    void initProperties(BlockProperties properties) {
        this.widgetBean = new CustomWidgetBean(properties);
        this.resizerProp = properties.getResizerProp();
        this.borderType = properties.getBorderType();
        this.resizer = properties.getResizer();
        this.sideBarCaption = properties.getSideBarCaption();
        this.sideBarType = properties.getSideBarType();
        this.background = properties.getBackground();
        this.dock = properties.getDock();
    }

    public CustomBlockBean fillData(BlockAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomBlockBean(Annotation... annotations) {
        AnnotationUtil.fillDefaultValue(BlockAnnotation.class, this);
        init(annotations);
    }

    void init(Annotation... annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof BlockAnnotation) {
                fillData((BlockAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
        containerBean = new ContainerBean(annotations);
    }

    public CustomBlockBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(BlockAnnotation.class, this);
        init(annotations.toArray(new Annotation[]{}));

    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLOCK;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();

        return classes;
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAllAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (containerBean != null) {
            annotationBeans.addAll(containerBean.getAnnotationBeans());
        }
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }

        return annotationBeans;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public CustomWidgetBean getWidgetBean() {
        return widgetBean;
    }

    public void setWidgetBean(CustomWidgetBean widgetBean) {
        this.widgetBean = widgetBean;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public CustomBlockBean() {

    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Boolean getResizer() {
        return resizer;
    }

    public void setResizer(Boolean resizer) {
        this.resizer = resizer;
    }

    public Map<String, Object> getResizerProp() {
        return resizerProp;
    }

    public void setResizerProp(Map<String, Object> resizerProp) {
        this.resizerProp = resizerProp;
    }

    public String getSideBarCaption() {
        return sideBarCaption;
    }

    public void setSideBarCaption(String sideBarCaption) {
        this.sideBarCaption = sideBarCaption;
    }

    public String getSideBarType() {
        return sideBarType;
    }

    public void setSideBarType(String sideBarType) {
        this.sideBarType = sideBarType;
    }

    public SideBarStatusType getSideBarStatus() {
        return sideBarStatus;
    }

    public void setSideBarStatus(SideBarStatusType sideBarStatus) {
        this.sideBarStatus = sideBarStatus;
    }

    public String getSideBarSize() {
        return sideBarSize;
    }

    public void setSideBarSize(String sideBarSize) {
        this.sideBarSize = sideBarSize;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
