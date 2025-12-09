package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.FormLayModeType;
import net.ooder.esd.annotation.ui.StretchType;
import net.ooder.esd.bean.field.CustomLayoutFieldBean;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class FormLayoutProperties extends PanelProperties {

    public Boolean floatHandler = false;
    public Boolean solidGridlines = true;
    public LayoutData layoutData;
    public Integer rowHeaderWidth;
    public String rendererCDNJS;
    public String rendererCDNCSS;
    public Integer columnHeaderHeight;
    public Integer defaultRowSize;
    public Integer defaultColumnSize;
    public Integer defaultRowHeight;
    public Integer defaultColWidth;
    public Integer lineSpacing;
    public StretchType stretchHeight = StretchType.none;
    public StretchType stretchH = StretchType.all;
    public String cssStyle;

    public FormLayModeType mode;

    public FormLayoutProperties(Dock dock) {
        super(dock);

    }
    public FormLayoutProperties(CustomFormViewBean viewBean,CustomLayoutFieldBean.CustomFormLayoutFieldBean fieldBean) {
        super();
        if (viewBean.getContainerBean() != null) {
            this.init(viewBean.getContainerBean());
        }
        init(viewBean);
        this.initFieldBean(fieldBean);
    }

    public FormLayoutProperties(CustomFormViewBean viewBean) {
        super();
        if (viewBean.getContainerBean() != null) {
            this.init(viewBean.getContainerBean());
        }
        init(viewBean);

    }

    public void initFieldBean(CustomLayoutFieldBean.CustomFormLayoutFieldBean fieldBean) {
        this.panelBgImg = fieldBean.getBgimg();
        this.imageClass = fieldBean.getImageClass();
        this.panelBgClr = fieldBean.getBackgroundColor();
        this.borderType = fieldBean.getBorderType();
    }

    void init(CustomFormViewBean viewBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(viewBean), Map.class), this, false, false);
    }

    public FormLayoutProperties() {
        super();
    }

    public Integer getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(Integer lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public Integer getDefaultColWidth() {
        return defaultColWidth;
    }

    public void setDefaultColWidth(Integer defaultColWidth) {
        this.defaultColWidth = defaultColWidth;
    }

    public Boolean getFloatHandler() {
        return floatHandler;
    }

    public Integer getRowHeaderWidth() {
        return rowHeaderWidth;
    }

    public void setRowHeaderWidth(Integer rowHeaderWidth) {
        this.rowHeaderWidth = rowHeaderWidth;
    }

    public String getRendererCDNJS() {
        return rendererCDNJS;
    }

    public void setRendererCDNJS(String rendererCDNJS) {
        this.rendererCDNJS = rendererCDNJS;
    }

    public String getRendererCDNCSS() {
        return rendererCDNCSS;
    }

    public void setRendererCDNCSS(String rendererCDNCSS) {
        this.rendererCDNCSS = rendererCDNCSS;
    }

    public Integer getColumnHeaderHeight() {
        return columnHeaderHeight;
    }

    public void setColumnHeaderHeight(Integer columnHeaderHeight) {
        this.columnHeaderHeight = columnHeaderHeight;
    }

    public Integer getDefaultRowSize() {
        return defaultRowSize;
    }

    public void setDefaultRowSize(Integer defaultRowSize) {
        this.defaultRowSize = defaultRowSize;
    }

    public Integer getDefaultColumnSize() {
        return defaultColumnSize;
    }

    public void setDefaultColumnSize(Integer defaultColumnSize) {
        this.defaultColumnSize = defaultColumnSize;
    }

    public Integer getDefaultRowHeight() {
        return defaultRowHeight;
    }

    public void setDefaultRowHeight(Integer defaultRowHeight) {
        this.defaultRowHeight = defaultRowHeight;
    }

    public StretchType getStretchH() {
        return stretchH;
    }

    public void setStretchH(StretchType stretchH) {
        this.stretchH = stretchH;
    }

    public StretchType getStretchHeight() {
        return stretchHeight;
    }

    public void setStretchHeight(StretchType stretchHeight) {
        this.stretchHeight = stretchHeight;
    }

    public FormLayModeType getMode() {
        return mode;
    }

    public void setMode(FormLayModeType mode) {
        this.mode = mode;
    }


    public Boolean isFloatHandler() {
        return floatHandler;
    }

    public void setFloatHandler(Boolean floatHandler) {
        this.floatHandler = floatHandler;
    }

    public LayoutData getLayoutData() {
        return layoutData;
    }

    public void setLayoutData(LayoutData layoutData) {
        this.layoutData = layoutData;
    }


    public Boolean getSolidGridlines() {
        return solidGridlines;
    }

    public void setSolidGridlines(Boolean solidGridlines) {
        this.solidGridlines = solidGridlines;
    }

}

