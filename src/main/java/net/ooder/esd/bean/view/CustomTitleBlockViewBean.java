package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.event.TitleBlockEventEnum;
import net.ooder.esd.annotation.event.CustomTitleBlockEvent;
import net.ooder.esd.annotation.TitleBlockAnnotation;
import net.ooder.esd.annotation.menu.TitleBlockMenu;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.gallery.TitleBlockItemBean;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.TitleBlockComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.TitleBlockProperties;
import net.ooder.esd.tool.properties.item.TitleBlockItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;

@AnnotationType(clazz = TitleBlockAnnotation.class)
public class CustomTitleBlockViewBean extends BaseGalleryViewBean<TitleBlockMenu, CustomTitleBlockEvent> implements ContextMenuBar, ToolsBar {

    ModuleViewType moduleViewType = ModuleViewType.TITLEBLOCKCONFIG;
    Boolean resizer;
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
    String defaultMore;
    SelModeType selMode;

    List<TitleBlockItemBean> titleBlockItemBeans = new ArrayList<>();


    @JSONField(serialize = false)
    List<TitleBlockItem> titleBlockItems;


    public List<TitleBlockMenu> toolBarMenu = new ArrayList<>();
    public List<TitleBlockMenu> customMenu = new ArrayList<>();
    public List<TitleBlockMenu> bottombarMenu = new ArrayList<>();
    public Set<CustomTitleBlockEvent> event = new LinkedHashSet<>();

    public CustomTitleBlockViewBean(ModuleComponent<TitleBlockComponent> moduleComponent) {
        super();
        this.updateModule(moduleComponent);
    }


    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(TitleBlockAnnotation.class, this);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        TitleBlockComponent titleBlockComponent = (TitleBlockComponent) moduleComponent.getCurrComponent();
        TitleBlockProperties titleBlockProperties = titleBlockComponent.getProperties();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(titleBlockProperties), Map.class), this, false, false);
        this.name = OODUtil.formatJavaName(titleBlockComponent.getAlias(), false);
        this.titleBlockItems = titleBlockProperties.getItems();
        if (titleBlockItems != null && titleBlockItems.size() > 0) {
            titleBlockItemBeans = new ArrayList<>();
            for (TitleBlockItem titleBlockItem : titleBlockItems) {
                TitleBlockItemBean lineListItemBean = new TitleBlockItemBean(titleBlockItem, this);
                titleBlockItemBeans.add(lineListItemBean);
            }
        }

        Map<TitleBlockEventEnum, Event> gridEventEventMap = titleBlockComponent.getEvents();
        Set<TitleBlockEventEnum> eventEnums = gridEventEventMap.keySet();
        for (TitleBlockEventEnum eventEnum : eventEnums) {
            Event event = gridEventEventMap.get(eventEnum);

            List<Action> actionList = event.getActions();
            for (Action action : actionList) {
                if (action.getEventKey() != null && action.getEnumValue() != null && CustomTitleBlockEvent.valueOf(action.getEnumValue()) != null) {
                    this.event.add(CustomTitleBlockEvent.valueOf(action.getEnumValue()));
                }
            }
        }
        this.name = OODUtil.formatJavaName(titleBlockComponent.getAlias(), false);
        addChildJavaSrc(javaSrcBeans);
        return javaSrcBeans;
    }


    public CustomTitleBlockViewBean() {

    }

    public void init(TitleBlockProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public CustomTitleBlockViewBean(Class clazz) {
        super.initViewClass(clazz);
        init(clazz);
    }


    public CustomTitleBlockViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        this.name = StringUtility.formatUrl(methodName);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        init(clazz);
        try {
            this.initHiddenField(this.viewClassName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    void init(Class clazz) {
        TitleBlockAnnotation annotation = AnnotationUtil.getClassAnnotation(clazz, TitleBlockAnnotation.class);
        if (annotation != null) {
            this.fillData(annotation);
        } else {
            AnnotationUtil.fillDefaultValue(TitleBlockAnnotation.class, this);
        }
        initBaseGrid(clazz);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLEBLOCK;
    }


    public TitleBlockItemBean getChildItemBean(TabItem tabItem) {
        List<TitleBlockItemBean> childTabViewBeans = this.getTitleBlockItemBeans();
        for (TitleBlockItemBean titleBlockItemBean : childTabViewBeans) {
            if (titleBlockItemBean.getTitleBlockItem() != null && titleBlockItemBean.getTitleBlockItem().equals(tabItem)) {
                return titleBlockItemBean;

            }
        }
        return null;
    }


    public List<TitleBlockItem> getTitleBlockItems() {
        return titleBlockItems;
    }

    public void setTitleBlockItems(List<TitleBlockItem> titleBlockItems) {
        this.titleBlockItems = titleBlockItems;
    }


    public List<TitleBlockItemBean> getTitleBlockItemBeans() {
        return titleBlockItemBeans;
    }

    public void setTitleBlockItemBeans(List<TitleBlockItemBean> titleBlockItemBeans) {
        this.titleBlockItemBeans = titleBlockItemBeans;
    }


    public String getDefaultMore() {
        return defaultMore;
    }

    public void setDefaultMore(String defaultMore) {
        this.defaultMore = defaultMore;
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

    public List<TitleBlockMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<TitleBlockMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public List<TitleBlockMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<TitleBlockMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public Set<CustomTitleBlockEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomTitleBlockEvent> event) {
        this.event = event;
    }

    public CustomTitleBlockViewBean fillData(TitleBlockAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public List<TitleBlockMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<TitleBlockMenu> toolBarMenu) {
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
