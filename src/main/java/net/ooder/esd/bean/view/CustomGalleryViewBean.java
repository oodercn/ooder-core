package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.event.CustomGalleryEvent;
import net.ooder.esd.annotation.menu.CustomGalleryMenu;
import net.ooder.esd.annotation.GalleryAnnotation;
import net.ooder.esd.annotation.NavGalleryAnnotation;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.gallery.GalleryItemBean;
import net.ooder.esd.dsm.gen.view.GenTabsChildModule;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.GalleryComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.GalleryProperties;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = GalleryAnnotation.class)
public class CustomGalleryViewBean extends BaseGalleryViewBean<CustomGalleryMenu, CustomGalleryEvent> implements ContextMenuBar, ToolsBar {

    ModuleViewType moduleViewType = ModuleViewType.GALLERYCONFIG;
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
    SelModeType selMode;



    public List<CustomGalleryMenu> toolBarMenu = new ArrayList<>();
    public List<CustomGalleryMenu> customMenu = new ArrayList<>();
    public List<CustomGalleryMenu> bottombarMenu = new ArrayList<>();
    public Set<CustomGalleryEvent> event = new LinkedHashSet<>();


    public CustomGalleryViewBean() {

    }

    public CustomGalleryViewBean(ModuleComponent<GalleryComponent> moduleComponent) {
        super();
        this.updateModule(moduleComponent);
    }


    public  List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(GalleryAnnotation.class, this);
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        if (moduleComponent != null && moduleComponent.getMethodAPIBean() != null) {
            MethodConfig sourceMethod = moduleComponent.getMethodAPIBean();
            this.methodName = sourceMethod.getMethodName();
            this.sourceClassName = sourceMethod.getSourceClassName();
            this.domainId = sourceMethod.getDomainId();
        }

        GalleryComponent galleryComponent = (GalleryComponent) moduleComponent.getCurrComponent();
        GalleryProperties galleryProperties = galleryComponent.getProperties();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(galleryProperties), Map.class), this, false, false);
        List<GalleryItem> galleryItems = galleryProperties.getItems();
        if (galleryItems != null && galleryItems.size() > 0) {
            galleryItemBeans = new ArrayList<>();
            for (GalleryItem galleryItem : galleryItems) {
                GalleryItemBean lineListItemBean = new GalleryItemBean(galleryItem, this);
                galleryItemBeans.add(lineListItemBean);
            }
        }
        Map<GalleryEventEnum, Event> gridEventEventMap = galleryComponent.getEvents();
        Set<GalleryEventEnum> eventEnums = gridEventEventMap.keySet();
        for (GalleryEventEnum eventEnum : eventEnums) {
            Event event = gridEventEventMap.get(eventEnum);
            List<Action> actionList = event.getActions();
            for (Action action : actionList) {
                if (action.getEventKey() != null && action.getEnumValue() != null && CustomGalleryEvent.valueOf(action.getEnumValue()) != null) {
                    this.event.add(CustomGalleryEvent.valueOf(action.getEnumValue()));
                }
            }
        }
        return tasks;
    }


    public CustomGalleryViewBean(Class clazz) {
        super.initViewClass(clazz);
        init(clazz);
    }

    public List<JavaGenSource> buildAll() {

        return build(childModules);
    }


    public CustomGalleryViewBean(MethodConfig methodAPIBean) {
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
        GalleryAnnotation annotation = AnnotationUtil.getClassAnnotation(clazz, GalleryAnnotation.class);
        NavGalleryAnnotation navannotation = AnnotationUtil.getClassAnnotation(clazz, NavGalleryAnnotation.class);
        if (annotation != null) {
            this.fillData(annotation);
        } else if (navannotation != null) {
            this.fillData(navannotation);
        } else {
            AnnotationUtil.fillDefaultValue(GalleryAnnotation.class, this);
        }
        initBaseGrid(clazz);
        initViewClass(clazz);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.GALLERY;
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

    public List<GalleryItemBean> getGalleryItemBeans() {
        return galleryItemBeans;
    }

    public void setGalleryItemBeans(List<GalleryItemBean> galleryItemBeans) {
        this.galleryItemBeans = galleryItemBeans;
    }

    @Override
    public List<CustomGalleryMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<CustomGalleryMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    @Override
    public List<CustomGalleryMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<CustomGalleryMenu> customMenu) {
        this.customMenu = customMenu;
    }

    @Override
    public List<CustomGalleryMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<CustomGalleryMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    @Override
    public Set<CustomGalleryEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomGalleryEvent> event) {
        this.event = event;
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


    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }


    public CustomGalleryViewBean fillData(GalleryAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomGalleryViewBean fillData(NavGalleryAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
