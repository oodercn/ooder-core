package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.NotNull;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.StatusButtonsAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomStatusButtonsComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.StatusButtonsComponent;
import net.ooder.esd.tool.properties.form.StatusButtonsProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CustomClass(clazz = CustomStatusButtonsComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.STATUSBUTTONS
)
@AnnotationType(clazz = StatusButtonsAnnotation.class)
public class StatusButtonsFieldBean extends FieldBaseBean<StatusButtonsComponent> {


    String barHeight;

    AlignType align;

    String itemMargin;

    String itemPadding;

    String itemWidth;

    AlignType itemAlign;

    String width;


    StatusItemType itemType;

    Boolean connected;

    CustomListBean customListBean;

    List<String> iconColors;

    List<String> itemColors;

    List<String> fontColors;

    Boolean autoIconColor;

    Boolean autoItemColor;

    Boolean autoFontColor;


    Dock dock;

    TagCmdsAlign tagCmdsAlign;

    @NotNull
    BorderType borderType;

    public StatusButtonsFieldBean() {

    }

    public StatusButtonsFieldBean(ModuleComponent moduleComponent, StatusButtonsComponent component) {
        update(moduleComponent, component);
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, StatusButtonsComponent component) {
        this.update(component.getProperties());
        customListBean = new CustomListBean();
        javaSrcBeans = customListBean.update(moduleComponent, component);
        return javaSrcBeans;

    }


    public void update(StatusButtonsProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public StatusButtonsFieldBean(ESDField esdField, Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(StatusButtonsAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof StatusButtonsAnnotation) {
                fillData((StatusButtonsAnnotation) annotation);
            }
        }
        this.customListBean = new CustomListBean(esdField, annotations);
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getBarHeight() {
        return barHeight;
    }

    public void setBarHeight(String barHeight) {
        this.barHeight = barHeight;
    }

    public CustomListBean getCustomListBean() {
        return customListBean;
    }

    public void setCustomListBean(CustomListBean customListBean) {
        this.customListBean = customListBean;
    }

    public AlignType getAlign() {
        return align;
    }

    public void setAlign(AlignType align) {
        this.align = align;
    }

    public String getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(String itemMargin) {
        this.itemMargin = itemMargin;
    }

    public String getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(String itemPadding) {
        this.itemPadding = itemPadding;
    }

    public String getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(String itemWidth) {
        this.itemWidth = itemWidth;
    }

    public AlignType getItemAlign() {
        return itemAlign;
    }

    public void setItemAlign(AlignType itemAlign) {
        this.itemAlign = itemAlign;
    }

    public StatusItemType getItemType() {
        return itemType;
    }

    public void setItemType(StatusItemType itemType) {
        this.itemType = itemType;
    }


    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public StatusButtonsFieldBean(StatusButtonsAnnotation annotation) {
        fillData(annotation);
    }

    public StatusButtonsFieldBean fillData(StatusButtonsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public List<String> getIconColors() {
        return iconColors;
    }

    public void setIconColors(List<String> iconColors) {
        this.iconColors = iconColors;
    }

    public List<String> getItemColors() {
        return itemColors;
    }

    public void setItemColors(List<String> itemColors) {
        this.itemColors = itemColors;
    }

    public List<String> getFontColors() {
        return fontColors;
    }

    public void setFontColors(List<String> fontColors) {
        this.fontColors = fontColors;
    }

    public Boolean getAutoIconColor() {
        return autoIconColor;
    }

    public void setAutoIconColor(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
    }

    public Boolean getAutoItemColor() {
        return autoItemColor;
    }

    public void setAutoItemColor(Boolean autoItemColor) {
        this.autoItemColor = autoItemColor;
    }

    public Boolean getAutoFontColor() {
        return autoFontColor;
    }

    public void setAutoFontColor(Boolean autoFontColor) {
        this.autoFontColor = autoFontColor;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.STATUSBUTTONS;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();

        return classes;
    }


}
