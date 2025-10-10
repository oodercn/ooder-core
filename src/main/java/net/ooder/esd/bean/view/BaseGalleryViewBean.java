package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.action.CustomGalleryAction;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.gallery.GalleryItemBean;
import net.ooder.esd.bean.grid.PageBarBean;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldGalleryConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.GalleryComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;

public abstract class BaseGalleryViewBean<M extends CustomMenu, E extends CustomEvent> extends CustomViewBean<FieldFormConfig, TabListItem, GalleryComponent> implements ComponentBean<GalleryComponent> {


    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;
    RightContextMenuBean contextMenuBean;
    PageBarBean pageBar;
    EnumsClassBean enumsClassBean;

    List<GalleryItemBean> galleryItemBeans = new ArrayList<>();

    public LinkedHashSet<GalleryEventBean> extAPIEvent = new LinkedHashSet<>();

    public BaseGalleryViewBean() {
        super();
    }


    public BaseGalleryViewBean(MethodConfig methodConfig) {
        super(methodConfig);
    }


    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        Component component = moduleComponent.getCurrComponent();
        if (moduleComponent != null && moduleComponent.getMethodAPIBean() != null) {
            MethodConfig sourceMethod = moduleComponent.getMethodAPIBean();
            this.methodName = sourceMethod.getMethodName();
            this.sourceClassName = sourceMethod.getSourceClassName();
            this.domainId = sourceMethod.getDomainId();
        }

        this.name = OODUtil.formatJavaName(component.getAlias(), false);
        return javaSrcBeans;
    }


    protected void initBaseGrid(Class clazz) {
        List<M> toolBarMenu = getToolBarMenu();
        List<M> bottombarMenu = getBottombarMenu();
        List<M> customMenu = getCustomMenu();
        if (this.getEnumClass() == null && clazz != null && clazz.isEnum()) {
            this.setEnumClass(clazz);
        }


        GalleryEvent galleryEvent = AnnotationUtil.getClassAnnotation(clazz, GalleryEvent.class);
        if (galleryEvent != null) {
            GalleryEventBean galleryEventBean = new GalleryEventBean(galleryEvent);
            extAPIEvent.add(galleryEventBean);
        }


        EnumsClass enums = AnnotationUtil.getClassAnnotation(clazz, EnumsClass.class);
        if (enums != null && enums.clazz() != null) {
            this.setEnumClass(enums.clazz());
            this.enumsClassBean = new EnumsClassBean(enums.clazz());
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
        PageBar pageBarAnnotation = AnnotationUtil.getClassAnnotation(clazz, PageBar.class);
        if (pageBarAnnotation != null) {
            pageBar = new PageBarBean(pageBarAnnotation);
            if (pageBar.getPageCount() == 20) {
                pageBar.setPageCount(8);
            }
        }

        RightContextMenu contextMenu = AnnotationUtil.getClassAnnotation(clazz, RightContextMenu.class);
        if (contextMenu != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), contextMenu);
        }

    }


//    void addActions(GalleryEventEnum eventEnum, List<Action> actions) {
//        List<Action> actionList = this.customActions.get(eventEnum);
//        if (actionList == null) {
//            actionList = new ArrayList<>();
//        }
//        for (Action ac : actions) {
//            if (!actionList.contains(ac)) {
//                actionList.add(ac);
//            }
//        }
//        customActions.put(eventEnum, actionList);
//
//    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (contextMenuBean != null && contextMenuBean.getMenuClass() != null) {
            classSet.addAll(Arrays.asList(contextMenuBean.getMenuClass()));
        }
        return classSet;
    }


    public List<GalleryItemBean> getGalleryItemBeans() {
        return galleryItemBeans;
    }

    public void setGalleryItemBeans(List<GalleryItemBean> galleryItemBeans) {
        this.galleryItemBeans = galleryItemBeans;
    }

    public LinkedHashSet<GalleryEventBean> getExtAPIEvent() {
        return extAPIEvent;
    }

    public void setExtAPIEvent(LinkedHashSet<GalleryEventBean> extAPIEvent) {
        this.extAPIEvent = extAPIEvent;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<M> toolBarMenu = getCustomMenu();
        List<M> bottombarMenu = getBottombarMenu();
        List<M> customMenu = getCustomMenu();
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (toolBar != null) {
            annotationBeans.add(toolBar);
        }
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

    public void initViewClass(Class clazz) {
        super.initViewClass(clazz);

    }

    @Override
    public ToolBarMenuBean getToolBar() {
        if (toolBar == null) {
            List<M> toolBarMenu = getCustomMenu();
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
                this.toolBar.setMenus(toolBarMenu.toArray(new CustomMenu[]{}));
            }
        }
        return toolBar;
    }

    @Override
    public MenuBarBean getMenuBar() {
        if (menuBar == null) {
            List<M> customMenu = getCustomMenu();
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
                this.menuBar.setMenus(customMenu.toArray(new CustomMenu[]{}));
            }
        }
        return menuBar;
    }


    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            List<M> bottombarMenu = getBottombarMenu();
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                this.bottomBar.setBottombar(bottombarMenu.toArray(new CustomMenu[]{}));
            }
        }
        return bottomBar;
    }


    public abstract List<M> getToolBarMenu();

    public abstract List<M> getCustomMenu();


    public abstract List<M> getBottombarMenu();

    public abstract Set<E> getEvent();


    public Boolean getAutoIconColor() {
        return autoIconColor;
    }

    public void setAutoIconColor(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
    }

    public Boolean getAutoItemColor() {
        return autoItemColor;
    }

    public void setAutoItemColor(Boolean autoItemColor) {
        this.autoItemColor = autoItemColor;
    }

    public Boolean getAutoFontColor() {
        return autoFontColor;
    }

    public void setAutoFontColor(Boolean autoFontColor) {
        this.autoFontColor = autoFontColor;
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public RightContextMenuBean genContextMenuBean() {
        if (contextMenuBean == null) {
            contextMenuBean = new RightContextMenuBean(this.id == null ? this.getMethodName() : this.id);
            AnnotationUtil.fillDefaultValue(RightContextMenu.class, contextMenuBean);
        }
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public PageBarBean getPageBar() {
        return pageBar;
    }

    public void setPageBar(PageBarBean pageBar) {
        this.pageBar = pageBar;
    }

    public EnumsClassBean getEnumsClassBean() {
        return enumsClassBean;
    }

    public void setEnumsClassBean(EnumsClassBean enumsClassBean) {
        this.enumsClassBean = enumsClassBean;
    }

}
