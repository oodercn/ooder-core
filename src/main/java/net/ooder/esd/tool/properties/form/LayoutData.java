package net.ooder.esd.tool.properties.form;

import net.ooder.esd.annotation.ui.ManualType;
import net.ooder.esd.bean.view.CustomFormViewBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutData {

    public Integer rows;

    public Integer cols;

    public List<LayoutMerged> merged;

    public Map<String, Map<ManualType, Object>> rowSetting;

    public Map<String, Map<ManualType, Object>> colSetting;

    public Map<String, LayoutCell> cells;

    public LayoutData() {

    }

    public LayoutData(CustomFormViewBean viewBean) {
        this.cols = viewBean.getCol() * 2;
        this.rows = viewBean.getRow() == -1 ? viewBean.getDisplayFieldNames().size():viewBean.getRow();
    }

    public LayoutData(int col, int rows) {
        this.cols = col * 2;
        this.rows = rows;
    }

    public LayoutData addColSetting(String colkey, ManualType key, Object value) {
        if (colSetting == null) {
            colSetting = new HashMap<String, Map<ManualType, Object>>();
        }
        Map<ManualType, Object> oldMap = colSetting.get(colkey);
        if (oldMap == null) {
            oldMap = new HashMap<ManualType, Object>();
        }
        oldMap.put(key, value);
        colSetting.put(colkey, oldMap);
        return this;
    }

    public LayoutData addRowSetting(String rowkey, ManualType key, Object value) {
        if (rowSetting == null) {
            rowSetting = new HashMap<String, Map<ManualType, Object>>();
        }
        Map<ManualType, Object> oldMap = rowSetting.get(rowkey);
        if (oldMap == null) {
            oldMap = new HashMap<ManualType, Object>();
        }
        oldMap.put(key, value);
        rowSetting.put(rowkey, oldMap);
        return this;
    }


    public LayoutData addRowSetting(String key, Map<ManualType, Object> rowMap) {
        if (rowSetting == null) {
            rowSetting = new HashMap<String, Map<ManualType, Object>>();
        }
        Map<ManualType, Object> oldMap = rowSetting.get(key);
        if (oldMap == null) {
            oldMap = new HashMap<ManualType, Object>();
        }
        oldMap.putAll(rowMap);
        rowSetting.put(key, oldMap);
        return this;
    }

    public LayoutData addColSetting(String key, Map<ManualType, Object> colMap) {
        if (colSetting == null) {
            colSetting = new HashMap<String, Map<ManualType, Object>>();
        }
        Map<ManualType, Object> oldMap = colSetting.get(key);
        if (oldMap == null) {
            oldMap = new HashMap<ManualType, Object>();
        }
        oldMap.putAll(colMap);
        colSetting.put(key, oldMap);
        return this;
    }

    public LayoutData addCol(String key, LayoutCell cell) {
        if (cells == null) {
            cells = new HashMap<String, LayoutCell>();
        }
        cells.put(key, cell);
        return this;
    }


    public LayoutData addMerged(LayoutMerged layoutMerged) {
        if (merged == null) {
            merged = new ArrayList<LayoutMerged>();
        }
        merged.add(layoutMerged);
        return this;
    }

    public Map<String, Map<ManualType, Object>> getRowSetting() {
        return rowSetting;
    }

    public void setRowSetting(Map<String, Map<ManualType, Object>> rowSetting) {
        this.rowSetting = rowSetting;
    }

    public Map<String, Map<ManualType, Object>> getColSetting() {
        return colSetting;
    }

    public void setColSetting(Map<String, Map<ManualType, Object>> colSetting) {
        this.colSetting = colSetting;
    }

    public Map<String, LayoutCell> getCells() {
        return cells;
    }

    public void setCells(Map<String, LayoutCell> cells) {
        this.cells = cells;
    }


    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public List<LayoutMerged> getMerged() {
        return merged;
    }

    public void setMerged(List<LayoutMerged> merged) {
        this.merged = merged;
    }

}
