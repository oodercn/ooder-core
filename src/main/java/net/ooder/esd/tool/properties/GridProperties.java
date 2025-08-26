package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.RowHead;
import net.ooder.esd.annotation.ui.TreeModeType;
import net.ooder.esd.annotation.action.ActiveModeType;
import net.ooder.esd.annotation.action.EditModeType;
import net.ooder.esd.annotation.action.HotRowModeType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.bean.view.CustomMGridViewBean;
import net.ooder.esd.bean.grid.GridRowCmdBean;
import net.ooder.esd.bean.grid.RowHeadBean;
import net.ooder.esd.custom.properties.CustomHeader;
import net.ooder.esd.dsm.view.field.FieldGridConfig;
import net.ooder.esd.util.json.EMSerializer;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridProperties extends AbsListProperties {

    public Boolean rowHandler;
    @JSONField(serializeUsing = EMSerializer.class)
    public String headerHeight = "3em";
    @JSONField(serializeUsing = EMSerializer.class)
    public String rowHeight = "3em";
    public String headerTail;
    public String gridHandlerCaption;
    @JSONField(serializeUsing = EMSerializer.class)
    public String rowHandlerWidth;
    public String uidColumn;
    public Boolean showHeader;
    public Boolean colResizer;
    public Header colOptions;
    public Boolean directInput;
    public Boolean rowResizer;
    public Boolean noCtrlKey;
    public EditModeType editMode;
    public Boolean altRowsBg;
    public Boolean colSortable;
    public Boolean rowNumbered;
    public Boolean editable;
    public Boolean firstCellEditable;
    public Boolean animCollapse;
    public Boolean colHidable;
    public Boolean colMovable;
    public List<Header> header = new ArrayList<Header>();
    public List<Row> rows;
    public Map<String, String> rowOptions;
    public TreeModeType treeMode;
    public HotRowModeType hotRowMode;
    public String hotRowNumber;
    public String hotRowCellCap;
    public String hotRowRequired;
    public ActiveModeType activeMode;
    public String currencyTpl;
    public String numberTpl;
    public Integer freezedRow;
    public Integer freezedColumn;
    public List<Object> rawData;
    public List<CmdItem> tagCmds;
    public Boolean iniFold;

    public GridProperties() {

    }

    public GridProperties(CustomMGridViewBean view) {
        this.init(view.getContainerBean());
        this.name = view.getName();
        List<FieldGridConfig> fieldList = view.getAllFields();
        RowHeadBean rowHead = view.getRowHead();
        if (rowHead == null) {
            rowHead = AnnotationUtil.fillDefaultValue(RowHead.class, new RowHeadBean());
        }


        this.rowNumbered = rowHead.getRowNumbered();
        this.selMode = rowHead.getSelMode();
        this.rowHandlerWidth = rowHead.getRowHandlerWidth();
        this.gridHandlerCaption = rowHead.getGridHandlerCaption();
        this.headerHeight = rowHead.getHeaderHeight();
        this.rowHandler = rowHead.getRowHandler();
        this.rowHeight = view.getRowHeight();
        this.altRowsBg = view.getAltRowsBg();
        this.setColSortable(view.getColSortable());
        this.setShowHeader(view.getShowHeader());
        this.setEditable(view.getEditable());
        this.iniFold = view.getIniFold();
        this.dock = view.getDock();
        this.animCollapse = view.getAnimCollapse();
        this.rowResizer = view.getRowResizer();
        this.colHidable = view.getColHidable();
        this.colMovable = view.getColMovable();
        this.noCtrlKey = view.getNoCtrlKey();
        this.freezedColumn = view.getFreezedColumn();
        this.freezedRow = view.getFreezedRow();
        this.showHeader = view.getShowHeader();
        this.editable = view.getEditable();
        this.uidColumn = view.getUidColumn();
        this.valueSeparator = view.getValueSeparator();
        this.currencyTpl = view.getCurrencyTpl();
        this.numberTpl = view.getNumberTpl();

        GridRowCmdBean rowCmdBean = view.getRowCmdBean();
        if (rowCmdBean != null) {
            this.activeMode = rowCmdBean.getActiveMode();
            this.editMode = rowCmdBean.getEditMode();
            this.treeMode = rowCmdBean.getTreeMode();
            this.hotRowMode = rowCmdBean.getHotRowMode();
            this.headerTail = rowCmdBean.getCaption();
            if (this.selMode.equals(SelModeType.multibycheckbox)) {
                this.selMode = SelModeType.multi;
            } else if (this.selMode.equals(SelModeType.singlecheckbox)) {
                this.selMode = SelModeType.single;
            }

            Integer size = rowCmdBean.getRowMenu().size();
            this.rowHandlerWidth = size * 2 + "em";

        }

        List<CustomHeader> displayList = new ArrayList<CustomHeader>();

        for (FieldGridConfig fieldInfo : fieldList) {
            FieldGridConfig gridConfig = fieldInfo;
            if (gridConfig.getSerialize() != null && gridConfig.getSerialize()) {
                CustomHeader headCol = new CustomHeader(gridConfig);
                this.getHeader().add(headCol);
                if (fieldInfo.getColHidden() != null && !fieldInfo.getColHidden()) {
                    displayList.add(headCol);
                }
            }
            if (fieldInfo.getUid() != null && fieldInfo.getUid()) {
                this.setUidColumn(fieldInfo.getId());
            }
        }
        //最后一行 平铺
        if (displayList.size() > 0) {
            displayList.get(displayList.size() - 1).setFlexSize(true);
        }

        this.setDesc(view.getMethodName());


    }

    public GridProperties(CustomGridViewBean view) {
        this.init(view.getContainerBean());
        this.name = view.getName();
        List<FieldGridConfig> fieldList = view.getAllFields();
        RowHeadBean rowHead = view.getRowHead();
        if (rowHead == null) {
            rowHead = AnnotationUtil.fillDefaultValue(RowHead.class, new RowHeadBean());
        }

        GridRowCmdBean rowCmdBean = view.getRowCmdBean();
        if (rowCmdBean != null) {
            this.activeMode = rowCmdBean.getActiveMode();
            this.editMode = rowCmdBean.getEditMode();
            this.treeMode = rowCmdBean.getTreeMode();
            this.hotRowMode = rowCmdBean.getHotRowMode();
            this.headerTail = rowCmdBean.getCaption();

        }
        this.rowNumbered = rowHead.getRowNumbered();
        this.selMode = rowHead.getSelMode();
        this.rowHandlerWidth = rowHead.getRowHandlerWidth();
        this.gridHandlerCaption = rowHead.getGridHandlerCaption();
        this.headerHeight = rowHead.getHeaderHeight();
        this.rowHandler = rowHead.getRowHandler();
        this.rowHeight = view.getRowHeight();
        this.altRowsBg = view.getAltRowsBg();
        this.setColSortable(view.getColSortable());
        this.setShowHeader(view.getShowHeader());
        this.setEditable(view.getEditable());
        this.iniFold = view.getIniFold();
        this.dock = view.getDock();
        this.animCollapse = view.getAnimCollapse();
        this.rowResizer = view.getRowResizer();
        this.colHidable = view.getColHidable();
        this.colMovable = view.getColMovable();
        this.noCtrlKey = view.getNoCtrlKey();
        this.freezedColumn = view.getFreezedColumn();
        this.freezedRow = view.getFreezedRow();
        this.showHeader = view.getShowHeader();
        this.editable = view.getEditable();
        this.uidColumn = view.getUidColumn();
        this.valueSeparator = view.getValueSeparator();
        this.currencyTpl = view.getCurrencyTpl();
        this.numberTpl = view.getNumberTpl();


        List<CustomHeader> displayList = new ArrayList<CustomHeader>();

        for (FieldGridConfig fieldInfo : fieldList) {
            FieldGridConfig gridConfig = fieldInfo;
            if (gridConfig.getSerialize() == null || gridConfig.getSerialize()) {
                CustomHeader headCol = new CustomHeader(gridConfig);
                this.getHeader().add(headCol);
                if (fieldInfo.getColHidden() != null && !fieldInfo.getColHidden()) {
                    displayList.add(headCol);
                }
            }
            if (fieldInfo.getUid() != null && fieldInfo.getUid()) {
                this.setUidColumn(fieldInfo.getId());
            }
        }
        //最后一行 平铺
        if (displayList.size() > 0) {
            displayList.get(displayList.size() - 1).setFlexSize(true);
        }

        this.setDesc(view.getMethodName());

    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }


    public String getHeaderTail() {
        return headerTail;
    }

    public void setHeaderTail(String headerTail) {
        this.headerTail = headerTail;
    }

    public Boolean getColSortable() {
        return colSortable;
    }

    public void setColSortable(Boolean colSortable) {
        this.colSortable = colSortable;
    }

    public String getRowHandlerWidth() {
        return rowHandlerWidth;
    }

    public void setRowHandlerWidth(String rowHandlerWidth) {
        this.rowHandlerWidth = rowHandlerWidth;
    }


    public String getGridHandlerCaption() {
        return gridHandlerCaption;
    }

    public void setGridHandlerCaption(String gridHandlerCaption) {
        this.gridHandlerCaption = gridHandlerCaption;
    }


    public String getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(String headerHeight) {
        this.headerHeight = headerHeight;
    }


    public String getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(String rowHeight) {
        this.rowHeight = rowHeight;
    }

    public String getUidColumn() {
        return uidColumn;
    }

    public void setUidColumn(String uidColumn) {
        this.uidColumn = uidColumn;
    }

    public Header getColOptions() {
        return colOptions;
    }

    public void setColOptions(Header colOptions) {
        this.colOptions = colOptions;
    }

    public Boolean getDirectInput() {
        return directInput;
    }

    public void setDirectInput(Boolean directInput) {
        this.directInput = directInput;
    }

    public EditModeType getEditMode() {
        return editMode;
    }

    public void setEditMode(EditModeType editMode) {
        this.editMode = editMode;
    }

    public Boolean getAltRowsBg() {
        return altRowsBg;
    }

    public void setAltRowsBg(Boolean altRowsBg) {
        this.altRowsBg = altRowsBg;
    }

    public Boolean getRowNumbered() {
        return rowNumbered;
    }

    public void setRowNumbered(Boolean rowNumbered) {
        this.rowNumbered = rowNumbered;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getFirstCellEditable() {
        return firstCellEditable;
    }

    public void setFirstCellEditable(Boolean firstCellEditable) {
        this.firstCellEditable = firstCellEditable;
    }

    public Boolean getAnimCollapse() {
        return animCollapse;
    }

    public void setAnimCollapse(Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public Boolean getColHidable() {
        return colHidable;
    }

    public void setColHidable(Boolean colHidable) {
        this.colHidable = colHidable;
    }

    public Boolean getColMovable() {
        return colMovable;
    }

    public void setColMovable(Boolean colMovable) {
        this.colMovable = colMovable;
    }

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Boolean getColResizer() {
        return colResizer;
    }

    public void setColResizer(Boolean colResizer) {
        this.colResizer = colResizer;
    }

    public Boolean getRowResizer() {
        return rowResizer;
    }

    public void setRowResizer(Boolean rowResizer) {
        this.rowResizer = rowResizer;
    }

    public Boolean getNoCtrlKey() {
        return noCtrlKey;
    }

    public void setNoCtrlKey(Boolean noCtrlKey) {
        this.noCtrlKey = noCtrlKey;
    }

    public TreeModeType getTreeMode() {
        return treeMode;
    }

    public void setTreeMode(TreeModeType treeMode) {
        this.treeMode = treeMode;
    }

    public String getCurrencyTpl() {
        return currencyTpl;
    }

    public void setCurrencyTpl(String currencyTpl) {
        this.currencyTpl = currencyTpl;
    }

    public String getNumberTpl() {
        return numberTpl;
    }

    public void setNumberTpl(String numberTpl) {
        this.numberTpl = numberTpl;
    }

    public String getHotRowNumber() {
        return hotRowNumber;
    }

    public void setHotRowNumber(String hotRowNumber) {
        this.hotRowNumber = hotRowNumber;
    }

    public String getHotRowCellCap() {
        return hotRowCellCap;
    }

    public void setHotRowCellCap(String hotRowCellCap) {
        this.hotRowCellCap = hotRowCellCap;
    }

    public String getHotRowRequired() {
        return hotRowRequired;
    }

    public void setHotRowRequired(String hotRowRequired) {
        this.hotRowRequired = hotRowRequired;
    }


    public List<Object> getRawData() {
        return rawData;
    }

    public void setRawData(List<Object> rawData) {
        this.rawData = rawData;
    }


    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }

    public Map<String, String> getRowOptions() {
        return rowOptions;
    }

    public void setRowOptions(Map<String, String> rowOptions) {
        this.rowOptions = rowOptions;
    }

    public HotRowModeType getHotRowMode() {
        return hotRowMode;
    }

    public ActiveModeType getActiveMode() {
        return activeMode;
    }

    public void setActiveMode(ActiveModeType activeMode) {
        this.activeMode = activeMode;
    }

    public Integer getFreezedRow() {
        return freezedRow;
    }

    public void setFreezedRow(Integer freezedRow) {
        this.freezedRow = freezedRow;
    }

    public Integer getFreezedColumn() {
        return freezedColumn;
    }

    public void setFreezedColumn(Integer freezedColumn) {
        this.freezedColumn = freezedColumn;
    }

    public void setHotRowMode(HotRowModeType hotRowMode) {
        this.hotRowMode = hotRowMode;
    }

    public Boolean getRowHandler() {
        return rowHandler;
    }

    public void setRowHandler(Boolean rowHandler) {
        this.rowHandler = rowHandler;
    }

    public Boolean getShowHeader() {
        return showHeader;
    }

    public void setShowHeader(Boolean showHeader) {
        this.showHeader = showHeader;
    }
}
