package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomGridEvent;
import net.ooder.esd.annotation.event.CustomMGridEvent;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.annotation.event.GridEventEnum;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.grid.ChildGridViewBean;
import net.ooder.esd.bean.grid.GridRowCmdBean;
import net.ooder.esd.bean.grid.PageBarBean;
import net.ooder.esd.bean.grid.RowHeadBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldGridConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.OODTypeMapping;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.lang.reflect.Constructor;
import java.util.*;

@AnnotationType(clazz = MGridAnnotation.class)
public class CustomMGridViewBean extends CustomViewBean<FieldGridConfig, UIItem, MTreeGridComponent> implements ContextMenuBar, ToolsBar {

    ModuleViewType moduleViewType = ModuleViewType.MGRIDCONFIG;

    Set<CustomMGridEvent> mevent = new LinkedHashSet<>();


    String rowHeight;

    Boolean altRowsBg;

    Boolean colSortable;

    String javaTempId;

    Boolean editable;

    Boolean iniFold;

    Boolean animCollapse;

    Boolean rowResizer;

    Boolean colHidable;

    Boolean colResizer;

    Boolean colMovable;

    Boolean noCtrlKey;

    Boolean showHeader;

    Integer freezedColumn;

    Integer freezedRow;


    String uidColumn;

    String valueSeparator;

    String currencyTpl;

    String numberTpl;

    Dock dock;

    List<GridMenu> toolBarMenu = new ArrayList<>();

    List<GridMenu> customMenu = new ArrayList<>();

    List<GridMenu> bottombarMenu = new ArrayList<>();

    Set<CustomGridEvent> event = new LinkedHashSet<>();

    RightContextMenuBean contextMenuBean;

    GridRowCmdBean rowCmdBean;

    PageBarBean pageBar;

    RowHeadBean rowHead;

    public CustomMGridViewBean() {
        super();
    }

    public CustomMGridViewBean(GridProperties gridProperties) {
        init(gridProperties);
    }

    public CustomMGridViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());

        MGridAnnotation annotation = AnnotationUtil.getClassAnnotation(clazz, MGridAnnotation.class);
        if (annotation != null) {
            AnnotationUtil.fillBean(annotation, this);
        }

        MRowHead head = AnnotationUtil.getClassAnnotation(clazz, MRowHead.class);
        if (head != null) {
            rowHead = new RowHeadBean(head);
        }

        MGridRowCmd cmdannotation = AnnotationUtil.getClassAnnotation(clazz, MGridRowCmd.class);
        if (cmdannotation != null) {
            rowCmdBean = new GridRowCmdBean(cmdannotation);
        }

        MPageBar pageBarAnnotation = AnnotationUtil.getClassAnnotation(clazz, MPageBar.class);
        if (pageBarAnnotation != null) {
            pageBar = new PageBarBean(pageBarAnnotation);
        }


        RightContextMenu contextMenu = AnnotationUtil.getClassAnnotation(clazz, RightContextMenu.class);
        if (contextMenu != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(),contextMenu);
        }


        if (toolBarMenu != null && toolBarMenu.size() > 0) {
            if (this.toolBar == null) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }

        if (customMenu != null && customMenu.size() > 0) {
            if (this.menuBar == null) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }

        if (bottombarMenu != null && bottombarMenu.size() > 0) {
            if (this.bottomBar == null) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }

        try {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
            if (esdClassConfig != null) {
                List<FieldAggConfig> cols = esdClassConfig.getCustomFieldList();
                for (FieldAggConfig esdField : cols) {

                    FieldGridConfig config = new FieldGridConfig(esdField, methodAPIBean.getSourceClassName(), methodAPIBean.getMethodName());
                    fieldConfigMap.put(esdField.getFieldname(), config);
                    fieldNames.add(esdField.getFieldname());
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            ChildGridViewBean childGridViewBean = new ChildGridViewBean(constructor, this);
            childBeans.add(childGridViewBean);
        }


    }


    Set<ChildGridViewBean> childBeans = new LinkedHashSet<>();


    public CustomMGridViewBean(ModuleComponent<TreeGridComponent> moduleComponent) {
        super();
        AnnotationUtil.fillDefaultValue(MGridAnnotation.class, this);
        this.updateModule(moduleComponent);

    }


    @Override
    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        TreeGridComponent gridComponent = (TreeGridComponent) moduleComponent.getCurrComponent();
        List<Component> allComponent = moduleComponent.getChildrenRecursivelyList();
        for (Component component : allComponent) {
            ComponentType componentType = ComponentType.fromType(component.getKey());
            switch (componentType) {
                case STATUSBUTTONS:
                    this.bottombarMenu = parBottomBar((StatusButtonsComponent) component);
                    break;
                case MENUBAR:
                    this.customMenu = parMenuBar((MenuBarComponent) component);
                    break;
                case TOOLBAR:
                    this.toolBarMenu = parToolBar((ToolBarComponent) component);
                    break;
                case PAGEBAR:
                    if (this.pageBar == null) {
                        this.pageBar = new PageBarBean((PageBarProperties) component.getProperties());
                    } else {
                        this.pageBar.update((PageBarProperties) component.getProperties());
                    }

                    break;
            }
        }
        Map<GridEventEnum, Event> gridEventEventMap = gridComponent.getEvents();
        Set<GridEventEnum> eventEnums = gridEventEventMap.keySet();
        for (GridEventEnum eventEnum : eventEnums) {
            Event event = gridEventEventMap.get(eventEnum);
            //  String eventName = event.getEventType();
            List<Action> actionList = event.getActions();
            for (Action action : actionList) {
                if (action.getEventKey() != null && action.getEnumValue() != null && CustomGridEvent.valueOf(action.getEnumValue()) != null) {
                    this.event.add(CustomGridEvent.valueOf(action.getEnumValue()));
                }
            }
        }

        //  this.name = OODUtil.formatJavaName(gridComponent.getAlias(), false);
        GridProperties gridProperties = gridComponent.getProperties();
        this.init(gridProperties);
        List<Header> headerList = gridProperties.getHeader();
        for (Header header : headerList) {
            FieldGridConfig config = fieldConfigMap.get(header.getId());
            if (config == null) {
                try {
                    createField(header.getId(), header.getType(), null);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
                config = new FieldGridConfig(header);
            } else {
                config.update(header);
            }

            fieldConfigMap.put(header.getId(), config);
            fieldNames.add(header.getId());
        }


        return javaSrcBeans;
    }


    public void init(GridProperties gridProperties) {
        List<Header> headerList = gridProperties.getHeader();
        for (Header header : headerList) {
            FieldGridConfig config = fieldConfigMap.get(header.getId());
            if (config == null) {
                config = new FieldGridConfig(header);
                fieldConfigMap.put(header.getId(), config);
                fieldNames.add(header.getId());
            } else {
                config.update(header);
            }
        }

        if (rowCmdBean == null) {
            this.rowCmdBean = new GridRowCmdBean(gridProperties);
        } else {
            rowCmdBean.update(gridProperties);
        }

        if (rowHead == null) {
            this.rowHead = new RowHeadBean(gridProperties);
        } else {
            rowHead.update(gridProperties);
        }
        this.altRowsBg = gridProperties.getAltRowsBg();
        this.rowHeight = gridProperties.getRowHeight();
        this.colSortable = gridProperties.getColSortable();
        this.showHeader = gridProperties.getShowHeader();
        this.editable = gridProperties.getEditable();
        this.iniFold = gridProperties.getIniFold();
        this.animCollapse = gridProperties.getAnimCollapse();
        this.rowResizer = gridProperties.getRowResizer();
        this.colHidable = gridProperties.getColHidable();
        this.colMovable = gridProperties.getColMovable();
        this.noCtrlKey = gridProperties.getNoCtrlKey();
        this.freezedColumn = gridProperties.getFreezedColumn();
        this.freezedRow = gridProperties.getFreezedRow();
        this.showHeader = gridProperties.getShowHeader();
        this.editable = gridProperties.getEditable();
        this.dock = gridProperties.getDock();
        this.uidColumn = gridProperties.getUidColumn();
        this.valueSeparator = gridProperties.getValueSeparator();
        this.currencyTpl = gridProperties.getCurrencyTpl();
        this.numberTpl = gridProperties.getNumberTpl();

    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.TREEGRID;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();

        if (pageBar != null) {
            annotationBeans.add(pageBar);
        }
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }
        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }

        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }

        if (rowCmdBean != null) {
            annotationBeans.add(rowCmdBean);
        }

        if (toolBar != null) {
            annotationBeans.add(toolBar);
        }
        if (menuBar != null) {
            annotationBeans.add(menuBar);
        }

        if (bottomBar != null) {
            annotationBeans.add(bottomBar);
        }

        if (contextMenuBean != null) {
            annotationBeans.add(contextMenuBean);
        }

        if (rowHead != null) {
            annotationBeans.add(rowHead);
        }
        annotationBeans.add(this);

        return annotationBeans;
    }

    public GridRowCmdBean getRowCmdBean() {
        return rowCmdBean;
    }

    public void setRowCmdBean(GridRowCmdBean rowCmdBean) {
        this.rowCmdBean = rowCmdBean;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (rowCmdBean != null) {
            classSet.addAll(Arrays.asList(rowCmdBean.getMenuClass()));
        }
        if (contextMenuBean != null && contextMenuBean.getMenuClass() != null) {
            classSet.addAll(Arrays.asList(contextMenuBean.getMenuClass()));
        }
        return classSet;
    }

    public List<ChildGridViewBean> getChildGridViewBean(Constructor constructor) {
        List<ChildGridViewBean> treeViewBeans = new ArrayList<>();
        for (ChildGridViewBean childGridViewBean : childBeans) {
            try {
                Constructor sourceConstructor = childGridViewBean.getConstructorBean().getSourceConstructor();
                if (sourceConstructor != null && constructor.equals(sourceConstructor)) {
                    treeViewBeans.add(childGridViewBean);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return treeViewBeans;
    }

    public Boolean getShowHeader() {
        return showHeader;
    }

    public void setShowHeader(Boolean showHeader) {
        this.showHeader = showHeader;
    }

    public RowHeadBean getRowHead() {
        return rowHead;
    }

    public void setRowHead(RowHeadBean rowHead) {
        this.rowHead = rowHead;
    }


    public PageBarBean getPageBar() {
        return pageBar;
    }

    public void setPageBar(PageBarBean pageBar) {
        this.pageBar = pageBar;
    }

    public String getJavaTempId() {
        return javaTempId;
    }

    public void setJavaTempId(String javaTempId) {
        this.javaTempId = javaTempId;
    }

    public List<GridMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<GridMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }


    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public Boolean getAnimCollapse() {
        return animCollapse;
    }

    public void setAnimCollapse(Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public Boolean getRowResizer() {
        return rowResizer;
    }

    public void setRowResizer(Boolean rowResizer) {
        this.rowResizer = rowResizer;
    }

    public Boolean getColHidable() {
        return colHidable;
    }

    public void setColHidable(Boolean colHidable) {
        this.colHidable = colHidable;
    }

    public Boolean getColResizer() {
        return colResizer;
    }

    public void setColResizer(Boolean colResizer) {
        this.colResizer = colResizer;
    }

    public Boolean getColMovable() {
        return colMovable;
    }

    public void setColMovable(Boolean colMovable) {
        this.colMovable = colMovable;
    }

    public Boolean getNoCtrlKey() {
        return noCtrlKey;
    }

    public void setNoCtrlKey(Boolean noCtrlKey) {
        this.noCtrlKey = noCtrlKey;
    }

    public Integer getFreezedColumn() {
        return freezedColumn;
    }

    public void setFreezedColumn(Integer freezedColumn) {
        this.freezedColumn = freezedColumn;
    }

    public Integer getFreezedRow() {
        return freezedRow;
    }

    public void setFreezedRow(Integer freezedRow) {
        this.freezedRow = freezedRow;
    }


    public String getUidColumn() {
        return uidColumn;
    }

    public void setUidColumn(String uidColumn) {
        this.uidColumn = uidColumn;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
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

    public List<GridMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<GridMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    public Boolean getColSortable() {
        return colSortable;
    }

    public void setColSortable(Boolean colSortable) {
        this.colSortable = colSortable;
    }


    public String getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(String rowHeight) {
        this.rowHeight = rowHeight;
    }

    public List<GridMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<GridMenu> customMenu) {
        this.customMenu = customMenu;
    }


    public Set<CustomGridEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomGridEvent> event) {
        this.event = event;
    }


    public Boolean getAltRowsBg() {
        return altRowsBg;
    }

    public void setAltRowsBg(Boolean altRowsBg) {
        this.altRowsBg = altRowsBg;
    }


    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    @Override
    public ToolBarMenuBean getToolBar() {
        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
                this.toolBar.setMenus(toolBarMenu.toArray(new GridMenu[]{}));
            }
        }
        return toolBar;
    }

    @Override
    public MenuBarBean getMenuBar() {
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
                this.menuBar.setMenus(customMenu.toArray(new GridMenu[]{}));
            }
        }
        return menuBar;
    }


    public FieldGridConfig createField(String fieldName, ComboInputType inputType, Class clazz) throws JDSException {
        FieldGridConfig fieldGridConfig = this.getFieldByName(fieldName);
        AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
        if (fieldGridConfig == null) {
            fieldGridConfig = new FieldGridConfig();
            fieldGridConfig.setId(fieldName);
            fieldGridConfig.setSourceClassName(sourceClassName);
            fieldGridConfig.setSourceMethodName(methodName);
            fieldGridConfig.setDomainId(domainId);
            fieldGridConfig.setViewClassName(getViewClassName());
            fieldGridConfig.setFieldname(fieldName);
        }

        if (aggEntityConfig != null) {
            FieldAggConfig aggConfig = aggEntityConfig.getFieldByName(fieldName);
            if (aggConfig == null) {
                if (inputType == null) {
                    inputType = ComboInputType.input;
                }
                if (clazz == null) {
                    clazz = OODTypeMapping.genType(inputType);
                }
                ComponentType componentType = OODTypeMapping.getComponentType(clazz);
                aggConfig = aggEntityConfig.createField(fieldName, clazz, componentType);
            }
        }
        fieldConfigMap.put(fieldName, fieldGridConfig);
        fieldNames.add(fieldName);

        return fieldGridConfig;
    }


    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                this.bottomBar.setBottombar(bottombarMenu.toArray(new GridMenu[]{}));
            }
        }
        return bottomBar;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    public Set<CustomMGridEvent> getMevent() {
        return mevent;
    }

    public void setMevent(Set<CustomMGridEvent> mevent) {
        this.mevent = mevent;
    }

    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

}
