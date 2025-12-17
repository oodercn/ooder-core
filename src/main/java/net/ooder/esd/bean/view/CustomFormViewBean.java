package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.annotation.AnnotationType;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.*;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = FormAnnotation.class)
public class CustomFormViewBean extends BaseFormViewBean<FormLayoutComponent> {


    ModuleViewType moduleViewType = ModuleViewType.FORMCONFIG;

    BorderType borderType;

    String background;

    OverflowType Overflow;

    String showEffects;

    String hideEffects;

    Boolean floatHandler;

    Boolean solidGridlines;

    Integer col;

    Integer row;

    FormLayModeType mode;

    StretchType stretchH;

    StretchType stretchHeight;

    Integer rowHeaderWidth;

    Integer columnHeaderHeight;

    Integer defaultRowSize;

    Integer defaultColumnSize;

    Integer defaultRowHeight;

    Integer defaultLabelWidth;

    Integer defaultColWidth;

    HAlignType textAlign;

    LayoutData layoutData;

    Integer lineSpacing;

    String rendererCDNJS;

    String rendererCDNCSS;

    String cssStyle;

    Boolean autoLayout = true;

    RightContextMenuBean contextMenuBean;

    List<CustomFormMenu> toolBarMenu = new ArrayList<>();

    List<CustomFormMenu> customMenu = new ArrayList<>();

    List<CustomFormMenu> bottombarMenu = new ArrayList<>();

    Set<CustomFormEvent> event = new LinkedHashSet<>();


    @JSONField(serialize = false)
    DBTableBean dbTableBean;


    public CustomFormViewBean() {

    }


    /**
     * 从视图模块构建
     *
     * @param moduleComponent
     */
    public CustomFormViewBean(ModuleComponent<FormLayoutComponent> moduleComponent) {
        super();
        this.updateModule(moduleComponent);
    }


    @Override
    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(FormAnnotation.class, this);
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        updateBar();
        FormLayoutComponent currComponent = (FormLayoutComponent) moduleComponent.getCurrComponent();
        this.name = OODUtil.formatJavaName(currComponent.getAlias(), false);
        if (containerBean == null) {
            containerBean = new ContainerBean(currComponent);
        } else {
            containerBean.update(currComponent);
        }
        List<Component> components = cloneComponentList(currComponent.getChildren());

        for (Component component : components) {
            FieldFormConfig fieldFormConfig = findFieldByCom(component);
            this.fieldConfigMap.put(fieldFormConfig.getFieldname(), fieldFormConfig);
        }
        tasks = genChildComponent(moduleComponent, components);
        initFormLayout(moduleComponent);
        try {
            DSMFactory.getInstance().saveCustomViewEntity(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return tasks;
    }


    private void updateBar() {
        if (getBottomBar() != null && getBottomBar().getEnumItems() != null) {
            bottombarMenu = getBottomBar().getEnumItems();
        }
        if (getMenuBar() != null && getMenuBar().getEnumItems() != null) {
            customMenu = getMenuBar().getEnumItems();
        }
        if (getToolBar() != null && getToolBar().getEnumItems() != null) {
            toolBarMenu = getToolBar().getEnumItems();
        }
    }


    private void initFormLayout(ModuleComponent moduleComponent) {
        FormLayoutComponent formLayoutComponent = (FormLayoutComponent) moduleComponent.getCurrComponent();
        FormLayoutProperties formLayoutProperties = formLayoutComponent.getProperties();
        this.autoLayout = false;
        this.init(formLayoutProperties);
        if (layoutData != null) {
            this.row = layoutData.getRows();
            this.col = layoutData.getCols() / 2;
            List<LayoutMerged> layoutMergeds = layoutData.getMerged();
            if (layoutMergeds != null) {
                for (LayoutMerged layoutMerged : layoutMergeds) {
                    String[] cellHead = HeadUtil.cellHead;
                    String target = cellHead[layoutMerged.getCol()] + (layoutMerged.getRow() + 1);
                    List<Component> componentList = moduleComponent.findComponentsByTarget(target);
                    for (Component component : componentList) {
                        FieldFormConfig fieldInfo = findFieldByCom(component);
                        if (fieldInfo != null) {
                            FieldBean fieldBean = fieldInfo.getFieldBean();
                            fieldBean.setRawCol(layoutMerged.getCol());
                            fieldBean.setRawRow(layoutMerged.getRow());
                            fieldBean.setRawRowSpan(layoutMerged.getRowspan());
                            fieldBean.setRawColSpan(layoutMerged.getColspan());
                            fieldBean.setRemoved(layoutMerged.getRemoved());
                        }
                    }
                }
            }


            Map<String, Map<ManualType, Object>> rowSetting = layoutData.getRowSetting();
            if (rowSetting != null) {
                Set<String> keySet = rowSetting.keySet();
                for (String key : keySet) {
                    Map<ManualType, Object> rowMap = rowSetting.get(key);
                    Integer manualHeight = TypeUtils.castToInt(TypeUtils.castToDouble(rowMap.get(ManualType.manualHeight)));
                    Object height = rowMap.get(ManualType.height);
                    String[] cellHead = HeadUtil.cellHead;
                    for (int k = 0; k < layoutData.getCols(); k++) {
                        String target = cellHead[k] + key;
                        List<Component> componentList = moduleComponent.findComponentsByTarget(target);
                        for (Component component : componentList) {
                            FieldFormConfig fieldInfo = findFieldByCom(component);
                            if (fieldInfo != null) {
                                FieldBean fieldBean = fieldInfo.getFieldBean();
                                fieldBean.setManualHeight(manualHeight);
                            }
                        }
                    }
                }

            }

            Map<String, Map<ManualType, Object>> colSetting = layoutData.getColSetting();
            if (colSetting != null) {
                Set<String> colkeySet = colSetting.keySet();
                for (String colKey : colkeySet) {
                    Map<ManualType, Object> colMap = colSetting.get(colKey);
                    Integer manualWidth = TypeUtils.castToInt(TypeUtils.castToDouble(colMap.get(ManualType.manualWidth)));
                    Object width = colMap.get(ManualType.width);
                    for (int k = 0; k < layoutData.getRows(); k++) {
                        String target = colKey + k;
                        List<Component> componentList = moduleComponent.findComponentsByTarget(target);
                        for (Component component : componentList) {
                            FieldFormConfig fieldInfo = findFieldByCom(component);
                            if (fieldInfo != null) {
                                FieldBean fieldBean = fieldInfo.getFieldBean();
                                fieldBean.setManualWidth(manualWidth);

                            }
                        }
                    }
                }
            }

            Map<String, LayoutCell> cellMap = layoutData.getCells();
            if (cellMap != null) {
                Set<String> cellkeySet = cellMap.keySet();
                for (String cellKey : cellkeySet) {
                    LayoutCell layoutCell = cellMap.get(cellKey);
                    String colKey = cellKey.substring(0, 1);
                    String rowKey = cellKey.substring(1, cellKey.length());
                    Integer colIndex = Arrays.asList(HeadUtil.cellHead).indexOf(colKey);
                    String nextColKey = HeadUtil.cellHead[colIndex + 1] + rowKey;
                    String caption = layoutCell.getValue();
                    List<Component> comList = moduleComponent.findComponentsByTarget(nextColKey);

                    if (comList.size() > 0) {
                        for (Component component : comList) {
                            FieldFormConfig fieldInfo = findFieldByCom(component);
                            if (fieldInfo != null) {
                                if (fieldInfo.getCustomBean() != null) {
                                    fieldInfo.getCustomBean().setCaption(caption);
                                }
                                if (fieldInfo.getLabelBean() != null) {
                                    fieldInfo.getLabelBean().setLabelCaption(caption);
                                    if (colSetting != null && colSetting.get(colKey) != null) {
                                        Map<ManualType, Object> colMap = colSetting.get(colKey);
                                        if (colMap.containsKey(ManualType.manualWidth)) {
                                            Integer manualWidth = TypeUtils.castToInt(TypeUtils.castToDouble(colMap.get(ManualType.manualWidth)));
                                            fieldInfo.getLabelBean().setManualWidth(manualWidth);
                                        }

                                    }

                                }
                            }
                        }
                    } else {
                        try {
                            FieldFormConfig fieldInfo = this.createField(cellKey, ComponentType.LABEL, moduleComponent, null, String.class, cellKey);
                            if (fieldInfo.getCustomBean() != null) {
                                fieldInfo.getCustomBean().setCaption(caption);
                            }

                        } catch (JDSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


    }


    @JSONField(serialize = false)
    public Set<String> getExeclNames() {
        Set<String> names = new HashSet<>();
        List<FieldFormConfig> customBeans = this.getAllFields();
        for (FieldFormConfig fieldFormConfig : customBeans) {
            names.add(fieldFormConfig.getFieldname());
            if (fieldFormConfig.getCustomBean().getTarget() != null) {
                names.add(fieldFormConfig.getCustomBean().getTarget());
            }
        }
        return names;
    }

    public DBTableBean getDbTableBean() {
        return dbTableBean;
    }

    public void setDbTableBean(DBTableBean dbTableBean) {
        this.dbTableBean = dbTableBean;
    }

    @Override
    public FieldFormConfig getFieldByName(String name) {
        FieldFormConfig fieldFormConfig = super.getFieldByName(name);
        return fieldFormConfig;
    }


    @Override
    public void moveTop(String fieldname) {
        super.moveTop(fieldname);
        FieldFormConfig formConfig = this.getFieldByName(fieldname);
        formConfig.createCustomBean().setIndex(0);
        formConfig.createFieldBean().setColSpan(-1);
        formConfig.createCustomBean().setTarget("A1");
        List<FieldFormConfig> customBeans = this.getAllFields();
        for (FieldFormConfig fieldFormConfig : customBeans) {
            if (!fieldFormConfig.getFieldname().equals(fieldname)) {
                String target = fieldFormConfig.createCustomBean().getTarget();
                String colKey = target.substring(0, 1);
                String rowKey = target.substring(1, target.length());
                Integer row = Integer.valueOf(rowKey);
                fieldFormConfig.createCustomBean().setTarget(colKey + (row + 1));

            }
        }

    }

    /**
     * 从代码接口构建
     *
     * @param methodAPIBean
     */
    public CustomFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        this.methodName = methodAPIBean.getMethodName();
        FormAnnotation formAnnotation = AnnotationUtil.getClassAnnotation(clazz, FormAnnotation.class);

        if (formAnnotation == null) {
            AnnotationUtil.fillDefaultValue(FormAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(formAnnotation, this);
        }

        RightContextMenu contextMenu = AnnotationUtil.getClassAnnotation(clazz, RightContextMenu.class);
        if (contextMenu != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), contextMenu);
        }
        if (toolBarMenu != null && toolBarMenu.size() > 0) {
            if (this.toolBar == null) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
            toolBar.setMenus(toolBarMenu.toArray(new CustomFormMenu[]{}));
        }

        if (customMenu != null && customMenu.size() > 0) {
            if (this.menuBar == null) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
            menuBar.setMenus(customMenu.toArray(new CustomFormMenu[]{}));
        }

        if (bottombarMenu != null && bottombarMenu.size() > 0) {
            if (this.bottomBar == null) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
            bottomBar.setBottombar(bottombarMenu.toArray(new CustomFormMenu[]{}));
        }
        try {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
            List<FieldAggConfig> cols = esdClassConfig.getFieldList();
            for (FieldAggConfig esdField : cols) {
                FieldFormConfig config = new FieldFormConfig(esdField, methodAPIBean.getSourceClassName(), methodAPIBean.getMethodName());
                fieldConfigMap.put(esdField.getFieldname(), config);
                fieldNames.add(esdField.getFieldname());
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }

        layoutData = new LayoutData(this.getCol(), row == -1 ? this.getDisplayFieldNames().size() : row);

    }

    public FieldFormConfig<? extends WidgetBean, ? extends ComboBoxBean> createField(String fieldName, ComponentType componentType, ComboInputType inputType, ModuleComponent moduleComponent, String target) throws JDSException {
        Class clazz = String.class;
        if (inputType == null) {
            inputType = ComboInputType.input;
        }
        if (inputType != null) {
            ComboType comboType = inputType.getComboType();
            switch (comboType) {
                case module:
                    clazz = ResultModel.class;
                    break;
                case list:
                    clazz = List.class;
                    break;
                case number:
                    clazz = Number.class;
                    break;
                default:
                    clazz = String.class;

            }
        }

        return createField(fieldName, componentType, moduleComponent, inputType, clazz, target);

    }


    public void init(FormLayoutProperties formLayoutProperties) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(formLayoutProperties), Map.class), this, false, false);
        if (this.layoutData != null) {
            this.col = layoutData.getCols() / 2;
        }
    }

    public Integer getDefaultLabelWidth() {
        if (defaultLabelWidth == null) {
            defaultLabelWidth = (Integer) AnnotationUtil.getDefaultValue(FormAnnotation.class, "defaultLabelWidth");
        }
        return defaultLabelWidth;
    }


    @JSONField(serialize = false)
    @Override
    public List<String> getDisplayFieldNames() {
        List<String> displayFieldNames = new ArrayList<>();
        List<FieldFormConfig> customBeans = this.getAllFields();
        for (FieldFormConfig fieldFormConfig : customBeans) {
            if (!displayFieldNames.contains(fieldFormConfig.getFieldname())) {
                if (fieldFormConfig.getCustomBean() != null && fieldFormConfig.getCustomBean().getTarget() != null && fieldFormConfig.getComponentType().equals(ComponentType.LABEL)) {
                    String target = fieldFormConfig.getCustomBean().getTarget();
                    if (!fieldFormConfig.getFieldname().toUpperCase().equals(target.toUpperCase())) {
                        displayFieldNames.add(fieldFormConfig.getFieldname());
                    }
                } else {
                    displayFieldNames.add(fieldFormConfig.getFieldname());
                }

            }
        }
        return displayFieldNames;
    }

    public void setDefaultLabelWidth(Integer defaultLabelWidth) {
        this.defaultLabelWidth = defaultLabelWidth;
    }

    public Boolean getAutoLayout() {
        return autoLayout;
    }

    public void setAutoLayout(Boolean autoLayout) {
        this.autoLayout = autoLayout;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public OverflowType getOverflow() {
        return Overflow;
    }

    public void setOverflow(OverflowType overflow) {
        Overflow = overflow;
    }

    public String getShowEffects() {
        return showEffects;
    }

    public void setShowEffects(String showEffects) {
        this.showEffects = showEffects;
    }

    public String getHideEffects() {
        return hideEffects;
    }

    public void setHideEffects(String hideEffects) {
        this.hideEffects = hideEffects;
    }

    public Integer getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(Integer lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public List<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public List<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public Integer getDefaultColWidth() {
        if (defaultColWidth == null) {
            defaultColWidth = (Integer) AnnotationUtil.getDefaultValue(FormAnnotation.class, "defaultColWidth");
        }
        return defaultColWidth;
    }

    public void setDefaultColWidth(Integer defaultColWidth) {
        this.defaultColWidth = defaultColWidth;
    }


    public LayoutData getLayoutData() {
        return layoutData;
    }

    public void setLayoutData(LayoutData layoutData) {
        this.layoutData = layoutData;
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

    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }


    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }


    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public FormLayModeType getMode() {
        return mode;
    }

    public void setMode(FormLayModeType mode) {
        this.mode = mode;
    }

    public HAlignType getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(HAlignType textAlign) {
        this.textAlign = textAlign;
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

    public Integer getRowHeaderWidth() {
        return rowHeaderWidth;
    }

    public void setRowHeaderWidth(Integer rowHeaderWidth) {
        this.rowHeaderWidth = rowHeaderWidth;
    }

    public Integer getColumnHeaderHeight() {
        return columnHeaderHeight;
    }

    public void setColumnHeaderHeight(Integer columnHeaderHeight) {
        this.columnHeaderHeight = columnHeaderHeight;
    }

    public Integer getDefaultRowSize() {
        if (defaultRowSize == null) {
            defaultRowSize = (Integer) AnnotationUtil.getDefaultValue(FormAnnotation.class, "defaultRowSize");
        }
        return defaultRowSize;
    }

    public void setDefaultRowSize(Integer defaultRowSize) {
        this.defaultRowSize = defaultRowSize;
    }

    public Integer getDefaultColumnSize() {
        if (defaultColumnSize == null) {
            defaultColumnSize = (Integer) AnnotationUtil.getDefaultValue(FormAnnotation.class, "defaultColumnSize");
        }
        return defaultColumnSize;
    }

    public void setDefaultColumnSize(Integer defaultColumnSize) {
        this.defaultColumnSize = defaultColumnSize;
    }

    public Integer getDefaultRowHeight() {
        if (defaultRowHeight == null) {
            defaultRowHeight = (Integer) AnnotationUtil.getDefaultValue(FormAnnotation.class, "defaultRowHeight");
        }
        return defaultRowHeight;
    }

    public void setDefaultRowHeight(Integer defaultRowHeight) {
        this.defaultRowHeight = defaultRowHeight;
    }

    public Boolean getFloatHandler() {
        return floatHandler;
    }

    public void setFloatHandler(Boolean floatHandler) {
        this.floatHandler = floatHandler;
    }

    public Boolean getSolidGridlines() {
        return solidGridlines;
    }

    public void setSolidGridlines(Boolean solidGridlines) {
        this.solidGridlines = solidGridlines;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public List<CustomFormMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<CustomFormMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.FORMLAYOUT;
    }

}
