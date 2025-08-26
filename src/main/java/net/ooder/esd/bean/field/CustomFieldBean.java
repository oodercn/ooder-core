package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ui.FontColorEnum;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.ItemColorEnum;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.form.HiddenInputProperties;
import net.ooder.esd.util.json.EnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = CustomAnnotation.class)
public class CustomFieldBean implements CustomBean {

    String id;

    Integer index = 0;

    String caption;

    Boolean readonly;

    Boolean disabled;

    Boolean hidden;

    Boolean uid;

    Boolean pid;

    String[] enums;

    ComboInputType inputType;

    Class<? extends Enum> enumClass;

    String imageClass;

    String tips;

    Boolean captionField;

    String target;


    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public IconColorEnum iconColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public ItemColorEnum itemColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public FontColorEnum fontColor;

    public CustomFieldBean() {

    }

    public CustomFieldBean(String id) {
        this.id = id;
    }

    public CustomFieldBean(TreeListItem listItem) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(listItem), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
        inputType = listItem.getType();
    }

    public void update(Component component) {
        this.target = component.getTarget();
        this.init(component.getProperties());
    }

    public CustomFieldBean(Component component) {
        AnnotationUtil.fillDefaultValue(CustomAnnotation.class, this);
        this.update(component);

    }

    private void init(Properties properties) {

        if (!(properties instanceof HiddenInputProperties)) {
            Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
            OgnlUtil.setProperties(valueMap, this, false, false);
        } else {
            HiddenInputProperties hiddenInputProperties = (HiddenInputProperties) properties;
            this.hidden = true;
            this.pid = hiddenInputProperties.getPid();
            this.id = hiddenInputProperties.getId();
        }

    }

    public ComboInputType getInputType() {
        return inputType;
    }

    public void setInputType(ComboInputType inputType) {
        this.inputType = inputType;
    }

    public Boolean getCaptionField() {
        return captionField;
    }

    public void setCaptionField(Boolean captionField) {
        this.captionField = captionField;
    }

    public String[] getEnums() {
        return enums;
    }

    public void setEnums(String[] enums) {
        this.enums = enums;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }


    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getUid() {
        return uid;
    }

    public void setUid(Boolean uid) {
        this.uid = uid;
    }

    public Boolean getPid() {
        return pid;
    }

    public void setPid(Boolean pid) {
        this.pid = pid;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
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

    public CustomFieldBean(CustomAnnotation annotation) {
        fillData(annotation);
    }

    public CustomFieldBean fillData(CustomAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
