package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.CustomHotKeyEvent;
import net.ooder.esd.annotation.event.FieldEvent;
import net.ooder.esd.annotation.event.FieldHotKeyEvent;
import net.ooder.esd.annotation.ui.*;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.List;
import java.util.Set;

@AnnotationType(clazz = CustomAnnotation.class)
public class CustomAnnotationBean implements CustomBean {


    String id;


    String caption;

    Boolean readonly;

    Boolean disabled;

    Dock dock;

    Boolean hidden;

    Boolean uid;

    Boolean captionField;

    Boolean pid;

    Integer index;

    String imageClass;

    ComponentType componentType;

    Class serviceClass;

    Class customContextMenuService;

    List<FieldEvent> event;

    List<ComponentType> bindTypes;


    public CustomAnnotationBean() {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }


    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getUid() {
        return uid;
    }

    public void setUid(Boolean uid) {
        this.uid = uid;
    }

    public Boolean getCaptionValue() {
        return captionField;
    }

    public void setCaptionValue(Boolean captionValue) {
        this.captionField = captionValue;
    }

    public Boolean getPid() {
        return pid;
    }

    public void setPid(Boolean pid) {
        this.pid = pid;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }


    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Class getCustomContextMenuService() {
        return customContextMenuService;
    }

    public void setCustomContextMenuService(Class customContextMenuService) {
        this.customContextMenuService = customContextMenuService;
    }
    public List<FieldEvent> getEvent() {
        return event;
    }

    public void setEvent(List<FieldEvent> event) {
        this.event = event;
    }

    public List<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setBindTypes(List<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    public CustomAnnotationBean(CustomAnnotation annotation) {
        fillData(annotation);
    }

    public CustomAnnotationBean fillData(CustomAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
