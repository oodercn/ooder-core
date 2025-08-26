package net.ooder.esd.custom.properties;

import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.custom.TableFieldInfo;
import net.ooder.esd.tool.properties.Header;

public class TableHeader extends Header {


    public TableHeader(TableFieldInfo info) {
        caption = info.getCaption();
        id = info.getName();
        type = info.getType();
        if (type.equals(ComboInputType.auto)) {
            type = ComboInputType.input;
        } else if (type.equals(ComboInputType.password)) {
            hidden = true;
        }
    }



}
