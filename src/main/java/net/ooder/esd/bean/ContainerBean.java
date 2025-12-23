package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.Disabled;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esd.annotation.ContainerAnnotation;
import net.ooder.esd.annotation.DockAnnotation;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.field.AnimBinder;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = ContainerAnnotation.class)
public class ContainerBean implements CustomBean {

    public String panelBgClr;
    public String panelBgImg;
    public String panelBgImgPos;
    public AttachmentType panelBgImgAttachment;
    public Integer conLayoutColumns;
    public ThemesType sandboxTheme;
    public String formMethod;
    public String formTarget;
    public String formDataPath;
    public String formAction;
    public String formEnctype;
    public String className;
    public String dropKeys;
    public String iframeAutoLoad;
    public String ajaxAutoLoad;
    public String html;
    public Boolean selectable;
    public String dragKey;
    public OverflowType overflow;
    public String panelBgImgRepeat;
    public String set;
    public String autoTips;
    public SpaceUnitType spaceUnit;
    public Boolean defaultFocus;
    public HoverPopType hoverPopType;
    public String hoverPop;
    public String showEffects;
    public String hideEffects;

    public String tips;
    public Integer rotate;
    public CustomAnimType activeAnim;
    public DockBean dockBean;
    public DisabledBean disabledBean;
    public CustomUIBean uiBean;


    public ContainerBean() {

    }


    public ContainerBean(Component component) {
        update(component);
    }

    public void update(Component component) {
        this.init(component.getProperties());
        String json = JSON.toJSONString(component.getProperties());

        dockBean = JSONObject.parseObject(json, DockBean.class);
        disabledBean = JSONObject.parseObject(json, DisabledBean.class);
        uiBean = JSONObject.parseObject(json, CustomUIBean.class);

        if (className != null && component.getModuleComponent() != null && className.equals(component.getModuleComponent().getClassName())) {
            className = null;
        }

    }

    private void init(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public ContainerBean(ContainerAnnotation annotation) {
        fillData(annotation);
    }

    public ContainerBean fillData(ContainerAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public ContainerBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ContainerAnnotation.class, this);
        init(annotations.toArray(new Annotation[]{}));
    }

    public ContainerBean(Annotation... annotations) {
        AnnotationUtil.fillDefaultValue(ContainerAnnotation.class, this);
        init(annotations);

    }


    void init(Annotation... annotations) {

        for (Annotation annotation : annotations) {
            if (annotation instanceof ContainerAnnotation) {
                fillData((ContainerAnnotation) annotation);
            }
            if (annotation instanceof AnimBinder) {
                this.activeAnim = ((AnimBinder) annotation).customAnim();
            }
            if (annotation instanceof DockAnnotation) {
                this.dockBean = new DockBean(annotations);
            }
            if (annotation instanceof Disabled) {
                this.disabledBean = new DisabledBean((Disabled) annotation);
            }
            if (annotation instanceof UIAnnotation) {
                this.uiBean = new CustomUIBean((UIAnnotation) annotation);
            }
        }
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (dockBean != null) {
            List<CustomBean> customBeans = dockBean.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                if (!annotationBeans.contains(customBean)) {
                    annotationBeans.add(customBean);
                }
            }
        }
        if (disabledBean != null && !AnnotationUtil.getAnnotationMap(disabledBean).isEmpty()) {
            annotationBeans.add(disabledBean);
        }
        if (uiBean != null && !AnnotationUtil.getAnnotationMap(uiBean).isEmpty()) {
            annotationBeans.add(uiBean);
        }

        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }

        return annotationBeans;
    }


    public String getPanelBgClr() {
        return panelBgClr;
    }

    public void setPanelBgClr(String panelBgClr) {
        this.panelBgClr = panelBgClr;
    }

    public String getPanelBgImg() {
        return panelBgImg;
    }

    public void setPanelBgImg(String panelBgImg) {
        this.panelBgImg = panelBgImg;
    }

    public String getPanelBgImgPos() {
        return panelBgImgPos;
    }

    public void setPanelBgImgPos(String panelBgImgPos) {
        this.panelBgImgPos = panelBgImgPos;
    }

    public AttachmentType getPanelBgImgAttachment() {
        return panelBgImgAttachment;
    }

    public void setPanelBgImgAttachment(AttachmentType panelBgImgAttachment) {
        this.panelBgImgAttachment = panelBgImgAttachment;
    }

    public Integer getConLayoutColumns() {
        return conLayoutColumns;
    }

    public void setConLayoutColumns(Integer conLayoutColumns) {
        this.conLayoutColumns = conLayoutColumns;
    }

    public ThemesType getSandboxTheme() {
        return sandboxTheme;
    }

    public void setSandboxTheme(ThemesType sandboxTheme) {
        this.sandboxTheme = sandboxTheme;
    }

    public String getFormMethod() {
        return formMethod;
    }

    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    public String getFormTarget() {
        return formTarget;
    }

    public void setFormTarget(String formTarget) {
        this.formTarget = formTarget;
    }

    public String getFormDataPath() {
        return formDataPath;
    }

    public void setFormDataPath(String formDataPath) {
        this.formDataPath = formDataPath;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getFormEnctype() {
        return formEnctype;
    }

    public void setFormEnctype(String formEnctype) {
        this.formEnctype = formEnctype;
    }

    public String getDropKeys() {
        return dropKeys;
    }

    public void setDropKeys(String dropKeys) {
        this.dropKeys = dropKeys;
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

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        if (className != null && EsbBeanFactory.findClass(className) == null) {
            this.className = className;
        }
    }

    public String getDragKey() {
        return dragKey;
    }

    public void setDragKey(String dragKey) {
        this.dragKey = dragKey;
    }

    public String getPanelBgImgRepeat() {
        return panelBgImgRepeat;
    }

    public void setPanelBgImgRepeat(String panelBgImgRepeat) {
        this.panelBgImgRepeat = panelBgImgRepeat;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }


    public String getAutoTips() {
        return autoTips;
    }

    public void setAutoTips(String autoTips) {
        this.autoTips = autoTips;
    }


    public SpaceUnitType getSpaceUnit() {
        return spaceUnit;
    }

    public void setSpaceUnit(SpaceUnitType spaceUnit) {
        this.spaceUnit = spaceUnit;
    }

    public Boolean getDefaultFocus() {
        return defaultFocus;
    }

    public void setDefaultFocus(Boolean defaultFocus) {
        this.defaultFocus = defaultFocus;
    }

    public HoverPopType getHoverPopType() {
        return hoverPopType;
    }

    public void setHoverPopType(HoverPopType hoverPopType) {
        this.hoverPopType = hoverPopType;
    }

    public String getHoverPop() {
        return hoverPop;
    }

    public void setHoverPop(String hoverPop) {
        this.hoverPop = hoverPop;
    }

    public String getHideEffects() {
        return hideEffects;
    }

    public void setHideEffects(String hideEffects) {
        this.hideEffects = hideEffects;
    }

    public String getShowEffects() {
        return showEffects;
    }

    public void setShowEffects(String showEffects) {
        this.showEffects = showEffects;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getRotate() {
        return rotate;
    }

    public void setRotate(Integer rotate) {
        this.rotate = rotate;
    }

    public CustomAnimType getActiveAnim() {
        return activeAnim;
    }

    public void setActiveAnim(CustomAnimType activeAnim) {
        this.activeAnim = activeAnim;
    }

    public DockBean getDockBean() {
        if (dockBean == null) {
            dockBean = new DockBean();
            dockBean.initDefault();
        }
        return dockBean;
    }

    public void setDockBean(DockBean dockBean) {
        this.dockBean = dockBean;
    }

    public DisabledBean getDisabledBean() {
        if (disabledBean == null) {
            disabledBean = AnnotationUtil.fillDefaultValue(Disabled.class, new DisabledBean());
        }
        return disabledBean;
    }

    public void setDisabledBean(DisabledBean disabledBean) {
        this.disabledBean = disabledBean;
    }

    public CustomUIBean getUiBean() {
        if (uiBean == null) {
            uiBean = AnnotationUtil.fillDefaultValue(UIAnnotation.class, new CustomUIBean());
        }
        return uiBean;
    }

    public void setUiBean(CustomUIBean uiBean) {
        this.uiBean = uiBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}


