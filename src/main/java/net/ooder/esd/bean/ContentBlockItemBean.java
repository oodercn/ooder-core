package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ContentBlockItemAnnotation;
import net.ooder.esd.annotation.ui.FontColorEnum;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.ItemColorEnum;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomContentBlockViewBean;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.util.json.EnumsSerializer;
import net.ooder.esd.tool.properties.item.ContentBlockItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = ContentBlockItemAnnotation.class)
public class ContentBlockItemBean<T extends FieldItemConfig> implements ContextMenuBar, Comparable<ContentBlockItemBean>, CustomBean {


    public String comment;
    public String renderer;
    public String imagePos;
    public String imageBgSize;
    public String imageRepeat;
    public String iconFontSize;
    public String iconFontCode;
    public String iconStyle;
    public String flagText;
    public String flagClass;
    public String flagStyle;
    public String image;
    public String id;
    public int index;
    public String valueSeparator;
    public String euClassName;
    public BorderType borderType;
    public Boolean activeLast;


    List<CustomFormMenu> customMenu;

    List<CustomFormMenu> bottombarMenu;


    @JSONField(serialize = false)
    Set<ComponentType> bindTypes = new LinkedHashSet<>();

    RightContextMenuBean contextMenuBean;

    ConstructorBean constructorBean;

    Class bindService;

    Boolean lazyLoad;

    ContentBlockItem contentBlockItem;

    Set<CustomFormEvent> event = new LinkedHashSet<>();

    public String sourceClassName;

    public String methodName;

    public String rootClassName;

    public String rootMethodName;

    public String viewClassName;


    @JSONField(serialize = false)
    Class fristClass;

    @JSONField(serialize = false)
    MethodConfig methodConfig;

    @JSONField(serialize = false)
    CustomContentBlockViewBean contentBlockViewBean;


    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public IconColorEnum iconColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public ItemColorEnum itemColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public FontColorEnum fontColor;


    public ContentBlockItemBean() {

    }


    public ContentBlockItemBean(ContentBlockItem contentBlockItem, CustomContentBlockViewBean contentBlockViewBean) {
        this.contentBlockViewBean = contentBlockViewBean;
        this.contentBlockItem = contentBlockItem;
        this.init(contentBlockItem);
    }

    private void init(ContentBlockItem contentBlockItem) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(contentBlockItem), Map.class), this, false, false);

    }

    public ContentBlockItemBean(MethodConfig methodConfig, CustomContentBlockViewBean ContentBlockViewBean) {
        this.methodConfig = methodConfig;
        this.contentBlockViewBean = ContentBlockViewBean;
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        ContentBlockItemAnnotation rawDataItem = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), ContentBlockItemAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(ContentBlockItemAnnotation.class, this);
        }


    }

    public ContentBlockItemBean(Constructor constructor, CustomContentBlockViewBean contentBlockViewBean) {
        constructorBean = new ConstructorBean(constructor);
        this.contentBlockViewBean = contentBlockViewBean;
        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }

        ContentBlockItemAnnotation ContentBlockItemAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, ContentBlockItemAnnotation.class);
        if (ContentBlockItemAnnotation != null) {
            fillData(ContentBlockItemAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(ContentBlockItemAnnotation.class, this);
        }

    }

    public List<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public List<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getImagePos() {
        return imagePos;
    }

    public void setImagePos(String imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getImageRepeat() {
        return imageRepeat;
    }

    public void setImageRepeat(String imageRepeat) {
        this.imageRepeat = imageRepeat;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public String getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }

    public String getFlagText() {
        return flagText;
    }

    public void setFlagText(String flagText) {
        this.flagText = flagText;
    }

    public String getFlagClass() {
        return flagClass;
    }

    public void setFlagClass(String flagClass) {
        this.flagClass = flagClass;
    }

    public String getFlagStyle() {
        return flagStyle;
    }

    public void setFlagStyle(String flagStyle) {
        this.flagStyle = flagStyle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public IconColorEnum getIconColor() {
        return iconColor;
    }

    public void setIconColor(IconColorEnum iconColor) {
        this.iconColor = iconColor;
    }

    public ItemColorEnum getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColorEnum itemColor) {
        this.itemColor = itemColor;
    }

    public FontColorEnum getFontColor() {
        return fontColor;
    }

    public void setFontColor(FontColorEnum fontColor) {
        this.fontColor = fontColor;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getActiveLast() {
        return activeLast;
    }

    public void setActiveLast(Boolean activeLast) {
        this.activeLast = activeLast;
    }

    public CustomContentBlockViewBean getContentBlockViewBean() {
        return contentBlockViewBean;
    }

    public void setContentBlockViewBean(CustomContentBlockViewBean contentBlockViewBean) {
        this.contentBlockViewBean = contentBlockViewBean;
    }

    public ContentBlockItem getContentBlockItem() {
        return contentBlockItem;
    }

    public void setContentBlockItem(ContentBlockItem contentBlockItem) {
        this.contentBlockItem = contentBlockItem;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public Class getFristClass() {
        return fristClass;
    }

    public void setFristClass(Class fristClass) {
        this.fristClass = fristClass;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    public String getRootMethodName() {
        return rootMethodName;
    }

    public void setRootMethodName(String rootMethodName) {
        this.rootMethodName = rootMethodName;
    }

    public String getViewClassName() {
        return viewClassName;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    @Override
    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    @Override
    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public ConstructorBean getConstructorBean() {
        return constructorBean;
    }

    public void setConstructorBean(ConstructorBean constructorBean) {
        this.constructorBean = constructorBean;
    }

    public MethodConfig getMethodConfig() {
        return methodConfig;
    }

    public void setMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
    }

    public ContentBlockItemBean<T> fillData(ContentBlockItemAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public int compareTo(ContentBlockItemBean o) {
        return index - o.getIndex();
    }

}
