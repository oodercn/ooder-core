package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.LayoutAnnotation;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.NavBaseViewBean;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class NavComboBaseViewBean<U extends TabListItem> extends NavBaseViewBean<LayoutListItem, U, TabsComponent> {

    public TabsViewBean tabsViewBean;

    public CustomLayoutViewBean layoutViewBean;

    List<LayoutListItem> itemBeans = new ArrayList<>();

    public NavComboBaseViewBean() {


        if (tabsViewBean == null) {
            tabsViewBean = AnnotationUtil.fillDefaultValue(TabsAnnotation.class, new TabsViewBean());
        }
        if (layoutViewBean == null) {
            layoutViewBean = AnnotationUtil.fillDefaultValue(LayoutAnnotation.class, new CustomLayoutViewBean());
        }
    }


    @Override
    public List<JavaSrcBean> updateModule(ModuleComponent parentModuleComponent) {
        if (layoutViewBean!=null){
            AnnotationUtil.fillDefaultValue(LayoutAnnotation.class, layoutViewBean);
        }

        if (tabsViewBean!=null){
            AnnotationUtil.fillDefaultValue(TabsAnnotation.class, tabsViewBean);
        }

        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(parentModuleComponent);
        addChildJavaSrc(javaSrcBeans);
        return javaSrcBeans;

    }


    public abstract CustomViewBean getCurrViewBean();

    public NavComboBaseViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        layoutViewBean = new CustomLayoutViewBean(methodAPIBean);
        tabsViewBean = (TabsViewBean) layoutViewBean.findComByPos(PosType.main);
        if (tabsViewBean == null) {
            tabsViewBean = new TabsViewBean(methodAPIBean);
        }


    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (tabsViewBean != null) {
            annotationBeans.add(tabsViewBean);
        }
        if (layoutViewBean != null) {
            annotationBeans.add(layoutViewBean);
        }

        return annotationBeans;
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (tabsViewBean != null) {
            classSet.addAll(tabsViewBean.getOtherClass());
        }
        classSet.addAll(layoutViewBean.getOtherClass());
        return classSet;
    }


    @Override
    public List<LayoutListItem> getItemBeans() {
        return itemBeans;
    }

    public void setItemBeans(List<LayoutListItem> itemBeans) {
        this.itemBeans = itemBeans;
    }

    public TabsViewBean getTabsViewBean() {
        return tabsViewBean;
    }

    public void setTabsViewBean(TabsViewBean tabsViewBean) {
        this.tabsViewBean = tabsViewBean;
    }

    public CustomLayoutViewBean getLayoutViewBean() {
        return layoutViewBean;
    }

    public void setLayoutViewBean(CustomLayoutViewBean layoutViewBean) {
        this.layoutViewBean = layoutViewBean;
    }
}
