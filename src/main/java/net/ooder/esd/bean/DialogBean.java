package net.ooder.esd.bean;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.DiaStatusType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.component.DialogComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.DialogProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.Locked;
import net.ooder.annotation.Modal;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@AnnotationType(clazz = DialogAnnotation.class)
public class DialogBean implements ComponentBean<DialogComponent> {

    String xpath;

    String methodName;

    String sourceClassName;

    String viewInstId;

    String domainId;

    String id;

    String caption;

    String tips;

    String left;

    String top;

    String showEffects;

    String hideEffects;

    String iframeAutoLoad;

    String ajaxAutoLoad;

    String name;

    String imageClass;

    Boolean mdia = false;

    Boolean cmd;

    Boolean movable;

    Boolean resizer;

    Boolean modal;

    Boolean locked;

    String fromRegion;

    String minWidth;

    String minHeight;

    String width;

    String height;

    String initPos;

    Dock dock = Dock.none;

    DiaStatusType status;

    Boolean isDio = false;

    CustomWidgetBean widgetBean;

    ContainerBean containerBean;

    DialogBtnBean dialogBtnBean;


    public DialogBean() {


    }

    public DialogBean(DialogComponent dialogComponent) {
        AnnotationUtil.fillDefaultValue(DialogAnnotation.class, this);
        update(dialogComponent);
    }


    public DialogBean(String viewInstId, String domainId, String sourceClassName, String methodName) {
        AnnotationUtil.fillDefaultValue(DialogAnnotation.class, this);
        this.sourceClassName = sourceClassName;
        this.viewInstId = viewInstId;
        this.domainId = domainId;
        this.methodName = methodName;
        this.id = methodName;
    }

    public void update(DialogComponent dialogComponent) {
        this.xpath = dialogComponent.getPath();
        DialogProperties dialogProperties = (DialogProperties) dialogComponent.getProperties();
        if (containerBean == null) {
            containerBean = new ContainerBean(dialogComponent);
        } else {
            containerBean.update(dialogComponent);
        }

        if (dialogBtnBean == null) {
            dialogBtnBean = new DialogBtnBean(dialogComponent);
        } else {
            dialogBtnBean.update(dialogComponent);
        }
        Map valueMap = AnnotationUtil.getDefaultAnnMap(DialogAnnotation.class);
        valueMap.putAll(JSON.parseObject(JSON.toJSONString(dialogProperties), Map.class));
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    @Override
    public String getXpath() {
        return xpath;
    }


    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String getInitPos() {
        return initPos;
    }

    public void setInitPos(String initPos) {
        this.initPos = initPos;
    }

    public DiaStatusType getStatus() {
        return status;
    }

    public void setStatus(DiaStatusType status) {
        this.status = status;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAllAnnotationBeans() {

        List<CustomBean> annotationBeans = new ArrayList<>();

        if (widgetBean != null && !AnnotationUtil.getAnnotationMap(widgetBean).isEmpty()) {
            annotationBeans.addAll(widgetBean.getAnnotationBeans());
        }

        if (dialogBtnBean != null && !AnnotationUtil.getAnnotationMap(dialogBtnBean).isEmpty()) {
            annotationBeans.add(dialogBtnBean);

        }
        if (containerBean != null && !AnnotationUtil.getAnnotationMap(containerBean).isEmpty()) {
            containerBean.setUiBean(null);
            annotationBeans.addAll(containerBean.getAnnotationBeans());

        }
        if (annotationBeans.size() > 0 || !AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }

        return annotationBeans;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.DIALOG;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {

        List<CustomBean> annotationBeans = new ArrayList<>();
        if (dialogBtnBean != null && !AnnotationUtil.getAnnotationMap(dialogBtnBean).isEmpty()) {
            annotationBeans.add(dialogBtnBean);
        }

        if (annotationBeans.size() > 0 || !AnnotationUtil.getAnnotationMap(this).isEmpty()) {
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


    public DialogBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(DialogAnnotation.class, this);
        this.containerBean = new ContainerBean(annotations);

        for (Annotation annotation : annotations) {
            if (annotation instanceof DialogAnnotation) {
                fillData((DialogAnnotation) annotation);

                isDio = true;
            }
            if (annotation instanceof MDialogAnnotation) {
                fillData((MDialogAnnotation) annotation);
                isDio = true;
                mdia = true;
            }
            if (annotation instanceof Widget) {
                this.widgetBean = new CustomWidgetBean((Widget) annotation);
                isDio = true;
            }


            if (annotation instanceof DialogBtnAnnotation) {
                dialogBtnBean = new DialogBtnBean((DialogBtnAnnotation) annotation);
                isDio = true;
            }
            if (containerBean.getUiBean() != null) {
                containerBean.getUiBean().setDock(this.getDock());
            }
        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof Modal) {
                this.modal = true;
            } else if (annotation instanceof Locked) {
                this.locked = true;
            }
        }

    }

    public ContainerBean getContainerBean() {
        if (this.containerBean == null) {
            containerBean = AnnotationUtil.fillDefaultValue(ContainerAnnotation.class, new ContainerBean());
        }
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public CustomWidgetBean getWidgetBean() {
        if (this.widgetBean == null) {
            widgetBean = AnnotationUtil.fillDefaultValue(Widget.class, new CustomWidgetBean());
        }
        return widgetBean;
    }

    public void setWidgetBean(CustomWidgetBean widgetBean) {
        this.widgetBean = widgetBean;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public Boolean getMdia() {
        return mdia;
    }

    public void setMdia(Boolean mdia) {
        this.mdia = mdia;
    }

    private void fillData(DialogAnnotation moduleAnnotation) {
        AnnotationUtil.fillBean(moduleAnnotation, this);
    }

    private void fillData(MDialogAnnotation moduleAnnotation) {
        AnnotationUtil.fillBean(moduleAnnotation, this);
    }

    public DialogBtnBean getDialogBtnBean() {
        if (this.dialogBtnBean == null) {
            dialogBtnBean = AnnotationUtil.fillDefaultValue(DialogBtnAnnotation.class, new DialogBtnBean());
        }

        return dialogBtnBean;
    }

    public void setDialogBtnBean(DialogBtnBean dialogBtnBean) {
        this.dialogBtnBean = dialogBtnBean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getViewInstId() {
        return viewInstId;
    }

    public void setViewInstId(String viewInstId) {
        this.viewInstId = viewInstId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
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

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getShowEffects() {
        return showEffects;
    }

    public void setShowEffects(String showEffects) {
        this.showEffects = showEffects;
    }

    public String getHideEffects() {
        return hideEffects;
    }

    public void setHideEffects(String hideEffects) {
        this.hideEffects = hideEffects;
    }

    public String getIframeAutoLoad() {
        return iframeAutoLoad;
    }

    public void setIframeAutoLoad(String iframeAutoLoad) {
        this.iframeAutoLoad = iframeAutoLoad;
    }

    public String getAjaxAutoLoad() {
        return ajaxAutoLoad;
    }

    public void setAjaxAutoLoad(String ajaxAutoLoad) {
        this.ajaxAutoLoad = ajaxAutoLoad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public Boolean getCmd() {
        return cmd;
    }

    public void setCmd(Boolean cmd) {
        this.cmd = cmd;
    }

    public Boolean getMovable() {
        return movable;
    }

    public void setMovable(Boolean movable) {
        this.movable = movable;
    }


    public Boolean getResizer() {
        return resizer;
    }

    public void setResizer(Boolean resizer) {
        this.resizer = resizer;
    }


    public Boolean getModal() {
        return modal;
    }

    public void setModal(Boolean modal) {
        this.modal = modal;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getFromRegion() {
        return fromRegion;
    }

    public void setFromRegion(String fromRegion) {
        this.fromRegion = fromRegion;
    }

    public String getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(String minWidth) {
        this.minWidth = minWidth;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Boolean getDio() {
        return isDio;
    }

    public void setDio(Boolean dio) {
        isDio = dio;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
