package net.ooder.esd.tool.properties;

public class Cell extends FieldProperties {
    String value;
    String formula;
    String colRenderer;
    String cellRenderer;
    String cellClass;
    String cellStyle;
    Boolean disabled;
    Boolean readonly;
    Boolean editable;
    Integer increment;
    Integer min;
    Integer max;
    Integer maxlength;
    String groupingSeparator;
    String decimalSeparator;
    Boolean forceFillZero;
    String numberTpl;
    String currencyTpl;
    String dateEditorTpl;

    public Cell(String key, Object value) {
        this.id = key.toLowerCase();
        this.value = value==null ?"":value.toString();
        this.caption= value==null ?"":value.toString();

    }

    public Cell() {

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getColRenderer() {
        return colRenderer;
    }

    public void setColRenderer(String colRenderer) {
        this.colRenderer = colRenderer;
    }

    public String getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(String cellRenderer) {
        this.cellRenderer = cellRenderer;
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

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    public String getGroupingSeparator() {
        return groupingSeparator;
    }

    public void setGroupingSeparator(String groupingSeparator) {
        this.groupingSeparator = groupingSeparator;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public Boolean getForceFillZero() {
        return forceFillZero;
    }

    public void setForceFillZero(Boolean forceFillZero) {
        this.forceFillZero = forceFillZero;
    }

    public String getNumberTpl() {
        return numberTpl;
    }

    public void setNumberTpl(String numberTpl) {
        this.numberTpl = numberTpl;
    }

    public String getCurrencyTpl() {
        return currencyTpl;
    }

    public void setCurrencyTpl(String currencyTpl) {
        this.currencyTpl = currencyTpl;
    }

    public String getDateEditorTpl() {
        return dateEditorTpl;
    }

    public void setDateEditorTpl(String dateEditorTpl) {
        this.dateEditorTpl = dateEditorTpl;
    }


}
