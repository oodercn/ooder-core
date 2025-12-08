package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.event.CustomOpinionEvent;
import net.ooder.esd.annotation.OpinionAnnotation;
import net.ooder.esd.annotation.menu.OpinionMenu;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.bean.gallery.OpinionItemBean;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.tool.component.OpinionComponent;
import net.ooder.esd.custom.properties.OpinionProperties;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.item.OpinionItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = OpinionAnnotation.class)
public class CustomOpinionViewBean extends BaseGalleryViewBean<OpinionMenu, CustomOpinionEvent> implements ContextMenuBar, ToolsBar {

    ModuleViewType moduleViewType = ModuleViewType.OPINIONCONFIG;
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
    List<OpinionItemBean> opinionItemBeans;


    public List<OpinionMenu> toolBarMenu = new ArrayList<>();
    public List<OpinionMenu> customMenu = new ArrayList<>();
    public List<OpinionMenu> bottombarMenu = new ArrayList<>();
    public Set<CustomOpinionEvent> event = new LinkedHashSet<>();


    public CustomOpinionViewBean() {

    }


    public CustomOpinionViewBean(ModuleComponent<OpinionComponent> moduleComponent) {
        AnnotationUtil.fillDefaultValue(OpinionAnnotation.class, this);
        this.updateModule(moduleComponent);
    }

    public  List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        OpinionComponent opinionComponent = (OpinionComponent) moduleComponent.getCurrComponent();
        OpinionProperties opinionProperties = opinionComponent.getProperties();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(opinionProperties), Map.class), this, false, false);
        this.name = OODUtil.formatJavaName(opinionComponent.getAlias(), false);
        List<OpinionItem> opinionItems = opinionProperties.getItems();

        if (opinionItems != null && opinionItems.size() > 0) {
            opinionItemBeans = new ArrayList<>();
            for (OpinionItem opinionItem : opinionItems) {
                OpinionItemBean lineListItemBean = new OpinionItemBean(opinionItem, this);
                opinionItemBeans.add(lineListItemBean);
            }
        }

        Map<GalleryEventEnum, Event> gridEventEventMap = opinionComponent.getEvents();
        Set<GalleryEventEnum> eventEnums = gridEventEventMap.keySet();
        for (GalleryEventEnum eventEnum : eventEnums) {
            Event event = gridEventEventMap.get(eventEnum);

            List<Action> actionList = event.getActions();
            for (Action action : actionList) {
                if (action.getEventKey() != null && action.getEnumValue() != null && CustomOpinionEvent.valueOf(action.getEnumValue()) != null) {
                    this.event.add(CustomOpinionEvent.valueOf(action.getEnumValue()));
                }
            }
        }
        this.name = OODUtil.formatJavaName(opinionComponent.getAlias(), false);
        return tasks;
    }


    public CustomOpinionViewBean(Class clazz) {
        super.initViewClass(clazz);
        init(clazz);
    }


    public CustomOpinionViewBean(MethodConfig methodAPIBean) {
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
        OpinionAnnotation annotation = AnnotationUtil.getClassAnnotation(clazz, OpinionAnnotation.class);
        if (annotation != null) {
            this.fillData(annotation);
        } else {
            AnnotationUtil.fillDefaultValue(OpinionAnnotation.class, this);
        }
        initBaseGrid(clazz);
    }


    @Override
    public List<OpinionMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<OpinionMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    @Override
    public List<OpinionMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<OpinionMenu> customMenu) {
        this.customMenu = customMenu;
    }

    @Override
    public List<OpinionMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<OpinionMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    @Override
    public Set<CustomOpinionEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomOpinionEvent> event) {
        this.event = event;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.OPINION;
    }


    public List<JavaGenSource> buildAll() {

        return build(childModules);
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


    public CustomOpinionViewBean fillData(OpinionAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public List<OpinionItemBean> getOpinionItemBeans() {
        if (opinionItemBeans == null) {
            opinionItemBeans = new ArrayList<>();
        }
        return opinionItemBeans;
    }

    public void setOpinionItemBeans(List<OpinionItemBean> opinionItemBeans) {
        this.opinionItemBeans = opinionItemBeans;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
