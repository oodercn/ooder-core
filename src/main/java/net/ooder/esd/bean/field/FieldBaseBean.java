package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.event.FieldEvent;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.FieldEventBean;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.FieldComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.Event;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;


public abstract class FieldBaseBean<M extends FieldComponent> implements FieldComponentBean<M> {

    String id;

    String xpath;

    Boolean selectable;

    ContainerBean containerBean;

    @JSONField(name = "CS")
    net.ooder.esd.tool.properties.CS CS;

    Map<?, Event> events = new HashMap();

    RightContextMenuBean contextMenuBean;

    CustomWidgetBean widgetBean;


    public List<JavaSrcBean> javaSrcBeans;


    public FieldBaseBean(M fieldComponent) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(fieldComponent.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
        this.xpath = fieldComponent.getPath();
        this.CS = fieldComponent.getCS();
        this.events = fieldComponent.getEvents();
        if (containerBean == null) {
            containerBean = new ContainerBean(fieldComponent);
        } else {
            containerBean.update(fieldComponent);
        }

    }


    public FieldBaseBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof RightContextMenu) {
                contextMenuBean = new RightContextMenuBean(this.getId(), (RightContextMenu) annotation);
            }
            if (annotation instanceof Widget) {
                this.widgetBean = new CustomWidgetBean((Widget) annotation);
            }

        }
        containerBean = new ContainerBean(annotations);
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (contextMenuBean != null) {
            annotationBeans.add(contextMenuBean);
        }
        if (widgetBean != null) {
            List<CustomBean> customBeans = widgetBean.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                if (!annotationBeans.contains(customBean)) {
                    annotationBeans.add(customBean);
                }
            }
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    public FieldBaseBean() {

    }

    public net.ooder.esd.tool.properties.CS getCS() {
        return CS;
    }

    public void setCS(net.ooder.esd.tool.properties.CS CS) {
        this.CS = CS;
    }

    public Map<?, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<?, Event> events) {
        this.events = events;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }


    public List<JavaSrcBean> getJavaSrcBeans() {
        if (javaSrcBeans == null) {
            javaSrcBeans = new ArrayList<>();
        }
        return javaSrcBeans;
    }

    public void setJavaSrcBeans(List<JavaSrcBean> javaSrcBeans) {
        this.javaSrcBeans = javaSrcBeans;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public CustomWidgetBean getWidgetBean() {
        return widgetBean;
    }

    public void setWidgetBean(CustomWidgetBean widgetBean) {
        this.widgetBean = widgetBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
