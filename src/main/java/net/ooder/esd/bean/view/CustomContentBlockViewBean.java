package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.ContentBlockItemBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.annotation.event.ContentBlockEventEnum;
import net.ooder.esd.annotation.ContentBlockAnnotation;
import net.ooder.esd.annotation.menu.ContentBlockMenu;
import net.ooder.esd.annotation.event.CustomContentBlockEvent;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ContentBlockComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ContentBlockProperties;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.item.ContentBlockItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = ContentBlockAnnotation.class)
public class CustomContentBlockViewBean extends BaseGalleryViewBean<ContentBlockMenu, CustomContentBlockEvent> implements ContextMenuBar, ToolsBar {

    ModuleViewType moduleViewType = ModuleViewType.CONTENTBLOCKCONFIG;
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
    public List<ContentBlockMenu> toolBarMenu = new ArrayList<>();
    public List<ContentBlockMenu> customMenu = new ArrayList<>();
    public List<ContentBlockMenu> bottombarMenu = new ArrayList<>();
    public Set<CustomContentBlockEvent> event = new LinkedHashSet<>();


    List<ContentBlockItemBean> contentBlockItemBeans = new ArrayList<>();

    @JSONField(serialize = false)
    List<ContentBlockItem> contentBlockItems;

    public CustomContentBlockViewBean(ModuleComponent<ContentBlockComponent> moduleComponent) {
        super();
        this.updateModule(moduleComponent);
    }


    public CustomContentBlockViewBean() {

    }

    public void init(ContentBlockProperties ContentBlockProperties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(ContentBlockProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public List<Callable> updateModule(ModuleComponent moduleComponent) {
        List<Callable> tasks = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        ContentBlockComponent contentBlockComponent = (ContentBlockComponent) moduleComponent.getCurrComponent();
        ContentBlockProperties contentBlockProperties = contentBlockComponent.getProperties();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(contentBlockProperties), Map.class), this, false, false);
        this.name = OODUtil.formatJavaName(contentBlockComponent.getAlias(), false);
        this.contentBlockItems = contentBlockProperties.getItems();

        if (contentBlockItems != null && contentBlockItems.size() > 0) {
            contentBlockItemBeans = new ArrayList<>();
            for (ContentBlockItem contentBlockItem : contentBlockItems) {
                ContentBlockItemBean lineListItemBean = new ContentBlockItemBean(contentBlockItem, this);
                contentBlockItemBeans.add(lineListItemBean);
            }
        }

        Map<ContentBlockEventEnum, Event> gridEventEventMap = contentBlockComponent.getEvents();
        Set<ContentBlockEventEnum> eventEnums = gridEventEventMap.keySet();
        for (ContentBlockEventEnum eventEnum : eventEnums) {
            Event event = gridEventEventMap.get(eventEnum);

            List<Action> actionList = event.getActions();
            for (Action action : actionList) {
                if (action.getEventKey() != null && action.getEnumValue() != null && CustomContentBlockEvent.valueOf(action.getEnumValue()) != null) {
                    this.event.add(CustomContentBlockEvent.valueOf(action.getEnumValue()));
                }
            }
        }
        this.name = OODUtil.formatJavaName(contentBlockComponent.getAlias(), false);

        return tasks;
    }


    public CustomContentBlockViewBean(Class clazz) {
        super.initViewClass(clazz);
        init(clazz);
    }


    public CustomContentBlockViewBean(MethodConfig methodAPIBean) {
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
        ContentBlockAnnotation annotation = AnnotationUtil.getClassAnnotation(clazz, ContentBlockAnnotation.class);
        if (annotation != null) {
            this.fillData(annotation);
        } else {
            AnnotationUtil.fillDefaultValue(ContentBlockAnnotation.class, this);
        }
        initBaseGrid(clazz);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.CONTENTBLOCK;
    }


    public ContentBlockItemBean getChildItemBean(TabItem tabItem) {
        List<ContentBlockItemBean> childTabViewBeans = this.getContentBlockItemBeans();
        for (net.ooder.esd.bean.ContentBlockItemBean ContentBlockItemBean : childTabViewBeans) {
            if (ContentBlockItemBean.getContentBlockItem() != null && ContentBlockItemBean.getContentBlockItem().equals(tabItem)) {
                return ContentBlockItemBean;

            }
        }
        return null;
    }


    public List<ContentBlockItem> getContentBlockItems() {
        return contentBlockItems;
    }

    public void setContentBlockItems(List<ContentBlockItem> contentBlockItems) {
        this.contentBlockItems = contentBlockItems;
    }


    public List<ContentBlockItemBean> getContentBlockItemBeans() {
        return contentBlockItemBeans;
    }

    public void setContentBlockItemBeans(List<ContentBlockItemBean> contentBlockItemBeans) {
        this.contentBlockItemBeans = contentBlockItemBeans;
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

    public List<ContentBlockMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<ContentBlockMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public List<ContentBlockMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<ContentBlockMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public Set<CustomContentBlockEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomContentBlockEvent> event) {
        this.event = event;
    }


    public CustomContentBlockViewBean fillData(ContentBlockAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public List<ContentBlockMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<ContentBlockMenu> toolBarMenu) {
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
