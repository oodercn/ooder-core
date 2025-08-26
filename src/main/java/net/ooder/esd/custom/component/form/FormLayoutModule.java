package net.ooder.esd.custom.component.form;

import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.ManualType;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.custom.component.form.field.CustomFieldInputComponent;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.properties.form.HeadUtil;
import net.ooder.esd.tool.properties.form.LayoutCell;
import net.ooder.esd.tool.properties.form.LayoutData;
import net.ooder.esd.tool.properties.form.LayoutMerged;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.*;

public class FormLayoutModule {

    LayoutData layoutData;

    List<Component> inputComponents = null;

    Map<String, LayoutCell> cellMap = new HashMap<>();

    Map<String, FieldFormConfig> componentsMap = new HashMap<>();

    Map<String, Object> valueMap = new HashMap<>();

    CustomFormViewBean viewBean;

    FormLayoutComponent formComponent;

    HAlignType textAlign = HAlignType.center;

    EUModule euModule;


    public FormLayoutModule(EUModule euModule, FormLayoutComponent formComponent, List<FieldFormConfig> formFieldList, Map dbMap, CustomFormViewBean viewBean) {
        this.viewBean = viewBean;
        this.euModule = euModule;
        this.formComponent = formComponent;
        List<FieldFormConfig> fieldList = new ArrayList<>();
        List<ESDFieldConfig> hiddenFieldList = new ArrayList<>();
        for (FieldFormConfig fieldFormConfig : formFieldList) {
            CustomFieldBean fieldBean = fieldFormConfig.getCustomBean();
            if (fieldFormConfig.getComponentType().equals(ComponentType.HIDDENINPUT)) {
                hiddenFieldList.add(fieldFormConfig);
            } else if (fieldBean != null) {
                if (fieldBean.getHidden() != null && fieldBean.getHidden()) {
                    hiddenFieldList.add(fieldFormConfig);
                } else if (fieldBean.getTarget() != null && fieldBean.getTarget().equals(fieldFormConfig.getFieldname().toUpperCase()) && fieldFormConfig.getComponentType().equals(ComponentType.LABEL)) {

                } else {
                    fieldList.add(fieldFormConfig);
                }
            }
        }

        if (hiddenFieldList != null && hiddenFieldList.size() > 0) {
            euModule.getComponent().getCtxBaseComponent().addFields(hiddenFieldList, new HashMap());
        }
        if (dbMap != null) {
            this.valueMap = dbMap;
        }
        Integer col = 2;
        if (viewBean != null && viewBean.getCol() != null) {
            col = viewBean.getCol();
            if (viewBean.getTextAlign() != null) {
                this.textAlign = viewBean.getTextAlign();
            }

        }

        if (viewBean.getAutoLayout() != null && !viewBean.getAutoLayout() && !reLayout(fieldList)) {
            paint(viewBean, fieldList);
        } else {
            autoPaint(col, fieldList);

        }

    }

    private boolean reLayout(List<FieldFormConfig> formFieldList) {
        for (FieldFormConfig fieldFormConfig : formFieldList) {
            if ((fieldFormConfig.getCustomBean() == null) || fieldFormConfig.getCustomBean().getTarget() == null || fieldFormConfig.getCustomBean().getTarget().equals("")) {
                return true;
            }
        }
        return false;
    }

    public void paint(CustomFormViewBean viewBean, List<FieldFormConfig> formFieldList) {
        inputComponents = new ArrayList<>();
        this.layoutData = viewBean.getLayoutData();
        if (layoutData == null) {
            layoutData = new LayoutData(viewBean);
        }

        for (FieldFormConfig fieldInfo : formFieldList) {
            FieldBean fieldBean = fieldInfo.getFieldBean();
            LabelBean labelBean = fieldInfo.getLabelBean();
            Object value = null;
            if (valueMap != null) {
                value = valueMap.get(fieldInfo.getFieldname());
                valueMap.put("parent", formComponent);
            }

            int colSpan = fieldBean.getColSpan() == null ? 1 : fieldBean.getColSpan();
            int rowSpan = fieldBean.getRowSpan() == null ? 1 : fieldBean.getRowSpan();


            if (fieldBean.getRawColSpan() != null) {
                colSpan = fieldBean.getRawColSpan();
            }
            if (fieldBean.getRawRowSpan() != null) {
                rowSpan = fieldBean.getRawRowSpan();
            }


            ComponentType componentType = fieldInfo.getComponentType();
            if (fieldInfo.getComboConfig() != null && (fieldInfo.getWidgetConfig() instanceof ComboInputFieldBean) && !componentType.equals(ComponentType.MODULE)) {
                componentType = ComponentType.COMBOINPUT;
            }
            String target = fieldInfo.getCustomBean().getTarget() == null ? fieldInfo.getFieldname() : fieldInfo.getCustomBean().getTarget();
            String colKey = target.substring(0, 1);
            String rowKey = target.substring(1, target.length());
            Integer colIndex = Arrays.asList(HeadUtil.cellHead).indexOf(colKey);

            if (fieldBean.getHaslabel() != null && fieldBean.getHaslabel() && colIndex > 0 && !fieldInfo.getComponentType().equals(ComponentType.LABEL)) {
                String lastColKey = HeadUtil.cellHead[colIndex - 1] + rowKey;
                LayoutCell cell = new LayoutCell(lastColKey);
                if (labelBean != null && labelBean.getLabelHAlign() != null) {
                    cell.addStyle("textAlign", labelBean.getLabelHAlign().name());
                } else {
                    cell.addStyle("textAlign", textAlign.name());
                }

                Integer manualWidth = viewBean.getDefaultLabelWidth();
                if (labelBean != null && labelBean.getManualWidth() != null) {
                    manualWidth = labelBean.getManualWidth();
                }

                layoutData.addColSetting(HeadUtil.cellHead[colIndex - 1], ManualType.manualWidth, manualWidth);
                if (labelBean != null && labelBean.getLabelCaption() != null && !labelBean.getLabelCaption().equals("")) {
                    cell.setValue(labelBean.getLabelCaption());
                } else if (fieldInfo.getAggConfig().getCaption() != null) {
                    cell.setValue(fieldInfo.getAggConfig().getCaption());
                }
                valueMap.put(lastColKey, cell.getValue());
                cellMap.put(lastColKey, cell);
                layoutData.addCol(lastColKey, cell);
            }

            if (colSpan > 1 || rowSpan > 1) {
                LayoutMerged layoutMerged = new LayoutMerged();
                layoutMerged.setRow(TypeUtils.castToInt(rowKey) - 1);
                layoutMerged.setColspan(colSpan);
                layoutMerged.setCol(colIndex);
                layoutData.addMerged(layoutMerged);
            }

            Component inputComponent = null;
            switch (componentType) {
                case COMBOINPUT:
                    ComboBoxBean comboBoxBean = fieldInfo.getComboConfig();
                    Class<Component> defaultFieldClass = CustomViewConfigFactory.getInstance().getComboBoxComponent(comboBoxBean.getInputType());
                    try {
                        if (defaultFieldClass != null) {
                            Constructor<Component> constructor = defaultFieldClass.getConstructor(new Class[]{EUModule.class, FieldFormConfig.class, String.class, Object.class, Map.class});
                            inputComponent = constructor.newInstance(euModule, fieldInfo, target, value, valueMap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    defaultFieldClass = CustomViewConfigFactory.getInstance().getComponent(componentType);
                    if (defaultFieldClass != null && fieldInfo.getWidgetConfig() != null) {
                        try {
                            Constructor<Component> constructor = defaultFieldClass.getConstructor(new Class[]{EUModule.class, FieldFormConfig.class, String.class, Object.class, Map.class});
                            inputComponent = constructor.newInstance(euModule, fieldInfo, target, value, valueMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        inputComponent = new CustomFieldInputComponent(euModule, fieldInfo, target, value, valueMap);
                    }
            }


            if (viewBean != null && viewBean.getDefaultRowHeight() != null && viewBean.getDefaultRowHeight() != -1) {
                layoutData.addRowSetting(rowKey + "", ManualType.manualHeight, viewBean.getDefaultRowHeight());
            }

            if (viewBean != null && viewBean.getDefaultColWidth() != null && viewBean.getDefaultColWidth() != -1) {
                layoutData.addColSetting(colKey, ManualType.manualWidth, viewBean.getDefaultColWidth());
            }


            Integer manualHeight = fieldBean.getManualHeight();

            if (manualHeight != null && viewBean.getDefaultRowHeight() != null && manualHeight != viewBean.getDefaultRowHeight()) {
                layoutData.addRowSetting(rowKey + "", ManualType.manualHeight, manualHeight);
            }

            Integer manualWidth = fieldInfo.getFieldBean().getManualWidth();
            if (manualWidth != null && viewBean.getDefaultColWidth() != null && manualWidth != viewBean.getDefaultColWidth()) {
                layoutData.addColSetting(colKey + "", ManualType.manualWidth, manualWidth);
            }


            if (fieldBean.getExpression() != null && !fieldBean.getExpression().equals("")) {
                inputComponent.getProperties().setExpression(fieldBean.getExpression());
            }


            componentsMap.put(target, fieldInfo);
            valueMap.put(target, fieldInfo.getAggConfig().getValue());
            inputComponents.add(inputComponent);


        }

        formComponent.getProperties().setLayoutData(layoutData);
        if (viewBean != null && viewBean.getDefaultRowHeight() != null && viewBean.getDefaultRowHeight() != -1) {
            formComponent.getProperties().setDefaultRowHeight(viewBean.getDefaultRowHeight());
        }
        if (viewBean != null && viewBean.getDefaultColWidth() != null && viewBean.getDefaultColWidth() != -1) {
            formComponent.getProperties().setDefaultColWidth(viewBean.getDefaultColWidth());
        }


        List<Component> inputComponents = this.getInputComponents();
        for (Component component : inputComponents) {
            if (component != null) {
                formComponent.addChildren(component);
            }
        }


    }

    public void autoPaint(int col, List<FieldFormConfig> fieldList) {

        inputComponents = new ArrayList<>();
        this.layoutData = new LayoutData(col, fieldList.size());
        String[] cellHead = HeadUtil.cellHead;
        int start = 0;
        int rowkey = 0;
        while (start < fieldList.size()) {
            rowkey = rowkey + 1;
            int k = 0;
            while (k < (col * 2 - 1) && start < fieldList.size()) {
                FieldFormConfig fieldInfo = fieldList.get(start);
                FieldBean fieldBean = fieldInfo.getFieldBean();
                LabelBean labelBean = fieldInfo.getLabelBean();
                Object value = null;
                if (valueMap != null) {
                    value = valueMap.get(fieldInfo.getFieldname());
                    valueMap.put("parent", formComponent);
                }


                int colSpan = fieldBean.getColSpan() == null ? 1 : fieldBean.getColSpan();
                int rowSpan = fieldBean.getRowSpan() == null ? 1 : fieldBean.getRowSpan();


                //如果 colspan=-1,则代表铺满 一行
                if (colSpan == -1) {
                    colSpan = (col * 2);
                } else {
                    colSpan = (colSpan * 2);
                }
                //是否显示标签
                if (fieldBean.getHaslabel() != null && fieldBean.getHaslabel() && !fieldInfo.getComponentType().equals(ComponentType.LABEL)) {
                    colSpan = colSpan - 1;
                }

//                if (fieldBean.getRawColSpan() != null) {
//                    colSpan = fieldBean.getRawColSpan();
//                }
//                if (fieldBean.getRawRowSpan() != null) {
//                    rowSpan = fieldBean.getRawRowSpan();
//                }


                //避免溢出行
                if ((k + colSpan) <= 2 * col) {
                    //列标 A,B
                    String colKey = cellHead[k];
                    //列标 A1 ,B2
                    String titleCellKey = colKey + rowkey;
                    ComponentType componentType = fieldInfo.getComponentType();
                    if (fieldInfo.getComboConfig() != null && (fieldInfo.getWidgetConfig() instanceof ComboInputFieldBean) && !componentType.equals(ComponentType.MODULE)) {
                        componentType = ComponentType.COMBOINPUT;
                    }

                    if (fieldBean.getHaslabel() != null && fieldBean.getHaslabel() && !fieldInfo.getComponentType().equals(ComponentType.LABEL)) {
                        LayoutCell cell = new LayoutCell(titleCellKey);
                        cell.addStyle("textAlign", textAlign.name());
                        if (labelBean != null && labelBean.getManualWidth() != null) {
                            layoutData.addColSetting(colKey, ManualType.manualWidth, labelBean.getManualWidth());
                        } else {
                            layoutData.addColSetting(colKey, ManualType.manualWidth, viewBean.getDefaultColWidth());
                        }

                        if (labelBean != null && labelBean.getLabelCaption() != null && !labelBean.getLabelCaption().equals("")) {
                            cell.setValue(fieldInfo.getLabelBean().getLabelCaption());
                        } else if (fieldInfo.getAggConfig().getCaption() != null) {
                            cell.setValue(fieldInfo.getAggConfig().getCaption());
                        }
                        valueMap.put(titleCellKey, cell.getValue());
                        cellMap.put(titleCellKey, cell);
                        layoutData.addCol(titleCellKey, cell);
                        //步进+1
                        k = k + 1;
                    }

                    if (colSpan > 1 || rowSpan > 1) {
                        LayoutMerged layoutMerged = new LayoutMerged();
                        layoutMerged.setRow(rowkey - 1);
                        layoutMerged.setRowspan(rowSpan);
                        layoutMerged.setColspan(colSpan);
                        layoutMerged.setCol(k);
                        layoutData.addMerged(layoutMerged);
                    }
                    String valueCellKey = cellHead[k] + rowkey;
                    Component inputComponent = null;
                    switch (componentType) {
                        case COMBOINPUT:
                            ComboBoxBean comboBoxBean = fieldInfo.getComboConfig();
                            Class<Component> defaultFieldClass = CustomViewConfigFactory.getInstance().getComboBoxComponent(comboBoxBean.getInputType());
                            try {
                                if (defaultFieldClass != null) {
                                    Constructor<Component> constructor = defaultFieldClass.getConstructor(new Class[]{EUModule.class, FieldFormConfig.class, String.class, Object.class, Map.class});
                                    inputComponent = constructor.newInstance(euModule, fieldInfo, valueCellKey, value, valueMap);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            defaultFieldClass = CustomViewConfigFactory.getInstance().getComponent(componentType);
                            if (defaultFieldClass != null) {
                                try {
                                    Constructor<Component> constructor = defaultFieldClass.getConstructor(new Class[]{EUModule.class, FieldFormConfig.class, String.class, Object.class, Map.class});
                                    inputComponent = constructor.newInstance(euModule, fieldInfo, valueCellKey, value, valueMap);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                inputComponent = new CustomFieldInputComponent(euModule, fieldInfo, valueCellKey, value, valueMap);
                            }
                    }

                    //保存相对定位
                    fieldInfo.getCustomBean().setTarget(valueCellKey);


                    if (viewBean != null && viewBean.getDefaultRowHeight() != null && viewBean.getDefaultRowHeight() != -1) {
                        layoutData.addRowSetting(rowkey + "", ManualType.manualHeight, viewBean.getDefaultRowHeight());
                    } else {
                        layoutData.addRowSetting(rowkey + "", ManualType.manualHeight, AnnotationUtil.getDefaultValue(FormAnnotation.class, "defaultRowHeight"));
                    }

                    if (viewBean != null && viewBean.getDefaultColWidth() != null && viewBean.getDefaultColWidth() != -1) {
                        layoutData.addColSetting(cellHead[k], ManualType.manualWidth, viewBean.getDefaultColWidth());
                    } else {
                        layoutData.addColSetting(cellHead[k], ManualType.manualWidth, AnnotationUtil.getDefaultValue(FormAnnotation.class, "defaultColWidth"));
                    }


                    Integer manualHeight = fieldBean.getManualHeight();


                    if (manualHeight != null && viewBean.getDefaultRowHeight() != null && manualHeight != viewBean.getDefaultRowHeight()) {
                        layoutData.addRowSetting(rowkey + "", ManualType.manualHeight, manualHeight);
                    }

                    Integer manualWidth = fieldBean.getManualWidth();
                    if (manualWidth != null && viewBean.getDefaultColWidth() != null && manualWidth != viewBean.getDefaultColWidth()) {
                        layoutData.addColSetting(cellHead[k] + "", ManualType.manualWidth, manualWidth);
                    }


                    if (fieldBean.getExpression() != null && !fieldBean.getExpression().equals("")) {
                        inputComponent.getProperties().setExpression(fieldBean.getExpression());
                    }


                    componentsMap.put(valueCellKey, fieldInfo);
                    valueMap.put(titleCellKey, fieldInfo.getAggConfig().getValue());
                    inputComponents.add(inputComponent);
                    //继续步进
                    start = start + 1;
                }
                k = k + colSpan;
            }


        }
        this.layoutData.setRows(rowkey);

        formComponent.getProperties().setLayoutData(layoutData);
        if (viewBean != null && viewBean.getDefaultRowHeight() != null && viewBean.getDefaultRowHeight() != -1) {
            formComponent.getProperties().setDefaultRowHeight(viewBean.getDefaultRowHeight());
        }
        if (viewBean != null && viewBean.getDefaultColWidth() != null && viewBean.getDefaultColWidth() != -1) {
            formComponent.getProperties().setDefaultColWidth(viewBean.getDefaultColWidth());
        }


        List<Component> inputComponents = this.getInputComponents();
        for (Component component : inputComponents) {
            if (component != null) {
                formComponent.addChildren(component);
            }
        }

        try {
            viewBean.setAutoLayout(false);
            viewBean.setLayoutData(layoutData);
            DSMFactory.getInstance().saveCustomViewBean(viewBean);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    public CustomFormViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(CustomFormViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public FormLayoutComponent getFormComponent() {
        return formComponent;
    }

    public void setFormComponent(FormLayoutComponent formComponent) {
        this.formComponent = formComponent;
    }

    public HAlignType getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(HAlignType textAlign) {
        this.textAlign = textAlign;
    }

    public EUModule getEuModule() {
        return euModule;
    }

    public void setEuModule(EUModule euModule) {
        this.euModule = euModule;
    }

    public Map<String, LayoutCell> getCellMap() {
        return cellMap;
    }

    public void setCellMap(Map<String, LayoutCell> cellMap) {
        this.cellMap = cellMap;
    }

    public Map<String, FieldFormConfig> getComponentsMap() {
        return componentsMap;
    }

    public void setComponentsMap(Map<String, FieldFormConfig> componentsMap) {
        this.componentsMap = componentsMap;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }


    public LayoutData getLayoutData() {
        return layoutData;
    }

    public void setLayoutData(LayoutData layoutData) {
        this.layoutData = layoutData;
    }

    public List<Component> getInputComponents() {
        return inputComponents;
    }

    public void setInputComponents(List<Component> inputComponents) {
        this.inputComponents = inputComponents;
    }
}
