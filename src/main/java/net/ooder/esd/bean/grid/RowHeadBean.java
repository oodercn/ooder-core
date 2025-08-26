package net.ooder.esd.bean.grid;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.MRowHead;
import net.ooder.esd.annotation.RowHead;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.tool.properties.GridProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = RowHead.class)
public class RowHeadBean implements CustomBean {

    String rowHandlerWidth;

    String gridHandlerCaption;

    Boolean rowNumbered;

    String headerHeight;

    Boolean rowHandler;

    SelModeType selMode;

    Boolean firstCellEditable;


    public RowHeadBean() {


    }

    public void update(GridProperties properties) {
        this.rowNumbered = properties.getRowNumbered();
        this.selMode = properties.getSelMode();
        this.rowHandlerWidth = properties.getRowHandlerWidth();
        this.gridHandlerCaption = properties.getGridHandlerCaption();
        this.headerHeight = properties.getHeaderHeight();
        this.rowHandler = properties.getRowHandler();

    }

    public RowHeadBean(GridProperties properties) {
        AnnotationUtil.fillDefaultValue(RowHead.class, this);
        this.update(properties);

    }


    public RowHeadBean(RowHead annotation) {
        this.rowHandlerWidth = annotation.rowHandlerWidth();
        this.gridHandlerCaption = annotation.gridHandlerCaption();
        this.rowNumbered = annotation.rowNumbered();
        this.headerHeight = annotation.headerHeight();
        this.selMode = annotation.selMode();
        this.rowHandler = annotation.rowHandler();
        this.firstCellEditable = annotation.firstCellEditable();
    }

    public RowHeadBean(MRowHead annotation) {
        this.rowHandlerWidth = annotation.rowHandlerWidth();
        this.gridHandlerCaption = annotation.gridHandlerCaption();
        this.rowNumbered = annotation.rowNumbered();
        this.headerHeight = annotation.headerHeight();
        this.selMode = annotation.selMode();
        this.rowHandler = annotation.rowHandler();
        this.firstCellEditable = annotation.firstCellEditable();
    }

    public Boolean getFirstCellEditable() {
        return firstCellEditable;
    }

    public void setFirstCellEditable(Boolean firstCellEditable) {
        this.firstCellEditable = firstCellEditable;
    }

    public Boolean getRowHandler() {
        return rowHandler;
    }

    public void setRowHandler(Boolean rowHandler) {
        this.rowHandler = rowHandler;
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

    public Boolean getRowNumbered() {
        return rowNumbered;
    }

    public void setRowNumbered(Boolean rowNumbered) {
        this.rowNumbered = rowNumbered;
    }

    public String getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(String headerHeight) {
        this.headerHeight = headerHeight;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
