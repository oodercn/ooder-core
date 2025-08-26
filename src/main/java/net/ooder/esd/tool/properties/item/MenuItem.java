package net.ooder.esd.tool.properties.item;


import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ItemType;

import java.util.Map;

public class MenuItem extends TabListItem {

    public ComboInputType type;

    public MenuItem() {

    }


    public MenuItem(Enum enumType) {
        super(enumType);
    }

    public MenuItem(String id, String caption) {
        super(id, caption);
    }

    public MenuItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        super(id, caption, imageClass, tips, params);
    }

    public MenuItem(String id, String caption, String imageClass) {
        super(id, caption, imageClass);
    }

    public MenuItem(ItemType itemType) {
        super(itemType.getId(), itemType.getCaption(), itemType.getImageClass());
    }

    public MenuItem(ItemType itemType, ComboInputType inputType) {
        super(itemType.getId(), itemType.getCaption(), itemType.getImageClass());
        if (inputType != null && !inputType.equals(ComboInputType.auto)) {
            this.type = inputType;
        } else {
            this.type = ComboInputType.button;
        }
    }

    public ComboInputType getType() {
        return type;
    }

    public void setType(ComboInputType type) {
        this.type = type;
    }


}
