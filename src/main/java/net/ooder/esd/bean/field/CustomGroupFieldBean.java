package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.AppendType;
import net.ooder.esd.annotation.ui.EmbedType;
import net.ooder.esd.annotation.field.GroupFieldAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.form.field.CustomFieldGroupComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.GroupComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.GroupProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomFieldGroupComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.GROUPCONFIG,
        componentType = ComponentType.GROUP
)
@AnnotationType(clazz = GroupFieldAnnotation.class)
public class CustomGroupFieldBean implements FieldComponentBean<GroupComponent> {
    Boolean iniFold;

    Boolean lazyAppend;

    Boolean autoReload;

    Boolean dynDestory;

    Boolean lazyLoad;

    Class customService;

    String groupName;

    Boolean displayBar;

    Boolean optBtn;

    Boolean toggleBtn;

    Boolean refreshBtn;

    Boolean infoBtn;

    Boolean closeBtn;

    String caption;

    String html;

    ImagePos imagePos;

    String imageBgSize;

    String iconFontCode;

    BorderType borderType;

    Boolean noFrame;

    HAlignType hAlign;

    ToggleIconType toggleIcon;

    String src;

    Dock dock;

    Boolean dynLoad;

    AppendType append;

    EmbedType embed;

    Class bindClass;

    String xpath;

    String euClassName;

    @JSONField(serialize = false)
    CustomModuleBean moduleBean;


    public CustomGroupFieldBean() {

    }

    public CustomGroupFieldBean(GroupProperties bean) {
        initProperties(bean);
    }


    public CustomGroupFieldBean(ModuleComponent parentModuleComponent, GroupComponent component) {
        this.update(parentModuleComponent, component);
    }

    public CustomGroupFieldBean(GroupComponent component) {
        this.update(null, component);
    }

    public CustomGroupFieldBean fillData(GroupFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    void init(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof GroupFieldAnnotation) {
                fillData((GroupFieldAnnotation) annotation);
            }
        }
    }


    public CustomGroupFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(GroupFieldAnnotation.class, this);
        init(annotations);

    }

    void initProperties(GroupProperties properties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(properties), Map.class), this, false, false);

    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.GROUP;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (moduleBean != null) {
            if (moduleBean.getModuleComponent() != null && moduleBean.getModuleComponent().getMethodAPIBean() != null
                    && moduleBean.getModuleComponent().getMethodAPIBean().getView() != null) {
                classSet.addAll(moduleBean.getModuleComponent().getMethodAPIBean().getView().getOtherClass());
            }
            if (moduleBean.getBindService() != null && !moduleBean.getBindService().equals(Void.class)) {
                classSet.add(moduleBean.getBindService());
            }

        }
        return ClassUtility.checkBase(classSet);
    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public Boolean getLazyAppend() {
        return lazyAppend;
    }

    public void setLazyAppend(Boolean lazyAppend) {
        this.lazyAppend = lazyAppend;
    }

    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }


    public Class getCustomService() {
        return customService;
    }

    public void setCustomService(Class customService) {
        this.customService = customService;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getDisplayBar() {
        return displayBar;
    }

    public void setDisplayBar(Boolean displayBar) {
        this.displayBar = displayBar;
    }

    public Boolean getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(Boolean optBtn) {
        this.optBtn = optBtn;
    }

    public Boolean getToggleBtn() {
        return toggleBtn;
    }

    public void setToggleBtn(Boolean toggleBtn) {
        this.toggleBtn = toggleBtn;
    }

    public Boolean getRefreshBtn() {
        return refreshBtn;
    }

    public void setRefreshBtn(Boolean refreshBtn) {
        this.refreshBtn = refreshBtn;
    }

    public Boolean getInfoBtn() {
        return infoBtn;
    }

    public void setInfoBtn(Boolean infoBtn) {
        this.infoBtn = infoBtn;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public ImagePos getImagePos() {
        return imagePos;
    }

    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public Boolean getNoFrame() {
        return noFrame;
    }

    public void setNoFrame(Boolean noFrame) {
        this.noFrame = noFrame;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public ToggleIconType getToggleIcon() {
        return toggleIcon;
    }

    public void setToggleIcon(ToggleIconType toggleIcon) {
        this.toggleIcon = toggleIcon;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean getDynLoad() {
        return dynLoad;
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

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public void setAppend(AppendType append) {
        this.append = append;
    }

    public EmbedType getEmbed() {
        return embed;
    }

    public void setEmbed(EmbedType embed) {
        this.embed = embed;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }


    public CustomModuleBean getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, GroupComponent component) {
        this.initProperties(component.getProperties());
        this.euClassName = component.getProperties().getEuClassName();
        try {
            if (euClassName != null) {
                MethodConfig methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euClassName, moduleComponent.getProjectName());
                if (methodConfig != null) {
                    bindClass = methodConfig.getSourceClass().getCtClass();
                }else{
                 EUModule euModule= ESDFacrory.getAdminESDClient().getModule(euClassName,moduleComponent.getProjectName());
                 if (euModule!=null){
                    String serviceClass= euModule.getSourceClassName();
                     try {
                         bindClass=ClassUtility.loadClass(serviceClass);
                     } catch (ClassNotFoundException e) {
                         e.printStackTrace();
                     }
                 }
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

}
