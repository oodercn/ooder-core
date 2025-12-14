package net.ooder.esd.bean.view;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.NavFoldingTreeAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = NavFoldingTreeAnnotation.class)
public class NavFoldingComboViewBean extends NavComboBaseViewBean {

    ModuleViewType moduleViewType = ModuleViewType.NAVFOLDINGTREECONFIG;

    List<CustomTreeViewBean> treeViewBeans;

    public List<NavTabListItem> tabItems = new ArrayList<>();


    public NavFoldingComboViewBean() {
        super();
    }

    @Override
    public CustomViewBean getCurrViewBean() {
        return this;
    }

    public NavFoldingComboViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        NavFoldingTreeAnnotation tabAnnotation = AnnotationUtil.getClassAnnotation(clazz, NavFoldingTreeAnnotation.class);
        if (tabAnnotation != null) {
            AnnotationUtil.fillBean(tabAnnotation, this);
        }
        this.initChildTree();

    }


    public CustomTreeViewBean findChildTree(String childViewId) {
        if (childViewId != null && !childViewId.equals("")) {
            for (CustomTreeViewBean child : this.getTreeViewBeans()) {
                if (child.getId().equals(childViewId)) {
                    return child;
                } else if (child.getChildBean(childViewId) != null) {
                    return child.getChildBean(childViewId);
                }
            }
        }
        return null;
    }

    private void initChildTree() {
        List<FieldModuleConfig> items = this.getNavItems();
        this.treeViewBeans = new ArrayList<>();
        for (FieldModuleConfig moduleConfig : items) {
            MethodConfig methodConfig = moduleConfig.getMethodConfig();
            if (methodConfig != null && methodConfig.getView() != null && methodConfig.getView() instanceof CustomTreeViewBean) {
                treeViewBeans.add((CustomTreeViewBean) methodConfig.getView());
            }
        }
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (toolBar != null) {
            annotationBeans.add(toolBar);
        }
        if (menuBar != null) {
            annotationBeans.add(menuBar);
        }
        if (bottomBar != null) {
            annotationBeans.add(bottomBar);
        }

        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    public List<NavTabListItem> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<NavTabListItem> tabItems) {
        this.tabItems = tabItems;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        for (CustomTreeViewBean treeViewBean : this.getTreeViewBeans()) {
            if (treeViewBean != null) {
                classSet.addAll(treeViewBean.getOtherClass());
            }
        }
        return ClassUtility.checkBase(classSet);
    }

    public List<CustomTreeViewBean> getTreeViewBeans() {
        if (treeViewBeans == null || treeViewBeans.isEmpty()) {
            initChildTree();
        }
        return treeViewBeans;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.FOLDINGTABS;
    }

    public void setTreeViewBeans(List<CustomTreeViewBean> treeViewBeans) {
        this.treeViewBeans = treeViewBeans;
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