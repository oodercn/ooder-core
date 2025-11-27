package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.field.PanelFieldAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomPanelFormViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldPanelComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.PanelComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomFieldPanelComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.PANELCONFIG,
        componentType = ComponentType.PANEL
)
@AnnotationType(clazz = PanelFieldAnnotation.class)
public class CustomPanelFieldBean extends BaseWidgetBean<CustomPanelFormViewBean, PanelComponent<PanelProperties>> {


    Dock dock;

    String caption;

    String html;

    String image;

    ImagePos imagePos;

    String imageBgSiz;

    String imageClass;

    String iconFontCode;

    BorderType borderType;

    boolean noFrame;

    HAlignType hAlign;

    ToggleIconType toggleIcon;

    boolean toggle;

    public CustomPanelFieldBean() {

    }

    public CustomPanelFieldBean(PanelProperties bean) {
        initProperties(bean);
    }

    public CustomPanelFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomPanelFormViewBean) methodConfig.getView();
        init(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(), true));
    }


    public CustomPanelFieldBean(ModuleComponent parentModuleComponent, PanelComponent component) {
        updateFieldBean(component);
       this.update(parentModuleComponent,component);
    }

    public CustomPanelFieldBean(PanelComponent component) {
        this.update(null, component);
    }

    public CustomPanelFieldBean fillData(PanelFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    void init(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof PanelFieldAnnotation) {
                fillData((PanelFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }

    }


    private void updateFieldBean(PanelComponent<PanelProperties> panelComponent) {
        if (panelComponent != null) {
            PanelProperties panelProperties = panelComponent.getProperties();
            this.dock = panelProperties.getDock();
            this.caption = panelProperties.getCaption();
            this.html = panelProperties.getHtml();
            this.image = panelProperties.getImage();
            this.imagePos = panelProperties.getImagePos();
            this.imageBgSiz = panelProperties.getImageBgSize();
            this.iconFontCode = panelProperties.getIconFontCode();
            this.borderType = panelProperties.getBorderType();
            this.noFrame = panelProperties.getNoFrame();
            this.hAlign = panelProperties.gethAlign();
            this.toggleIcon = panelProperties.getToggleIcon();
            this.toggle = panelProperties.getToggle();

        }


    }

    @Override
    public CustomPanelFormViewBean createViewBean(ModuleComponent currModuleComponent, PanelComponent<PanelProperties> component) {
        if (viewBean == null) {
            viewBean = new CustomPanelFormViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }

    public CustomPanelFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(PanelFieldAnnotation.class, this);
        init(annotations);

    }


    @Override
    public List<JavaSrcBean> update(ModuleComponent parentModuleComponent, PanelComponent component) {
        this.initWidget(parentModuleComponent,component);
        updateFieldBean(component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (component.getChildren() != null && component.getChildren().size() > 0) {
            javaSrcBeans.addAll(super.update(parentModuleComponent, component));
        }
        return javaSrcBeans;

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        if (viewBean != null) {
            return viewBean.getOtherClass();
        }
        return new HashSet<>();
    }

    void initProperties(PanelProperties properties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(properties), Map.class), this, false, false);
        widgetBean = new CustomWidgetBean(properties);

    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ImagePos getImagePos() {
        return imagePos;
    }

    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSiz() {
        return imageBgSiz;
    }

    public void setImageBgSiz(String imageBgSiz) {
        this.imageBgSiz = imageBgSiz;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public boolean isNoFrame() {
        return noFrame;
    }

    public void setNoFrame(boolean noFrame) {
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

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PANEL;
    }


    public CustomPanelFormViewBean getViewBean() {
        return viewBean;
    }


    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
