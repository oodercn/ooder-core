package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.NavGalleryAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.GalleryNavItem;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = NavGalleryAnnotation.class)
public class NavGalleryComboViewBean extends NavComboBaseViewBean<GalleryNavItem> {

    ModuleViewType moduleViewType = ModuleViewType.NAVGALLERYCONFIG;

    CustomGalleryViewBean galleryViewBean;

    public NavGalleryComboViewBean(ModuleComponent<LayoutComponent> moduleComponent) {
        AnnotationUtil.fillDefaultValue(NavGalleryAnnotation.class, this);
        layoutViewBean = new CustomLayoutViewBean(moduleComponent);
        galleryViewBean = (CustomGalleryViewBean) layoutViewBean.findComByPos(PosType.before);
        CustomViewBean mainBean = (CustomViewBean) layoutViewBean.findComByPos(PosType.main);
        if (mainBean instanceof TabsViewBean) {
            tabsViewBean = (TabsViewBean) mainBean;
        } else {
            tabsViewBean = new TabsViewBean();
        }


    }


    public NavGalleryComboViewBean() {

    }


    public NavGalleryComboViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        galleryViewBean = (CustomGalleryViewBean) layoutViewBean.findComByPos(PosType.before);
        tabsViewBean.setCloseBtn(true);
        layoutViewBean.setType(LayoutType.vertical);

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (toolBar != null) {
            annotationBeans.add(toolBar);
        }
        if (menuBar != null &&
                ((menuBar.getMenus() != null && menuBar.getMenus().length > 0)
                        || (menuBar.getMenuClasses() != null && menuBar.getMenuClasses().length > 0))) {
            annotationBeans.add(menuBar);
        }
        if (bottomBar != null) {
            annotationBeans.add(bottomBar);
        }
        if (galleryViewBean != null) {
            annotationBeans.add(galleryViewBean);
        }

        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (galleryViewBean != null) {
            classSet.addAll(galleryViewBean.getOtherClass());
        }
        return ClassUtility.checkBase(classSet);
    }


    @Override
    public List<GalleryNavItem> getTabItems() {
        if (this.tabItems == null || tabItems.size() == 0) {
            tabItems = new ArrayList<>();
            List<FieldModuleConfig> moduleItems = getNavItems();
            for (FieldModuleConfig itemInfo : moduleItems) {
                GalleryNavItem navItemProperties = null;
                navItemProperties = new GalleryNavItem(itemInfo);
                navItemProperties.setComment(navItemProperties.getCaption());
                navItemProperties.setCaption("");
                tabItems.add(navItemProperties);
            }
        }
        return tabItems;
    }


    @Override
    public CustomViewBean getCurrViewBean() {
        return galleryViewBean;
    }

    public CustomGalleryViewBean getGalleryViewBean() {
        return galleryViewBean;
    }

    public void setGalleryViewBean(CustomGalleryViewBean galleryViewBean) {
        this.galleryViewBean = galleryViewBean;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.GALLERY;
    }
}
