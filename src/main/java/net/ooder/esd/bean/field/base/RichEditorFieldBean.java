package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.RichEditorAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.component.form.field.CustomRichEditorComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.RichEditorComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomRichEditorComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.RICHEDITOR
)
@AnnotationType(clazz = RichEditorAnnotation.class)
public class RichEditorFieldBean extends FieldBaseBean<RichEditorComponent> {

    String id;

    Boolean dynLoad;

    Boolean selectable;

    Boolean enableBar;

    String value;

    String width;

    Dock dock;

    String height;

    String frameStyle;

    String cmdList;

    String cmdFilter;

    String textType;

    Integer steps;

    String labelCaption;

    public ToolBarMenuBean toolBar;


    @Override
    public void  update(ModuleComponent moduleComponent, RichEditorComponent component) {

    }

    public RichEditorFieldBean(RichEditorComponent richEditorComponent) {
        super(richEditorComponent);
    }

    public RichEditorFieldBean() {

    }

    public RichEditorFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(RichEditorAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof RichEditorAnnotation) {
                fillData((RichEditorAnnotation) annotation);
            }
        }
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        annotationBeans.add(this);
        return annotationBeans;
    }


    public ToolBarMenuBean getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBarMenuBean toolBar) {
        this.toolBar = toolBar;
    }

    public Boolean getEnableBar() {
        return enableBar;
    }

    public void setEnableBar(Boolean enableBar) {
        this.enableBar = enableBar;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getFrameStyle() {
        return frameStyle;
    }

    public void setFrameStyle(String frameStyle) {
        this.frameStyle = frameStyle;
    }

    public String getCmdList() {
        return cmdList;
    }

    public void setCmdList(String cmdList) {
        this.cmdList = cmdList;
    }

    public String getCmdFilter() {
        return cmdFilter;
    }

    public void setCmdFilter(String cmdFilter) {
        this.cmdFilter = cmdFilter;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }


    public String getLabelCaption() {
        return labelCaption;
    }

    public void setLabelCaption(String labelCaption) {
        this.labelCaption = labelCaption;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public RichEditorFieldBean(RichEditorAnnotation annotation) {
        fillData(annotation);
    }

    public RichEditorFieldBean fillData(RichEditorAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.RICHEDITOR;
    }
}
