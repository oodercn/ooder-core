package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.ImagePos;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.InputBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.field.combo.ComboxFieldBean;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;

public class ComboInputProperties extends InputProperties {

    public String imageBgSize;
    public String iconFontCode;
    public String image;
    public ImagePos imagePos;
    public String fontColor;
    public String fontSize;
    public String fontWeight;
    public String fontFamily;
    public Boolean cachePopWnd;
    public String filter;
    public String itemsExpression;
    public String dateEditorTpl;
    public String groupingSeparator;
    public String decimalSeparator;
    public Boolean forceFillZero;
    public Boolean trimTailZero;
    public String parentID;
    public String popCtrlProp;
    public List<String> popCtrlEvents;
    public String dropImageClass;
    public String unit;
    public String units;
    public String numberTpl;
    public String currencyTpl;
    public Integer dropListWidth;
    public Integer dropListHeight;
    public String showMode;
    public Integer precision;
    public Double increment;
    public String commandBtn;
    public Class bindClass;
    public Boolean inputReadonly;
    public String min;
    public String max;
    public String fileAccept;
    public Boolean fileMultiple;


    public ComboInputProperties() {

    }

    public ComboInputProperties(FieldFormConfig<ComboInputFieldBean, ? extends ComboBoxBean> field) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(field.getFieldBean()), Map.class), this, false, false);
        ContainerBean containerBean = field.getContainerBean();
        ComboInputFieldBean comboInputFieldBean = (ComboInputFieldBean) field.getComboConfig();
        CustomFieldBean customFieldBean = field.getCustomBean();
        if (field.getWidgetConfig() != null && field.getWidgetConfig().getInputBean() != null) {
            InputBean inputFieldBean = field.getWidgetConfig().getInputBean();
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(inputFieldBean), Map.class), this, false, false);
        }
        if (containerBean != null) {
            this.init(containerBean);
        }
        FieldBean fieldBean = field.getFieldBean();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(comboInputFieldBean), Map.class), this, false, false);
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        FieldAggConfig aggConfig = field.getAggConfig();

        this.readonly = aggConfig.getReadonly();
        this.disabled = aggConfig.getDisabled();
        this.caption = aggConfig.getCaption();
        this.name = field.getFieldname();
        if (field.getAggConfig().getEnumClass() != null) {
            this.enumClass = field.getAggConfig().getEnumClass();
        }
        if (fieldBean != null) {
            if (fieldBean.getExpression() != null && !fieldBean.getExpression().equals("")) {
                this.setExpression(fieldBean.getExpression());
            }
        }

        if (customFieldBean != null) {
            if (customFieldBean.getCaption() != null && !customFieldBean.getCaption().equals("")) {
                this.desc = customFieldBean.getCaption();
            }
            if (customFieldBean.getReadonly() != null && customFieldBean.getReadonly()) {
                this.setReadonly(true);
            }
            if (customFieldBean.getDisabled() != null && customFieldBean.getDisabled()) {
                this.setDisabled(true);
            }
            if (customFieldBean.getImageClass() != null && !customFieldBean.getImageClass().equals("")) {
                this.imageClass = customFieldBean.getImageClass();
            }
        }

        this.setType(comboInputFieldBean.getInputType());
        if (field.getAggConfig().getEnumClass() != null) {
            this.setEnumClass(field.getAggConfig().getEnumClass());
        }

        if (field.getColHidden() != null && field.getColHidden()) {
            this.setVisibility(VisibilityType.hidden);
        } else {
            this.setVisibility(VisibilityType.visible);
        }
        this.setCaption(field.getAggConfig().getCaption());
        this.setType(comboInputFieldBean.getInputType());
        if (comboInputFieldBean instanceof ComboxFieldBean) {
            CustomListBean customListBean = ((ComboxFieldBean) comboInputFieldBean).getListBean().getCustomListBean();
            if (customListBean != null) {
                this.setFilter(customListBean.getFilter());
                List<TreeListItem> items = customListBean.getItems();
                for (TreeListItem item : items) {
                    this.addItem(item);
                }
                this.setDynLoad(customListBean.getDynLoad());

                if ((customListBean.getFilter() != null && !customListBean.getFilter().equals(""))) {
                    this.setFilter(customListBean.getFilter());
                }
                if ((customListBean.getItemsExpression() != null && !customListBean.getItemsExpression().equals(""))) {
                    this.setItemsExpression(customListBean.getItemsExpression());
                }
            }

        }


        if (comboInputFieldBean.getLabelCaption() != null && comboInputFieldBean.getLabelCaption().equals("")) {
            this.setLabelCaption(comboInputFieldBean.getLabelCaption());
        } else {
            this.setLabelCaption(aggConfig.getCaption());
        }


        if (field.getTipsBean() != null) {
            OgnlUtil.setProperties((Map) JSON.toJSON(field.getTipsBean()), this, false, false);

        }
        if (field.getLabelBean() != null) {
            OgnlUtil.setProperties((Map) JSON.toJSON(field.getLabelBean()), this, false, false);

        }
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public ImagePos getImagePos() {
        return imagePos;
    }

    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }


    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public String getItemsExpression() {
        return itemsExpression;
    }

    public void setItemsExpression(String itemsExpression) {
        this.itemsExpression = itemsExpression;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Boolean getCachePopWnd() {
        return cachePopWnd;
    }

    public void setCachePopWnd(Boolean cachePopWnd) {
        this.cachePopWnd = cachePopWnd;
    }

    public String getDateEditorTpl() {
        return dateEditorTpl;
    }

    public void setDateEditorTpl(String dateEditorTpl) {
        this.dateEditorTpl = dateEditorTpl;
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

    public Boolean getTrimTailZero() {
        return trimTailZero;
    }

    public void setTrimTailZero(Boolean trimTailZero) {
        this.trimTailZero = trimTailZero;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getPopCtrlProp() {
        return popCtrlProp;
    }

    public void setPopCtrlProp(String popCtrlProp) {
        this.popCtrlProp = popCtrlProp;
    }

    public List<String> getPopCtrlEvents() {
        return popCtrlEvents;
    }

    public void setPopCtrlEvents(List<String> popCtrlEvents) {
        this.popCtrlEvents = popCtrlEvents;
    }

    public String getDropImageClass() {
        return dropImageClass;
    }

    public void setDropImageClass(String dropImageClass) {
        this.dropImageClass = dropImageClass;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
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


    public Integer getDropListWidth() {
        return dropListWidth;
    }

    public void setDropListWidth(Integer dropListWidth) {
        this.dropListWidth = dropListWidth;
    }

    public Integer getDropListHeight() {
        return dropListHeight;
    }

    public void setDropListHeight(Integer dropListHeight) {
        this.dropListHeight = dropListHeight;
    }


    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(String showMode) {
        this.showMode = showMode;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Double getIncrement() {
        return increment;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getCommandBtn() {
        return commandBtn;
    }

    public void setCommandBtn(String commandBtn) {
        this.commandBtn = commandBtn;
    }

    public Boolean getInputReadonly() {
        return inputReadonly;
    }

    public void setInputReadonly(Boolean inputReadonly) {
        this.inputReadonly = inputReadonly;
    }

    public String getFileAccept() {
        return fileAccept;
    }

    public void setFileAccept(String fileAccept) {
        this.fileAccept = fileAccept;
    }

    public Boolean getFileMultiple() {
        return fileMultiple;
    }

    public void setFileMultiple(Boolean fileMultiple) {
        this.fileMultiple = fileMultiple;
    }
}
