package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.LabelAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.custom.component.form.field.CustomLabelComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.LabelComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.LabelProperties;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.esd.util.json.EnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomLabelComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.LABEL
)
@AnnotationType(clazz = LabelAnnotation.class)
public class LabelFieldBean extends FieldBaseBean<LabelComponent> {


    String clock;

    String image;

    String caption;

    ImagePos imagePos;

    String imageBgSize;

    String imageClass;

    String iconFontCode;

    HAlignType hAlign;

    VAlignType vAlign;

    String fontColor;

    String fontSize;

    String fontWeight;

    String fontFamily;

    @JSONField(serializeUsing = CaseEnumsSerializer.class, deserializeUsing = CaseEnumsSerializer.class)
    UIPositionType position;

    String excelCellFormula;

    @Override
    public void update(ModuleComponent moduleComponent, LabelComponent component) {

    }

    public LabelFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(LabelAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof LabelAnnotation) {
                fillData((LabelAnnotation) annotation);
            }
        }
    }


    public LabelFieldBean(LabelProperties labelProperties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(labelProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public LabelFieldBean(Map valueMap) {
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public LabelFieldBean() {

    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getExcelCellFormula() {
        return excelCellFormula;
    }

    public void setExcelCellFormula(String excelCellFormula) {
        this.excelCellFormula = excelCellFormula;
    }

    public     UIPositionType getPosition() {
        return position;
    }

    public void setPosition(UIPositionType position) {
        this.position = position;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
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

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
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

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public VAlignType getvAlign() {
        return vAlign;
    }

    public void setvAlign(VAlignType vAlign) {
        this.vAlign = vAlign;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public LabelFieldBean(LabelAnnotation annotation) {
        fillData(annotation);
    }

    public LabelFieldBean fillData(LabelAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.LABEL;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }
}
