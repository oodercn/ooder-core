package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.field.BlockFieldAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomBlockFormViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldBlockComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CustomClass(clazz = CustomFieldBlockComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.BLOCKCONFIG,
        componentType = ComponentType.BLOCK
)
@AnnotationType(clazz = BlockFieldAnnotation.class)
public class CustomBlockFieldBean extends BaseWidgetBean<CustomBlockFormViewBean, BlockComponent> {

    public static String skipStr = "__" + ComponentType.BLOCK.getType();

    public ModuleViewType moduleViewType = ModuleViewType.BLOCKCONFIG;
    BorderType borderType;
    Boolean resizer;
    Map<String, Object> resizerProp;
    String sideBarCaption;
    String sideBarType;
    SideBarStatusType sideBarStatus;
    String sideBarSize;
    String background;
    Dock dock;


    public CustomBlockFieldBean() {

    }

    public CustomBlockFieldBean(BlockProperties bean) {
        initProperties(bean);
    }

    public CustomBlockFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomBlockFormViewBean) methodConfig.getView();
        init(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(), true));
    }


    public CustomBlockFieldBean(ModuleComponent parentModuleComponent, BlockComponent component) {
        updateFieldBean(component);
        if (component.getChildren().size() > 0) {
            this.update(parentModuleComponent, component);
        }

    }


    public CustomBlockFieldBean(BlockComponent component) {
        this.update(null, component);
    }

    public CustomBlockFieldBean fillData(BlockFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    void init(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof BlockFieldAnnotation) {
                fillData((BlockFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }

    }

    public CustomBlockFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(BlockFieldAnnotation.class, this);
        init(annotations);

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> otherClass = new HashSet<>();

        if (viewBean != null) {
            otherClass.addAll(viewBean.getOtherClass());
        }
        return ClassUtility.checkBase(otherClass);
    }

    void initProperties(BlockProperties properties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(properties), Map.class), this, false, false);
        widgetBean = new CustomWidgetBean(properties);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLOCK;
    }


    public CustomBlockFormViewBean getViewBean() {
        return viewBean;
    }

    @Override
    public CustomBlockFormViewBean createViewBean(ModuleComponent currModuleComponent, BlockComponent component) {
        if (viewBean == null) {
            viewBean = new CustomBlockFormViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        initProperties(component);
        updateFieldBean(component);
        return viewBean;
    }


    private void updateFieldBean(BlockComponent blockComponent) {
        if (blockComponent != null) {
            BlockProperties blockProperties = blockComponent.getProperties();
            this.resizer = blockProperties.getResizer();
            this.resizerProp = blockProperties.getResizerProp();
            this.sideBarCaption = blockProperties.getSideBarCaption();
            this.sideBarType = blockProperties.getSideBarType();
            this.sideBarStatus = blockProperties.getSideBarStatus();
            this.sideBarSize = blockProperties.getSideBarSize();
            this.borderType = blockProperties.getBorderType();
            this.background = blockProperties.getBackground();
            this.dock = blockProperties.getDock();
        }


    }


    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public void setViewBean(CustomBlockFormViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public CustomWidgetBean getWidgetBean() {
        return widgetBean;
    }

    public void setWidgetBean(CustomWidgetBean widgetBean) {
        this.widgetBean = widgetBean;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
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
