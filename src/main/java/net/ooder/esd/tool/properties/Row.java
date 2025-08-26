package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSONObject;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.item.EditorItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Row {
    List<Cell> cells;
    Boolean iniFold;
    String id;
    List<Row> sub;
    String caption;
    String firstCellStyle;
    String firstCellClass;
    String group;
    Boolean hidden;
    Boolean rowResizer;
    String rowRenderer;
    String cellRenderer;
    String cellCapTpl;
    String cellClass;
    String cellStyle;
    Boolean disabled;
    Boolean readonly;
    Boolean editable;
    String editMode;
    List<EditorItem> editorListItems;
    String tag;
    Map<String, Object> tagVar;
    List<CmdItem> tagCmds;

    public Row() {

    }

    public Row(Object obj) {
        if (obj != null) {
            cells = new ArrayList<Cell>();
            Cell cell = new Cell("objValue", obj);
            cells.add(cell);
        }
    }

    public Row(Map<String, Object> map) {
        if (!map.isEmpty()) {
            cells = new ArrayList<Cell>();
            map.forEach((k, v) -> {
                if (v != null) {
                    if (v.getClass().isArray()) {
                    } else {
                        if (v instanceof Cell) {
                            cells.add((Cell) v);
                        } else {
                            Cell cell = new Cell(k, v);
                            String json = JSONObject.toJSONString(v);
                            boolean isObject = JSONObject.isValidObject(json);
                            if (isObject) {
                                cell = JSONObject.parseObject(json, Cell.class);
                            }
                            cells.add(cell);
                        }
                    }

                }

            });
        }

    }


    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public List<Row> getSub() {
        return sub;
    }

    public void setSub(List<Row> sub) {
        this.sub = sub;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFirstCellStyle() {
        return firstCellStyle;
    }

    public void setFirstCellStyle(String firstCellStyle) {
        this.firstCellStyle = firstCellStyle;
    }

    public String getFirstCellClass() {
        return firstCellClass;
    }

    public void setFirstCellClass(String firstCellClass) {
        this.firstCellClass = firstCellClass;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getRowResizer() {
        return rowResizer;
    }

    public void setRowResizer(Boolean rowResizer) {
        this.rowResizer = rowResizer;
    }

    public String getRowRenderer() {
        return rowRenderer;
    }

    public void setRowRenderer(String rowRenderer) {
        this.rowRenderer = rowRenderer;
    }

    public String getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(String cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    public String getCellCapTpl() {
        return cellCapTpl;
    }

    public void setCellCapTpl(String cellCapTpl) {
        this.cellCapTpl = cellCapTpl;
    }

    public String getCellClass() {
        return cellClass;
    }

    public void setCellClass(String cellClass) {
        this.cellClass = cellClass;
    }

    public String getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(String cellStyle) {
        this.cellStyle = cellStyle;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getEditMode() {
        return editMode;
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    public List<EditorItem> getEditorListItems() {
        return editorListItems;
    }

    public void setEditorListItems(List<EditorItem> editorListItems) {
        this.editorListItems = editorListItems;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> getTagVar() {
        return tagVar;
    }

    public void setTagVar(Map<String, Object> tagVar) {
        this.tagVar = tagVar;
    }

    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
