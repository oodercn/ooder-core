package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.TextEditorAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.component.form.field.CustomTextEditorComponent;
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
@CustomClass(clazz = CustomTextEditorComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.TEXTEDITOR
)
@AnnotationType(clazz = TextEditorAnnotation.class)
public class TextEditorFieldBean extends FieldBaseBean<RichEditorComponent> {

    String id;

    Boolean selectable;

    Boolean dynLoad;

    Boolean enableBar;

    String value;

    String width;

    String height;

    String frameStyle;

    String cmdList;

    String cmdFilter;

    Boolean disabled;

    Boolean readonly;

    String textType = "text";

    Integer steps;

    String labelSize;

    LabelPos labelPos;

    String labelGap;

    String labelCaption;

    HAlignType labelHAlign;

    VAlignType labelVAlign;


    @Override
    public List<JavaSrcBean>  update(ModuleComponent moduleComponent, RichEditorComponent component) {

        return new ArrayList<>();
    }

    public TextEditorFieldBean(RichEditorComponent component) {
        super(component);

    }



    public TextEditorFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(TextEditorAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof TextEditorAnnotation) {
                fillData((TextEditorAnnotation) annotation);
            }
        }
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        annotationBeans.add(this);
        return annotationBeans;
    }

    public TextEditorFieldBean() {

    }

    public Boolean getEnableBar() {
        return enableBar;
    }

    public void setEnableBar(Boolean enableBar) {
        this.enableBar = enableBar;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
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

    public String getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(String labelSize) {
        this.labelSize = labelSize;
    }

    public LabelPos getLabelPos() {
        return labelPos;
    }

    public void setLabelPos(LabelPos labelPos) {
        this.labelPos = labelPos;
    }

    public String getLabelGap() {
        return labelGap;
    }

    public void setLabelGap(String labelGap) {
        this.labelGap = labelGap;
    }

    public String getLabelCaption() {
        return labelCaption;
    }

    public void setLabelCaption(String labelCaption) {
        this.labelCaption = labelCaption;
    }

    public HAlignType getLabelHAlign() {
        return labelHAlign;
    }

    public void setLabelHAlign(HAlignType labelHAlign) {
        this.labelHAlign = labelHAlign;
    }

    public VAlignType getLabelVAlign() {
        return labelVAlign;
    }

    public void setLabelVAlign(VAlignType labelVAlign) {
        this.labelVAlign = labelVAlign;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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



    public TextEditorFieldBean(TextEditorAnnotation annotation) {
        fillData(annotation);
    }

    public TextEditorFieldBean fillData(TextEditorAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TEXTEDITOR;
    }
}
