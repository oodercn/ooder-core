package net.ooder.esd.bean.grid;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.GridColItemAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.util.json.EMSerializer;
import net.ooder.esd.tool.OODTypeMapping;
import net.ooder.esd.tool.properties.Header;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = GridColItemAnnotation.class)
public class GridColItemBean implements CustomBean {

    String title;

    Boolean flexSize;

    String headerStyle;

    Boolean colResizer;

    Boolean editable;

    Boolean lock;

    Class<? extends Enum> enumClass;


    ComboInputType inputType;
    @JSONField(serializeUsing = EMSerializer.class)
    String width;


    public void update(Header header) {
        this.setHeaderStyle(header.getHeaderStyle());
        this.setColResizer(header.getColResizer());
        this.setColResizer(header.getEditable());
        this.setFlexSize(header.getFlexSize());
        this.setInputType(header.getType());
        this.setWidth(header.getWidth());
        this.setInputType(inputType);
    }

    public GridColItemBean(ESDField colInfo) {
        if (colInfo != null) {
            Class returnType = colInfo.getReturnType();
            if (returnType.isEnum()) {
                enumClass = returnType;
            }

            if (colInfo.getComponentType().equals(ComponentType.COMBOINPUT) && colInfo.getComboConfig() != null) {
                inputType = colInfo.getComboConfig().getInputType();
            } else {
                inputType = OODTypeMapping.getType(returnType);
            }
            this.width = colInfo.getWidth();
            if (inputType.equals(ComboInputType.auto)) {
                inputType = ComboInputType.input;
            }


            switch (inputType) {
                case date:
                    if (width == null || width.equals("auto")) {
                        this.width = "12em";
                    }
                    break;
                case number:
                    if (width == null || width.equals("auto")) {
                        this.width = "6em";
                    }
                    break;
                case datetime:
                    if (width == null || width.equals("auto")) {
                        this.width = "6em";
                    }
                    break;
            }
        } else {
            AnnotationUtil.fillDefaultValue(GridColItemAnnotation.class, this);
        }

    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public ComboInputType getInputType() {
        return inputType;
    }

    public void setInputType(ComboInputType inputType) {
        this.inputType = inputType;
    }

    public GridColItemBean() {
        AnnotationUtil.fillDefaultValue(GridColItemAnnotation.class, this);
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public GridColItemBean(GridColItemAnnotation annotation) {
        fillData(annotation);
    }

    public Boolean getFlexSize() {
        return flexSize;
    }

    public void setFlexSize(Boolean flexSize) {
        this.flexSize = flexSize;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public Boolean getColResizer() {
        return colResizer;
    }

    public void setColResizer(Boolean colResizer) {
        this.colResizer = colResizer;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GridColItemBean fillData(GridColItemAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
