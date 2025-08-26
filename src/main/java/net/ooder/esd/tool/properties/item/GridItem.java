package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridItem<T extends TreeListItem> extends UIItem {
    @JSONField(serialize = false)
    public static final String ESDSearchPattern = "esdsearchpattern";
    public  Boolean group;
    public List<T> sub;
    public List<Cell> cells;
    public List<CmdItem> tagCmds;


    public GridItem() {
    }


    public GridItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
        this.tagVar = params;
    }


    public Map<String, Object> addTagVar(String name, Object value) {
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        tagVar.put(name, value);
        return tagVar;
    }

    public static String getESDSearchPattern() {
        return ESDSearchPattern;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public List<T> getSub() {
        return sub;
    }

    public void setSub(List<T> sub) {
        this.sub = sub;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }

    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public String getPattern() {
        String pattern = null;
        if (JDSActionContext.getActionContext().getParams(GridItem.ESDSearchPattern) != null) {
            pattern = JDSActionContext.getActionContext().getParams(GridItem.ESDSearchPattern).toString();
        }
        return pattern;
    }


}
