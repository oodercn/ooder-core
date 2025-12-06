package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.JavaEditorAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.component.form.field.CustomJavaEditorComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.JavaEditorComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.CodeEditorProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomJavaEditorComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.JAVAEDITOR
)
@AnnotationType(clazz = JavaEditorAnnotation.class)
public class JavaEditorFieldBean extends FieldBaseBean<JavaEditorComponent> {

    String id;

    Boolean selectable;

    String value;

    String width;

    String height;

    String frameStyle;

    String cmdList;

    String cmdFilter;

    String codeType;

    String labelGap;

    Boolean dynLoad;

    public ToolBarMenuBean toolBar;


    public JavaEditorFieldBean() {

    }

    public void update(JavaEditorComponent component) {
        CodeEditorProperties properties = component.getProperties();
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    @Override
    public void  update(ModuleComponent moduleComponent, JavaEditorComponent component) {

    }

    public JavaEditorFieldBean(JavaEditorComponent component) {
        AnnotationUtil.fillDefaultValue(JavaEditorAnnotation.class, this);
        update(component);
    }


    public JavaEditorFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(JavaEditorAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof JavaEditorAnnotation) {
                fillData((JavaEditorAnnotation) annotation);
            }
            if (annotation instanceof ToolBarMenu) {
                toolBar = new ToolBarMenuBean((ToolBarMenu) annotation);
            }
        }

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (toolBar != null) {
            classSet.addAll(toolBar.getOtherClass());
        }
        return ClassUtility.checkBase(classSet);
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (toolBar != null) {
            annotationBeans.add(toolBar);
        }

        annotationBeans.add(this);
        return annotationBeans;
    }

    public String getLabelGap() {
        return labelGap;
    }

    public void setLabelGap(String labelGap) {
        this.labelGap = labelGap;
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

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
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


    public JavaEditorFieldBean(JavaEditorAnnotation annotation) {
        fillData(annotation);
    }

    public ToolBarMenuBean getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBarMenuBean toolBar) {
        this.toolBar = toolBar;
    }


    public JavaEditorFieldBean fillData(JavaEditorAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.JAVAEDITOR;
    }
}
