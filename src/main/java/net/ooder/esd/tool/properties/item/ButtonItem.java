package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.FontColorEnum;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.ItemColorEnum;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ItemType;
import net.ooder.esd.util.json.EnumsSerializer;

import java.util.Map;

public class ButtonItem extends UIItem {

    public ComboInputType type;

    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public IconColorEnum iconColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public ItemColorEnum itemColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public FontColorEnum fontColor;

    public ButtonItem() {

    }

    public ButtonItem(String id, String caption) {
        super(id, caption);
    }

    public ButtonItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        super(id, caption, imageClass, tips, params);
    }

    public ButtonItem(String id, String caption, String imageClass) {
        super(id, caption, imageClass);
    }

    public ButtonItem(ItemType itemType) {
        super(itemType.getId(), itemType.getCaption(), itemType.getImageClass());
    }

    public ButtonItem(ItemType itemType, ComboInputType inputType) {
        super(itemType.getId(), itemType.getCaption(), itemType.getImageClass());
        if (inputType != null && !inputType.equals(ComboInputType.auto)) {
            this.type = inputType;
        } else {
            this.type = ComboInputType.button;
        }
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

    public ComboInputType getType() {
        return type;
    }

    public void setType(ComboInputType type) {
        this.type = type;
    }


}
