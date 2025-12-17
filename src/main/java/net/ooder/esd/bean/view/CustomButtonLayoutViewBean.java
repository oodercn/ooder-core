package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.ButtonLayoutAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.event.CustomButtonLayoutEvent;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.ButtonLayoutMenu;
import net.ooder.esd.annotation.menu.OpinionMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.view.field.FieldGalleryConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.ButtonLayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonLayoutProperties;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = ButtonLayoutAnnotation.class)
public class CustomButtonLayoutViewBean extends CustomViewBean<FieldGalleryConfig, ButtonLayoutItem, ButtonLayoutComponent> implements ContextMenuBar, ToolsBar {

    ModuleViewType moduleViewType = ModuleViewType.BUTTONLAYOUTCONFIG;
    Boolean resizer;
    Boolean showDirtyMark;
    String bgimg;
    String iconFontSize;
    BorderType borderType;
    Dock dock;
    Boolean autoImgSize;
    Boolean autoItemSize;
    Boolean iconOnly;
    String itemPadding;
    String itemMargin;
    String itemWidth;
    String itemHeight;
    String imgWidth;
    String imgHeight;
    Integer columns;
    Integer rows;
    String flagText;
    String flagClass;
    String flagStyle;
    SelModeType selMode;

    List<ButtonLayoutMenu> toolBarMenu = new ArrayList<>();
    List<ButtonLayoutMenu> customMenu = new ArrayList<>();
    List<ButtonLayoutMenu> bottombarMenu = new ArrayList<>();
    Set<CustomButtonLayoutEvent> event = new LinkedHashSet<>();
    RightContextMenuBean contextMenuBean;

    List<ButtonLayoutItem> buttonLayoutItemBeans;

    public List<ButtonLayoutItem> tabItems = new ArrayList<>();

    public CustomButtonLayoutViewBean() {

    }

    public CustomButtonLayoutViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        ButtonLayoutAnnotation annotation = AnnotationUtil.getClassAnnotation(clazz, ButtonLayoutAnnotation.class);
        if (annotation != null) {
            this.fillData(annotation);
        } else {
            AnnotationUtil.fillDefaultValue(ButtonLayoutAnnotation.class, this);
        }

        RightContextMenu contextMenu = AnnotationUtil.getClassAnnotation(clazz, RightContextMenu.class);
        if (contextMenu != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), contextMenu);
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
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.viewClassName, false);
            List<ESDField> cols = esdClassConfig.getESDClass().getFieldList();
            for (ESDField esdField : cols) {
                FieldGalleryConfig config = new FieldGalleryConfig(esdField, methodAPIBean.getSourceClassName(), methodAPIBean.getMethodName());
                fieldConfigMap.put(esdField.getName(), config);
                fieldNames.add(esdField.getName());
            }


        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    public List<JavaGenSource> buildAll() {

        return build(childModules);
    }


    public CustomButtonLayoutViewBean(ModuleComponent<ButtonLayoutComponent> moduleComponent) {
        this.updateModule(moduleComponent);
    }

    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(ButtonLayoutAnnotation.class, this);
        super.updateBaseModule(moduleComponent);
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        ButtonLayoutComponent component = (ButtonLayoutComponent) moduleComponent.getCurrComponent();
        ButtonLayoutProperties layoutProperties = component.getProperties();
        this.init(layoutProperties);
        buttonLayoutItemBeans = layoutProperties.getItems();
        Map<GalleryEventEnum, Event> gridEventEventMap = component.getEvents();
        Set<GalleryEventEnum> eventEnums = gridEventEventMap.keySet();
        for (GalleryEventEnum eventEnum : eventEnums) {
            Event event = gridEventEventMap.get(eventEnum);
            List<Action> actionList = event.getActions();
            for (Action action : actionList) {
                if (action.getEventKey() != null && action.getEnumValue() != null && CustomButtonLayoutEvent.valueOf(action.getEnumValue()) != null) {
                    this.event.add(CustomButtonLayoutEvent.valueOf(action.getEnumValue()));
                }
            }
        }
        childModules = tasks;
        return tasks;
    }

    void init(ButtonLayoutProperties opinionProperties) {

        if (opinionProperties.getItemPadding() != null && !opinionProperties.getItemPadding().equals("")) {
            this.itemPadding = opinionProperties.getItemPadding();
        }


        if (opinionProperties.getBgimg() != null && !opinionProperties.getBgimg().equals("")) {
            this.bgimg = opinionProperties.getBgimg();
        }

        if (opinionProperties.getImageClass() != null && !opinionProperties.getImageClass().equals("")) {
            this.imageClass = opinionProperties.getImageClass();
        }


        if (opinionProperties.getImgHeight() != null && !opinionProperties.getImgHeight().equals("-1")) {
            this.imgHeight = opinionProperties.getImgHeight();
        }

        if (opinionProperties.getItemHeight() != null && !opinionProperties.getItemHeight().equals("-1")) {
            this.itemHeight = opinionProperties.getItemHeight();
        }


        if (opinionProperties.getAutoImgSize() != null && opinionProperties.getAutoImgSize()) {
            this.autoImgSize = opinionProperties.getAutoImgSize();
        }
        if (opinionProperties.getAutoItemSize() != null && opinionProperties.getAutoItemSize()) {
            this.autoItemSize = opinionProperties.getAutoItemSize();
        }


        if (opinionProperties.getItemMargin() != null && !opinionProperties.getItemMargin().equals("")) {
            this.itemMargin = opinionProperties.getItemMargin();
        }


        if (opinionProperties.getBorderType() != null && !opinionProperties.getBorderType().equals("")) {
            this.borderType = opinionProperties.getBorderType();
        }

        if (opinionProperties.getImgWidth() != null && !opinionProperties.getImgWidth().equals("-1")) {
            this.imgWidth = opinionProperties.getImgWidth();
        }


        if (opinionProperties.getItemWidth() != null && !opinionProperties.getItemWidth().equals("-1")) {
            this.itemWidth = opinionProperties.getItemWidth();
        }
        if (opinionProperties.getIconOnly() != null && opinionProperties.getIconOnly()) {
            this.iconOnly = opinionProperties.getIconOnly();
        }
        if (opinionProperties.getColumns() != null && opinionProperties.getColumns() > 0) {
            this.columns = opinionProperties.getColumns();
        }
        if (opinionProperties.getRows() != null && opinionProperties.getRows() > 0) {
            this.rows = opinionProperties.getRows();
        }

        this.iconFontSize = opinionProperties.getIconFontSize();
        this.dock = opinionProperties.getDock();
        this.selMode = opinionProperties.getSelMode();
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.BUTTONLAYOUT;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (toolBar != null) {
            annotationBeans.add(toolBar);
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


        if (menuBar != null) {
            annotationBeans.add(menuBar);
        }

        if (bottomBar != null) {
            annotationBeans.add(bottomBar);
        }

        if (contextMenuBean != null) {
            annotationBeans.add(contextMenuBean);
        }

        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    public List<ButtonLayoutItem> getTabItems() {
        if (tabItems == null || tabItems.size() == 0) {
            tabItems = new ArrayList<>();
            List<FieldModuleConfig> moduleItems = getNavItems();
            for (FieldModuleConfig itemInfo : moduleItems) {
                try {
                    ButtonLayoutItem navItemProperties = new ButtonLayoutItem(itemInfo);
                    navItemProperties.setComment(navItemProperties.getCaption());
                    navItemProperties.setCaption("");
                    tabItems.add(navItemProperties);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        return tabItems;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (contextMenuBean != null && contextMenuBean.getMenuClass() != null) {
            classSet.addAll(Arrays.asList(contextMenuBean.getMenuClass()));
        }
        return ClassUtility.checkBase(classSet);
    }


    @Override
    public ToolBarMenuBean getToolBar() {
        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
                this.toolBar.setMenus(toolBarMenu.toArray(new OpinionMenu[]{}));
            }
        }
        return toolBar;
    }

    @Override
    public MenuBarBean getMenuBar() {
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
                this.menuBar.setMenus(customMenu.toArray(new OpinionMenu[]{}));
            }
        }
        return menuBar;
    }


    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                this.bottomBar.setBottombar(bottombarMenu.toArray(new OpinionMenu[]{}));
            }
        }
        return bottomBar;
    }

    public List<ButtonLayoutItem> getButtonLayoutItemBeans() {
        return buttonLayoutItemBeans;
    }

    public void setButtonLayoutItemBeans(List<ButtonLayoutItem> buttonLayoutItemBeans) {
        this.buttonLayoutItemBeans = buttonLayoutItemBeans;
    }

    public Boolean getShowDirtyMark() {
        return showDirtyMark;
    }

    public void setShowDirtyMark(Boolean showDirtyMark) {
        this.showDirtyMark = showDirtyMark;
    }

    public String getFlagText() {
        return flagText;
    }

    public void setFlagText(String flagText) {
        this.flagText = flagText;
    }

    public String getFlagClass() {
        return flagClass;
    }

    public void setFlagClass(String flagClass) {
        this.flagClass = flagClass;
    }

    public String getFlagStyle() {
        return flagStyle;
    }

    public void setFlagStyle(String flagStyle) {
        this.flagStyle = flagStyle;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public Boolean getResizer() {
        return resizer;
    }

    public void setResizer(Boolean resizer) {
        this.resizer = resizer;
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getAutoImgSize() {
        return autoImgSize;
    }

    public void setAutoImgSize(Boolean autoImgSize) {
        this.autoImgSize = autoImgSize;
    }

    public Boolean getAutoItemSize() {
        return autoItemSize;
    }

    public void setAutoItemSize(Boolean autoItemSize) {
        this.autoItemSize = autoItemSize;
    }

    public Boolean getIconOnly() {
        return iconOnly;
    }

    public void setIconOnly(Boolean iconOnly) {
        this.iconOnly = iconOnly;
    }

    public String getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(String itemPadding) {
        this.itemPadding = itemPadding;
    }

    public String getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(String itemMargin) {
        this.itemMargin = itemMargin;
    }

    public String getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(String itemWidth) {
        this.itemWidth = itemWidth;
    }

    public String getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(String itemHeight) {
        this.itemHeight = itemHeight;
    }

    public String getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(String imgWidth) {
        this.imgWidth = imgWidth;
    }

    public String getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(String imgHeight) {
        this.imgHeight = imgHeight;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public List<ButtonLayoutMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<ButtonLayoutMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public List<ButtonLayoutMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<ButtonLayoutMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public Set<CustomButtonLayoutEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomButtonLayoutEvent> event) {
        this.event = event;
    }

    @Override
    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public CustomButtonLayoutViewBean fillData(ButtonLayoutAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public void setTabItems(List<ButtonLayoutItem> tabItems) {
        this.tabItems = tabItems;
    }

    public List<ButtonLayoutMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<ButtonLayoutMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
