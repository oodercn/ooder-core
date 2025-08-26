package net.ooder.esd.custom.component.form;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.ManualType;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.custom.component.form.field.CustomFieldInputComponent;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.MFormLayoutComponent;
import net.ooder.esd.tool.properties.form.HeadUtil;
import net.ooder.esd.tool.properties.form.LayoutCell;
import net.ooder.esd.tool.properties.form.LayoutData;
import net.ooder.esd.tool.properties.form.LayoutMerged;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MFormLayoutModule {

    LayoutData layoutData;

    List<Component> inputComponents = null;

    Map<String, LayoutCell> cellMap = new HashMap<>();

    Map<String, FieldFormConfig> componentsMap = new HashMap<>();

    Map<String, Object> valueMap = new HashMap<>();

    CustomFormViewBean viewBean;

    MFormLayoutComponent formComponent;

    HAlignType textAlign = HAlignType.center;

    EUModule euModule;


    public MFormLayoutModule(EUModule euModule, MFormLayoutComponent formComponent, List<FieldFormConfig> formFieldList, Map dbMap, CustomFormViewBean viewBean) {
        this.viewBean = viewBean;
        this.euModule = euModule;
        this.formComponent = formComponent;


        List<FieldFormConfig> fieldList = new ArrayList<>();
        for (FieldFormConfig fieldFormConfig : formFieldList) {
            if ((fieldFormConfig.getColHidden() == null || !fieldFormConfig.getColHidden()) && !fieldFormConfig.getComponentType().equals(ComponentType.HIDDENINPUT)) {
                fieldList.add(fieldFormConfig);
            }
        }
        if (dbMap != null) {
            this.valueMap = dbMap;
        }

        Integer col = 2;
        if (viewBean != null && viewBean.getCol() != null) {
            col = viewBean.getCol();
            this.textAlign = viewBean.getTextAlign();
        }
        paint(col, fieldList);
    }

    public void paint(int col, List<FieldFormConfig> fieldList) {

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
                    valueMap.put("parent",formComponent);
                }
                int colSpan = fieldInfo.getFieldBean().getColSpan();
                int rowSpan = fieldInfo.getFieldBean().getRowSpan();
                //如果 colspan=-1,则代表铺满 一行
                if (colSpan == -1) {
                    colSpan = (col * 2);
                    //colSpan = (col * 2) - 1;
                } else {
                    colSpan = (colSpan * 2);
                }
                //是否显示标签
                if (fieldInfo.getFieldBean().getHaslabel()) {
                    colSpan = colSpan - 1;
                }


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

                    if (fieldInfo.getFieldBean().getHaslabel()) {
                        LayoutCell cell = new LayoutCell(titleCellKey);
                        cell.addStyle("textAlign", textAlign.name());
                        if (fieldInfo.getLabelBean().getManualWidth()  != null) {
                            layoutData.addColSetting(colKey, ManualType.manualWidth, fieldInfo.getLabelBean().getManualWidth() );
                        } else {
                            layoutData.addColSetting(colKey, ManualType.manualWidth, viewBean.getDefaultColWidth());
                        }


                        if (fieldInfo.getAggConfig().getCaption() != null) {
                            cell.setValue(fieldInfo.getAggConfig().getCaption());
                        }
                        valueMap.put(titleCellKey, cell.getValue());
                        cellMap.put(titleCellKey, cell);
                        layoutData.addCol(titleCellKey, cell);
                        //步进+1
                        k = k + 1;
                    } else {

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

                    if (viewBean != null && viewBean.getDefaultRowHeight() != null && viewBean.getDefaultRowHeight() != -1) {
                        layoutData.addRowSetting(rowkey + "", ManualType.manualHeight, viewBean.getDefaultRowHeight().toString());
                    }

                    if (viewBean != null && viewBean.getDefaultColWidth() != null && viewBean.getDefaultColWidth() != -1) {
                        layoutData.addColSetting(cellHead[k], ManualType.manualWidth, viewBean.getDefaultColWidth().toString());
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

    }

    public CustomFormViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(CustomFormViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public MFormLayoutComponent getFormComponent() {
        return formComponent;
    }

    public void setFormComponent(MFormLayoutComponent formComponent) {
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
