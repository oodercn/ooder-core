package net.ooder.esd.tool.properties.form;

public class LayoutMerged {

    public Integer row = 1;

    public Integer col = 0;

    public Integer rowspan = 1;

    public Integer colspan = 1;

    Boolean removed = false;

    public Boolean getRemoved() {
        return removed;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public Boolean isRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }


}
