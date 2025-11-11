package net.ooder.esd.bean.view;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.NavTreeAnnotation;
import net.ooder.esd.annotation.TreeAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.bean.*;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.List;
import java.util.Set;

@AnnotationType(clazz = NavTreeAnnotation.class)
public class NavTreeComboViewBean extends NavComboBaseViewBean<TreeListItem> {

    ModuleViewType moduleViewType = ModuleViewType.NAVTREECONFIG;

    public CustomTreeViewBean treeViewBean;

    public NavTreeComboViewBean(ModuleComponent<LayoutComponent> moduleComponent) {
        AnnotationUtil.fillDefaultValue(NavTreeAnnotation.class, this);
        layoutViewBean = new CustomLayoutViewBean(moduleComponent);
        treeViewBean = (CustomTreeViewBean) layoutViewBean.findComByPos(PosType.before);
        CustomViewBean mainBean = (CustomViewBean) layoutViewBean.findComByPos(PosType.main);
        if (mainBean instanceof TabsViewBean) {
            tabsViewBean = (TabsViewBean) mainBean;
        } else {
            tabsViewBean = new TabsViewBean();
        }


    }


    public NavTreeComboViewBean() {
        super();
    }

    @Override
    @JSONField(serialize = false)
    public CustomViewBean getCurrViewBean() {
        if (treeViewBean == null) {
            treeViewBean = createTreeViewBean();
        }
        return treeViewBean;
    }

    private CustomTreeViewBean createTreeViewBean() {
        if (treeViewBean == null) {
            treeViewBean = AnnotationUtil.fillDefaultValue(TreeAnnotation.class, new CustomTreeViewBean());
            treeViewBean.setRootClassName(this.getSourceClassName());
            treeViewBean.setRootMethodName(this.getMethodName());
        }
        return treeViewBean;
    }

    public NavTreeComboViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        NavTreeAnnotation navTreeAnnotation = AnnotationUtil.getClassAnnotation(clazz, NavTreeAnnotation.class);
        if (navTreeAnnotation == null) {
            AnnotationUtil.fillDefaultValue(NavTreeAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(navTreeAnnotation, this);
        }
        treeViewBean = (CustomTreeViewBean) layoutViewBean.findComByPos(PosType.before);
        if (treeViewBean == null) {
            treeViewBean = new CustomTreeViewBean(methodAPIBean);
        }

        layoutViewBean = fillLayoutBean(layoutViewBean);
    }


    @Override
    public CustomLayoutViewBean getLayoutViewBean() {
        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        if (itemBeanList.isEmpty()) {
            layoutViewBean = fillLayoutBean(layoutViewBean);
        }
        return layoutViewBean;
    }

    public CustomLayoutViewBean fillLayoutBean(CustomLayoutViewBean layoutViewBean) {
        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        if (itemBeanList.isEmpty()) {
            LayoutListItem beforItem = new LayoutListItem(PosType.before);
            LayoutListItem mainItem = new LayoutListItem(PosType.main);
            itemBeanList.add(new CustomLayoutItemBean(beforItem));
            itemBeanList.add(new CustomLayoutItemBean(mainItem));
        }
        return layoutViewBean;
    }

    public ComponentBean findComByPath(String path) {
        path = OODUtil.formatJavaName(path, false);
        ComponentBean componentBean = null;
        if (path != null) {
            path = path.toLowerCase();
        }
        if (OODUtil.formatJavaName(this.getXpath(), false).equals(path)) {
            componentBean = this;
        } else {
            componentBean = this.tabsViewBean.findComByPath(path);
            if (componentBean == null) {
                componentBean = treeViewBean.findComByPath(path);
            }
        }
        return componentBean;
    }


    public ComponentBean findComByAlias(String alias) {
        String path = OODUtil.formatJavaName(alias, false).toLowerCase();
        path = OODUtil.formatJavaName(path, false);
        ComponentBean componentBean = null;
        if (path != null) {
            path = path.toLowerCase();
        }
        if (OODUtil.formatJavaName(this.getXpath(), false).equals(path)) {
            componentBean = this;
        } else {
            componentBean = this.tabsViewBean.findComByAlias(path);
            if (componentBean == null) {
                componentBean = treeViewBean.findComByAlias(path);
            }
        }
        return componentBean;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TREEVIEW;
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
        annotationBeans.add(treeViewBean);
        annotationBeans.add(this);
        return annotationBeans;
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        classSet.addAll(treeViewBean.getOtherClass());
        return classSet;
    }


    public CustomTreeViewBean getTreeViewBean() {
        return treeViewBean;
    }

    public void setTreeViewBean(CustomTreeViewBean treeViewBean) {
        this.treeViewBean = treeViewBean;
    }

    public NavTreeComboViewBean fillData(NavTreeAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

}