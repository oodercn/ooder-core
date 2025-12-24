package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.FileUploadAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.custom.component.form.field.CustomFileUploadComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ButtonComponent;
import net.ooder.esd.tool.component.FileUploadComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.tool.properties.form.ButtonProperties;
import net.ooder.esd.tool.properties.form.FileUploadProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomFileUploadComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.FILEUPLOAD
)
@AnnotationType(clazz = FileUploadAnnotation.class)
public class FileUploadFieldBean extends FieldBaseBean<FileUploadComponent> {

    String width;

    String height;

    String src;

    Class bindClass;

    Boolean prepareFormData;

    String uploadUrl;

    Dock dock;



    public FileUploadFieldBean(ModuleComponent moduleComponent, FileUploadComponent component) {

        super(moduleComponent, component);
    }




    public void updateProperties(FileUploadProperties fileUploadProperties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(fileUploadProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    @Override
    public void update(ModuleComponent moduleComponent, FileUploadComponent component) {
        updateProperties(component.getProperties());
        super.update(moduleComponent, component);
    }

    public FileUploadFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(FileUploadAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof FileUploadAnnotation) {
                fillData((FileUploadAnnotation) annotation);
            }
        }
    }

    public FileUploadFieldBean() {

    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean getPrepareFormData() {
        return prepareFormData;
    }

    public void setPrepareFormData(Boolean prepareFormData) {
        this.prepareFormData = prepareFormData;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public FileUploadFieldBean(FileUploadAnnotation annotation) {
        fillData(annotation);
    }

    public FileUploadFieldBean fillData(FileUploadAnnotation annotation) {
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
        return ComponentType.FILEUPLOAD;
    }
}
