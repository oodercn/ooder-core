package net.ooder.esd.tool.properties.form;

import java.util.HashMap;
import java.util.Map;

public class LayoutCell {

    public String value;

    public String cellName;

    public Integer colspan;

    public Map<String, String> style;

    public LayoutCell(String cellName) {
        this.cellName = cellName;
    }

    public LayoutCell addStyle(String key, String value) {
        if (style == null) {
            style = new HashMap<String, String>();
        }
        style.put(key, value);
        return this;
    }

    public LayoutCell() {

    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getStyle() {
        return style;
    }

    public void setStyle(Map<String, String> style) {
        this.style = style;
    }


}
